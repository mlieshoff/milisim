package org.mili.multisim.core.plugin;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author
 */
public abstract class AbstractPlugin implements Plugin {

    private ServerConfig serverConfig = new ServerConfig();
    private String name;

    private EbeanServer ebeanServer;

    private Set<PluginEventListener> listeners = new HashSet<>();

    protected AbstractPlugin(String name) {
        this.name = name;

        final String DB_HOME = System.getProperty("user.home") + "/" + name + ".db";

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver("org.h2.Driver");
        dataSourceConfig.setUsername("");
        dataSourceConfig.setPassword("");
        dataSourceConfig.setUrl("jdbc:h2:" + DB_HOME);
        dataSourceConfig.setHeartbeatSql("select now();");

        serverConfig.setName(name);
        serverConfig.setDataSourceConfig(dataSourceConfig);
        serverConfig.setDdlGenerate(true);
        serverConfig.setDdlRun(true);
        serverConfig.setDefaultServer(true);
        serverConfig.setRegister(false);
    }

    protected <T> void addModelClass(Class<T> clazz) {
        serverConfig.addClass(clazz);
    }

    protected EbeanServer getEbeanServer() {
        if (ebeanServer == null) {
            ebeanServer = EbeanServerFactory.create(serverConfig);
        }
        return ebeanServer;
    }

    public String getName() {
        return name;
    }

    @Override
    public void fireEvent(byte[] pluginEvent) {
        for (PluginEventListener pluginEventListener : listeners) {
            pluginEventListener.onEvent(pluginEvent);
        }
    }

    @Override
    public void addListener(PluginEventListener pluginEventListener) {
        listeners.add(pluginEventListener);
    }

}
