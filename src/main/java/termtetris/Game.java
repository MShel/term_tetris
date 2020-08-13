/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package termtetris;

import java.io.IOException;

import canvas.Canvas;

public class Game {

    private final Canvas canvas;

    private boolean paused;

    private boolean finished;

    public Game(Canvas canvas) {
        this.paused = false;
        this.finished = false;
        this.canvas = canvas;
    }

    public void start() throws InterruptedException {
        Integer topRow = null;
        while (true) {
            if (!paused) {
                topRow = canvas.dropShape(topRow);
            }

            Thread.sleep(canvas.getSpeed());
        }
    }

    public synchronized void pause()  {
        paused = !paused;
    }
}
