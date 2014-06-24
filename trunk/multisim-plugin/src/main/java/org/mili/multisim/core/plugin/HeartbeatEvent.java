package org.mili.multisim.core.plugin;

/**
 * @author
 */
public class HeartbeatEvent extends SimulatorEvent {

    public HeartbeatEvent() {
        addParameter(SimulatorParameters.TIMESTAMP, System.currentTimeMillis());
    }

}
