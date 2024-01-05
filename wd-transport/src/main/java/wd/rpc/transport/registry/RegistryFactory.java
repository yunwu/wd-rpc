package wd.rpc.transport.registry;

import wd.rpc.transport.Common.ExtensionLoader;

import java.util.HashMap;
import java.util.Map;

public class RegistryFactory {

    public static final String registryName = "zookeeper";

    //获取ExtensionLoader并处理，最好不要在写一个单独的registry

    public static Registry getRegistry(String registryName) throws Exception {
        ExtensionLoader<Registry> extensionLoader = new ExtensionLoader();
        return extensionLoader.getExtensionClazz(Registry.class.getName(), registryName);
    }
}
