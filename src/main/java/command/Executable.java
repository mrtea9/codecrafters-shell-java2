package command;

import shell.Shell;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public record Executable(Path path) implements Command {

    @Override
    public void execute(Shell shell, String[] arguments) {
        try {

            final var commandArguments = Stream
                    .concat(
                            Stream.of(path.toString()),
                            Arrays.stream(arguments).skip(1)
                    )
                    .toList();

            final var finalCommandArguments = new ArrayList<String>();

            for (String command : commandArguments) {
                if (command.startsWith("'")) command = command.replace("'", "").replace(",", "");

                finalCommandArguments.add(command);
            }

            System.out.println(finalCommandArguments);

            final var process = new ProcessBuilder(commandArguments)
                    .inheritIO()
                    .directory(shell.getWorkingDirectory().toFile())
                    .start();

            process.waitFor();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
