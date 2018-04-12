package edu.cmu.cs.cs214.hw5.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface implemented by Data Plugins in order to fetch data from an external source and import it into the framework.
 */
public interface DataPlugin {

    /**
     * The name this plugin will be displayed with in the framework for purposes of selection and loading.
     * @return name string
     */
    String getName();

    /**
     * The client is expected to provide a list of desired input strings, in order to parameterize data
     * and provide plugin-specific or use-specific information (e.g. file path). Each parameter string will be
     * associated with a text field in a popup. A result is expected for each of these, which the plugin
     * can then choose to use or disregard.
     *
     * @return list of field labels.
     */
    List<String> getPopupParameters();

    /**
     * The client implements this method to fetch the data from the data source. The client is provided the results
     * of the getPopupParameters they implemented earlier in the form of a mapping from key (parameter field label)
     * to value (input result from client). The client is then responsible for validating these parameters.
     *
     * @param argumentMap map from field parameters to client-provided arguments
     * @return collection of Points ready to be input into the framework
     * @throws Exception if there was an I/O or parsing error (FileNotFound, I/O Exception, ParseException). The
     * framework will display an appropriate error to the user.
     */
    Collection<ClientPoint> getCollection(Map<String,String> argumentMap) throws Exception;

}
