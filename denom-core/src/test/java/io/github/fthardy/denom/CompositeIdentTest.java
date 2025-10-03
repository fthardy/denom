package io.github.fthardy.denom;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompositeIdentTest {

    @Test
    void creation__composite_identities_are_not_allowed_to_be_null() {
        IdentType type = new IdentType("test");
        assertThrows(NullPointerException.class, () -> new CompositeIdentImplForTest(type, null));
        assertThrows(NullPointerException.class, () -> new CompositeIdentImplForTest(type, //
                        new StringBasedAtomicIdentImplForTest(type, "1"),
                        null,
                        new StringBasedAtomicIdentImplForTest(type, "2")));
    }

    @Test
    void creation__at_least_two_identities_must_be_defined() {
        IdentType type = new IdentType("test");
        assertThrows(IllegalArgumentException.class, () -> //
                new CompositeIdentImplForTest(new IdentType("test"), new StringBasedAtomicIdentImplForTest(type, "1")));

        assertDoesNotThrow(() -> new CompositeIdentImplForTest(type, //
                new StringBasedAtomicIdentImplForTest(type, "1"),
                new StringBasedAtomicIdentImplForTest(type,"2")));
    }

    @Test
    void creation__all_identities_must_be_distinct() {
        IdentType type = new IdentType("test");
        assertThrows(IllegalArgumentException.class, () -> new CompositeIdentImplForTest(type, //
                new StringBasedAtomicIdentImplForTest(type, "1"),
                new StringBasedAtomicIdentImplForTest(type, "1")));
    }

    @Test
    void equalsAndHashCodeWithSet() {

        IdentType type = new IdentType("test");

        CompositeIdent instance = new CompositeIdentImplForTest(type,
                new StringBasedAtomicIdentImplForTest(type, "foo"),
                new StringBasedAtomicIdentImplForTest(type, "bar"));
        assertThat(instance.components()).hasSize(2);

        CompositeIdent equalInstance = new CompositeIdentImplForTest(type,
                new StringBasedAtomicIdentImplForTest(type, "foo"),
                new StringBasedAtomicIdentImplForTest(type, "bar"));
        assertThat(equalInstance).isEqualTo(instance);
        CompositeIdent notEqualInstance = new CompositeIdentImplForTest(type,
                new StringBasedAtomicIdentImplForTest(type, "foo"),
                new StringBasedAtomicIdentImplForTest(type, "bar"),
                new StringBasedAtomicIdentImplForTest(type, "baz"));
        assertThat(notEqualInstance).isNotEqualTo(instance);

        DomainIdent otherImpl = new StringBasedAtomicIdentImplForTest(type, "foo");

        Set<DomainIdent> set = new HashSet<>();

        assertTrue(set.add(instance));
        assertThat(set).hasSize(1);

        assertFalse(set.add(instance));
        assertThat(set).hasSize(1);

        assertFalse(set.add(equalInstance));
        assertThat(set).hasSize(1);

        assertTrue(set.add(notEqualInstance));
        assertThat(set).hasSize(2);

        assertTrue(set.add(otherImpl));
        assertThat(set).hasSize(3);

        assertTrue(set.remove(notEqualInstance));
        assertTrue(set.remove(otherImpl));
        assertTrue(set.remove(equalInstance));

        assertThat(set).isEmpty();
    }
}