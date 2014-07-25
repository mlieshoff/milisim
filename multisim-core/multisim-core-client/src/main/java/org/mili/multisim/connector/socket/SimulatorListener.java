package org.mili.multisim.connector.socket;

/**
 * @author
 */
public interface SimulatorListener {

    void onEvent(byte[] event);

}
