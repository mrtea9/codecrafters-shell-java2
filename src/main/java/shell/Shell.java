package shell;

import command.Command;
import command.Executable;
import command.builtin.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        var arguments = new ArrayList<String>();
        final var command = parseCommand(line);
        if (command.length() == line.length()) {
            line = line.substring(command.length());
        } else {
            line = line.substring(command.length() + 1);
        }

        final var commandArguments = command.equals("cat") ? parseCatArguments(line) : parseArguments(line);

        arguments.add(command);
        arguments.addAll(commandArguments);

        String[] result = new String[arguments.size()];

        return arguments.toArray(result);
    }

    private static List<String> parseCatArguments(String line) {
        List<String> arguments = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "\"(\\\\.|[^\"])*\"|'(\\\\.|[^'])*'|\\S+"
        );
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            String match = matcher.group();
            if (match.startsWith("\"") && match.endsWith("\"")) {
                // Keep escaped characters and remove surrounding double quotes
                match = match.substring(1, match.length() - 1).replace("\\\"", "\"");
            } else if (match.startsWith("'") && match.endsWith("'")) {
                // Keep escaped characters and remove surrounding single quotes
                match = match.substring(1, match.length() - 1).replace("\\'", "'");
            }
            arguments.add(match);
        }

        return arguments;
    }

    private static List<String> parseArguments(String line) {
        String arg;
        List<String> commandArguments = new ArrayList<>();

        while (!line.isEmpty()) {
            if (line.startsWith("'")) {
                arg = singleQuotes(line);
                line = line.substring(arg.length() + 2);
            } else if (line.startsWith("\"")) {
                arg = doubleQuotes(line);
                line = line.substring(arg.length() + 2);
            } else if (line.contains("\\")) {
                arg = blackSlash(line);
                commandArguments.add(arg);
                return commandArguments;
            } else {
                arg = line.replaceAll("\\s+", " ");
                arg = simpleLine(arg);
                if (arg.length() == line.length()) line = line.substring(arg.length());
                else line = line.substring(arg.length() +  1);
            }

            if (!arg.isEmpty()) commandArguments.add(arg);
        }

        return commandArguments;
    }

    private static String parseCommand(String line) {
        String command;

        if (line.startsWith("'")) {
            command = singleQuotes(line);
        } else if (line.startsWith("\"")) {
            command = doubleQuotes(line);
        } else {
            command = simpleLine(line);
        }

        return command;
    }

    private static String simpleLine(String message) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            final var firstChar = message.charAt(i);

            if (firstChar == ' ') return sb.toString();

            sb.append(firstChar);
        }

        return sb.toString();
    }

    private static String singleQuotes(String message) {
        StringBuilder sb = new StringBuilder();
        boolean startSingle = false;

        for (int i = 0; i < message.length(); i++) {
            final var firstChar = message.charAt(i);

            if (!startSingle && firstChar == ' ' && message.charAt(i + 1) == ' ') continue;

            if (startSingle && firstChar == '\'') {
                return sb.toString();
            }

            if (firstChar == '\'') {
                startSingle = true;
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
                return sb.toString();
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
