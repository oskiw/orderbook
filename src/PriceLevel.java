public final class PriceLevel implements Level {
    private final double price;
    private final long volume;

    public PriceLevel(double price, long volume) {
        this.price = price;
        this.volume = volume;
    }

    @Override
    public long getVolume() {
        return volume;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
