package cart.domain;

import java.util.List;

import cart.exception.IllegalPointUsageException;

public class Order {
    private static final int MINIMUM_UNIT_POINTS = 10;

    private final Long id;
    private final int points;
    private final List<CartItem> cartItems;

    public Order(final Long id, final int points, final List<CartItem> cartItems) {
        validatePointUnit(points);
        this.id = id;
        this.points = points;
        this.cartItems = cartItems;
    }

    private void validatePointUnit(final int points) {
        if (points % MINIMUM_UNIT_POINTS != 0) {
            throw new IllegalPointUsageException();
        }
    }

    public Order(final int points, final List<CartItem> cartItems) {
        this(null, points, cartItems);
    }

    public void validatePoints(final int memberPoints) {
        if (points > memberPoints) {
            throw new IllegalPointUsageException();
        }
    }

    public Long getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
