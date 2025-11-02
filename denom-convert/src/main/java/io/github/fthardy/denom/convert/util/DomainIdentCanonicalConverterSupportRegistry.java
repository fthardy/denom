package io.github.fthardy.denom.convert.util;

import io.github.fthardy.denom.DomainIdent;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple registry implementation for {@link DomainIdentConvertSupport} instances.
 *
 * @see DomainIdentConvertSupport
 */
public class DomainIdentCanonicalConverterSupportRegistry {

    private final Map<Class<?>, DomainIdentConvertSupport<?>> supportByIdentClass = new HashMap<>();
    private final Map<String, DomainIdentConvertSupport<?>> supportByTypeAlias = new HashMap<>();

    /**
     * Add a new support.
     *
     * @param support the support to add.
     */
    public void addSupport(DomainIdentConvertSupport<?> support) {
        IdentTypeClass2AliasBinding<?> identTypeAlias = support.typeClass2AliasBinding();

        if (supportByIdentClass.containsKey(identTypeAlias.identClass())) {
            throw new IllegalArgumentException(Messages.bindingExistsForTypeClass( //
                    support.typeClass2AliasBinding(), supportByIdentClass.get(identTypeAlias.identClass())));
        }
        if (supportByTypeAlias.containsKey(identTypeAlias.typeAlias())) {
            throw new IllegalArgumentException(Messages.bindingExistsForTypeAlias( //
                    support.typeClass2AliasBinding(), supportByTypeAlias.get(identTypeAlias.typeAlias())));
        }

        supportByIdentClass.put(identTypeAlias.identClass(), support);
        supportByTypeAlias.put(identTypeAlias.typeAlias(), support);
    }

    /**
     * Get a support implementation by the type class of a domain identifier.
     *
     * @param identClass the type class of the domain identifier.
     *
     * @return the support implementation instance or {@code null} when there is no registration for the given class.
     */
    public DomainIdentConvertSupport<?> getSupportByClass(Class<? extends DomainIdent> identClass) {
        return supportByIdentClass.get(identClass);
    }

    /**
     * Get a support implementation by the alias name of a domain identifier type class.
     *
     * @param typeAlias the alias name.
     *
     * @return the support implementation instance or {@code null} when there is no registration for the given type.
     */
    public DomainIdentConvertSupport<?> getSupportByTypeAlias(String typeAlias) {
        return supportByTypeAlias.get(typeAlias);
    }

    static final class Messages {
        private Messages() {}

        static String bindingExistsForTypeClass(IdentTypeClass2AliasBinding<?> providedBinding, DomainIdentConvertSupport<?> existingSupport) {
            return "Cannot add support implementation because the type class of the provided binding (%s) is already bound to a support: %s".formatted(
                    providedBinding, existingSupport);
        }

        static String bindingExistsForTypeAlias(IdentTypeClass2AliasBinding<?> providedBinding, DomainIdentConvertSupport<?> existingSupport) {
            return "Cannot add support implementation because the type alias name of the provided binding (%s) is already bound to a support: %s".formatted(
                    providedBinding, existingSupport);
        }
    }
}
