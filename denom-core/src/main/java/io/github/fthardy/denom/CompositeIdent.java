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
     * {@code null} values are going to be ignored but at least two identifiers must be defined and all identifiers must be distinct. Otherwise, an
     * {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param first the first identifier.
     * @param further any further identifiers.
     */
    protected CompositeIdent(DomainIdent first, DomainIdent... further) {
        List<DomainIdent> list = new ArrayList<>();
        list.add(first);
        if (further.length != 0) {
            list.addAll(Arrays.stream(further).toList());
        }
        List<DomainIdent> nullStripped = list.stream().filter(Objects::nonNull).toList();
        List<DomainIdent> distinctList = nullStripped.stream().distinct().toList();
        if (distinctList.size() < nullStripped.size() || distinctList.size() < 2) {
            throw new IllegalArgumentException("At least two identifiers must be defined and all identifiers must be distinct!");
        }
        this.components = distinctList;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) && this.getClass().equals(obj.getClass())) {
            CompositeIdent other = (CompositeIdent) obj;
            return Objects.equals(components, other.components);
        }
        return false;
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
