package org.mili.multisim.core.plugin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
public class Result implements Serializable {

    private final Map<ParameterName, Object> parameters = new HashMap<>();

    public <T> T getParameter(ParameterName parameterName) {
        return (T) parameters.get(parameterName);
    }

    public <T> void addParameter(ParameterName parameterName, T value) {
        parameters.put(parameterName, value);
    }

    @Override
    public String toString() {
        return "Result{" +
                "parameters=" + parameters +
                '}';
    }

}
