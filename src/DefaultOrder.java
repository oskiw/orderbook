import java.util.concurrent.atomic.AtomicLong;

public final class DefaultOrder implements Order {

    private static AtomicLong nextOrderId = new AtomicLong(System.nanoTime());
    private final double price;
    private final long quantity;
    private final Side side;
    private final long orderId;

    //TODO: Extend to add more fields like expire time, etc.

    public DefaultOrder(double price, long quantity, Side side) {

        if (!Double.isFinite(price)) {
            throw new IllegalArgumentException(Order.PRICE_IS_NOT_FINITE);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(Order.QUANTITY_NOT_POSITIVE);
        }
        if (side == null) {
            throw new IllegalArgumentException(Order.SIDE_IS_NULL);
        }

        //TODO: Validate if price is rounded to tick size or round it here
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.orderId = nextOrderId.getAndAdd(1);
    }

    @Override
    public long getId() {
        return orderId;
    }

    @Override
    public long getQuantity() {
        return quantity;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public Side getSide() {
        return side;
    }
}
