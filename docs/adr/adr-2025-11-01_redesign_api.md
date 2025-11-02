# Redesign of the API - especially the handling of the canonicalization

## Context
Initially, the canonization of domain identifier instances was directly available via a corresponding method.
De-Canonization was provided via a static function on the domain identifier base type. The implementation of the logic for canonization and 
de-canonization was outsourced, making it possible to replace it with a different, custom implementation. To replace the implementation, an SPI approach was 
chosen, and factory methods could be declared for each identifier type using annotations, which were then used for de-canonization.

To make the approach more flexible (and also for better testability), a possibility was created so that the SPI approach could also be replaced by a
use in the DI environment.

All in all, however, testing revealed that this approach generally proved to be "too complicated". Therefore, the idea arose to further simplify the
implementation, leaving it up to the API user to decide whether to provide the canonization logic via DI or SPI.

## Decision
The implementation was simplified by moving the canonization logic into a separate subproject and thus into its own artifact. The IdentType was semantically
redefined as a binding between the identifier's type class and an alias name (used in the canonization) for it.
The DomainIdentCanonicalConverter interface, which represents canonization, is provided as a simple class with your default implementation.

## Consequences
Simplification makes the application much more flexible, and API users have the choice of how the integration should look. 