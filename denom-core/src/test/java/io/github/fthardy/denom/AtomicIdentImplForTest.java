package io.github.fthardy.denom;

import java.util.Objects;

public final class AtomicIdentImplForTest extends AtomicIdent {

    private final String text;
    private final Integer number;

    public AtomicIdentImplForTest(String text, Integer number) {
        super("atomic");
        this.text = Objects.requireNonNull(text);
        this.number = Objects.requireNonNull(number);
    }

    @Override
    public String getIdentitySequence() {
        return text + number;
    }
}
