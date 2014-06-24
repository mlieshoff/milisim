package org.mili.multisim.core.server;

import org.mili.multisim.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author
 */
public class Config {

    private List<String> pluginClassnames = new ArrayList<>();
    private List<String> pluginIds = new ArrayList<>();
    private int port;
    private int listenerPort;

    public void loadConfig() throws IOException {
        File dir = new File("../config/config.properties");
        Log.info(this, "loadConfig", "load config from: " + dir.getAbsolutePath());
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(dir)) {
            properties.load(is);
        } catch (IOException e) {
            throw e;
        }
        extractPluginClassnames(properties);
        extractPluginIds(properties);
        extractPorts(properties);
    }

    private void extractPluginClassnames(Properties properties) {
        String s = properties.get("pluginClassnames").toString();
        if (s != null) {
            pluginClassnames.addAll(Arrays.asList(s.split("[,]")));
        }
    }

    private void extractPluginIds(Properties properties) {
        String s = properties.get("pluginIds").toString();
        if (s != null) {
            pluginIds.addAll(Arrays.asList(s.split("[,]")));
        }
    }

    private void extractPorts(Properties properties) {
        port = Integer.valueOf(properties.getProperty("port"));
        listenerPort = Integer.valueOf(properties.getProperty("listenerPort"));
    }

    public List<String> getPluginClassnames() {
        return pluginClassnames;
    }

    public String getPluginIdFor(String pluginClassname) {
        return pluginIds.get(pluginClassnames.indexOf(pluginClassname));
    }

    public int getPort() {
        return port;
    }

    public int getListenerPort() {
        return listenerPort;
    }

}
