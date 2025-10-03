package io.github.fthardy.denom;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AtomicIdentTest {

    @Test
    void testEquals() {
        IdentType type = new IdentType("test");
        assertThat(new AtomicIdentImplForTest(type, "foo", 42)).isEqualTo(new AtomicIdentImplForTest(type, "foo", 42));
        assertThat(new AtomicIdentImplForTest(type, "foo", 42)).isNotEqualTo(new AtomicIdentImplForTest(type, "foo", 666));
    }
}