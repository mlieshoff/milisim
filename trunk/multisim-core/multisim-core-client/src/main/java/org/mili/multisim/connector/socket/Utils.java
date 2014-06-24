package org.mili.multisim.connector.socket;

import org.mili.multisim.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author
 */
public class Utils {

    public static byte[] objectToBytes(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            return bos.toByteArray();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static <T> T bytesToObject(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            try {
                return (T) in.readObject();
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static byte[] readFromStream(InputStream inputStream, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int read;
        while((read = inputStream.read(buffer)) > 0) {
            byte[] bytesObject = new byte[read];
            System.arraycopy(buffer, 0, bytesObject, 0, read);
            return bytesObject;
        }
        return new byte[0];
    }

    public static Socket connect(String socketHost, int socketPort) {
        while(true) {
            Log.debug(Utils.class, "connect", "try connecting to host=%s, port=%s", socketHost, socketPort);
            try {
                Socket socket = new Socket(socketHost, socketPort);
                socket.setKeepAlive(true);
                Log.debug(Utils.class, "connect", "connection established to host=%s, port=%s", socketHost, socketPort);
                return socket;
            } catch (IOException e) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    // ignore
                }
            }
        }
    }

}
