package org.mili.multisim.plugins.ekahau.servlet;

import org.mili.multisim.core.plugin.PluginEvent;
import org.mili.multisim.core.plugin.PluginEventListener;
import org.mili.multisim.util.Log;

/**
 * @author
 */
public class EkahauPluginEventListener implements PluginEventListener {

    @Override
    public void onEvent(PluginEvent pluginEvent) {
        Log.debug(this, "onEvent", "event received: %s", pluginEvent);
    }

}


