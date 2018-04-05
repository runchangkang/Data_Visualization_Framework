import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader implements DataPlugin {
    /**
     * Uses the DataSet's makePoint method
     * to add points.
     *
     * @param path
     * @param xcol
     * @param ycol
     * @param tcol
     * @param attrCols
     */
    @Override
    public void inputData(String path, int xcol, int ycol, int tcol, List<Integer> attrCols) throws IOException {
        FileReader file = new FileReader(path);
        CsvMapReader reader = null;
        try {
            reader = new CsvMapReader(file, CsvPreference.STANDARD_PREFERENCE);
            final String[] header = reader.getHeader(true);
            final CellProcessor[] processors = null;
            Map<String, Object> values = new HashMap<>();
            while ((values = reader.read(header, processors)) != null) {
                System.out.println(String.format("lineNo=%s, rowNo=%s, customerMap=%s", reader.getLineNumber(),
                        reader.getRowNumber(), values));
            }
        }
        finally{
            if( reader != null ) {
                reader.close();
            }
        }

    }

}
