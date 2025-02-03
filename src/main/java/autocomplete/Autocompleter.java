package autocomplete;

import autocomplete.impl.BuiltinCompletionResolver;
import autocomplete.impl.ExecutableCompletionResolver;

import java.util.Comparator;
import java.util.List;

public class Autocompleter {

    public static final Comparator<String> SHORTEST_FIRST = Comparator.comparingInt(String::length).thenComparing(String::compareTo);


    public final List<CompletionResolver> resolvers = List.of(
            BuiltinCompletionResolver.INSTANCE,
            ExecutableCompletionResolver.INSTANCE
    );

}
