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

    Game game;

    public KeyboardListener(String name, Terminal terminal, Canvas canvas, Game game) {
        super(name);
        this.canvas = canvas;
        this.terminal = terminal;
        this.game = game;
    }

    public void run() {
        terminal.enterRawMode();
        try {
            while (true) {
                int value = terminal.reader().read();
                switch (value) {
                    case 68:
                        canvas.moveShapeLeft();
                        break;
                    case 67:
                        canvas.moveShapeRight();
                        break;
                    case 114:
                        canvas.rotateShape();
                        break;
                    case 32:
                        canvas.speedUp();
                        break;
                    case 112:
                        game.pause();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
