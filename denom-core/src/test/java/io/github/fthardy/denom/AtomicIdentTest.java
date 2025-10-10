package io.github.fthardy.denom;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AtomicIdentTest {

    @Test
    void test_equals() {
        assertThat(new AtomicIdentImplForTest("foo", 42)).isEqualTo(new AtomicIdentImplForTest( "foo", 42));
        assertThat(new AtomicIdentImplForTest( "foo", 42)).isNotEqualTo(new AtomicIdentImplForTest("foo", 666));
    }

    @Test
    void test_toString() {
        AtomicIdentImplForTest ident = new AtomicIdentImplForTest("foo", 42);
        assertThat(ident.toString()).isEqualTo("%s:%s".formatted(ident.typeId(), ident.getIdentitySequence()));
    }
}