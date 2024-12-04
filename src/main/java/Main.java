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
        final var arguments = line.split("");
        final var program = arguments[0];

        final var command = shell.find(program);
        if (command != null) {
            command.execute(shell, arguments);
        } else {
            System.out.println("%s: command not found".formatted(program));
        }
    }
}
