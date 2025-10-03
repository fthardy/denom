package io.github.fthardy.denom;

import java.util.Objects;

/**
 * The base type representing a domain identifier.
 */
public sealed abstract class DomainIdent permits AtomicIdent, CompositeIdent {

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
     * @return the type of this domain identifier.
     */
    public abstract IdentType type();
}
