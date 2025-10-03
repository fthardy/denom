package io.github.fthardy.denom;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainIdentTest {

    @Test
    void equals__null_is_false() {
        assertFalse(new StringBasedAtomicIdentImplForTest(new IdentType("test"), "id").equals(null));
    }

    @Test
    void equals__same_instance_is_true() {
        DomainIdent instance = new StringBasedAtomicIdentImplForTest(new IdentType("test"), "id");
        assertTrue(instance.equals(instance));
    }

    @Test
    void equals__other_identical_instance_is_true() {
        DomainIdent instance1 = new StringBasedAtomicIdentImplForTest(new IdentType("test"), "id");
        DomainIdent instance2 = new StringBasedAtomicIdentImplForTest(new IdentType("test"), "id");
        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));
    }

    @Test
    void equals__other_implementation_type_is_false() {
        DomainIdent instance1 = new StringBasedAtomicIdentImplForTest(new IdentType("test"), "foo42");
        DomainIdent instance2 = new AtomicIdentImplForTest(new IdentType("test"), "foo", 42);
        assertFalse(instance1.equals(instance2));
        assertFalse(instance2.equals(instance1));
    }

    @Test
    void equalsAndHashCodeWithHashSet() {

        IdentType type = new IdentType("test");

        DomainIdent instance = new StringBasedAtomicIdentImplForTest(type, "foo42");
        DomainIdent otherEqual = new StringBasedAtomicIdentImplForTest(type, "foo42");
        DomainIdent otherNotEqual = new StringBasedAtomicIdentImplForTest(type, "foo-42");
        DomainIdent otherWithOtherType = new StringBasedAtomicIdentImplForTest(new IdentType("other"), "foo42");
        DomainIdent otherImpl = new AtomicIdentImplForTest(type, "foo", 42);

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

        assertTrue(set.add(otherWithOtherType));
        assertThat(set).hasSize(4);

        assertTrue(set.remove(instance));
        assertTrue(set.remove(otherNotEqual));
        assertTrue(set.remove(new AtomicIdentImplForTest(type, "foo", 42))); // Other impl but different instance
        assertTrue(set.remove(otherWithOtherType));

        assertThat(set).isEmpty();
    }
}
