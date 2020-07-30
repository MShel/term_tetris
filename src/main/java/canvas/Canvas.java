/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package canvas;

import java.io.IOException;

import org.jline.terminal.Terminal;
import com.sun.tools.javac.util.Pair;

import shapes.AbstractShape;
import shapes.ZShape;

public class Canvas {

    private int width;

    private int height;

    private AbstractShape[] shapes;

    private Terminal terminal;

    private AbstractShape currShape;

    public Canvas(Terminal terminal) {
        this.terminal = terminal;
        this.height = terminal.getHeight();
        this.width = terminal.getWidth();
        currShape = new ZShape(Pair.of(width/2, 0));
    }

    public void dropShape() throws IOException, InterruptedException {
        terminal.flush();

        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            currShape.setCoordinates(Pair.of(currShape.getCurrCenter().fst, canvasRow++));
            drawShape(currShape);
            terminal.flush();
            Thread.sleep(500);
        }
    }

    private void drawShape(AbstractShape shape) throws IOException {
        terminal.flush();
        for (int canvasRow = 0; canvasRow < height; canvasRow++) {
            for (int canvasColumn = 0; canvasColumn < width; canvasColumn++) {
                Pair<Integer, Integer> pairToTest = Pair.of(canvasColumn, canvasRow);
                if (shape.getCurrCoordinates().contains(pairToTest)){
                    System.out.print("*");
                }else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }


    public void moveLeft(){
        Pair<Integer, Integer> currCenter = currShape.getCurrCenter();
        currShape.setCoordinates(Pair.of(currCenter.fst-1, currCenter.snd));
    }

    public void moveRight(){
        Pair<Integer, Integer> currCenter = currShape.getCurrCenter();
        currShape.setCoordinates(Pair.of(currCenter.fst+1, currCenter.snd));
    }

    @Override
    public String toString() {
        return "Canvas{" + "width=" + width + ", height=" + height + '}';
    }
}
