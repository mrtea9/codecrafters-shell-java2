package autocomplete.impl;

import autocomplete.CompletionResolver;
import shell.Shell;

import java.util.Set;
import java.util.stream.Collectors;

public enum BuiltinCompletionResolver implements CompletionResolver {

    INSTANCE;

    @Override
    public Set<String> getCompletions(Shell shell, String beginning) {
        return shell.getBuiltins()
                .keySet()
                .stream()
                .filter((name) -> name.startsWith(beginning))
                .collect(Collectors.toSet());
    }
}
