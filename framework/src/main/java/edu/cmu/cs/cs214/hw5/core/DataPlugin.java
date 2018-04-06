package edu.cmu.cs.cs214.hw5.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Data Plugin Interface
 */
public interface DataPlugin {

    /**
     * @return name of this data plugin in the selection screen
     */
    String getName();

    /**
     * @return list of field labels that the framework will prompt the client to provide
     */
    List<String> getPopupParameters();

    /**
     *
     * @param argumentMap map from field parameters to client-provided arguments
     * @return collection of Points ready to be parsed into a DataSet
     * @throws Exception if there was an I/O or parsing error (FileNotFound, I/O Exception, ParseException)
     */
    Collection<ClientPoint> getCollection(Map<String,String> argumentMap) throws Exception;

}
