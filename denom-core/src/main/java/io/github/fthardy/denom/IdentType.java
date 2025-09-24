package io.github.fthardy.denom;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Domain-level type descriptor for identifiers.
 * <p>
 * An {@code IdentType} names the “kind” of an identifier (e.g., {@code customer},
 * {@code order}, {@code isbn}) and is intentionally minimal:
 * <ul>
 *   <li><b>Canonical form:</b> the name must be provided in lowercase.</li>
 *   <li>Limited length: the name must have a length of 1-32 characters.</li>
 *   <li><b>Conservative syntax:</b> compatible with the intersection of common URI/URN rules.</li>
 *   <li><b>Value object:</b> equality and hashing rely solely on {@code name}.</li>
 * </ul>
 *
 * <h2>Allowed names</h2>
 * The name must match the following pattern (1–32 characters):
 * <pre><code>{@value #REGEX_PATTERN}</code></pre>
 * Which implies:
 * <ul>
 *   <li>First character must be a letter {@code a–z}.</li>
 *   <li>Allowed characters overall: {@code a–z}, {@code 0–9}, hyphen {@code -}.</li>
 *   <li>No double hyphen {@code --} anywhere in the name.</li>
 *   <li>Last character must be alphanumeric (no trailing hyphen).</li>
 *   <li>No leading or trailing whitespace (not tolerated).</li>
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
public record IdentType(String name) implements Serializable {

    /** Defines the Reg-Ex-Pattern for the syntax validation. Every given name must match this pattern. */
    public static final String REGEX_PATTERN = "^(?!.*--)[a-z](?:[a-z0-9-]{0,30}[a-z0-9])?$";

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern PATTERN = Pattern.compile(REGEX_PATTERN);

    public IdentType {
        if (!PATTERN.matcher(Objects.requireNonNull(name, "The name must not be null!")).matches()) {
            throw new IllegalArgumentException("Invalid name '%s': Must match /%s/.".formatted(name, PATTERN.pattern()));
        }
    }
}
