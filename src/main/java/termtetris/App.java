package termtetris;

import java.io.IOException;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import canvas.Canvas;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Terminal terminal = getTerminal();

        Canvas canvas = new Canvas(terminal);
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
