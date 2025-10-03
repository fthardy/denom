package io.github.fthardy.denom;

import java.util.Objects;

final class AtomicIdentImplForTest extends AtomicIdent {

    private final IdentType type;

    private final String text;
    private final Integer number;

    AtomicIdentImplForTest(IdentType type, String text, Integer number) {
        this.type = Objects.requireNonNull(type);
        this.text = Objects.requireNonNull(text);
        this.number = Objects.requireNonNull(number);
    }

    @Override
    public IdentType type() {
        return type;
    }

    @Override
    public String getIdentitySequence() {
        return text + number;
    }
}
