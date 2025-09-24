package io.github.fthardy.denom;

import java.util.Objects;

/**
 * The base type representing a domain identifier.
 */
public sealed abstract class DomainIdent permits AtomicIdent, CompositeIdent {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj != null && this.getClass().equals(obj.getClass())) {
            DomainIdent other = (DomainIdent) obj;
            return type().equals(other.type());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type());
    }

    /**
     * @return the type of this domain identifier.
     */
    public abstract IdentType type();
}
