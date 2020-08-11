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
    private static int CEILING_ROW = 4;
    private static String CLEAR_ANSI_STYLE = "\u001b[0m";

    private final int width;
    private final int height;

    private int speed;

    private boolean paused = false;

    private Integer frozenOnRow;

    private AbstractShape[] shapes;

    private Terminal terminal;

    private AbstractShape currShape;

    private Shape[][] canvasValueMatrix;

    private Display display;

    private AbstractShape nextShape;

    private int score;

    public Canvas(Terminal terminal, String speed) {
        this.terminal = terminal;
        this.height = terminal.getHeight();
        this.width = terminal.getWidth();
        DEFAULT_SPEED = (StringUtils.isNumeric(speed)) ? Integer.parseInt(speed) : DEFAULT_SPEED;
        this.score = 0;
        Pair<Integer, Integer> topCenter = Pair.of(width / 2, 0);
        this.shapes = new AbstractShape[]{
                new ZShapeLeft(topCenter),
                new ZShapeRight(topCenter),
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
        nextShape();
    }

    public void dropShape() throws InterruptedException {
        checkCleanFullLines();
        this.currShape = nextShape;
        nextShape();
        this.speed = DEFAULT_SPEED;
        Pair<Integer, Integer> currCoord;
        List<Pair<Integer, Integer>> prevShapeCoords = new ArrayList<>();
        for (int canvasRow = CEILING_ROW + 1; canvasRow < height; canvasRow++) {
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
        for (int row = CEILING_ROW + 1; row < canvasValueMatrix.length - 2; row++) {
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
                moveDownRowsUpTo(row);
            }
        }

    }

    private void moveDownRowsUpTo(int rowToMoveTo) {
        for (int row = rowToMoveTo; row > CEILING_ROW + 1; row--){
            for (int column = 1; column < canvasValueMatrix[0].length - 2; column++) {
                canvasValueMatrix[row][column] = canvasValueMatrix[row - 1][column];
            }
        }
    }

    private void drawShape(AbstractShape shape)  {
        ArrayList<AttributedString> toUpdate = new ArrayList<>();
        addMenuHeader(toUpdate);

        for (int canvasRow = CEILING_ROW; canvasRow < height; canvasRow++) {
            StringBuilder rowStringBuilder = new StringBuilder();
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                Pair<Integer, Integer> pairToTest = Pair.of(canvasColumn, canvasRow);

                if (shape.getCurrCoordinates().contains(pairToTest)) {
                    rowStringBuilder.append(shape.getColor());
                    rowStringBuilder.append(shape.getSymbol());
                    rowStringBuilder.append(CLEAR_ANSI_STYLE);
                } else {
                    if (canvasValueMatrix[canvasRow][canvasColumn] != null) {
                        rowStringBuilder.append(canvasValueMatrix[canvasRow][canvasColumn].getColor());
                        rowStringBuilder.append(canvasValueMatrix[canvasRow][canvasColumn].getSymbol());
                        rowStringBuilder.append(CLEAR_ANSI_STYLE);
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

    private void addMenuHeader(List<AttributedString> toUpdate) {
        toUpdate.add(new AttributedString("Score:" + score));
        for (int row = 1; row < CEILING_ROW; row++) {
            StringBuilder rowStringBuilder = new StringBuilder();
            for (int column = 0; column < width; column++) {
                Pair<Integer, Integer> pairToTest = Pair.of(column, row);
                if (nextShape.getCurrCoordinates().contains(pairToTest)){
                    rowStringBuilder.append(nextShape.getColor());
                    rowStringBuilder.append(nextShape.getSymbol());
                    rowStringBuilder.append(CLEAR_ANSI_STYLE);
                    continue;
                }

                rowStringBuilder.append(" ");
            }

            AttributedString attributedString = AttributedString.fromAnsi(rowStringBuilder.toString());
            toUpdate.add(attributedString);
        }
    }


    private void populateValueMatrix() {
        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                if (canvasRow == CEILING_ROW && canvasColumn < width) {
                    canvasValueMatrix[canvasRow][canvasColumn] = new CeilingShape();
                } else if (canvasColumn == 0 || canvasColumn == width-1 ) {
                    canvasValueMatrix[canvasRow][canvasColumn] = new WallShape();
                } else if (canvasRow == height-1 && canvasColumn < width) {
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
        nextShape.setCoordinates(Pair.of(width/2, 1));
        this.nextShape = nextShape;
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

    public void pause() {
        this.paused = ! paused;
        if (this.paused) {
            System.out.println("!!! Paused !!!");
        } else {
            System.out.println("!!! UnPaused !!!");
        }
    }


    public boolean isPaused(){
        return paused;
    }

    @Override
    public String toString() {
        return "Canvas{" + "width=" + width + ", height=" + height + '}';
    }
}
