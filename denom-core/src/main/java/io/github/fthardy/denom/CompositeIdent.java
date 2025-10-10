package io.github.fthardy.denom;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a composite domain identifier.
 */
public non-sealed abstract class CompositeIdent extends DomainIdent {

    /**
     * Creates an instance from a particular domain identifier type name and domain identifier instances as components.
     *
     * @param typeId the domain identifier type name.
     * @param first the first domain identifier instance.
     * @param further the second domain identifier and any further instances.
     *
     * @return a new composite identifier instance.
     */
    public static CompositeIdent createInstance(String typeId, DomainIdent first, DomainIdent second, DomainIdent... further) {
        return (CompositeIdent) createInstance(findAnnotatedProducerMethod( //
                DomainIdent.classForTypeId(typeId), new Class<?>[] {DomainIdent.class, DomainIdent.class, further.getClass()}), first, second, further);
    }

    private final List<DomainIdent> components;

    /**
     * Initializes a new instance of a composite domain identifier.
     * <p>
     * At least two identifiers must be defined and all identifiers must be distinct. Otherwise, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param typeId the type-ID for the new instance. IMPORTANT: Never expose the type-ID as parameter at the concrete implementation
     *                 constructors! Always set a unique, "hard coded" type-ID for each concrete implementation on construction.
     * @param first the first identifier.
     * @param second the second identifier.
     * @param further optionally any further identifiers.
     */
    protected CompositeIdent(String typeId, DomainIdent first, DomainIdent second, DomainIdent... further) {
        super(typeId);

        Set<DomainIdent> set = new LinkedHashSet<>();
        set.add(Objects.requireNonNull(first));
        set.add(Objects.requireNonNull(second));
        if (further.length > 0) {
            set.addAll(Arrays.stream(further).map(Objects::requireNonNull).toList());
        }
        if (set.size() != further.length + 2) {
            throw new IllegalArgumentException("Duplicate identifiers detected! All identifiers must be distinct!");
        }
        this.components = set.stream().toList();
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
        return "%s[%s]".formatted(typeId(), components.stream().map(DomainIdent::toString).collect(Collectors.joining(", ")));
    }

    /**
     * @return the domain identifier instances which make up the components of this composite identifier.
     */
    public List<DomainIdent> components() {
        return components;
    }
}
