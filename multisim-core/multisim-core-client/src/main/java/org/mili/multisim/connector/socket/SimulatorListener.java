package org.mili.multisim.connector.socket;

import org.mili.multisim.core.plugin.Event;

/**
 * @author
 */
public interface SimulatorListener {

    void onEvent(Event event);

}
