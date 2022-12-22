package org.zj2.lite.service.cache;

abstract class AbsCacheHelper implements CacheHelper {
    protected final long expireIn;

    AbsCacheHelper(long expireIn) {
        this.expireIn = expireIn;
    }

    @Override
    public long getExpireIn() {
        return expireIn;
    }
}