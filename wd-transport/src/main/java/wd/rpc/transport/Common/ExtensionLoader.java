package wd.rpc.transport.Common;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ExtensionLoader<T> {

    private static final String SEPARATION = "=";

    private static final Map<Class<?>, Object> extensionCache = new ConcurrentHashMap<>();

    private static final Map<String, Class<?>> extensionClazzCache = new ConcurrentHashMap<>();


    //1.根据指定key加载类， 将类缓存起来，类是否需要单例
    //1.通过SPI加载类

    public T getExtensionClazz(String extendClazz, String key) throws Exception {
        Class<?> extensionClazz = extensionClazzCache.get(extendClazz);
        if (extensionClazz == null){
            extensionClazz = loadClass(extendClazz, key);
        }
        if (extensionClazz == null){
            throw new Exception(" extend clazz is null," + extendClazz +key);
        }
        Object extension = extensionCache.get(extensionClazz);
        if (extension == null){
            extension = extensionClazz.newInstance();
            extensionCache.put(extensionClazz, extension);
        }
        return (T) extension;
    }


    public static Class<?> loadClass(String extendClazz, String key){
        InputStream inputStream = ExtensionLoader.class.getClassLoader().getResourceAsStream("internal/"+extendClazz);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Map<String, String> extentdClasses = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null && line.length() >0){
                String[] array = line.split(SEPARATION);
                if (array.length == 2){
                    String extendKey = array[0].trim();
                    String extendValue = array[1].trim();
                    if (extendKey.length() > 0 && extendValue.length() > 0){
                        extentdClasses.put(array[0], array[1]);
                    }
                }
            }
            String loadClass = extentdClasses.get(key);
            if (loadClass != null && loadClass.length() > 0){
                Class<?> loadClazz = ExtensionLoader.class.getClassLoader().loadClass(loadClass);
                extensionClazzCache.put(key, loadClazz);
                return loadClazz;
            }
        } catch (Exception e) {
            log.error("load extend class error,{}", e);
        }
        return null;
    }

}
