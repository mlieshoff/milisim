package org.mili.multisim.connector.web;

import org.apache.commons.codec.binary.Base64;
import org.mili.multisim.connector.socket.Utils;
import org.mili.multisim.core.plugin.Call;
import org.mili.multisim.core.plugin.PluginEventListener;
import org.mili.multisim.core.plugin.Result;
import org.mili.multisim.util.Log;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author
 */
public class SimulatorServlet extends javax.servlet.http.HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String eventHandlerClassname = getServletConfig().getInitParameter("PluginEventListener");
        try {
            PluginEventListener pluginEventListener = (PluginEventListener) Class.forName(eventHandlerClassname).newInstance();
            Connector.getInstance().registerPluginEventListener(pluginEventListener);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service(req, resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String base64Command = req.getParameter("command");

        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        Call call = Utils.bytesToObject(Base64.decodeBase64(base64Command));
        Log.debug(this, "service", "call=%s", call);

        Result result = Connector.getInstance().call(call);

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(Base64.encodeBase64String(Utils.objectToBytes(result)));
        printWriter.flush();

        resp.setStatus(200);
    }

}
