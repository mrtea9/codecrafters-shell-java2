package command;

import shell.Shell;

import java.nio.file.Path;
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

            System.out.println(commandArguments);

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
