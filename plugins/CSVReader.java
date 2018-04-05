import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class CSVReader implements DataPlugin {
    /**
     * Uses the DataSet's makePoint method
     * to add points.
     */
    @Override
    public void inputData(String path, int xcol, int ycol, int tcol, List<Integer> attrCols) throws FileNotFoundException {
        CsvMapReader reader = new CsvMapReader(new FileReader(path), CsvPreference.STANDARD_PREFERENCE);

    }

}
