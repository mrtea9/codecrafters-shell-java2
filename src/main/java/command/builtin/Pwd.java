package command.builtin;

import io.RedirectStreams;
import shell.Shell;

import java.util.List;

public enum Pwd implements Builtin {

    INSTANCE;

    @Override
    public void execute(Shell shell, List<String> arguments, RedirectStreams redirectStreams) {
        redirectStreams.output().println(shell.getWorkingDirectory().toString());
    }
}
