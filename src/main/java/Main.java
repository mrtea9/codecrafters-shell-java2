import io.RedirectStreams;
import parse.LineParser;
import shell.Shell;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Shell shell = new Shell();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                final var line = read(scanner);
                eval(shell, line);
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String read(Scanner scanner) {
        while (true) {
            System.out.print("$ ");
            final var line = scanner.nextLine();

            if (!line.isEmpty()) return line;
        }
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
