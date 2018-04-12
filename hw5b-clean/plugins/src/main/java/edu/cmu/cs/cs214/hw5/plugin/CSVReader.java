package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.ClientPoint;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This API takes in any kind of CSV file, and relies on the user to input the column name for the
 * X values, Y values, Time values, and other Attribute values the user wishes to read
 * -- the assumption is being made that users know what the file contains. Each row represents a DataPoint, and
 * each column header a possible attribute value.
 *
 * See the project Wiki for further details
 */
public class CSVReader implements DataPlugin {

    private static final String FILE_PATH_LABEL = "File Path";
    private static final String X_LABEL = "X";
    private static final String Y_LABEL = "Y";
    private static final String T_LABEL = "T";
    private static final String ATTRIBUTE_LABEL = "Attributes";

    /**
     * @return name of the plugin
     */
    @Override
    public String getName(){
        return "CSVReader";
    }

    /**
     * Maps the labels we provided to (arbitrary) arguments given by the user:
     *   eg: < <"File Path","C://plugins/test.csv">,
     *          <"X","X">,
     *          <"Y","Y">,
     *          <"T","Time">
     *          <"Attributes","Wind,Light,Temperature">>
     *
     *  See the plugin documentation on the project wiki for further details.
     *
     * @param argumentMap map of arguments from user
     * @return collection of data ready to be made into a DataSet
     */
    @Override
    public Collection<ClientPoint> getCollection (Map<String,String> argumentMap) throws Exception{
        String path = argumentMap.get(FILE_PATH_LABEL);
        String xcol = argumentMap.get(X_LABEL);
        String ycol = argumentMap.get(Y_LABEL);
        String tcol = argumentMap.get(T_LABEL);
        List<String> attrCols = Arrays.asList(argumentMap.get(ATTRIBUTE_LABEL).split(","));
        return inputData (path,xcol,ycol,tcol,attrCols);
    }

    /**
     * The client is expected to provide a list of desired input strings, in order to parameterize data
     * and provide plugin-specific or use-specific information (e.g. file path). Each parameter string will be
     * associated with a text field in a popup. A result is expected for each of these, which the plugin
     * can then choose to use or disregard.
     *
     * @return list of field labels.
     */
    @Override
    public List<String> getPopupParameters(){
        return new ArrayList<>(Arrays.asList(FILE_PATH_LABEL,X_LABEL,Y_LABEL,T_LABEL,ATTRIBUTE_LABEL));
    }


    /**
     * Helper method -- Fetches data directly from CSV
     * @param path file path
     * @param xcol name of column containing x data
     * @param ycol name of column containing y data
     * @param tcol name of column containing time data
     * @param attrCols name of columns containing other data
     */
    public List<ClientPoint> inputData(String path, String xcol, String ycol,
                                       String tcol, List<String> attrCols) throws Exception {
        //Setup reading
        FileReader file = new FileReader(path);
        CsvMapReader reader = null;

        List<ClientPoint> pointList = new ArrayList<>();

        try {
            reader = new CsvMapReader(file, CsvPreference.STANDARD_PREFERENCE);
            final String[] header = reader.getHeader(true);
            final CellProcessor[] processors = new CellProcessor[header.length];

            //parse the CSV
            Map<String, Object> values;
            while ((values = reader.read(header, processors)) != null) {
                double x = Double.parseDouble((String) values.get(xcol));
                double y = Double.parseDouble((String) values.get(ycol));
                double z = Double.parseDouble((String) values.get(tcol));

                Map<String,Double> attributeMap = new HashMap<>();
                for (String attribute : attrCols){
                    if (values.containsKey(attribute)) {
                        attributeMap.put(attribute, Double.parseDouble((String) values.get(attribute)));
                    }
                }
                //Add a new clientPoint (no labels in this implementation)
                ClientPoint p = new ClientPoint(x,y,z,attributeMap,"");
                pointList.add(p);
            }
        }
        finally{
            if(reader != null) {
                //cleanup
                reader.close();
            }
        }

        return pointList;
    }

}