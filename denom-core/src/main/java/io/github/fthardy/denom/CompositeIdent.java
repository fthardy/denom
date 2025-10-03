package io.github.fthardy.denom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a composite domain identifier.
 */
public non-sealed abstract class CompositeIdent extends DomainIdent {

    private final List<DomainIdent> components;

    /**
     * Initializes a new instance of a composite domain identifier.
     * <p>
     * At least two identifiers must be defined and all identifiers must be distinct. Otherwise, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param first the first identifier.
     * @param further must contain the second any further identifiers.
     */
    protected CompositeIdent(DomainIdent first, DomainIdent... further) {
        List<DomainIdent> list = new ArrayList<>();
        list.add(Objects.requireNonNull(first));
        if (further.length == 0) {
            throw new IllegalArgumentException("At least two identifiers must be defined!");
        }
        list.addAll(Arrays.stream(further).map(Objects::requireNonNull).toList());
        List<DomainIdent> distinctList = list.stream().distinct().toList();
        if (list.size() != list.stream().distinct().toList().size()) {
            throw new IllegalArgumentException("Duplicate identifiers detected! All identifiers must be distinct!");
        }
        this.components = distinctList;
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object) &&
                Objects.equals(components, ((CompositeIdent) object).components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), components);
    }

    /**
     * @return the domain identifier instances which make up the components of this composite identifier.
     */
    public List<DomainIdent> components() {
        return components;
    }
}
