package io.github.fthardy.denom;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompositeIdentTest {

    @Test
    void toList__no_nulls_allowed() {
        assertThrows(NullPointerException.class, () -> CompositeIdent.toList(null, new UserIdent(42)));
        assertThrows(NullPointerException.class, () -> CompositeIdent.toList(new UserIdent(42), null));
        assertThrows(NullPointerException.class, () -> CompositeIdent.toList( //
                new UserIdent(42), new UserIdent(666), new OrderIdent(42), null));
    }

    @Test
    void toList__no_duplicates_allowed() {
        assertThrows(IllegalArgumentException.class, () -> //
                CompositeIdent.toList(new UserIdent(42), new UserIdent(666),  new UserIdent(42)));
    }

    @Test
    void toList__given_order_is_kept() {
        UserIdent ident1 = new UserIdent(42);
        OrderIdent ident2 = new OrderIdent(42);
        UserIdent ident3 = new UserIdent(666);

        assertThat(CompositeIdent.toList(ident1, ident2, ident3)).containsExactly(ident1, ident2, ident3);
    }

    @Test
    void equals__self_and_null() {
        UserOrderIdent ident = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));

        assertTrue(ident.equals(ident));
        assertFalse(ident.equals(null));
    }

    @Test
    void notEquals() {
        UserOrderIdent ident = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));
        UserOrderIdent anotherIdent = new UserOrderIdent(new UserIdent(123), new OrderIdent(666));

        assertNotEquals(ident, anotherIdent);
    }

    @Test
    void equals__reflexiveness() {
        UserOrderIdent ident = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));
        UserOrderIdent anotherIdent = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));

        assertEquals(ident, anotherIdent);
        assertEquals(anotherIdent, ident);
    }

    @Test
    void hashCode__equal_when_same_type_different_instances() {
        UserOrderIdent ident = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));
        UserOrderIdent anotherIdent = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));

        assertEquals(ident.hashCode(), anotherIdent.hashCode());
    }

    @Test
    void toString__equal() {
        UserOrderIdent ident = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));
        UserOrderIdent anotherIdent = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));

        assertEquals(ident.toString(), anotherIdent.toString());
    }

    @Test
    void toString__notEqual() {
        UserOrderIdent ident = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));
        UserOrderIdent anotherIdent = new UserOrderIdent(new UserIdent(123), new OrderIdent(666));

        assertNotEquals(ident.toString(), anotherIdent.toString());
    }

    @Test
    void components() {
        UserOrderIdent ident = new UserOrderIdent(new UserIdent(42), new OrderIdent(666));

        assertThat(ident.components()).containsExactly(new UserIdent(42), new OrderIdent(666));
    }
}