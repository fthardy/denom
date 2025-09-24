package io.github.fthardy.denom;

import java.util.Objects;

/**
 * A base implementation for an {@code AtomicIdent}-Type where the identity sequence can be represented by a string.
 */
public abstract class StringBasedAtomicIdent extends AtomicIdent {

    private final String identitySequence;

    /**
     * Initialize a new instance of this identifier.
     *
     * @param identitySequence the raw identity string sequence.
     */
    protected StringBasedAtomicIdent(String identitySequence) {
        this.identitySequence = Objects.requireNonNull(identitySequence);
    }

    @Override
    public String getIdentitySequence() {
        return identitySequence;
    }
}