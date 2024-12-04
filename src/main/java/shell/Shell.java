package shell;

import command.Command;
import command.Executable;
import command.builtin.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    private Path workingDirectory = Path.of(".").toAbsolutePath().normalize();

    public Command find(String program) {
        final var builtin = builtins.get(program);
        if (builtin != null) return builtin;

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

    public static String[] parse(String line) {
        final var args = line.split(" ", 2);
        var arguments = new ArrayList<String>();

        for (var arg : args) {
            if (arg.startsWith("'")) {
                arg = singleQuotes(arg);
            } else if (arg.startsWith("\"")) {
                arg = doubleQuotes(arg);
            } else if (arg.contains("\\")) {
                arg = blackSlash(arg);
            } else {
                arg = arg.replaceAll("\\s+", " ");
            }

            arguments.add(arg);
        }
        String[] result = new String[arguments.size()];

        return arguments.toArray(result);
    }

    private static String singleQuotes(String message) {
        StringBuilder sb = new StringBuilder();
        boolean startSingle = false;

        for (int i = 0; i < message.length(); i++) {
            final var firstChar = message.charAt(i);

            if (!startSingle && firstChar == ' ' && message.charAt(i + 1) == ' ') continue;

            if (startSingle && firstChar == '\'') {
                startSingle = false;
                continue;
            }

            if (firstChar == '\'') {
                startSingle = true;
                continue;
            }

            if (firstChar == '\\' && message.charAt(i + 1) != ' ') {
                sb.append(message.charAt(i + 1));
                i++;
                continue;
            }

            sb.append(firstChar);
        }

        return sb.toString();
    }

    private static String doubleQuotes(String message) {
        StringBuilder sb = new StringBuilder();
        boolean startDouble = false;

        for (int i = 0; i < message.length(); i++) {
            final var firstChar = message.charAt(i);

            if (!startDouble && firstChar == ' ' && message.charAt(i + 1) == ' ') continue;

            if (startDouble && firstChar == '"') {
                startDouble = false;
                continue;
            }

            if (firstChar == '"') {
                startDouble = true;
                continue;
            }

            if (firstChar == '\\' && message.charAt(i + 1) != ' ') {
                sb.append(message.charAt(i + 1));
                i++;
                continue;
            }

            sb.append(firstChar);
        }

        return sb.toString();
    }

    private static String blackSlash(String message) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            final var firstChar = message.charAt(i);

            if (firstChar == '\\') {
                sb.append(message.charAt(i + 1));
                i++;
                continue;
            }

            sb.append(firstChar);
        }

        return sb.toString();
    }
}
