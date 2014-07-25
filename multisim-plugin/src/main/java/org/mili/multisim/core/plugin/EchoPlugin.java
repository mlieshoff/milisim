package org.mili.multisim.core.plugin;

/**
 * @author
 */
public class EchoPlugin extends AbstractPlugin {

    public EchoPlugin(String name) {
        super(name);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public byte[] call(byte[] call) {
        String message = new String(call).trim();

        if ("ping".equals(message)) {
            return "pong".getBytes();
        }
        return null;
    }

}
