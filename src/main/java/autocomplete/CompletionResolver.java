package autocomplete;

import shell.Shell;

import java.util.Set;

@FunctionalInterface
public interface CompletionResolver {

    Set<String> getCompletions(Shell shell, String beginning);

}
