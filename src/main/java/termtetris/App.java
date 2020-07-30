/***************************************************************************
 * COPYRIGHT (C) 2012-2020, Rapid7 LLC, Boston, MA, USA.
 * All rights reserved. This material contains unpublished, copyrighted
 * work including confidential and proprietary information of Rapid7.
 **************************************************************************/
package termtetris;

import java.io.IOException;
import java.util.Scanner;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import canvas.Canvas;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner keyboardScanner = new Scanner(System.in);
        Canvas canvas = new Canvas(getTerminal());

        Game game = new Game(keyboardScanner, canvas);
        game.start();
    }

    private static Terminal getTerminal() throws IOException {
        return TerminalBuilder.terminal();
    }
}
