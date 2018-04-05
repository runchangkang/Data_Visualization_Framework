/**
 * The DataPoint representation.
 * Has x, y, time, and attribute value.
 */
public class DataPoint {
    private double x;
    private double y;
    private double t;
    private double attr;

    /**
     * Takes x, y, t, and attr value to initialize the data point
     * @param x the x value of the data point
     * @param y the y value of the data point
     * @param t the time value of the data point
     * @param attr the attribute associated with the given x y t
     */
    public DataPoint(double x, double y, double t, double attr){
        this.x = x;
        this.y = y;
        this.t = t;
        this.attr = attr;
    }

}
