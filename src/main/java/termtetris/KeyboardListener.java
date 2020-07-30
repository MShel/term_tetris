/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package termtetris;


import java.io.IOException;

import canvas.Canvas;
import org.jline.terminal.Terminal;

public class KeyboardListener extends Thread {

    Terminal terminal;

    Canvas canvas;

    public KeyboardListener(String name, Terminal terminal, Canvas canvas) {
        super(name);
        this.canvas = canvas;
        this.terminal = terminal;
    }

    public void run() {
        terminal.enterRawMode();
        try {
            while (true) {
                int value = terminal.reader().read();
                switch (value) {
                    case 68:
                        canvas.moveLeft();
                        break;
                    case 67:
                        canvas.moveRight();
                        break;
                    default:
                        //System.out.println(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
