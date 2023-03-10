package org.zj2.lite.service.cache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zj2.lite.service.broadcast.ServerSignal;
import org.zj2.lite.service.broadcast.StandardServerSignalListener;

/**
 * CacheSignalListener
 *
 * @author peijie.ye
 * @date 2022/12/13 1:05
 */
@Component
public class CacheSignalListener extends StandardServerSignalListener {
    public CacheSignalListener() {
        super(CacheUtil.DEFAULT_CACHE_SIGNAL_TAG);
    }

    @Override
    public void onMsg(ServerSignal signal) {
        String s = signal.getSignal();
        if(StringUtils.equalsIgnoreCase(s, "CLEAR")) {
            LocalCacheHelper cacheHelper = (LocalCacheHelper)CacheUtil.DEF_CACHE;
            cacheHelper.clear();
        } else {
            CacheUtil.DEF_CACHE.removeCache(s);
        }
    }
}
