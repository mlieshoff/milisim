package org.mili.multisim.core.plugin;

/**
 * @author
 */
public interface Plugin {

    void start();

    void stop();

    byte[] call(byte[] call);

    String getName();

    void fireEvent(byte[] pluginEvent);

    void addListener(PluginEventListener pluginEventListener);

}
