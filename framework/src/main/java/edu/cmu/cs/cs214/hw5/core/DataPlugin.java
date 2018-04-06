package edu.cmu.cs.cs214.hw5.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DataPlugin {

    String getName();

    List<String> getPopupParameters();

    Collection<ClientPoint> getCollection(Map<String,String> argumentMap) throws Exception;

}
