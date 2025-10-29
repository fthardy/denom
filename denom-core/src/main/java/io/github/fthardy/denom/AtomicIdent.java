package io.github.fthardy.denom;

import java.util.Objects;

/**
 * Represents an atomic domain identifier.
 */
public non-sealed abstract class AtomicIdent extends DomainIdent {

    private final String identitySequence;

    /**
     * Initializes a new instance of an atomic domain identifier.
     *
     * @param identType the identity type for the new domain identifier.
     *                  <strong>The identity type is an internal detail. Do not expose it to the outside!</strong>
     * @param identitySequence the identity sequence.
     */
    protected AtomicIdent(IdentType identType, String identitySequence) {
        super(identType);
        this.identitySequence = Objects.requireNonNull(identitySequence);
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object) && getIdentitySequence().equals(((AtomicIdent) object).getIdentitySequence());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdentitySequence());
    }

    @Override
    public String toString() {
        return "%s:%s".formatted(type(), getIdentitySequence());
    }

    /**
     * @return the raw identity sequence as a string representation.
     */
    public String getIdentitySequence() {
        return identitySequence;
    }
}
