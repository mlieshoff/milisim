package org.mili.multisim.core.server;

import org.mili.multisim.connector.socket.SimulatorListener;
import org.mili.multisim.core.plugin.Plugin;
import org.mili.multisim.core.plugin.PluginEventListener;
import org.mili.multisim.core.plugin.PluginManager;

import java.util.Collection;

/**
 * @author
 */
public class Simulator implements PluginEventListener {

    private static Simulator INSTANCE = new Simulator() ;

    private PluginManager pluginManager = new PluginManager();

    private SimulatorListener simulatorListener;

    public static Simulator getInstance() {
        return INSTANCE;
    }

    public void registerPlugin(Plugin plugin) {
        plugin.addListener(this);
        pluginManager.register(plugin.getName(), plugin);
    }

    public Plugin getPlugin(String pluginId) {
        return pluginManager.get(pluginId);
    }

    public void start() {
        pluginManager.start();
    }

    public void startListening() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    simulatorListener.onEvent(String.valueOf(System.currentTimeMillis()).getBytes());
                }
            }
        }).start();

    }

    public void registerListener(SimulatorListener simulatorListener) {
        this.simulatorListener = simulatorListener;
    }

    public Collection<Plugin> getPlugins() {
        return pluginManager.getPlugins();
    }

    @Override
    public void onEvent(byte[] pluginEvent) {
        simulatorListener.onEvent(pluginEvent);
    }
}