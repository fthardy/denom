package io.github.fthardy.denom;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

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

    @Test
    void sortOrderIsAscending() {
        IdentType type1 = new IdentType("t3");
        IdentType type2 = new IdentType("t1");
        IdentType type3 = new IdentType("t2");

        List<IdentType> list = Stream.of(type1, type2, type3).sorted().toList();
        assertThat(list).containsExactly(type2, type3, type1);
    }
}