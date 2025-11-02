package io.github.fthardy.denom.convert.impl;

import io.github.fthardy.denom.AtomicIdent;
import io.github.fthardy.denom.CompositeIdent;
import io.github.fthardy.denom.DomainIdent;
import io.github.fthardy.denom.convert.util.AtomicIdentCanonicalConverter;
import io.github.fthardy.denom.convert.util.CompositeIdentFactory;
import io.github.fthardy.denom.convert.util.DomainIdentCanonicalConverterSupportRegistry;
import io.github.fthardy.denom.convert.util.IdentTypeClass2AliasBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DefaultDomainIdentCanonicalConverterTest {

    static final class AtomicIdent1 extends AtomicIdent<String> {
        AtomicIdent1(String identitySequence) {
            super(identitySequence);
        }
    }

    static final class AtomicIdent1Converter implements AtomicIdentCanonicalConverter<AtomicIdent1> {

        @Override
        public IdentTypeClass2AliasBinding<AtomicIdent1> typeClass2AliasBinding() {
            return new IdentTypeClass2AliasBinding<>(AtomicIdent1.class, "atomic1");
        }

        @Override
        public AtomicIdent1 fromCanonical(String identifierSequence) {
            if (identifierSequence.equals("throw")) {
                throw new RuntimeException("atomic1");
            }
            return new AtomicIdent1(identifierSequence);
        }
    }

    static final class AtomicIdent2 extends AtomicIdent<String> {
        AtomicIdent2(String identitySequence) {
            super(identitySequence);
        }
    }

    static final class AtomicIdent2Factory implements AtomicIdentCanonicalConverter<AtomicIdent2> {

        @Override
        public IdentTypeClass2AliasBinding<AtomicIdent2> typeClass2AliasBinding() {
            return new IdentTypeClass2AliasBinding<>(AtomicIdent2.class, "atomic2");
        }

        @Override
        public AtomicIdent2 fromCanonical(String identifierSequence) {
            return new AtomicIdent2(identifierSequence);
        }
    }

    static final class AtomicIdent3 extends AtomicIdent<String> {
        AtomicIdent3(String identitySequence) {
            super(identitySequence);
        }
    }

    static final class AtomicIdent3Factory implements AtomicIdentCanonicalConverter<AtomicIdent3> {

        @Override
        public IdentTypeClass2AliasBinding<AtomicIdent3> typeClass2AliasBinding() {
            return new IdentTypeClass2AliasBinding<>(AtomicIdent3.class, "atomic3");
        }

        @Override
        public AtomicIdent3 fromCanonical(String identifierSequence) {
            return new AtomicIdent3(identifierSequence);
        }
    }

    static final class AtomicIdent4 extends AtomicIdent<String> {
        AtomicIdent4(String identitySequence) {
            super(identitySequence);
        }
    }

    static final class AtomicIdent4Factory implements AtomicIdentCanonicalConverter<AtomicIdent4> {

        @Override
        public IdentTypeClass2AliasBinding<AtomicIdent4> typeClass2AliasBinding() {
            return new IdentTypeClass2AliasBinding<>(AtomicIdent4.class, "atomic4");
        }

        @Override
        public AtomicIdent4 fromCanonical(String identifierSequence) {
            return new AtomicIdent4(identifierSequence);
        }
    }

    static final class AtomicIdent5 extends AtomicIdent<String> {
        AtomicIdent5(String identitySequence) {
            super(identitySequence);
        }
    }

    static final class AtomicIdent5Factory implements AtomicIdentCanonicalConverter<AtomicIdent5> {

        @Override
        public IdentTypeClass2AliasBinding<AtomicIdent5> typeClass2AliasBinding() {
            return new IdentTypeClass2AliasBinding<>(AtomicIdent5.class, "atomic5");
        }

        @Override
        public AtomicIdent5 fromCanonical(String identifierSequence) {
            return new AtomicIdent5(identifierSequence);
        }
    }

    static final class CompositeIdent1 extends CompositeIdent {
        CompositeIdent1(AtomicIdent1 atomicIdent1, AtomicIdent2 atomicIdent2, AtomicIdent5 atomicIdent5) {
            super(atomicIdent1, atomicIdent2, atomicIdent5);
        }
    }

    static final class CompositeIdent1Factory implements CompositeIdentFactory<CompositeIdent1> {

        @Override
        public IdentTypeClass2AliasBinding<CompositeIdent1> typeClass2AliasBinding() {
            return new IdentTypeClass2AliasBinding<>(CompositeIdent1.class, "composite1");
        }

        @Override
        public CompositeIdent1 fromComponents(DomainIdent first, DomainIdent second, DomainIdent... further) {
            return new CompositeIdent1((AtomicIdent1) first, (AtomicIdent2) second, (AtomicIdent5) further[0]);
        }
    }

    static final class CompositeIdent2 extends CompositeIdent {
        CompositeIdent2(AtomicIdent3 atomicIdent3, CompositeIdent3 compositeIdent3) {
            super(atomicIdent3, compositeIdent3);
        }
    }

    static final class CompositeIdent2Factory implements CompositeIdentFactory<CompositeIdent2> {

        @Override
        public IdentTypeClass2AliasBinding<CompositeIdent2> typeClass2AliasBinding() {
            return new IdentTypeClass2AliasBinding<>(CompositeIdent2.class, "composite2");
        }

        @Override
        public CompositeIdent2 fromComponents(DomainIdent first, DomainIdent second, DomainIdent... further) {
            return new CompositeIdent2((AtomicIdent3) first, (CompositeIdent3) second);
        }
    }

    static final class CompositeIdent3 extends CompositeIdent {
        CompositeIdent3(CompositeIdent1 compositeIdent1, AtomicIdent4 atomicIdent4) {
            super(compositeIdent1, atomicIdent4);
        }
    }

    static final class CompositeIdent3Factory implements CompositeIdentFactory<CompositeIdent3> {

        @Override
        public IdentTypeClass2AliasBinding<CompositeIdent3> typeClass2AliasBinding() {
            return new IdentTypeClass2AliasBinding<>(CompositeIdent3.class, "composite3");
        }

        @Override
        public CompositeIdent3 fromComponents(DomainIdent first, DomainIdent second, DomainIdent... further) {
            return new CompositeIdent3((CompositeIdent1) first, (AtomicIdent4) second);
        }
    }

    private DefaultDomainIdentCanonicalConverter converter;

    @BeforeEach
    void setUp() {
        DomainIdentCanonicalConverterSupportRegistry registry = new DomainIdentCanonicalConverterSupportRegistry();
        converter = new DefaultDomainIdentCanonicalConverter(registry);
        Stream.of( //
                new AtomicIdent1Converter(), new AtomicIdent2Factory(), new AtomicIdent3Factory(), new AtomicIdent4Factory(), new AtomicIdent5Factory(), //
                new CompositeIdent1Factory(), new CompositeIdent2Factory(), new CompositeIdent3Factory()).forEach(registry::addSupport);
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
    void toCanonical__Can_be_represented_as_URI() {
        AtomicIdent1 atomicIdent = new AtomicIdent1("atomic1");
        CompositeIdent2 compositeIdent = new CompositeIdent2(
                new AtomicIdent3("atomic3"),
                new CompositeIdent3(
                        new CompositeIdent1(
                                new AtomicIdent1("atomic1"),
                                new AtomicIdent2("atomic2"),
                                new AtomicIdent5("atomic5")),
                        new AtomicIdent4("atomic4")));

        assertDoesNotThrow(() -> URI.create(converter.toCanonical(atomicIdent)));
        assertDoesNotThrow(() -> URI.create(converter.toCanonical(compositeIdent)));
    }

    @Test
    void fromCanonical__Invalid_start_of_type_alias() {
        String canonical = "(foo(test-id)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(CanonicalParseContext.Messages.invalidStartOfSymbol('(', "type-alias", 0, canonical));
    }

    @Test
    void fromCanonical__Invalid_content_begin() {
        String canonical = "atomic1)test-id)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(CanonicalDomainIdentParser.Messages.invalidContentBegin(canonical, 0));
    }

    @Test
    void fromCanonical__Unknown_type_ID() {
        String canonical = "foo(test-id)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(CanonicalDomainIdentParser.Messages.unknownTypeAlias("foo", canonical, 0));
    }

    @Test
    void fromCanonical__Cannot_create_atomic_ident() {
        String canonical = "atomic1(throw)";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(CanonicalDomainIdentParser.Messages.parsingFailed(canonical, 13));
        assertThat(exception.getCause()).isInstanceOf(RuntimeException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("atomic1");
    }

    @Test
    void fromCanonical__Expected_End() {
        String canonical = "atomic1(test)x";
        CanonicalParseException exception = assertThrows(CanonicalParseException.class, () -> converter.fromCanonical(canonical));
        assertThat(exception.getMessage()).isEqualTo(CanonicalParseContext.Messages.expectedEndOfCanonical(canonical, 13));
    }
}