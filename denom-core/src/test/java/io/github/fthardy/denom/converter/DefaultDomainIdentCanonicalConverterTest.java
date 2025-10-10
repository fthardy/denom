package io.github.fthardy.denom.converter;

import io.github.fthardy.denom.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultDomainIdentCanonicalConverterTest {

    @Mock
    BiFunction<String, String, AtomicIdent> atomicIdentFactoryMock;

    @Mock
    BiFunction<String, DomainIdent[], CompositeIdent> compositeIdentFactoryMock;

    @Captor
    ArgumentCaptor<DomainIdent[]> componentCaptor;

    DefaultDomainIdentCanonicalConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DefaultDomainIdentCanonicalConverter(atomicIdentFactoryMock, compositeIdentFactoryMock);
    }

    @Test
    void toCanonical__AtomicIdent() {
        assertThat(converter.toCanonical(new AtomicIdentImplForTest("text", 42))).isEqualTo("atomic(text42)");
    }

    @Test
    void toCanonical__CompositeIdent() {
        CompositeIdentImplForTest ident = new CompositeIdentImplForTest( //
                new AtomicIdentImplForTest("text", 42), new CompositeIdentImplForTest( //
                        new StringBasedAtomicIdentImplForTest("foo"), new StringBasedAtomicIdentImplForTest("bar")));
        assertThat(converter.toCanonical(ident)).isEqualTo("composite[atomic(text42),composite[string-atomic(foo),string-atomic(bar)]]");
    }

    @Test
    void fromCanonical__Invalid_format() {
        assertThrows(IllegalArgumentException.class, () -> converter.fromCanonical("alkdjflkaj"));
    }

    @Test
    void fromCanonical__AtomicIdent_Invalid_format__missing_end() {
        assertThrows(IllegalArgumentException.class, () -> converter.fromCanonical("foo(bar"));
    }

    @Test
    void fromCanonical__CompositeIdent_Invalid_format__missing_end() {
        assertThrows(IllegalArgumentException.class, () -> converter.fromCanonical("foo[bar"));
    }

    @Test
    void fromCanonical__CompositeIdent_Invalid_format__not_enough_components() {
        assertThrows(IllegalArgumentException.class, () -> converter.fromCanonical("foo[]"));
        assertThrows(IllegalArgumentException.class, () -> converter.fromCanonical("foo[bar]"));
    }

    @Test
    void fromCanonical__AtomicIdent() {
        AtomicIdentImplForTest atomicIdent = new AtomicIdentImplForTest("foo", 42);
        when(atomicIdentFactoryMock.apply("atomic", "foo42")).thenReturn(atomicIdent);
        assertThat(converter.fromCanonical(converter.toCanonical(atomicIdent))).isSameAs(atomicIdent);
    }

    @Test
    void fromCanonical__CompositeIdent() {

        StringBasedAtomicIdentImplForTest component1 = new StringBasedAtomicIdentImplForTest("foo");
        StringBasedAtomicIdentImplForTest component2 = new StringBasedAtomicIdentImplForTest("bar");
        CompositeIdentImplForTest compositeIdent = new CompositeIdentImplForTest(component1, component2);

        when(compositeIdentFactoryMock.apply(eq("composite"), any())).thenReturn(compositeIdent);
        when(atomicIdentFactoryMock.apply("string-atomic", "foo")).thenReturn(component1);
        when(atomicIdentFactoryMock.apply("string-atomic", "bar")).thenReturn(component2);

        assertThat(converter.fromCanonical(converter.toCanonical(compositeIdent))).isSameAs(compositeIdent);

        verify(compositeIdentFactoryMock).apply(eq("composite"), componentCaptor.capture());
        DomainIdent[] components = componentCaptor.getValue();
        assertThat(components).hasSize(2);
        assertThat(components).contains(component1, component2);
    }
}