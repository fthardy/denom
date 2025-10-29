package io.github.fthardy.denom.converter;

import io.github.fthardy.denom.AtomicIdent;
import io.github.fthardy.denom.CompositeIdent;
import io.github.fthardy.denom.DomainIdent;
import io.github.fthardy.denom.IdentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static io.github.fthardy.denom.converter.DefaultDomainIdentCanonicalConverter.CanonicalParseException;
import static io.github.fthardy.denom.converter.DefaultDomainIdentCanonicalConverter.Messages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DefaultDomainIdentCanonicalConverterTest {

    static final class AtomicIdent1 extends AtomicIdent {
        AtomicIdent1(String identitySequence) {
            super(new IdentType("atomic1"), identitySequence);
        }
    }

    static final class AtomicIdent1Factory implements AtomicIdentFactory {

        @Override
        public IdentType type() {
            return new IdentType("atomic1");
        }

        @Override
        public AtomicIdent createFrom(String identifierSequence) {
            if (identifierSequence.equals("throw")) {
                throw new RuntimeException("atomic1");
            }
            return new AtomicIdent1(identifierSequence);
        }
    }

    static final class AtomicIdent2 extends AtomicIdent {
        AtomicIdent2(String identitySequence) {
            super(new IdentType("atomic2"), identitySequence);
        }
    }

    static final class AtomicIdent2Factory implements AtomicIdentFactory {

        @Override
        public IdentType type() {
            return new IdentType("atomic2");
        }

        @Override
        public AtomicIdent createFrom(String identifierSequence) {
            return new AtomicIdent2(identifierSequence);
        }
    }

    static final class AtomicIdent3 extends AtomicIdent {
        AtomicIdent3(String identitySequence) {
            super(new IdentType("atomic3"), identitySequence);
        }
    }

    static final class AtomicIdent3Factory implements AtomicIdentFactory {

        @Override
        public IdentType type() {
            return new IdentType("atomic3");
        }

        @Override
        public AtomicIdent createFrom(String identifierSequence) {
            return new AtomicIdent3(identifierSequence);
        }
    }

    static final class AtomicIdent4 extends AtomicIdent {
        AtomicIdent4(String identitySequence) {
            super(new IdentType("atomic4"), identitySequence);
        }
    }

    static final class AtomicIdent4Factory implements AtomicIdentFactory {

        @Override
        public IdentType type() {
            return new IdentType("atomic4");
        }

        @Override
        public AtomicIdent createFrom(String identifierSequence) {
            return new AtomicIdent4(identifierSequence);
        }
    }

    static final class AtomicIdent5 extends AtomicIdent {
        AtomicIdent5(String identitySequence) {
            super(new IdentType("atomic5"), identitySequence);
        }
    }

    static final class AtomicIdent5Factory implements AtomicIdentFactory {

        @Override
        public IdentType type() {
            return new IdentType("atomic5");
        }

        @Override
        public AtomicIdent createFrom(String identifierSequence) {
            return new AtomicIdent5(identifierSequence);
        }
    }

    static final class CompositeIdent1 extends CompositeIdent {
        CompositeIdent1(AtomicIdent1 atomicIdent1, AtomicIdent2 atomicIdent2, AtomicIdent5 atomicIdent5) {
            super(new IdentType("composite1"), atomicIdent1, atomicIdent2, atomicIdent5);
        }
    }

    static final class CompositeIdent1Factory implements CompositeIdentFactory {

        @Override
        public IdentType type() {
            return new IdentType("composite1");
        }

        @Override
        public CompositeIdent createFrom(DomainIdent first, DomainIdent second, DomainIdent... further) {
            return new CompositeIdent1((AtomicIdent1) first, (AtomicIdent2) second, (AtomicIdent5) further[0]);
        }
    }

    static final class CompositeIdent2 extends CompositeIdent {
        CompositeIdent2(AtomicIdent3 atomicIdent3, CompositeIdent3 compositeIdent3) {
            super(new IdentType("composite2"), atomicIdent3, compositeIdent3);
        }
    }

    static final class CompositeIdent2Factory implements CompositeIdentFactory {

        @Override
        public IdentType type() {
            return new IdentType("composite2");
        }

        @Override
        public CompositeIdent createFrom(DomainIdent first, DomainIdent second, DomainIdent... further) {
            return new CompositeIdent2((AtomicIdent3) first, (CompositeIdent3) second);
        }
    }

    static final class CompositeIdent3 extends CompositeIdent {
        CompositeIdent3(CompositeIdent1 compositeIdent1, AtomicIdent4 atomicIdent4) {
            super(new IdentType("composite3"), compositeIdent1, atomicIdent4);
        }
    }

    static final class CompositeIdent3Factory implements CompositeIdentFactory {

        @Override
        public IdentType type() {
            return new IdentType("composite3");
        }

        @Override
        public CompositeIdent createFrom(DomainIdent first, DomainIdent second, DomainIdent... further) {
            return new CompositeIdent3((CompositeIdent1) first, (AtomicIdent4) second);
        }
    }

    private DefaultDomainIdentCanonicalConverter converter;

    @BeforeEach
    void setUp() {
        this.converter = new DefaultDomainIdentCanonicalConverter(new DomainIdentFactoryProvider(List.of( //
                new AtomicIdent1Factory(), new AtomicIdent2Factory(), new AtomicIdent3Factory(), new AtomicIdent4Factory(), new AtomicIdent5Factory(), //
                new CompositeIdent1Factory(), new CompositeIdent2Factory(), new CompositeIdent3Factory())));
        DomainIdent.initDomainIdentCanonicalConverterImplProvider(()  -> this.converter);
    }

    @AfterEach
    void tearDown() {
        DomainIdent.resetDomainIdentCanonicalConverterImplProvider();
    }

    @Test
    void testConversionRoundTripFor_AtomicIdent() {
        AtomicIdent1 atomicIdent = new AtomicIdent1("atomic1");
        DomainIdent domainIdent = converter.fromCanonical(converter.toCanonical(atomicIdent));
        assertThat(domainIdent).isNotSameAs(atomicIdent);
        assertThat(domainIdent).isEqualTo(atomicIdent);
    }

    @Test
    void testConversionRoundTripFor_CompositeIdent() {
        CompositeIdent2 compositeIdent = new CompositeIdent2(
                new AtomicIdent3("atomic3"),
                new CompositeIdent3(
                        new CompositeIdent1(
                                new AtomicIdent1("atomic1"),
                                new AtomicIdent2("atomic2"),
                                new AtomicIdent5("atomic5")),
                        new AtomicIdent4("atomic4")));
        DomainIdent domainIdent = converter.fromCanonical(converter.toCanonical(compositeIdent));
        assertThat(domainIdent).isNotSameAs(compositeIdent);
        assertThat(domainIdent).isEqualTo(compositeIdent);
    }

    @Test
    void fromCanonical__type_ID() {
        String canonical = "-foo(test-id)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.errorWhileParsing(canonical, 4));
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).endsWith("-foo");
    }

    @Test
    void fromCanonical__Invalid_start_of_type_ID() {
        String canonical = "(foo(test-id)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.invalidStartOfSymbol('(', "type-ID", 0, canonical));
    }

    @Test
    void fromCanonical__Invalid_content_begin() {
        String canonical = "atomic1)test-id)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.invalidContentBegin(canonical, 7));
    }

    @Test
    void fromCanonical__Invalid_start_of_identifier_sequence() {
        String canonical = "atomic1(]test-id)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.invalidStartOfSymbol(']', "identity-sequence", 8, canonical));
    }

    @Test
    void fromCanonical__Unknown_type_ID() {
        String canonical = "foo(test-id)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.errorWhileParsing(canonical, 3));
        assertThat(exception.getCause()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void fromCanonical__Cannot_create_atomic_ident() {
        String canonical = "atomic1(throw)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.errorWhileParsing(canonical, 14));
        assertThat(exception.getCause()).isInstanceOf(RuntimeException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("atomic1");
    }

    @Test
    void fromCanonical__Atomic_instead_of_composite() {
        String canonical = "atomic1[atomic2(foo),atomic3(bar)]";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.expectedCompositeIdent("atomic1"));
    }

    @Test
    void fromCanonical__Composite_instead_of_atomic() {
        String canonical = "composite1(foo)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.expectedAtomicIdent("composite1"));
    }

    @Test
    void fromCanonical__At_least_two_components_for_composite() {
        String canonical = "composite1[]";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.atLeastTwoComponents(canonical, 10));
    }

    @Test
    void fromCanonical__Missing_expected_character() {
        String canonical = "composite1[atomic1(test)]";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.missingExpectedCharacter(',', canonical, 25));
    }

    @Test
    void fromCanonical__Expected_End() {
        String canonical = "atomic1(test)x";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(Messages.expectedEndOfCanonical(canonical, 13));
    }
}