package shell;

import command.Command;
import command.builtin.Builtin;
import command.builtin.Exit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Shell {

    public static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    public final Map<String, Builtin> builtins = Map.of(
            "exit", Exit.INSTANCE
    );

    private Path workingDirectory = Path.of(".").toAbsolutePath().normalize();

    public Command find(String program) {
        final var builtin = builtins.get(program);
        if (builtin != null) return builtin;
//
//        final var separator = IS_WINDOWS ? ";" : ":";
//        final var paths = System.getenv("PATH").split(separator);
//
//        for (final var directory : paths) {
//            final var path = Paths.get(directory, program).normalize().toAbsolutePath();
//
//            if (Files.exists(path))
//        }

        return null;
    }

    public boolean changeWorkingDirectory(Path path) {
        if (!Files.exists(path)) return false;

        workingDirectory = path;
        return true;
    }
}
