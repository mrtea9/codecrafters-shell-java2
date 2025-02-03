package autocomplete;

import autocomplete.impl.BuiltinCompletionResolver;
import autocomplete.impl.ExecutableCompletionResolver;
import lombok.Getter;
import shell.Shell;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Autocompleter {

    public static final Comparator<String> SHORTEST_FIRST = Comparator.comparingInt(String::length).thenComparing(String::compareTo);

    @Getter
    public final List<CompletionResolver> resolvers = List.of(
            BuiltinCompletionResolver.INSTANCE,
            ExecutableCompletionResolver.INSTANCE
    );

    public Result autocomplete(Shell shell, StringBuilder line, boolean bellRang) {
        final var beginning = line.toString();
        if (beginning.isBlank()) return Result.FOUND;

        final var candidates = resolvers.stream()
                .map((resolver) -> resolver.getCompletions(shell, beginning))
                .flatMap(Set::stream)
                .map((candidate) -> candidate.substring(beginning.length()))
                .collect(Collectors.toCollection(() -> new TreeSet<>(SHORTEST_FIRST)));

        if (candidates.isEmpty()) return Result.NONE;

        if (candidates.size() == 1) {
            final var candidate = candidates.first();

            writeCandidate(line, candidate, false);

            return Result.FOUND;
        }

        return Result.MORE;
    }

    private void writeCandidate(StringBuilder line, String candidate, boolean hasMore) {
        line.append(candidate);
        System.out.print(candidate);

        if (!hasMore) {
            line.append(' ');
            System.out.print(' ');
        }
    }

    public enum Result {

        NONE,
        FOUND,
        MORE;

    }
}
