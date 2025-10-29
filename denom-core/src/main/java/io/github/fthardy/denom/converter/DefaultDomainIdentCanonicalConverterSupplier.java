package io.github.fthardy.denom.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * TODO
 */
public final class DefaultDomainIdentCanonicalConverterSupplier implements Supplier<DomainIdentCanonicalConverter> {

    static Logger _log = Logger.getLogger(DefaultDomainIdentCanonicalConverterSupplier.class.getName());

    static DomainIdentCanonicalConverter loadCanonicalConverterImpl(Iterable<DomainIdentCanonicalConverter> implementations) {

        final List<DomainIdentCanonicalConverter> converters = new ArrayList<>();
        implementations.forEach(converters::add);

        DomainIdentCanonicalConverter converterInstance = new DefaultDomainIdentCanonicalConverter();
        if (converters.size() > 1) {
            _log.warning(Messages.ambiguousImplementationsOnClasspath(converters));
        } else if (converters.size() == 1) { // replace the default implementation with the custom implementation
            converterInstance = converters.getFirst();
        }
        return converterInstance;
    }

    private final DomainIdentCanonicalConverter converterInstance;

    public DefaultDomainIdentCanonicalConverterSupplier() {
        converterInstance = loadCanonicalConverterImpl(ServiceLoader.load(DomainIdentCanonicalConverter.class));
    }

    @Override
    public DomainIdentCanonicalConverter get() {
        return converterInstance;
    }

    static final class Messages {

        private Messages() {}

        static String ambiguousImplementationsOnClasspath(List<DomainIdentCanonicalConverter> converters) {
            return "More than one implementation for class '%s' found on class path:\n%s\n!!! Falling back to default implementation !!!".formatted( //
                    DomainIdentCanonicalConverter.class.getName(),
                    converters.stream().map(c -> c.getClass().getName()).collect(Collectors.joining("\n")));
        }
    }
}
