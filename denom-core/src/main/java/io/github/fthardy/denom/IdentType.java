package io.github.fthardy.denom;

/**
 * Represents the type-ID of a domain identifier.
 * <p>
 * Identifier types are not meant to be publicly used. They are an API implementation specific detail. Every concrete subtype of {@link AtomicIdent} or
 * {@link CompositeIdent} must define a unique type identifier and a {@link io.github.fthardy.denom.converter.DomainIdentFactory factory} which provides this
 * type identifier. The factories are registered internally at a registry used by the
 * {@link io.github.fthardy.denom.converter.DomainIdentCanonicalConverter canonical converter}. There must always be a one-to-one relationship between a
 * particular type identifier and a concrete domain identifier implementation class.
 * </p>
 *
 * @param typeID the type-ID. Must have a length between 1-32 characters, can only contain lower case letters and digits as well as dashes. Must start with a
 *              letter and is not allowed to contain consecutive dashes.
 */
public record IdentType(String typeID) {

    public IdentType {
        if (!typeID.matches("^(?!.*--)[a-z](?:[a-z0-9-]{0,30}[a-z0-9])?$")) {
            throw new IllegalArgumentException("Invalid type-ID: " + typeID);
        }
    }
}
