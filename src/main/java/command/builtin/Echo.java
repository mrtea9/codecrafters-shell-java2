package command.builtin;

import shell.Shell;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Echo implements Builtin {

    INSTANCE;

    @Override
    public void execute(Shell shell, String[] arguments) {
        var line = Arrays.stream(arguments).skip(1).collect(Collectors.joining(" "));
//
//        if (line.startsWith("'")) {
//            line = singleQuotes(line);
//        } else if (line.startsWith("\"")) {
//            line = doubleQuotes(line);
//        } else if (line.contains("\\")) {
//            line = blackSlash(line);
//        } else {
//            line = line.replaceAll("\\s+", " ");
//        }

        System.out.println(line);
    }

    private String singleQuotes(String message) {
        return message.substring(1, message.length() - 1);
    }

    private String doubleQuotes(String message) {
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

    private String blackSlash(String message) {
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
