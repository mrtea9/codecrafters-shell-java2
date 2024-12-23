package command.builtin;

import io.RedirectStreams;
import shell.Shell;

import java.util.List;

public enum Exit implements Builtin {

    INSTANCE;

    @Override
    public void execute(Shell shell, List<String> arguments, RedirectStreams redirectStreams) {
        System.exit(0);
    }
}
