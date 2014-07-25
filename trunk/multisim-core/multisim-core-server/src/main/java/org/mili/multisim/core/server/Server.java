package org.mili.multisim.core.server;

import org.mili.multisim.connector.socket.SimulatorListener;
import org.mili.multisim.util.Utils;
import org.mili.multisim.core.plugin.Plugin;
import org.mili.multisim.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author
 */
public class Server implements SimulatorListener {

    private Simulator simulator;

    private ServerSocket listenerSocket;

    private List<Socket> listeners = new Vector<>();

    private Deque<byte[]> events = new ConcurrentLinkedDeque<>();

    private void start() throws Exception {
        Log.info(this, "start", "starting...");

        simulator = new Simulator();

        Config config = new Config();

        config.loadConfig();

        for (String pluginClassname : config.getPluginClassnames()) {
            String id = config.getPluginIdFor(pluginClassname);
            Class<? extends Plugin> pluginClass = (Class<? extends Plugin>) Class.forName(pluginClassname);
            Constructor<? extends Plugin> constructor = pluginClass.getConstructor(String.class);
            simulator.registerPlugin(constructor.newInstance(id));
        }

        simulator.start();
        simulator.registerListener(this);
        simulator.startListening();

        ServerSocket serverSocket = new ServerSocket(config.getPort());
        listenerSocket = new ServerSocket(config.getListenerPort());

        Log.info(this, "start", "started.");

        // listener accept thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final Socket socket;
                    try {
                        socket = listenerSocket.accept();
                        socket.setKeepAlive(true);
                        Log.debug(this, "listenerAcceptThread", "accept listening on socket [%s]", socket);
                        listeners.add(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        // wait for calls
        while (true) {
            final Socket socket = serverSocket.accept();
            socket.setKeepAlive(true);

            Log.debug(this, "waitForCallsThread", "accepted: %s", socket);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OutputStream outputStream = socket.getOutputStream();
                        InputStream inputStream = socket.getInputStream();

                        byte[] call = readCallFromClient(inputStream, socket.getReceiveBufferSize());

                        for (Plugin plugin : simulator.getPlugins()) {
                            byte[] result = plugin.call(call);

                            if (result != null) {
                                Log.debug(this, "CALL", "plugin[%s] answers: [%s]", plugin.getName(), new String(result));
                                sendResultToClient(result, outputStream);
                            }
                        }

                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // listener event thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        byte[] event = events.poll();
                        if (event != null) {
                            for (Iterator<Socket> iterator = listeners.iterator(); iterator.hasNext(); ) {
                                Socket socket = iterator.next();
                                try {
                                    OutputStream outputStream = socket.getOutputStream();
                                    sendEventToClient(event, outputStream);
                                } catch (IOException e) {
                                    Log.error(this, "sendEvent", "cannot fire event [%s] to socket [%s]", event, socket);
                                    iterator.remove();
                                }
                            }
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }
                }
            }).start();

        }
    }

    private void sendResultToClient(byte[] bytes, OutputStream outputStream) throws IOException {
        Log.debug(this, "sendResultToClient", "send [%s] to client", new String(bytes));
        outputStream.write(bytes, 0, bytes.length);
        outputStream.flush();
    }

    private byte[] readCallFromClient(InputStream inputStream, int bufferSize) throws IOException {
        byte[] bytes = Utils.readFromStream(inputStream, bufferSize);
        Log.debug(this, "readCallFromClient", "read [%s] from client", new String(bytes));
        return bytes;
    }

    private byte[] readBytesFromClient(InputStream inputStream, int bufferSize) throws IOException {
        return Utils.readFromStream(inputStream, bufferSize);
    }

    @Override
    public void onEvent(byte[] event) {
        Log.debug(this, "onEvent", "add event [%s] to queue [%s]", new String(event), events.size());
        events.add(event);
    }

    private void sendEventToClient(byte[] event, OutputStream outputStream) throws IOException {
        Log.debug(this, "sendEventToClient", "send event [%s] to client", new String(event));
        outputStream.write(event, 0, event.length);
        outputStream.flush();
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
    }

}