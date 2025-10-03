package io.github.fthardy.denom;

import java.util.Objects;

/**
 * Represents an atomic domain identifier.
 */
public non-sealed abstract class AtomicIdent extends DomainIdent {

    @Override
    public boolean equals(Object object) {
        return super.equals(object) && getIdentitySequence().equals(((AtomicIdent) object).getIdentitySequence());
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
