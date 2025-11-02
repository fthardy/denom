package io.github.fthardy.denom;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a composite domain identifier.
 */
public non-sealed abstract class CompositeIdent extends DomainIdent {

    /**
     * A utility function which aggregates a number of given domain identifiers into a <code>List</code>-Instance.
     * <p>
     * This function makes sure that at least two domain identifiers are given and all identifiers are distinct. {@code null} is not a valid input value!
     * </p>
     *
     * @param first the first domain identifier.
     * @param second the second domain identifier.
     * @param further any further domain identifiers.
     *
     * @return the distinct list of domain identifiers in the given order.
     *
     * @throws IllegalArgumentException when there is a duplicate domain identifier in the given list.
     */
    static List<DomainIdent> toList(DomainIdent first, DomainIdent second, DomainIdent... further) {
        Set<DomainIdent> set = new LinkedHashSet<>();
        set.add(Objects.requireNonNull(first));
        set.add(Objects.requireNonNull(second));
        if (further.length > 0) {
            set.addAll(Arrays.stream(further).map(Objects::requireNonNull).toList());
        }
        if (set.size() != further.length + 2) {
            throw new IllegalArgumentException("Duplicate identifiers detected! Each component identifier must be unique within the composite!");
        }
        return set.stream().toList();
    }

    private final List<DomainIdent> components;

    /**
     * Initializes a new instance of a composite domain identifier.
     * <p>
     * At least two identifiers must be defined and no duplicate identifiers are allowed.
     * </p>
     *
     * @param first the first identifier.
     * @param second the second identifier.
     * @param further optionally any further identifiers.
     */
    protected CompositeIdent(DomainIdent first, DomainIdent second, DomainIdent... further) {
        this.components = toList(first, second, further);
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object) && Objects.equals(components, ((CompositeIdent) object).components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), components);
    }

    @Override
    public String toString() {
        return "%s[%s]".formatted(getClass().getSimpleName(), components.stream().map(DomainIdent::toString).collect(Collectors.joining(", ")));
    }

    /**
     * @return an immutable list of the domain identifier instances which make up the components of this composite identifier.
     */
    public List<DomainIdent> components() {
        return components;
    }
}
