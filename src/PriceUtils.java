import java.util.Comparator;

public class PriceUtils {

    public final static double THRESHOLD = 1E-10;

    public final static Comparator<Double> ascendingPriceComparator = (price1, price2) -> {
        if (Math.abs(price1 - price2) < THRESHOLD) return 0;
        if (price1 < price2) return -1;
        return 1;
    };

    public final static Comparator<Double> descendingPriceComparator = (price1, price2) -> {
        if (Math.abs(price1 - price2) < THRESHOLD) return 0;
        if (price1 < price2) return 1;
        return -1;
    };

    /**
     * @return true if order is more passive than the reference price
     */
    public static boolean morePassive(Order order, double referencePrice) {
        if (order.getSide() == Side.BUY) {
            return ascendingPriceComparator.compare(order.getPrice(), referencePrice) < 0;
        } else {
            return ascendingPriceComparator.compare(order.getPrice(), referencePrice) > 0;
        }
    }


}
