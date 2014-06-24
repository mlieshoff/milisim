package org.mili.multisim.connector.web;


import org.mili.multisim.connector.socket.Client;
import org.mili.multisim.connector.socket.SimulatorListener;
import org.mili.multisim.core.plugin.Call;
import org.mili.multisim.core.plugin.Event;
import org.mili.multisim.core.plugin.PluginEvent;
import org.mili.multisim.core.plugin.PluginEventListener;
import org.mili.multisim.core.plugin.Result;
import org.mili.multisim.util.Log;

import java.io.IOException;

/**
 * @author
 */
public class Connector implements SimulatorListener {

    private static Connector INSTANCE = new Connector("localhost", 50001, 50002);

    private PluginEventListener pluginEventListener;

    public static Connector getInstance() {
        return INSTANCE;
    }

    private Client client;

    public Connector(String listenerHost, int port, int listenerPort) {
        client = new Client(listenerHost, port, listenerPort);
        try {
            client.registerListener(this);
            client.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Result call(Call call) {
        return client.call(call);
    }

    @Override
    public void onEvent(Event event) {
        Log.info(this, "onEvent", "event received: %s", event);
        if (event instanceof PluginEvent) {
            pluginEventListener.onEvent((PluginEvent) event);
        }
    }

    public void registerPluginEventListener(PluginEventListener pluginEventListener) {
        this.pluginEventListener = pluginEventListener;
    }

}
