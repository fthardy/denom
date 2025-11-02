package io.github.fthardy.denom;

/** Implementation of an atomic identifier for testing. */
final class OrderIdent extends AtomicIdent<Integer> {
    OrderIdent(Integer identityValue) {
        super(identityValue);
    }
}
