package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.ClientPoint;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader implements DataPlugin {

    @Override
    public String getName(){
        return "CSVReader";
    }

    @Override
    public boolean hasNext(){
        return true;
    }

    @Override
    public ClientPoint getNext(){
        return new ClientPoint(0,0,0,new HashMap<>());
    }

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
    public void inputData(String path, int xcol, int ycol, int tcol, List<Integer> attrCols) throws IOException {
        FileReader file = new FileReader(path);
        CsvMapReader reader = null;
        try {
            reader = new CsvMapReader(file, CsvPreference.STANDARD_PREFERENCE);
            final String[] header = reader.getHeader(true);
            final CellProcessor[] processors = new CellProcessor[header.length];

            Map<String, Object> values;
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