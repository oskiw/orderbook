public final class Trade {

    private final long firstOrderId;
    private final long secondOrderId;
    private final long volume;
    private final double price;

    public Trade(long firstOrderId, long secondOrderId, long volume, double price) {
        this.firstOrderId = firstOrderId;
        this.secondOrderId = secondOrderId;
        this.volume = volume;
        this.price = price;
    }

    public long getVolume() {
        return volume;
    }

    public double getPrice() {
        return price;
    }

    public long getFirstOrderId() {
        return firstOrderId;
    }

    public long getSecondOrderId() {
        return secondOrderId;
    }
}
