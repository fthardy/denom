package io.github.fthardy.denom;

/** Implementation of a composite identifier for testing. */
final class UserOrderIdent extends CompositeIdent {
    UserOrderIdent(UserIdent userID, OrderIdent orderID) {
        super(userID, orderID);
    }
}
