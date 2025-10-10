package io.github.fthardy.denom;

import java.lang.annotation.*;

/**
 * Marks a producer method which produces a domain identifier instance.
 * <p>
 * The annotated method must be static, and it must be defined at a final class which extends from {@link AtomicIdent} or {@link CompositeIdent}.
 * </p>
 *
 * @see DomainIdent
 * @see AtomicIdent
 * @see CompositeIdent
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainIdentProducer {
    // intentionally empty
}
