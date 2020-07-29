/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package termtetris;

import java.util.Scanner;

import canvas.Canvas;

public class Game {

    private final Scanner keyboardScanner;

    private final Canvas canvas;

    public Game(Scanner keyboardScanner, Canvas canvas) {
        this.keyboardScanner = keyboardScanner;
        this.canvas = canvas;
    }

    public void start() {
        System.out.println(canvas);
        while (true) {
            System.out.println(keyboardScanner.next());
        }
    }
}
