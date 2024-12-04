package command.builtin;

import shell.Shell;

import java.util.Arrays;

public enum Type implements Builtin {

    INSTANCE;

    @Override
    public void execute(Shell shell, String[] arguments) {
        final var program = arguments[1];
        System.out.println(Arrays.toString(arguments));
    }

}
