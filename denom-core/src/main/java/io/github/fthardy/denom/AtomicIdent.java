package io.github.fthardy.denom;

import java.util.Objects;

/**
 * Represents an atomic domain identifier.
 * <p>
 * An atomic identifier holds an identity value. The value can be any type of object the only constraint is that the type of the identity value should be an
 * immutable value type. However, there is no way to ensure this for any subtype. So it is up to the implementor of a concrete subtype of this class to choose
 * such an immutable value type (like e.g. String or Integer etc.).
 * </p>
 *
 * @param <T> the type of the identity value.
 */
public non-sealed abstract class AtomicIdent<T> extends DomainIdent {

    private final T identityValue;

    /**
     * Initializes a new instance of an atomic domain identifier.
     *
     * @param identityValue the identity value.
     */
    protected AtomicIdent(T identityValue) {
        this.identityValue = Objects.requireNonNull(identityValue);
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object) && getIdentityValue().equals(((AtomicIdent<?>) object).getIdentityValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdentityValue());
    }

    @Override
    public String toString() {
        return "%s:%s".formatted(getClass().getSimpleName(), getIdentityValue().toString());
    }

    /**
     * @return the raw identity value.
     */
    public T getIdentityValue() {
        return identityValue;
    }
}
