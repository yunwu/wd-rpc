package wd.rpc.transport.registry;

import wd.rpc.transport.Common.ExtensionLoader;
import wd.rpc.transport.Common.GlobalContext;

public class RegistryFactory {

    public static final String REGISTRY_NAME = "zookeeper";
    public static final String RPC_PROPERTY_KEY = "rpc.registry";

    public static Registry getRegistry() throws Exception {
        String key = GlobalContext.getByKeyOrDefault(RPC_PROPERTY_KEY, REGISTRY_NAME);
        return getRegistry(key);
    }
    private static Registry getRegistry(String registryName) throws Exception {
        ExtensionLoader<Registry> extensionLoader = new ExtensionLoader();
        return extensionLoader.getExtensionClazz(Registry.class.getName(), registryName);
    }


}
