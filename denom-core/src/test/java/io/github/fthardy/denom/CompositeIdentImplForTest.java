package io.github.fthardy.denom;

public final class CompositeIdentImplForTest extends CompositeIdent {

    @DomainIdentProducer
    static CompositeIdentImplForTest produce(DomainIdent first, DomainIdent second, DomainIdent... further) {
        return new CompositeIdentImplForTest(first, second, further);
    }

    public CompositeIdentImplForTest(DomainIdent first, DomainIdent second, DomainIdent... further) {
        super("composite", first, second, further);
    }
}
