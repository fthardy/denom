package io.github.fthardy.denom;

import java.util.Objects;

/**
 * Represents an atomic domain identifier.
 */
public non-sealed abstract class AtomicIdent extends DomainIdent {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (super.equals(obj) && this.getClass().equals(obj.getClass())) {
            AtomicIdent other = (AtomicIdent) obj;
            return this.getIdentitySequence().equals(other.getIdentitySequence());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdentitySequence());
    }

    /**
     * @return the raw identity sequence as a string representation.
     */
    public abstract String getIdentitySequence();
}
