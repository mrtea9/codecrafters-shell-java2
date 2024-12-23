package command.builtin;

import command.Command;

public interface Builtin extends Command {

    default boolean acceptForType() {
        return true;
    }

}
