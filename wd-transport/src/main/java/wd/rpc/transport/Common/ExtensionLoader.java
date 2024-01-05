package wd.rpc.transport.Common;

import lombok.extern.slf4j.Slf4j;
import wd.rpc.transport.Common.exceptions.ErrorCode;
import wd.rpc.transport.Common.exceptions.RpcException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ExtensionLoader<T> {

    // key: 实现类， value： 实现类单例对象
    private static final Map<Class<?>, Object> extensionCache = new ConcurrentHashMap<>();

    //缓存的扩展类：key:接口全名称 value：加载的实现类名称
    private static final Map<String, Class<?>> extensionClazzCache = new ConcurrentHashMap<>();


    //1.根据指定key加载类， 将类缓存起来
    //1.通过SPI加载类

    public T getExtensionClazz(String extendClazz, String key) throws Exception {
        if (extendClazz == null || key == null ){
            throw new RpcException(ErrorCode.PARAM_ERROR, "SPI 加载插件异常,extend:" + extendClazz + ",key: " + key);
        }
        extendClazz = extendClazz.trim();
        key = key.trim();
        if ("".equals(extendClazz) || "".equals(key)){
            throw new RpcException(ErrorCode.PARAM_ERROR, "SPI 加载插件异常,extend:" + extendClazz + ",key: " + key);
        }

        Class<?> extensionClazz = extensionClazzCache.get(extendClazz);
        if (extensionClazz == null){
            extensionClazz = loadClass(extendClazz, key);
        }
        if (extensionClazz == null){
            throw new NullPointerException(" extend clazz is null," + extendClazz +key);
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
        if (inputStream == null){
            throw new RpcException(ErrorCode.PARAM_ERROR, "SPI文件异常,extend:" + extendClazz + ",key: " + key);
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Map<String, String> extentedClasses = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null && line.length() >0){
                String[] array = line.split(GlobalContents.EQUAL_SEPARATION);
                if (array.length == 2){
                    String extendKey = array[0].trim();
                    String extendValue = array[1].trim();
                    if (extendKey.length() > 0 && extendValue.length() > 0){
                        extentedClasses.put(array[0], array[1]);
                    }
                }
            }
            String loadClass = extentedClasses.get(key);
            if (loadClass != null && loadClass.length() > 0){
                Class<?> loadClazz = ExtensionLoader.class.getClassLoader().loadClass(loadClass);
                extensionClazzCache.put(key, loadClazz);
                return loadClazz;
            }
        } catch (IOException e) {
            throw new RpcException(ErrorCode.IO_ERROR, "SPI文件读取异常,extend:" + extendClazz + ",key: " + key);
        } catch (ClassNotFoundException e) {
            throw new RpcException(ErrorCode.CLASS_ERROR, "SPI文件读取异常,extend:" + extendClazz + ",key: " + key);
        }
        return null;
    }

}
