package shell;

import autocomplete.Autocompleter;
import io.RedirectStreams;
import lombok.SneakyThrows;
import parse.LineParser;
import terminal.Termios;

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

    @SneakyThrows
    public static String read(Shell shell) {
        final var autocompleter = new Autocompleter();

        try (final var scope = Termios.enableRawMode()) {
            prompt();

            var bellRang = false;

            final var line = new StringBuilder();
            while (true) {
                final var input = System.in.read();

                if (input == -1) return null;


                final var character = (char) input;
                switch (character) {
                    case 0x4: {
                        if (!line.isEmpty()) continue;

                        return null;
                    }

                    case '\r': {
                        break;
                    }

                    case '\n': {
                        System.out.print('\n');

                        return line.toString();
                    }

                    case '\t': {
                        switch (autocompleter.autocomplete(shell, line, bellRang)) {
                            case NONE -> {
                                bellRang = false;
                                bell();
                            }
                            case FOUND -> {
                                bellRang = false;
                            }
                            case MORE -> {
                                bellRang = true;
                                bell();
                            }
                        };

                        break;
                    }

                    case 0x1b: {
                        System.in.read(); // '['
                        System.in.read(); // 'A' or 'B' or 'C' or 'D'

                        break;
                    }

                    case 0x7f: {
                        if (line.isEmpty()) continue;

                        line.setLength(line.length() - 1);

                        System.out.print("\b \b");

                        break;
                    }

                    default: {
                        line.append(character);

                        System.out.print(character);

                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
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
