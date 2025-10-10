package io.github.fthardy.denom;

import io.github.fthardy.denom.converter.DefaultDomainIdentCanonicalConverter;
import io.github.fthardy.denom.converter.DomainIdentCanonicalConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class DomainIdentTest {

    @Mock
    Logger loggerMock;

    @BeforeEach
    void setUp() {
        DomainIdent.log = loggerMock;
    }

    @AfterEach
    void tearDown() {
        DomainIdent._typeIdToClassRegistry.clear();
        DomainIdent.log = Logger.getLogger(DomainIdent.class.getName());
    }

    @Test
    void assertIsValidTypeId__Invalid() {
        assertThrows(IllegalArgumentException.class, () -> DomainIdent.assertIsValidTypeId("foo--bar"));
    }

    @Test
    void assertIsValidTypeId__Valid() {
        assertThat(DomainIdent.assertIsValidTypeId("foo-bar")).isEqualTo("foo-bar");
    }

    @Test
    void classForTypeId__No_class_registered_for_typeID() {
        assertThrows(NoSuchElementException.class, () -> DomainIdent.classForTypeId("foo"));
    }

    @Test
    void classForTypeId__Class_avalailable() {
        DomainIdent._typeIdToClassRegistry.put("foo", AtomicIdentImplForTest.class);
        assertThat(DomainIdent.classForTypeId("foo")).isSameAs(AtomicIdentImplForTest.class);
    }

    @Test
    void bindTypeIdToClass__None_bound_for_typeID() {
        assertThat(DomainIdent.bindTypeIdToClass("foo", AtomicIdentImplForTest.class)).isEqualTo("foo");
        assertThat(DomainIdent._typeIdToClassRegistry).hasSize(1);
        assertThat(DomainIdent._typeIdToClassRegistry).containsKey("foo");
        assertThat(DomainIdent._typeIdToClassRegistry).containsValue(AtomicIdentImplForTest.class);
    }

    @Test
    void bindTypeIdToClass__Class_must_be_final() {
        assertThrows(DomainIdent.InvalidImplementationClassException.class, () -> DomainIdent.bindTypeIdToClass("foo", DomainIdent.class));
    }

    @Test
    void bindTypeIdToClass__Class_is_already_bound() {
        DomainIdent.bindTypeIdToClass("foo", AtomicIdentImplForTest.class);
        assertThrows(IllegalStateException.class, () -> DomainIdent.bindTypeIdToClass("bar", AtomicIdentImplForTest.class));
    }

    @Test
    void bindTypeIdToClass__TypeID_is_alread_bound() {
        DomainIdent.bindTypeIdToClass("foo", AtomicIdentImplForTest.class);
        assertThrows(IllegalStateException.class, () -> DomainIdent.bindTypeIdToClass("foo", StringBasedAtomicIdentImplForTest.class));
    }

    @Test
    void bindTypeIdToClass__Already_bound() {
        DomainIdent.bindTypeIdToClass("foo", AtomicIdentImplForTest.class);
        assertDoesNotThrow(() -> DomainIdent.bindTypeIdToClass("foo", AtomicIdentImplForTest.class));
    }

    @Test
    void getCanonicalConverter__No_custom_implementation() {
        assertThat(DomainIdent.getCanonicalConverter(Collections::emptyList)).isInstanceOf(DefaultDomainIdentCanonicalConverter.class);
    }

    @Test
    void getCanonicalConverter__Custom_implementation() {
        DomainIdentCanonicalConverter customConverter = new DomainIdentCanonicalConverter() {
            @Override
            public DomainIdent fromCanonical(String canonical) {
                return null;
            }

            @Override
            public String toCanonical(DomainIdent domainIdent) {
                return "";
            }
        };
        assertThat(DomainIdent.getCanonicalConverter(() -> Collections.singleton(customConverter))).isSameAs(customConverter);
    }

    @Test
    void getCanonicalConverter__More_than_one_custom_implementation() {
        DomainIdentCanonicalConverter customConverter1 = new DomainIdentCanonicalConverter() {
            @Override
            public DomainIdent fromCanonical(String canonical) {
                return null;
            }

            @Override
            public String toCanonical(DomainIdent domainIdent) {
                return "";
            }
        };
        DomainIdentCanonicalConverter customConverter2 = new DomainIdentCanonicalConverter() {
            @Override
            public DomainIdent fromCanonical(String canonical) {
                return null;
            }

            @Override
            public String toCanonical(DomainIdent domainIdent) {
                return "";
            }
        };
        List<DomainIdentCanonicalConverter> converterList = List.of(customConverter1, customConverter2);

        // Fall back to default implementation!
        assertThat(DomainIdent.getCanonicalConverter(() -> converterList)).isInstanceOf(DefaultDomainIdentCanonicalConverter.class);

        verify(loggerMock).warning(DomainIdent.Messages.ambiguousImplementationsOnClasspath(converterList));
        verifyNoMoreInteractions(loggerMock);
    }

    @Test
    void findAnnotatedProducerMethod() {
        assertNotNull(DomainIdent.findAnnotatedProducerMethod(StringBasedAtomicIdentImplForTest.class, new Class[]{String.class}));
    }

    @Test
    void equals__false() {
        DomainIdent ident = new StringBasedAtomicIdentImplForTest("id");
        DomainIdent otherIdent = new StringBasedAtomicIdentImplForTest("other");
        DomainIdent differentIdent = new AtomicIdentImplForTest("foo", 42);
        for (Object o : new Object[] {null, new Object(), differentIdent, otherIdent}) {
            assertNotEquals(ident, o);
        }

        assertThat(ident.equals(otherIdent)).isEqualTo(otherIdent.equals(ident));
    }

    @Test
    void equals__same_instance_is_true() {
        DomainIdent ident = new StringBasedAtomicIdentImplForTest("id");
        DomainIdent otherEqualIdent = new StringBasedAtomicIdentImplForTest("id");
        for (Object o : new Object[] {ident, otherEqualIdent}) {
            assertEquals(ident, o);
        }

        assertThat(otherEqualIdent.equals(ident)).isEqualTo(ident.equals(otherEqualIdent));
    }

    @Test
    void equalsAndHashCodeWithHashSet() {

        DomainIdent instance = new StringBasedAtomicIdentImplForTest("foo42");
        DomainIdent otherEqual = new StringBasedAtomicIdentImplForTest("foo42");
        DomainIdent otherNotEqual = new StringBasedAtomicIdentImplForTest("foo-42");
        DomainIdent otherImpl = new AtomicIdentImplForTest("foo", 42);

        Set<DomainIdent> set = new HashSet<>();
        assertTrue(set.add(instance));
        assertThat(set).hasSize(1);

        assertFalse(set.add(instance));
        assertThat(set).hasSize(1);

        assertFalse(set.add(otherEqual));
        assertThat(set).hasSize(1);

        assertTrue(set.add(otherNotEqual));
        assertThat(set).hasSize(2);

        assertTrue(set.add(otherImpl));
        assertThat(set).hasSize(3);

        assertTrue(set.remove(instance));
        assertTrue(set.remove(otherNotEqual));
        assertTrue(set.remove(new AtomicIdentImplForTest("foo", 42))); // Other impl but different instance

        assertThat(set).isEmpty();
    }
}
