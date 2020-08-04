package termtetris;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import canvas.Canvas;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Terminal terminal = getTerminal();
        String speed = null;
        if (args.length > 0) {
            speed = args[0];
        }
        Canvas canvas = new Canvas(terminal, speed);
        KeyboardListener keyboardListener = new KeyboardListener("keyboard listener", terminal, canvas);
        keyboardListener.start();
        Game game = new Game(canvas);
        game.start();
    }

    private static Terminal getTerminal() throws IOException {
        Terminal terminal = TerminalBuilder.terminal();

        return terminal;
    }
}
