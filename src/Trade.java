public interface Trade {
    long getVolume();

    double getPrice();

    long getFirstOrderId();

    long getSecondOrderId();
}
