package net.rickiekarp.botlib.net;

import net.rickiekarp.core.net.NetworkAction;
import net.rickiekarp.core.net.NetworkApi;
import net.rickiekarp.core.net.provider.NetworkParameterProvider;
import net.rickiekarp.botlib.model.PluginData;

public class BotNetworkApi extends NetworkApi {
    private static String AUTH_DOMAIN = "auth";
    private static String PLUGIN_DOMAIN = "plugin";
    private static String VALIDATE_ACTION = "validate";
    private static String PLUGIN_DATA_ACTION = "data";
    private static String PLUGIN_NAME_ACTION = "name";

    public static NetworkAction requestValidation(PluginData plugin) {
        NetworkParameterProvider provider = NetworkParameterProvider.Companion.create();
        provider.put("plugin", plugin.getPluginName());
        return NetworkAction.Builder.create().setHost(NetworkAction.DATASERVER).setDomain(AUTH_DOMAIN).setAction(VALIDATE_ACTION).setParameters(provider).setMethod("POST").build();
    }

    public static NetworkAction requestPlugins() {
        return NetworkAction.Builder.create().setHost(NetworkAction.DATASERVER).setDomain(PLUGIN_DOMAIN).setAction(PLUGIN_DATA_ACTION).setMethod("GET").build();
    }
}
