package io.github.fthardy.denom;

import io.github.fthardy.denom.converter.DefaultDomainIdentCanonicalConverter;
import io.github.fthardy.denom.converter.DomainIdentCanonicalConverter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.github.fthardy.denom.DomainIdent.Messages.*;

/**
 * The base type representing a domain identifier.
 */
public sealed abstract class DomainIdent permits AtomicIdent, CompositeIdent {

    static final Map<String, Class<? extends DomainIdent>> _typeIdToClassRegistry = new HashMap<>();

    /**
     * Checks a given type-ID if it is valid.
     *
     * @param typeId the type-ID to check.
     *
     * @return the given type-ID by convenience for inline invocations.
     *
     * @throws IllegalArgumentException when the given type-ID is invalid.
     */
    static String assertIsValidTypeId(String typeId) {
        if (!typeId.matches("^(?!.*--)[a-z](?:[a-z0-9-]{0,30}[a-z0-9])?$")) {
            throw new IllegalArgumentException(invalidTypeId(typeId));
        }
        return typeId;
    }

    /**
     * Get the bound domain identifier implementation class for a particular type-ID.
     *
     * @param typeId the type-ID to lookup for.
     *
     * @return the bound domain identifier implementation class.
     *
     * @throws NoSuchElementException when there is no bound class for the given type-ID.
     */
    protected static Class<? extends DomainIdent> classForTypeId(String typeId) {
        if (_typeIdToClassRegistry.containsKey(assertIsValidTypeId(typeId))) {
            return _typeIdToClassRegistry.get(typeId);
        } else {
            throw new NoSuchElementException(noBoundClassFoundForTypeId(typeId));
        }
    }

    /**
     * Binds a type-ID to a domain identifier implementation class.
     * <p>
     * If for a given type-ID the class is alread bound nothing is done and the given type-ID is returned.
     * </p>
     *
     * @param typeId the type-ID
     * @param domainIdentifierClass the domain identifier implementation class.
     *
     * @return the given type-ID.
     */
    static String bindTypeIdToClass(String typeId, Class<? extends DomainIdent> domainIdentifierClass) {
        if (_typeIdToClassRegistry.containsKey(assertIsValidTypeId(typeId))) {
            Class<? extends DomainIdent> boundClass = _typeIdToClassRegistry.get(typeId);
            if (!boundClass.equals(domainIdentifierClass)) {
                throw new IllegalStateException(typeIdAlreadyBound(typeId, domainIdentifierClass, boundClass));
            } // already registered - totally okay, keep on
        } else if (_typeIdToClassRegistry.containsValue(domainIdentifierClass)) {
            String boundTypeId = _typeIdToClassRegistry.entrySet().stream() //
                    .filter(e -> e.getValue().equals(domainIdentifierClass)) //
                    .findFirst().orElseThrow().getKey();
            throw new IllegalStateException(classAlreadyBound(domainIdentifierClass, typeId, boundTypeId));
        } else {
            if (Modifier.isFinal(domainIdentifierClass.getModifiers())) {
                _typeIdToClassRegistry.put(typeId, domainIdentifierClass);
            } else {
                throw new InvalidImplementationClassException(implementationClassMustBeFinal(domainIdentifierClass));
            }
        }
        return typeId;
    }

    static Logger log = Logger.getLogger(DomainIdent.class.getName());

    private static volatile DomainIdentCanonicalConverter _canonicalConverter;

    /**
     * Obtains the instance of the canonical converter.
     *
     * @param implementationsSupplier a supplier which provides the implementations of the converter instances.
     *
     * @return the converter instance.
     */
    static DomainIdentCanonicalConverter getCanonicalConverter(Supplier<Iterable<DomainIdentCanonicalConverter>> implementationsSupplier) {

        final List<DomainIdentCanonicalConverter> converters = new ArrayList<>();
        for (DomainIdentCanonicalConverter converter : implementationsSupplier.get()) { converters.add(converter); }

        DomainIdentCanonicalConverter converterInstance = new DefaultDomainIdentCanonicalConverter();
        if (converters.size() > 1) {
            log.warning(ambiguousImplementationsOnClasspath(converters));
        } else if (converters.size() == 1) { // replace the default implementation with the custom implementation
            converterInstance = converters.getFirst();
        }
        return converterInstance;
    }

    // Encapsulates the standard "double-check-lock-stuff"
    private static DomainIdentCanonicalConverter canonicalConverterInstance() {
        if (_canonicalConverter == null) {
            synchronized (DomainIdent.class) {
                if (_canonicalConverter == null) {
                    _canonicalConverter = getCanonicalConverter(() -> ServiceLoader.load(DomainIdentCanonicalConverter.class));
                }
            }
        }
        return _canonicalConverter;
    }

    /**
     * Converts a string that is presumably a canonical representation of a domain identifier back into its model representation.
     *
     * @param canonical the canonical string to convert.
     *
     * @return a new instance of a domain identifier.
     */
    public static DomainIdent fromCanonical(String canonical) {
        return canonicalConverterInstance().fromCanonical(canonical);
    }

    /**
     * Creates an instance of a domain identifier from the given annotated producer method.
     *
     * @param annotatedProducerMethod the producer method.
     * @param parameter the parameter for producing the instance.
     *
     * @return the new domain identifier instance.
     */
    protected static DomainIdent createInstance(Method annotatedProducerMethod, Object... parameter) {
        try {
            return (DomainIdent) annotatedProducerMethod.invoke(null, parameter);
        } catch (Exception e) {
            throw new InstanceCreationFailedException(unexpectedExceptionOnProducerMethodInvocation(annotatedProducerMethod), e);
        }
    }

    /**
     * Find the annotated producer method at a given domain identifier class.
     *
     * @param onClass the domain identifier class to search at.
     * @param signature the signature of the method.
     *
     * @return the method instance.
     *
     * @throws IllegalArgumentException when the method is not found or more than one method (ambiguity) is found.
     */
    protected static Method findAnnotatedProducerMethod(Class<? extends DomainIdent> onClass, Class<?>[] signature) {
        List<Method> methodList = Arrays.stream(onClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(DomainIdentProducer.class)).toList();
        if (methodList.isEmpty()) {
            throw new InvalidImplementationClassException(missingProducerMethod(signature, onClass));
        } else if (methodList.size() != 1) {
            throw new InvalidImplementationClassException(ambiguousProducerMethod(methodList));
        }

        Method method = methodList.getFirst();
        if (!DomainIdent.class.isAssignableFrom(method.getReturnType())) {
            throw new InvalidProducerMethodException(invalidProducerMethodReturnType(method.getReturnType()));
        }
        if (!Arrays.equals(method.getParameterTypes(), signature)) {
            throw new InvalidProducerMethodException(invalidProducerMethodParameterSignature(signature));
        }
        return method;
    }

    private final String typeId;

    /**
     * Initializes the new instance.
     *
     * @param typeId the type-ID for the new domain identifier.
     */
    protected DomainIdent(String typeId) {
        this.typeId = bindTypeIdToClass(typeId, this.getClass());
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        return object != null && getClass().equals(object.getClass()) && typeId().equals(((DomainIdent) object).typeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), typeId());
    }

    /**
     * @return the name of the type of this domain identifier.
     */
    public final String typeId() {
        return typeId;
    }

    /**
     * Converts this domain identifier instance into a canonical string representation.
     *
     * @return the canonical string representation of this domain identifier.
     */
    public String toCanonical() {
        return canonicalConverterInstance().toCanonical(this);
    }

    /**
     * Is thrown when the creation of a domain identifier instance fails.
     */
    public static final class InstanceCreationFailedException extends RuntimeException {
        InstanceCreationFailedException(String message, Exception cause) {
            super(message, cause);
        }
    }

    /**
     * Is thrown when the implementation class of a domain identifier is invalid.
     */
    public static final class InvalidImplementationClassException extends RuntimeException {
        InvalidImplementationClassException(String message) {
            super(message);
        }
    }

    /**
     * Is thrown when the annotated producer method is invalid.
     */
    public static final class InvalidProducerMethodException extends RuntimeException {
        public InvalidProducerMethodException(String message) {
            super(message);
        }
    }

    static final class Messages {

        private Messages() {}

        static String ambiguousImplementationsOnClasspath(List<DomainIdentCanonicalConverter> converters) {
            return "More than one implementation for class '%s' found on class path:\n%s\nFalling back to default implementation!".formatted( //
                    DomainIdentCanonicalConverter.class.getName(),
                    converters.stream().map(c -> c.getClass().getName()).collect(Collectors.joining("\n")));
        }

        static String missingProducerMethod(Class<?>[] signature, Class<?> domainIdentClass) {
            return "No annotated producer method with signature [%s] found at domain identifier class '%s'!".formatted(signature, domainIdentClass.getName());
        }

        static String invalidProducerMethodParameterSignature(Class<?>[] signature) {
            return "Invalid parameter signature! Expected signature:\n(%s)".formatted( //
                    Arrays.stream(signature).map(Class::getName).collect(Collectors.joining(", ")));
        }

        static String invalidProducerMethodReturnType(Class<?> returnType) {
            return "Invalid return type '%s'! Return type must be an extension from '%s'.".formatted(returnType.getName(), DomainIdent.class.getName());
        }

        public static String ambiguousProducerMethod(List<Method> methodList) {
            return "Ambiguous producer method! Found %d matching annotated methods:\n%s".formatted( //
                    methodList.size(), methodList.stream().map(Method::toString).collect(Collectors.joining("\t\n")));
        }

        public static String classAlreadyBound(Class<? extends DomainIdent> domainIdentifierClass, String typeId, String boundTypeId) {
            return "Cannot bind class '%s' to type-ID '%s' because it is already bound to type-ID '%s'!".formatted( //
                    domainIdentifierClass.getName(), typeId, boundTypeId);
        }

        public static String typeIdAlreadyBound(String typeId, Class<? extends DomainIdent> domainIdentifierClass, Class<? extends DomainIdent> boundClass) {
            return "Cannot bind type-ID '%s' to class '%s' because it is already bound to '%s'!".formatted( //
                    typeId, domainIdentifierClass.getName(), boundClass.getName());
        }

        public static String noBoundClassFoundForTypeId(String typeId) {
            return "No bound domain identifier implementation class found for type-ID: %s".formatted(typeId);
        }

        public static String invalidTypeId(String typeId) {
            return "Invalid type-ID: %s".formatted(typeId);
        }

        public static String implementationClassMustBeFinal(Class<? extends DomainIdent> domainIdentifierClass) {
            return "Class to bind must be final: " + domainIdentifierClass.getName();
        }

        public static String unexpectedExceptionOnProducerMethodInvocation(Method annotatedProducerMethod) {
            return "Unexpected exception on invoking annotated producer method: %s".formatted(annotatedProducerMethod);
        }
    }
}
