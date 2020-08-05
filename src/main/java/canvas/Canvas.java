package canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.Display;

import shapes.*;

public class Canvas {
    private static int DEFAULT_SPEED = 300;

    private final int width;
    private final int height;

    private int speed;

    private AbstractShape[] shapes;

    private Terminal terminal;

    private AbstractShape currShape;

    private Shape[][] canvasValueMatrix;

    private Display display;

    private int score;
    public Canvas(Terminal terminal, String speed) {
        this.terminal = terminal;
        this.height = terminal.getHeight();
        this.width = terminal.getWidth();
        DEFAULT_SPEED = (StringUtils.isNumeric(speed)) ? Integer.parseInt(speed) : DEFAULT_SPEED;
        this.score = 0;
        Pair<Integer, Integer> topCenter = Pair.of(width / 2, 2);
        this.shapes = new AbstractShape[]{
                new ZShape(topCenter),
                new LeftSevenShape(topCenter),
                new PoleShape(topCenter),
                new RightSevenShape(topCenter),
                new SquareShape(topCenter),
                new HillShape(topCenter)
        };
        this.canvasValueMatrix = new Shape[this.height + 1][this.width + 1];
        this.display = new Display(terminal, false);
        display.resize(terminal.getHeight(), terminal.getWidth());
        populateValueMatrix();
    }

    public void dropShape() throws InterruptedException {
        checkCleanFullLines();
        this.currShape = nextShape();
        this.speed = DEFAULT_SPEED;
        Pair<Integer, Integer> currCoord;
        List<Pair<Integer, Integer>> prevShapeCoords = new ArrayList<>();
        for (int canvasRow = 1; canvasRow < height; canvasRow++) {
            currCoord = Pair.of(currShape.getCurrCenter().getLeft(), canvasRow);
            currShape.setCoordinates(currCoord);
            for (Pair<Integer, Integer> shapeCoord : currShape.getCurrCoordinates()) {
                if (canvasValueMatrix[shapeCoord.getRight()][shapeCoord.getLeft()] != null &&
                        !canvasValueMatrix[shapeCoord.getRight()][shapeCoord.getLeft()].isWall() &&
                        !canvasValueMatrix[shapeCoord.getRight()][shapeCoord.getLeft()].isCeiling()
                ) {
                    for (Pair<Integer, Integer> prevShapeCoord : prevShapeCoords) {
                        canvasValueMatrix[prevShapeCoord.getRight()][prevShapeCoord.getLeft()] = currShape;
                    }
                    return;
                }
            }
            drawShape(currShape);
            Thread.sleep(speed);
            prevShapeCoords = currShape.getCurrCoordinates();
        }
    }

    private void checkCleanFullLines() {
        //ignoring ceiling and bottom
        List<Integer> cleanedUpRows = new ArrayList<>();
        for (int row = 2; row < canvasValueMatrix.length - 2; row++) {
            boolean fullRow = true;
            //ignoring walls
            for (int column = 1; column < canvasValueMatrix[0].length - 2; column++) {
                if (canvasValueMatrix[row][column] == null) {
                    fullRow = false;
                    break;
                }
            }

            if (fullRow) {
                score += width;
                cleanedUpRows.add(row);
                moveDownRowsUpTo(row);
            }
        }

    }

    private void moveDownRowsUpTo(int rowToMoveTo) {
        for (int row = rowToMoveTo; row > 2; row--){
            for (int column = 1; column < canvasValueMatrix[0].length - 2; column++) {
                canvasValueMatrix[row][column] = canvasValueMatrix[row - 1][column];
            }
        }
    }

    private void drawShape(AbstractShape shape)  {
        ArrayList<AttributedString> toUpdate = new ArrayList<>();
        toUpdate.add(new AttributedString("Score:" + score));
        String clearStyleAnsi = "\u001b[0m";

        for (int canvasRow = 1; canvasRow < height; canvasRow++) {
            StringBuilder rowStringBuilder = new StringBuilder();
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                Pair<Integer, Integer> pairToTest = Pair.of(canvasColumn, canvasRow);
                //skipping our border and score rows
                if (canvasRow > 1 && shape.getCurrCoordinates().contains(pairToTest)) {
                    rowStringBuilder.append(shape.getColor());
                    rowStringBuilder.append(shape.getSymbol());
                    rowStringBuilder.append(clearStyleAnsi);
                } else {
                    if(canvasValueMatrix[canvasRow][canvasColumn]!=null) {
                        rowStringBuilder.append(canvasValueMatrix[canvasRow][canvasColumn].getColor());
                        rowStringBuilder.append(canvasValueMatrix[canvasRow][canvasColumn].getSymbol());
                        rowStringBuilder.append(clearStyleAnsi);
                    } else {
                        rowStringBuilder.append(" ");
                    }
                }
            }
            AttributedString attributedString = AttributedString.fromAnsi(rowStringBuilder.toString());
            toUpdate.add(attributedString);
        }
        display.update(toUpdate, terminal.getSize().cursorPos(0,0), true);
    }

    private void populateValueMatrix() {
        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                if (canvasRow == 1 && canvasColumn < width-1) {
                    canvasValueMatrix[canvasRow][canvasColumn] = new CeilingShape();
                } else if (canvasColumn == 0 || canvasColumn == width-1 ) {
                    canvasValueMatrix[canvasRow][canvasColumn] = new WallShape();
                } else if (canvasRow == height-1 && canvasColumn < width-1) {
                    canvasValueMatrix[canvasRow][canvasColumn] = new BottomShape();
                } else {
                    canvasValueMatrix[canvasRow][canvasColumn] = null;
                }
            }
        }
    }

    private AbstractShape nextShape() {
        int rndPointer = new Random().nextInt(this.shapes.length);
        AbstractShape nextShape = this.shapes[rndPointer];
        nextShape.setCoordinates(Pair.of(width/2, 0));
        return nextShape;
    }

    public void moveShapeLeft() {
        synchronized (this) {
            Pair<Integer, Integer> currCenter = currShape.getCurrCenter();
            currShape.setCoordinates(Pair.of(currCenter.getLeft() - 1, currCenter.getRight()));
            for (Pair<Integer, Integer> shapeCoord : currShape.getCurrCoordinates()) {
                if (canvasValueMatrix[shapeCoord.getRight()][shapeCoord.getLeft()] != null) {
                    currShape.setCoordinates(Pair.of(currCenter.getLeft() , currCenter.getRight()));
                    return;
                }
            }
        }
    }

    public void moveShapeRight() {
        synchronized (this) {
            Pair<Integer, Integer> currCenter = currShape.getCurrCenter();
            currShape.setCoordinates(Pair.of(currCenter.getLeft() + 1, currCenter.getRight()));
            for (Pair<Integer, Integer> shapeCoord : currShape.getCurrCoordinates()) {
                if (canvasValueMatrix[shapeCoord.getRight()][shapeCoord.getLeft()] != null) {
                    currShape.setCoordinates(Pair.of(currCenter.getLeft() , currCenter.getRight()));
                    return;
                }
            }
        }
    }

    public void rotateShape() {
        currShape.rotate();
    }


    public void speedUp() {
        speed = 15;
    }

    @Override
    public String toString() {
        return "Canvas{" + "width=" + width + ", height=" + height + '}';
    }
}
