package canvas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.Display;

import shapes.*;

public class Canvas {
    private static int DEFAULT_SPEED = 500;

    private final int width;
    private final int height;

    private int speed;

    private AbstractShape[] shapes;

    private Terminal terminal;

    private AbstractShape currShape;

    private Shape[][] canvasValueMatrix;

    private Display display;

    public Canvas(Terminal terminal) {
        this.terminal = terminal;
        this.height = terminal.getHeight();
        this.width = terminal.getWidth();
        Pair<Integer, Integer> topCenter = Pair.of(width / 2, 0);
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
        printCanvas();
    }

    public void dropShape() throws IOException, InterruptedException {
        this.speed = DEFAULT_SPEED;
        this.currShape = nextShape();
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
            terminal.flush();
            Thread.sleep(speed);
            prevShapeCoords = currShape.getCurrCoordinates();
        }
    }


    private void printCanvas() {
        display.clear();
        terminal.flush();
        List<AttributedString> attributedStrings = new ArrayList<>();
        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                if (canvasValueMatrix[canvasRow][canvasColumn] != null) {
                    //stringBuilder.append(canvasValueMatrix[canvasRow][canvasColumn].getColor());
                    stringBuilder.append(canvasValueMatrix[canvasRow][canvasColumn].getSymbol());
                    //stringBuilder.append("\u001b[0m");
                }
                stringBuilder.append(" ");
            }
            attributedStrings.add(new AttributedString(stringBuilder.toString()));
        }
        display.update(attributedStrings, terminal.getSize().cursorPos(0,0), true);
    }

    private void drawShape(AbstractShape shape) throws IOException {
        ArrayList<AttributedString> toUpdate = new ArrayList<>();
        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            StringBuilder rowStringBuilder = new StringBuilder();
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                Pair<Integer, Integer> pairToTest = Pair.of(canvasColumn, canvasRow);
                if (shape.getCurrCoordinates().contains(pairToTest)) {
                    rowStringBuilder.append(shape.getSymbol());
                } else {
                    if(canvasValueMatrix[canvasRow][canvasColumn]!=null) {
                        rowStringBuilder.append(canvasValueMatrix[canvasRow][canvasColumn].getSymbol());
                    } else {
                        rowStringBuilder.append(" ");
                    }
                }
            }
            AttributedString attributedString = new AttributedString(rowStringBuilder.toString());
            toUpdate.add(attributedString);
        }
        display.update(toUpdate, terminal.getSize().cursorPos(0,0), false);
        terminal.flush();
    }

    private void populateValueMatrix() {
        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                if (canvasRow == 1 && canvasColumn < width - 10) {
                    canvasValueMatrix[canvasRow][canvasColumn] = new CeilingShape();
                } else if (canvasColumn == 0 || canvasColumn == width - 10 ) {
                    canvasValueMatrix[canvasRow][canvasColumn] = new WallShape();
                } else if (canvasRow == height - 2 && canvasColumn < width - 10) {
                    canvasValueMatrix[canvasRow][canvasColumn] = new BottomShape();
                } else {
                    canvasValueMatrix[canvasRow][canvasColumn] = null;
                }
            }
        }
    }

    private AbstractShape nextShape() {
        int rndPointer = new Random().nextInt(this.shapes.length);
        return this.shapes[rndPointer];
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

    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        Canvas canvas = new Canvas(terminal);
        canvas.drawShape(canvas.nextShape());
    }
}
