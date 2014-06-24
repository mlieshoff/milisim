package org.mili.multisim.plugins.ekahau;

import com.ekahau.engine.sdk.PositioningEngine;

/**
 * @author
 */
public class Console {

    public static void main(String[] args) throws Exception {
        PositioningEngine positioningEngine = new PositioningEngine("localhost", 58080, "bla", "bla");
        positioningEngine.startTracking();
        Thread.sleep(5000);
        positioningEngine.findDevices();
        positioningEngine.stopTracking();
    }

}
