package io.github.fthardy.denom;

final class StringBasedAtomicIdentImplForTest extends StringBasedAtomicIdent {

    private final IdentType type;

    StringBasedAtomicIdentImplForTest(IdentType type, String identitySequence) {
        super(identitySequence);
        this.type = type;
    }

    @Override
    public IdentType type() {
        return type;
    }
}
