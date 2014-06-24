package org.mili.multisim.core.plugin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
public class Call implements Serializable {

    private final String pluginId;
    private final String methodName;
    private final Map<ParameterName, Object> parameters = new HashMap<>();

    public Call(String pluginId, String methodName) {
        this.pluginId = pluginId;
        this.methodName = methodName;
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getMethodName() {
        return methodName;
    }

    public <T> T getParameter(ParameterName parameterName) {
        return (T) parameters.get(parameterName);
    }

    public <T> void addParameter(ParameterName parameterName, T value) {
        parameters.put(parameterName, value);
    }

    @Override
    public String toString() {
        return "Call{" +
                "pluginId='" + pluginId + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameters=" + parameters +
                '}';
    }

}
