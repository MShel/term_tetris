/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package canvas;

import java.io.IOException;
import java.util.Random;

import org.jline.terminal.Terminal;
import com.sun.tools.javac.util.Pair;

import shapes.AbstractShape;
import shapes.IShape;
import shapes.ZShape;

public class Canvas {
    private static int DEFAULT_SPEED = 500;
    private int width;

    private int height;

    private int speed;

    private AbstractShape[] shapes;

    private Terminal terminal;

    private AbstractShape currShape;

    public Canvas(Terminal terminal) {
        this.terminal = terminal;
        this.height = terminal.getHeight();
        this.width = terminal.getWidth();
        this.shapes = new AbstractShape[] {
                new ZShape(Pair.of(width / 2, 0)),
                new IShape(Pair.of(width / 2, 0))
        };
    }

    public void dropShape() throws IOException, InterruptedException {
        terminal.flush();
        this.speed = DEFAULT_SPEED;
        this.currShape = nextShape();
        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            currShape.setCoordinates(Pair.of(currShape.getCurrCenter().fst, canvasRow++));
            drawShape(currShape);
            terminal.flush();
            Thread.sleep(speed);
        }
    }

    private void drawShape(AbstractShape shape) throws IOException {
        terminal.flush();
        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                Pair<Integer, Integer> pairToTest = Pair.of(canvasColumn, canvasRow);
                if (shape.getCurrCoordinates().contains(pairToTest)){
                    System.out.print("*");
                    continue;
                }

                System.out.print(" ");
            }
            System.out.println();
        }
    }

    private AbstractShape nextShape() {
        int rndPointer = new Random().nextInt(this.shapes.length);
        return this.shapes[rndPointer];
    }

    public void moveShapeLeft(){
        Pair<Integer, Integer> currCenter = currShape.getCurrCenter();
        currShape.setCoordinates(Pair.of(currCenter.fst-1, currCenter.snd));
    }

    public void moveShapeRight(){
        Pair<Integer, Integer> currCenter = currShape.getCurrCenter();
        currShape.setCoordinates(Pair.of(currCenter.fst+1, currCenter.snd));
    }

    public void rotateShape() {
        currShape.rotate();
    }


    public void speedUp(){
        speed = 15;
    }

    @Override
    public String toString() {
        return "Canvas{" + "width=" + width + ", height=" + height + '}';
    }
}
