package io.github.fthardy.denom;

public final class StringBasedAtomicIdentImplForTest extends StringBasedAtomicIdent {

    @DomainIdentProducer
    static StringBasedAtomicIdentImplForTest fromIdentitySequence(String identitySequence) {
        return new StringBasedAtomicIdentImplForTest(identitySequence);
    }

    public StringBasedAtomicIdentImplForTest(String identitySequence) {
        super("string-atomic", identitySequence);
    }
}
