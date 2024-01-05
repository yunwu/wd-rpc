package wd.rpc.transport.Common.servicestrategy;

import java.math.BigDecimal;
import java.util.List;

public class RandomStrategy implements ServiceStrategy {

    @Override
    public String getUrl(List<String> urls) {
        if (urls == null || urls.size() == 0){
            return null;
        }
        int index = new BigDecimal(Math.random() * urls.size()).intValue();
        return urls.get(index);
    }
}
