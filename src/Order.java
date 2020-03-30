public interface Order {
    long getId();

    long getQuantity();

    double getPrice();

    Side getSide();
}
