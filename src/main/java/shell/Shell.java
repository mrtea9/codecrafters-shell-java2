package shell;

import command.Command;
import command.Executable;
import command.builtin.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Shell {

    public static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    public final Map<String, Builtin> builtins = Map.of(
            "exit", Exit.INSTANCE,
            "echo", Echo.INSTANCE,
            "type", Type.INSTANCE,
            "pwd", Pwd.INSTANCE,
            "cd", Cd.INSTANCE
    );

    public Map<String, Builtin> getBuiltins() {
        return builtins;
    }

    private Path workingDirectory = Path.of(".").toAbsolutePath().normalize();

    public Command find(String program, boolean isForType) {
        final var builtin = builtins.get(program);
        if (builtin != null && (!isForType || builtin.acceptForType())) return builtin;

        if (IS_WINDOWS) program = program.replace('\\', '/');

        final var separator = IS_WINDOWS ? ";" : ":";
        final var paths = System.getenv("PATH").split(separator);

        for (final var directory : paths) {
            final var path = Paths.get(directory, program).normalize().toAbsolutePath();

            if (Files.exists(path)) return new Executable(path);
        }

        return null;
    }

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    public boolean changeWorkingDirectory(Path path) {
        if (!Files.exists(path)) return false;

        workingDirectory = path;
        return true;
    }

    public String[] get$PATH() {
        final var separator = IS_WINDOWS ? ";" : ":";

        return System.getenv("PATH").split(separator);
    }
}
