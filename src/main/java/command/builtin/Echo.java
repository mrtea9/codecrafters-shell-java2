package command.builtin;

import shell.Shell;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Echo implements Builtin {

    INSTANCE;

    @Override
    public void execute(Shell shell, String[] arguments) {
        var line = Arrays.stream(arguments).skip(1).collect(Collectors.joining(" "));

        System.out.println(line);
    }
}
