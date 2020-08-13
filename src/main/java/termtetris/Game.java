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

    public Game(Canvas canvas) {
        this.paused = false;
        this.canvas = canvas;
    }

    public void start() throws InterruptedException {
        Integer topRow = null;
        while (true) {
            if (!paused && !canvas.isGameWon()) {
                topRow = canvas.dropShape(topRow);
            } else if (paused) {
                canvas.printOnDollarScreen("PAUSED", "\u001b[44;1m");
            }
            canvas.checkCleanFullLines();

            if (canvas.isGameWon()) {
                canvas.printOnDollarScreen(String.format("YOU've won, your score %s", canvas.getScore()),
                        "\u001b[45;1m");

            }

            Thread.sleep(canvas.getSpeed());
        }
    }

    public synchronized void pause() {
        paused = !paused;
    }
}
