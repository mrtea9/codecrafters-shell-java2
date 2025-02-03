import autocomplete.Autocompleter;
import io.RedirectStreams;
import parse.LineParser;
import shell.Shell;
import terminal.Termios;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Shell shell = new Shell();

        while (true) {
            final var line = read(shell);

            if (line == null) {
                break;
            } else if (line.isBlank()) {
                continue;
            } else {
                eval(shell, line);
            }
        }
    }

    public static void prompt() {
        System.out.print("$ ");
    }

    public static void bell() {
        System.out.print((char) 0x7);
    }

    public static String read(Shell shell) {
        final var autocompleter = new Autocompleter();

        try (final var scope = Termios.enableRawMode()) {
            prompt();


        }

        return "";
    }

    public static void eval(Shell shell, String line) {
        final var parsedLine = new LineParser(line).parse();

        final var arguments = parsedLine.arguments();
        final var program = arguments.getFirst();

        final var command = shell.find(program, false);
        if (command != null) {
            try (final var redirectStreams = RedirectStreams.from(parsedLine.redirects())) {
                command.execute(shell, arguments, redirectStreams);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("%s: command not found".formatted(program));
        }
    }
}
