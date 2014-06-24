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
    public Result call(Call call) {
        Result result = new Result();
        if ("ping".equals(call.getMethodName())) {
            result.addParameter(SimulatorParameters.STATUS, "pong");
        }
        return result;
    }

}
