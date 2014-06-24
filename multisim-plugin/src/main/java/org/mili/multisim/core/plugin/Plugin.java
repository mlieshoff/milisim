package org.mili.multisim.core.plugin;

/**
 * @author
 */
public interface Plugin {

    void start();

    void stop();

    Result call(Call call);

    String getName();

    void fireEvent(PluginEvent pluginEvent);

    void addListener(PluginEventListener pluginEventListener);

}
