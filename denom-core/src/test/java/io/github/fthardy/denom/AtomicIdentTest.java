package io.github.fthardy.denom;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AtomicIdentTest {

    @Test
    void equals__constructor_allows_no_null_identity_value() {
        assertThrows(NullPointerException.class, () -> new AtomicIdent<Integer>(null) {});
    }

    @Test
    void equals__self_and_null() {
        UserIdent ident = new UserIdent(42);

        assertTrue(ident.equals(ident));
        assertFalse(ident.equals(null));
    }

    @Test
    void notEquals() {
        UserIdent ident = new UserIdent(42);
        UserIdent anotherIdent = new UserIdent(666);

        assertNotEquals(ident, anotherIdent);
    }

    @Test
    void notEquals__equal_identity_value_but_different_type() {
        UserIdent userIdent = new UserIdent(42);
        OrderIdent orderIdent = new OrderIdent(42);

        assertNotEquals(userIdent, orderIdent);
    }

    @Test
    void equals__reflexiveness() {
        UserIdent ident = new UserIdent(42);
        UserIdent anotherIdent = new UserIdent(42);

        assertEquals(ident, anotherIdent);
        assertEquals(anotherIdent, ident);
    }

    @Test
    void hashCode__equal_when_same_type_different_instances() {
        UserIdent ident = new UserIdent(42);
        UserIdent anotherIdent = new UserIdent(42);

        assertEquals(ident.hashCode(), anotherIdent.hashCode());
    }

    @Test
    void hashCode__not_equal_when_different_type_with_same_identity_value() {
        UserIdent userIdent = new UserIdent(42);
        OrderIdent orderIdent = new OrderIdent(42);

        assertNotEquals(userIdent.hashCode(), orderIdent.hashCode());
    }

    @Test
    void equals_and_hashCode() {
        UserIdent userIdent = new UserIdent(42);
        OrderIdent orderIdent = new OrderIdent(42);

        Set<DomainIdent> set = new HashSet<>();

        set.add(userIdent);
        set.add(orderIdent);
        assertThat(set).hasSize(2);

        assertTrue(set.remove(userIdent));
        assertThat(set).hasSize(1);

        assertTrue(set.remove(orderIdent));
        assertThat(set).isEmpty();
    }

    @Test
    void toString__equal() {
        UserIdent userIdent = new UserIdent(42);
        UserIdent anotherUserIdent = new UserIdent(42);

        assertEquals(userIdent.toString(), anotherUserIdent.toString());
    }

    @Test
    void toString__notEqual() {
        UserIdent userIdent = new UserIdent(42);
        OrderIdent orderIdent = new OrderIdent(42);

        assertNotEquals(userIdent.toString(), orderIdent.toString());
    }
}