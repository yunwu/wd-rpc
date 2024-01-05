package wd.rpc.transport.Common;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GlobalContext {

    private static final Map<String, String> globalProperties = new ConcurrentHashMap<>();

    public static void readProperties(){
        InputStream inputStream = ExtensionLoader.class.getClassLoader().getResourceAsStream("global.properties");
        if (inputStream == null){
            log.warn("读取全局变量文件失败");
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null && line.length() >0){
                String[] array = line.split(GlobalContents.EQUAL_SEPARATION);
                if (array.length == 2){
                    String extendKey = array[0].trim();
                    String extendValue = array[1].trim();
                    if (extendKey.length() > 0 && extendValue.length() > 0){
                        globalProperties.put(array[0], array[1]);
                    }
                }
            }
        } catch (IOException e) {
            log.error("读取全局变量文件失败，{}", e);
        }
    }

    public static String getByKeyOrDefault(String key, String defaultValue){
        return globalProperties.getOrDefault(key, defaultValue);
    }
}
