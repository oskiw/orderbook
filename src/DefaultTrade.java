public final class DefaultTrade implements Trade {

    private final long firstOrderId;
    private final long secondOrderId;
    private final long volume;
    private final double price;

    public DefaultTrade(long firstOrderId, long secondOrderId, long volume, double price) {
        this.firstOrderId = firstOrderId;
        this.secondOrderId = secondOrderId;
        this.volume = volume;
        this.price = price;
    }

    @Override
    public long getVolume() {
        return volume;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public long getFirstOrderId() {
        return firstOrderId;
    }

    @Override
    public long getSecondOrderId() {
        return secondOrderId;
    }
}
