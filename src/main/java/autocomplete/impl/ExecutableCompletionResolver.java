package autocomplete.impl;

import autocomplete.CompletionResolver;
import shell.Shell;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

public enum ExecutableCompletionResolver implements CompletionResolver {

    INSTANCE;

    @Override
    public Set<String> getCompletions(Shell shell, String beginning) {
        final var candidates = new HashSet<String>();

        final FileFilter filter = (file) -> {
            return file.getName().startsWith(beginning) && file.isFile() && file.canExecute();
        };

        for (final var path : shell.get$PATH()) {
            final var files = new File(path).listFiles(filter);
            if (files == null) continue;

            for (final var file : files) {
                candidates.add(file.getName());
            }
        }

        return candidates;
    }
}
