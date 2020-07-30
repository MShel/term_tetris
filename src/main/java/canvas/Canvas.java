/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package canvas;

import java.io.IOException;
import java.util.List;

import org.jline.terminal.Terminal;
import com.sun.tools.javac.util.Pair;

import shapes.Shape;
import shapes.ZShape;

public class Canvas {

    private int width;

    private int height;

    private Shape[] shapes;

    private Terminal terminal;

    public Canvas(Terminal terminal) {
        this.terminal = terminal;
        this.height = terminal.getHeight();
        this.width = terminal.getWidth();
        this.shapes = new Shape[]{
                new ZShape(Pair.of(width/2, 0))
        };
    }

    public void drawShape() throws IOException {
        Shape shape = this.shapes[0];
        terminal.flush();
        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                Pair<Integer, Integer> pairToTest = Pair.of(canvasColumn, canvasRow);
                if (shape.getCoordinates().contains(pairToTest)){
                    System.out.print("*");
                }else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public void dropShape() throws IOException, InterruptedException {
        Shape shape = this.shapes[0];
        terminal.flush();

        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            shape.setCoordinates(Pair.of(width/2, canvasRow));
            drawShape();
            terminal.flush();
            Thread.sleep(500);
        }
    }

    @Override
    public String toString() {
        return "Canvas{" + "width=" + width + ", height=" + height + '}';
    }
}
