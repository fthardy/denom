package io.github.fthardy.denom.convert.util;

import io.github.fthardy.denom.DomainIdent;

import java.lang.reflect.Modifier;

/**
 * Represents a binding between the concrete implementation type class of a domain identifier and an alias name which is intended to represent this type class
 * in a canonical representation.
 *
 * @param identClass a concrete domain identifier type class i.e. the class must be final!
 * @param typeAlias the type alias name. The alias name is restricted to a format which conforms to RCF-3986. Its length is restricted to 1-32 characters. It
 *                  can only contain lower case letters, digits and dashes (-) and must start with a letter. Only single dashes are allowed. More than one
 *                  dash in a sequence is not allowed.
 *
 * @param <T> a concrete domain identifier type class.
 *
 * @see DomainIdentConvertSupport
 * @see DomainIdentCanonicalConverterSupportRegistry
 */
public record IdentTypeClass2AliasBinding<T extends DomainIdent>(Class<T> identClass, String typeAlias) {

    public IdentTypeClass2AliasBinding {
        int modifiers = identClass.getModifiers();
        if (!Modifier.isFinal(modifiers)) {
            throw new IllegalArgumentException("The given domain identifier class must be final!");
        }

        if (!typeAlias.matches("^(?!.*--)[a-z](?:[a-z0-9-]{0,30}[a-z0-9])?$")) {
            throw new IllegalArgumentException("Invalid type alias: " + typeAlias);
        }
    }
}
