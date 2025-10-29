package io.github.fthardy.denom;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a composite domain identifier.
 */
public non-sealed abstract class CompositeIdent extends DomainIdent {

    static List<DomainIdent> createComponentListFrom(DomainIdent first, DomainIdent second, DomainIdent... further) {
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
     * @param identType the identity type for the new domain identifier.
     *                  <strong>The identity type is an internal detail. Do not expose it to the outside!</strong>
     * @param first the first identifier.
     * @param second the second identifier.
     * @param further optionally any further identifiers.
     */
    protected CompositeIdent(IdentType identType, DomainIdent first, DomainIdent second, DomainIdent... further) {
        super(identType);
        this.components = createComponentListFrom(first, second, further);
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
        return "%s[%s]".formatted(type(), components.stream().map(DomainIdent::toString).collect(Collectors.joining(", ")));
    }

    /**
     * @return the domain identifier instances which make up the components of this composite identifier.
     */
    public List<DomainIdent> components() {
        return components;
    }
}
