package io.github.fthardy.denom;

/** Implementation of an atomic identifier for testing. */
final class UserIdent extends AtomicIdent<Integer> {
    UserIdent(Integer identityValue) {
        super(identityValue);
    }
}
