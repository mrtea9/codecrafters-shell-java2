package command;

import io.RedirectStream;
import io.RedirectStreams;
import io.StandardNamedStream;
import shell.Shell;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public record Executable(Path path) implements Command {

    static int times = 0;

    @Override
    public void execute(Shell shell, List<String> arguments, RedirectStreams redirectStreams) {
        try {

            final var commandArguments = Stream
                    .concat(
                            Stream.of(path.getFileName().toString()),
                            arguments.stream().skip(1)
                    )
                    .toList();

            final var builder = new ProcessBuilder(commandArguments)
                    .inheritIO()
                    .directory(shell.getWorkingDirectory().toFile());

            applyRedirect(builder, redirectStreams.output(), StandardNamedStream.OUTPUT);
            applyRedirect(builder, redirectStreams.error(), StandardNamedStream.ERROR);

            final var process = builder.start();

            process.waitFor();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void applyRedirect(ProcessBuilder builder, RedirectStream stream, StandardNamedStream streamName) {
        final var isStderr = StandardNamedStream.ERROR.equals(streamName);

        switch (stream) {
            case RedirectStream.Standard standard -> {
                if (isStderr && StandardNamedStream.OUTPUT.equals(standard.name())) {
                    builder.redirectErrorStream(true);
                }
            }

            case RedirectStream.File file -> {
                file.close();

                final var redirect = file.append()
                        ? ProcessBuilder.Redirect.appendTo(file.path().toFile())
                        : ProcessBuilder.Redirect.to(file.path().toFile());

                if (isStderr) {
                    builder.redirectError(redirect);
                } else {
                    builder.redirectOutput(redirect);
                }
            }
        }
    }
}
