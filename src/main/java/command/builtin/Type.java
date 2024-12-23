package command.builtin;

import command.Executable;
import io.RedirectStreams;
import shell.Shell;

import java.util.Arrays;
import java.util.List;

public enum Type implements Builtin {

    INSTANCE;

    @Override
    public void execute(Shell shell, List<String> arguments, RedirectStreams redirectStreams) {
        final var program = arguments.get(1);
        final var command = shell.find(program, true);

        if (command instanceof Builtin) {
            redirectStreams.output().println("%s is a shell builtin".formatted(program));
        } else if (command instanceof Executable(final var path)) {
            redirectStreams.output().println("%s is %s".formatted(program, path));
        } else {
            redirectStreams.output().println("%s: not found".formatted(program));
        }
    }
}
