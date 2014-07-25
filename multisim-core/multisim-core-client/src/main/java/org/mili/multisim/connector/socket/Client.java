package org.mili.multisim.connector.socket;

import org.mili.multisim.util.Log;
import org.mili.multisim.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private Socket socket;
    private Socket listenerSocket;

    private SimulatorListener simulatorListener;

    private final String host;

    private final int port;
    private final int listenerPort;

    public Client(String host, int port, int listenerPort) {
        this.host = host;
        this.port = port;
        this.listenerPort = listenerPort;
    }

    public void start() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listenerSocket = Utils.connect(host, listenerPort);
                while (true) {
                    try {
                        Log.debug(this, "run", "waiting...");
                        byte[] bytes = Utils.readFromStream(listenerSocket.getInputStream(), listenerSocket.getReceiveBufferSize());
                        if (bytes.length > 0) {
                            Log.debug(this, "run", "bytes received: %s", bytes.length);
                            fireEvent(bytes);
                        } else {
                            listenerSocket = Utils.connect(host, listenerPort);
                        }
                    } catch (Exception e) {
                        Log.error(this, "run", "error while receiving: %s", e.getMessage());
                        listenerSocket = Utils.connect(host, listenerPort);
                    }
                }
            }
        }).start();
    }

    private void fireEvent(byte[] event) {
        Log.debug(this, "fireEvent", "event: %s", event);
        if (simulatorListener != null) {
            simulatorListener.onEvent(event);
        }
    }

    public void registerListener(SimulatorListener simulatorListener) {
        this.simulatorListener = simulatorListener;
    }

    public byte[] call(byte[] call) {
        socket = Utils.connect(host, port);
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(call, 0, call.length);
            outputStream.flush();

            InputStream inputStream = socket.getInputStream();
            return Utils.readFromStream(inputStream, socket.getReceiveBufferSize());
        } catch (Exception e) {
            Log.error(this, "call", "error while calling: %s", e.getMessage());
        }
        return null;
    }

}