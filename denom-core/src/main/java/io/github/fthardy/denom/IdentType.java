package io.github.fthardy.denom;

import java.io.Serial;
import java.io.Serializable;

/**
 * A type identifier for any domain identifiers.
 * <p>
 * An {@code IdentType} names the “kind” of a domain identifier (e.g., {@code isbn}, {@code isin}, {@code order}) and is intentionally minimal:
 * <ul>
 *   <li><b>Canonical form:</b> letters must be provided in lowercase.</li>
 *   <li><b>Limited length:</b> the name must have a length of 1-32 characters.</li>
 *   <li><b>Value object:</b> equality and hashing rely solely on {@code name}.</li>
 *   <li><b>Comparable:</b> natural sorting order on name.</li>
 * </ul>
 *
 * <h2>Allowed names</h2>
 * The name must match the following rules:
 * <ul>
 *   <li><b>First character:</b> must be a letter {@code a–z}.</li>
 *   <li><b>Allowed characters overall:</b> {@code a–z}, {@code 0–9}, hyphen {@code -}.</li>
 *   <li><b>Last character:</b> must be a letter or digit.</li>
 *   <li>No leading or trailing whitespace (not tolerated).</li>
 *   <li>No double hyphen {@code --} anywhere in the name.</li>
 * </ul>
 *
 * <h2>Examples</h2>
 * <pre>
 * new IdentType("customer");        // OK → "customer"
 * new IdentType("abc123");          // OK
 * new IdentType("foo1-bar2");       // OK
 *
 * new IdentType("1abc");            // IllegalArgumentException (must start with a letter)
 * new IdentType("abc-");            // IllegalArgumentException (trailing hyphen)
 * new IdentType("ab--cd");          // IllegalArgumentException (double hyphen)
 * new IdentType("foo.bar");         // IllegalArgumentException ('.' not allowed)
 * </pre>
 *
 * <h2>Evolution notes</h2>
 * <ul>
 *   <li>This class validates <b>syntax only</b>. An optional policy layer
 *       (e.g., reserved names, IANA registry checks) can be added later
 *       without changing the semantics of this value type.</li>
 *   <li>Serialization: increase {@code serialVersionUID} only when making
 *       truly incompatible changes (e.g., record component changes).</li>
 * </ul>
 *
 * @param name the domain type name of the identifier; stored canonically in lowercase
 *
 * @since 0.1
 */
public record IdentType(String name) implements Serializable, Comparable<IdentType> {

    @Serial private static final long serialVersionUID = 1L;

    /**
     * Checks a given name whether it is valid in terms of the defined rules.
     *
     * @param name the name to check.
     *
     * @return {@code true} if the name is valid.
     */
    public static boolean isValidName(String name) {
        return name.matches("^(?!.*--)[a-z](?:[a-z0-9-]{0,30}[a-z0-9])?$");
    }

    public IdentType {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Invalid name: %s".formatted(name));
        }
    }

    @Override
    public int compareTo(IdentType identType) {
        return name.compareTo(identType.name);
    }
}
