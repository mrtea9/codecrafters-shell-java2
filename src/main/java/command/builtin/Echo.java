package command.builtin;

import io.RedirectStreams;
import shell.Shell;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Echo implements Builtin {

    INSTANCE;

    @Override
    public void execute(Shell shell, List<String> arguments, RedirectStreams redirectStreams) {
        final var line = arguments.stream().skip(1).collect(Collectors.joining(" "));

        redirectStreams.output().println(line);
    }
}
