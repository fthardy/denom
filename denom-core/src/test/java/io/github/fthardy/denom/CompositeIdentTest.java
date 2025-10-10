package io.github.fthardy.denom;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompositeIdentTest {

    @Test
    void creation__composite_identities_are_not_allowed_to_be_null() {
        assertThrows(NullPointerException.class, () -> new CompositeIdentImplForTest(null, null));
        assertThrows(NullPointerException.class, () -> new CompositeIdentImplForTest(new StringBasedAtomicIdentImplForTest("1"), null));
        assertThrows(NullPointerException.class, () -> new CompositeIdentImplForTest( //
                new StringBasedAtomicIdentImplForTest("1"), new StringBasedAtomicIdentImplForTest("2"), (DomainIdent[]) null));
    }

    @Test
    void creation__at_least_two_identities_must_be_defined() {
        assertDoesNotThrow(() -> new CompositeIdentImplForTest( //
                new StringBasedAtomicIdentImplForTest("1"), new StringBasedAtomicIdentImplForTest("2")));
    }

    @Test
    void creation__all_identities_must_be_distinct() {
        assertThrows(IllegalArgumentException.class, () -> new CompositeIdentImplForTest(
                new StringBasedAtomicIdentImplForTest("1"), new StringBasedAtomicIdentImplForTest("1")));
    }

    @Test
    void equalsAndHashCodeWithSet() {

        CompositeIdent instance = new CompositeIdentImplForTest(
                new StringBasedAtomicIdentImplForTest("foo"),
                new StringBasedAtomicIdentImplForTest("bar"));
        assertThat(instance.components()).hasSize(2);

        CompositeIdent equalInstance = new CompositeIdentImplForTest(
                new StringBasedAtomicIdentImplForTest("foo"), new StringBasedAtomicIdentImplForTest("bar"));
        assertThat(equalInstance).isEqualTo(instance);
        CompositeIdent notEqualInstance = new CompositeIdentImplForTest(
                new StringBasedAtomicIdentImplForTest("foo"),
                new StringBasedAtomicIdentImplForTest("bar"),
                new StringBasedAtomicIdentImplForTest("baz"));
        assertThat(notEqualInstance).isNotEqualTo(instance);

        DomainIdent otherImpl = new StringBasedAtomicIdentImplForTest("foo");

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