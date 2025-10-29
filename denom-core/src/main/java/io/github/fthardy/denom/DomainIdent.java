package io.github.fthardy.denom;

import io.github.fthardy.denom.converter.DefaultDomainIdentCanonicalConverterSupplier;
import io.github.fthardy.denom.converter.DomainIdentCanonicalConverter;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * The base type representing a domain identifier.<p>
 * Domain identifiers represent identifiers for domain entities. Any domain identifier can be one of the two categories: {@link AtomicIdent atomic} or
 * {@link CompositeIdent composite} identifiers.</p>
 * <p>Technically, domain identifiers are immutable value type objects.</p><p>
 * Domain identifiers are meant to be used as business keys for domain entities. They might be used as technical keys as well but this is not the main idea.
 * Each concrete domain identifier implementation class has to define and register a unique type-ID by as an instance of {@link IdentType} that is registered
 * with the class of the implementation. This is done by the constructor.
 * </p><p>It is very important to note that the type-ID is an internal detail which should not be exposed publicly.</p><p>
 * Each domain identifier can be converted into a canonical string representation. This is done by invoking {@link DomainIdent#toCanonical()}.</p><p>
 * With {@link DomainIdent#fromCanonical(String)} such a canonical string representation can be reconverted into a domain identifier implementation instance.
 * </p>
 */
public sealed abstract class DomainIdent permits AtomicIdent, CompositeIdent {

    private static volatile Supplier<DomainIdentCanonicalConverter> _converterProvider;

    /**
     * Initialize a different supplier for an implementation instance of a {@link DomainIdentCanonicalConverter}.
     * <strong><p>IMPORTANT NOTE:</p><p>
     * This method must be invoked before any invocation of {@link DomainIdent#fromCanonical(String)} and before any domain identifier instance has been
     * created!
     * </p></strong>
     *
     * @param converterProvider the supplier implementation.
     *
     * @throws IllegalStateException when the supplier implementation already has been initialized.
     */
    public static void initDomainIdentCanonicalConverterImplProvider(Supplier<DomainIdentCanonicalConverter> converterProvider) {
        if (_converterProvider == null) {
            _converterProvider = Objects.requireNonNull(converterProvider);
        } else {
            throw new IllegalStateException("Cannot initialize! A provider implementation is already set: " + _converterProvider.getClass().getName());
        }
    }

    // TODO Make it package private and provide a test base in this package with a tearDown that calls reset
    public static void resetDomainIdentCanonicalConverterImplProvider() {
        _converterProvider = null;
    }

    // lazy-init, singleton access
    private static Supplier<DomainIdentCanonicalConverter> converterProvider() {
        if (_converterProvider == null) {
            synchronized (DomainIdent.class) {
                if (_converterProvider == null) {
                    _converterProvider = new DefaultDomainIdentCanonicalConverterSupplier();
                }
            }
        }
        return _converterProvider;
    }

    /**
     * Converts a string that is presumably a canonical representation of a domain identifier back into a domain identifier instance.
     *
     * @param canonical the canonical string to convert.
     *
     * @return a new instance of a domain identifier.
     *
     * @see #toCanonical()
     */
    public static DomainIdent fromCanonical(String canonical) {
        return converterProvider().get().fromCanonical(canonical);
    }

    private final IdentType type;

    /**
     * Initializes the new instance.
     *
     * @param type the identity type for the new domain identifier.
     */
    protected DomainIdent(IdentType type) {
        converterProvider().get().assertSupports(type);
        this.type = type;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        return object != null && getClass().equals(object.getClass()) && type().equals(((DomainIdent) object).type());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), type());
    }

    /**
     * @return the name of the type of this domain identifier.
     */
    public final IdentType type() {
        return type;
    }

    /**
     * Converts this domain identifier instance into a canonical string representation.
     *
     * @return the canonical string representation of this domain identifier.
     *
     * @see #fromCanonical(String)
     */
    public String toCanonical() {
        return converterProvider().get().toCanonical(this);
    }
}
