import java.io.FileNotFoundException;
import java.util.List;

public interface DataPlugin {

    /**
     * Uses the DataSet's makePoint method
     * to add points.
     */
    public void inputData(String path,  int xcol, int ycol, int tcol, List<Integer> attrCols) throws FileNotFoundException;

}
