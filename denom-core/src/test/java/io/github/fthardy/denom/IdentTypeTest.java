package io.github.fthardy.denom;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdentTypeTest {

    @Test
    void nameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new IdentType(null));
    }

    @Test
    void nameMatchesPattern() {
        assertThat(new IdentType("ag-nr").name()).isEqualTo("ag-nr");
    }

    @Test
    void nameDoesNotMatchPattern() {
        assertThrows(IllegalArgumentException.class, () -> new IdentType("AG-Nr"));
    }
}