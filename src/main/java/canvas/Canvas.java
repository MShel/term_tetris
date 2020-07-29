/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package canvas;

import org.jline.terminal.Terminal;

public class Canvas {

    private int width;

    private int height;

    public Canvas(Terminal terminal) {
        this.height = terminal.getHeight();
        this.width = terminal.getWidth();
    }

    @Override
    public String toString() {
        return "Canvas{" + "width=" + width + ", height=" + height + '}';
    }
}
