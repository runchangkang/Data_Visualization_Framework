package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.ClientPoint;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.util.*;

/**
 * Parses a CSV file using user-defined parameters. Each row represents a DataPoint.
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
     *          //todo: have variable numbers of user-defined attributes to be able to match other datasets?
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
     * @return Labels of fields that this plugin asks the user to provide
     */
    @Override
    public List<String> getPopupParameters(){
        return new ArrayList<>(Arrays.asList(FILE_PATH_LABEL,X_LABEL,Y_LABEL,T_LABEL,ATTRIBUTE_LABEL));
    }


    /**
     * Fetches data directly from CSV
     * @param path file path
     * @param xcol name of column containing x data
     * @param ycol name of column containing y data
     * @param tcol name of column containing time data
     * @param attrCols name of columns containing other data
     */
    public List<ClientPoint> inputData(String path, String xcol, String ycol, String tcol, List<String> attrCols) throws Exception {
        FileReader file = new FileReader(path);
        CsvMapReader reader = null;

        List<ClientPoint> pointList = new ArrayList<>();

        try {
            reader = new CsvMapReader(file, CsvPreference.STANDARD_PREFERENCE);
            final String[] header = reader.getHeader(true);
            final CellProcessor[] processors = new CellProcessor[header.length];

            Map<String, Object> values;
            while ((values = reader.read(header, processors)) != null) {
                double x = Double.parseDouble((String) values.get(xcol));
                double y = Double.parseDouble((String) values.get(ycol));
                double z = Double.parseDouble((String) values.get(tcol));

                //Todo: the assumption here is that all rows have all attributes. This might not be the case.
                Map<String,Double> attributeMap = new HashMap<>();
                for (String attribute : attrCols){
                    attributeMap.put(attribute,Double.parseDouble((String) values.get(attribute)));
                }
                ClientPoint p = new ClientPoint(x,y,z,attributeMap);
                System.out.println(p);
                pointList.add(p);
            }
        }
        finally{
            if( reader != null ) {
                reader.close();
            }
        }

        return pointList;

    }

}