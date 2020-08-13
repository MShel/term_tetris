package termtetris;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import canvas.Canvas;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        Terminal terminal = TerminalBuilder.terminal();
        String speed = null;
        if (args.length > 0) {
            speed = args[0];
        }
        Canvas canvas = new Canvas(terminal, speed);
        Game game = new Game(canvas);
        KeyboardListener keyboardListener = new KeyboardListener("keyboard listener", terminal, canvas, game);
        keyboardListener.start();
        game.start();
    }
}
