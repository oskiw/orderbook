public interface Order {

    String PRICE_IS_NOT_FINITE = "price needs to be finite";
    String QUANTITY_NOT_POSITIVE = "quantity needs to be positive";
    String SIDE_IS_NULL = "side needs to be equal to Side.SELL or Side.BUY";

    long getId();

    long getQuantity();

    double getPrice();

    Side getSide();
}
