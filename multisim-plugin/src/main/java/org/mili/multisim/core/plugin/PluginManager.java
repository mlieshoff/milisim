package org.mili.multisim.core.plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
public class PluginManager {

    private Map<String, Plugin> plugins = new HashMap<>();

    public void start() {
        for (Map.Entry<String, Plugin> entry : plugins.entrySet()) {
            entry.getValue().start();
        }
    }

    public void stop() {
        for (Map.Entry<String, Plugin> entry : plugins.entrySet()) {
            entry.getValue().stop();
        }
    }

    public void register(String pluginId, Plugin plugin) {
        plugins.put(pluginId, plugin);
    }

    public Plugin get(String pluginId) {
        return plugins.get(pluginId);
    }

    public Collection<Plugin> getPlugins() {
        return plugins.values();
    }

}
