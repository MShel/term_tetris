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
import org.jline.utils.InfoCmp;

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
        populateValueMatrix();
        initCanvas();
        this.display = new Display(terminal, true);
        display.resize(width+1, height+1);
    }

    public void dropShape() throws IOException, InterruptedException {
        terminal.puts(InfoCmp.Capability.bell);
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
            Thread.sleep(speed);
            prevShapeCoords = currShape.getCurrCoordinates();
        }
    }


    private void initCanvas() {
        terminal.flush();
        for (int canvasRow = 1; canvasRow < height - 1; canvasRow++) {
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                if (canvasValueMatrix[canvasRow][canvasColumn] != null) {
                    System.out.print(canvasValueMatrix[canvasRow][canvasColumn].getColor());
                    System.out.print(canvasValueMatrix[canvasRow][canvasColumn].getSymbol());
                    System.out.print("\u001b[0m");
                    continue;
                }
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    private void drawShape(AbstractShape shape) throws IOException {
        ArrayList<AttributedString> toUpdate = new ArrayList<>();
        for (int canvasRow = 1; canvasRow < height - 1; canvasRow++) {
            StringBuilder rowStringBuilder = new StringBuilder();
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                Pair<Integer, Integer> pairToTest = Pair.of(canvasColumn, canvasRow);
                if (shape.getCurrCoordinates().contains(pairToTest)) {
                    rowStringBuilder.append(shape.getColor());
                    rowStringBuilder.append(shape.getSymbol());
                    rowStringBuilder.append("\u001b[0m");
                    continue;
                } else {
                    rowStringBuilder.append(" ");
                }
            }
            AttributedString attributedString = new AttributedString(rowStringBuilder.toString());
            toUpdate.add(attributedString);
        }

        display.update(toUpdate, 0, true);
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
