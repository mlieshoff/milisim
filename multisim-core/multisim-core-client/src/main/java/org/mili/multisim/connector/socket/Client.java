package org.mili.multisim.connector.socket;

import org.mili.multisim.core.plugin.Call;
import org.mili.multisim.core.plugin.Event;
import org.mili.multisim.core.plugin.Result;
import org.mili.multisim.util.Log;

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
                            Event event = Utils.bytesToObject(bytes);
                            fireEvent(event);
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

    private void fireEvent(Event event) {
        Log.debug(this, "fireEvent", "event: %s", event);
        if (simulatorListener != null) {
            simulatorListener.onEvent(event);
        }
    }

    public void registerListener(SimulatorListener simulatorListener) {
        this.simulatorListener = simulatorListener;
    }

    public Result call(Call call) {
        socket = Utils.connect(host, port);
        try {
            OutputStream outputStream = socket.getOutputStream();
            byte[] callBytes = Utils.objectToBytes(call);
            outputStream.write(callBytes, 0, callBytes.length);
            outputStream.flush();

            InputStream inputStream = socket.getInputStream();
            byte[] bytesObject = Utils.readFromStream(inputStream, socket.getReceiveBufferSize());
            return Utils.bytesToObject(bytesObject);
        } catch (Exception e) {
            Log.error(this, "call", "error while calling: %s", e.getMessage());
        }
        return null;
    }

}