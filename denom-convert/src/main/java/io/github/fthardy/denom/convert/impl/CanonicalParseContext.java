package io.github.fthardy.denom.convert.impl;

/**
 * The context for parsing a canonical domain identifier representation.
 */
final class CanonicalParseContext {

    private final String canonical;

    private int index = 0;

    CanonicalParseContext(String canonical) {
        this.canonical = canonical;
    }

    public String getCanonical() {
        return canonical;
    }

    public int getIndex() {
        return index;
    }

    public char peek() {
        return canonical.charAt(index);
    }

    public char next() {
        return canonical.charAt(index++);
    }

    public void consume(char c) {
        char next = next();
        if (next != c) {
            throw new CanonicalParseException(Messages.missingExpectedCharacter(c, canonical, index));
        }
    }

    public void assertEndOfSequence() {
        if (index != canonical.length()) {
            throw new CanonicalParseException(Messages.expectedEndOfCanonical(canonical, index));
        }
    }

    public String parseTypeAlias() {
        return parseSymbol("type-alias");
    }

    public String parseIdentityValue() {
        return parseSymbol("identity-value");
    }

    private boolean isNonBoundaryChar(char c) {
        return c != DefaultDomainIdentCanonicalConverter.CONTENT_BEGIN && c != DefaultDomainIdentCanonicalConverter.CONTENT_END;
    }

    private String parseSymbol(String name) {
        if (!isNonBoundaryChar(peek())) {
            throw new CanonicalParseException(Messages.invalidStartOfSymbol(peek(), name, index, canonical));
        }
        StringBuilder symbol = new StringBuilder();
        do {
            symbol.append(next());
        } while (isNonBoundaryChar(peek()));
        return symbol.toString();
    }

    static final class Messages {
        private Messages() {}

        static String missingExpectedCharacter(char c, String canonical, int position) {
            return "Char '%s' expected in canonical '%s' at position %d!".formatted(c, canonical, position);
        }

        static String expectedEndOfCanonical(String canonical, int position) {
            return "Expected end of canonical '%s' at position %d!".formatted(canonical, position);
        }

        static String invalidStartOfSymbol(char c, String name, int index, String canonical) {
            return "Invalid character '%s' at begin of expected %s at position %d in canonical %s!".formatted(c, name, index, canonical);
        }
    }
}
