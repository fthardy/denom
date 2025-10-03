package io.github.fthardy.denom;

final class CompositeIdentImplForTest extends CompositeIdent {

    private final IdentType type;

    CompositeIdentImplForTest(IdentType type, DomainIdent first, DomainIdent... further) {
        super(first, further);
        this.type = type;
    }

    @Override
    public IdentType type() {
        return type;
    }
}
