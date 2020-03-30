import java.util.concurrent.atomic.AtomicLong;

public final class Order {

    final static String PRICE_IS_NOT_FINITE = "price needs to be finite";
    final static String QUANTITY_NOT_POSITIVE = "quantity needs to be positive";
    final static String SIDE_IS_NULL = "side needs to be equal to Side.SELL or Side.BUY";

    private static AtomicLong nextOrderId = new AtomicLong(System.nanoTime());
    private final double price;
    private final long quantity;
    private final Side side;
    private final long orderId;

    //TODO: Extend to add more fields like expire time, etc.

    //TODO: Add support for market orders
    public Order(double price, long quantity, Side side) {

        if (!Double.isFinite(price)) {
            throw new IllegalArgumentException(PRICE_IS_NOT_FINITE);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(QUANTITY_NOT_POSITIVE);
        }
        if (side == null) {
            throw new IllegalArgumentException(SIDE_IS_NULL);
        }

        //TODO: Validate if price is rounded to tick size or round it here
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.orderId = nextOrderId.getAndAdd(1);
    }

    public long getId() {
        return orderId;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Side getSide() {
        return side;
    }
}
