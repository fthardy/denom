package io.github.fthardy.denom;

import java.util.Objects;

/**
 * Represents an atomic domain identifier.
 */
public non-sealed abstract class AtomicIdent extends DomainIdent {

    /**
     * Creates an instance from a particular domain identifier type name and identity sequence.
     *
     * @param typeId the domain identifier type name.
     * @param identitySequence the identity sequence.
     *
     * @return a new atomic identifier instance.
     */
    public static AtomicIdent createInstance(String typeId, String identitySequence) {
        return (AtomicIdent) createInstance(findAnnotatedProducerMethod( //
                DomainIdent.classForTypeId(typeId), new Class<?>[] { identitySequence.getClass() }), identitySequence);
    }

    /**
     * Initializes a new instance of an atomic domain identifier.
     *
     * @param typeId the type-ID for the new instance. IMPORTANT: Never expose the type-ID as parameter at the concrete implementation
     *                 constructors! Always set a unique, "hard coded" type-ID for each concrete implementation on construction.
     */
    protected AtomicIdent(String typeId) {
        super(typeId);
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
        return "%s:%s".formatted(typeId(), getIdentitySequence());
    }

    /**
     * @return the raw identity sequence as a string representation.
     */
    public abstract String getIdentitySequence();
}
