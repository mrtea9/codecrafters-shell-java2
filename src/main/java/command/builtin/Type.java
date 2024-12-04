package command.builtin;

import shell.Shell;

import java.util.Arrays;

public enum Type implements Builtin {

    INSTANCE;

    @Override
    public void execute(Shell shell, String[] arguments) {
        final var program = arguments[1];
        final var command = shell.find(program);

        if (command instanceof Builtin) {
            System.out.println("%s is a shell builtin".formatted(program));
        } else {
            System.out.println("%s not found".formatted(program));
        }
    }

}
