package command;

import io.RedirectStreams;
import shell.Shell;

import java.util.List;

public interface Command {

    void execute(Shell shell, List<String> arguments, RedirectStreams redirectStreams);

}
