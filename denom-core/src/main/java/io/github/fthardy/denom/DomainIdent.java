package io.github.fthardy.denom;

/**
 * The base type representing a domain identifier.
 * <p>
 * Domain identifiers are intended to represent identifiers for domain entities. Any domain identifier can be either one of the following two categories:
 * {@link AtomicIdent atomic} or {@link CompositeIdent composite} identifiers.
 * </p>
 */
public sealed abstract class DomainIdent permits AtomicIdent, CompositeIdent {

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        return object != null && getClass().equals(object.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
