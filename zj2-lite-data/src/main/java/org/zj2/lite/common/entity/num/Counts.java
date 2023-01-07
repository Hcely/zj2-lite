package org.zj2.lite.common.entity.num;

import java.util.HashMap;
import java.util.Map;

public class Counts {
    private Map<String, IntHolder> countMap;

    private IntHolder get0(String key) {
        if (countMap == null) { countMap = new HashMap<>(256); }
        return countMap.computeIfAbsent(key, k -> new IntHolder());
    }

    public int getAndIncrease(String key) {
        return get0(key).getAndIncrease();
    }

    public int increaseAndGet(String key) {
        return get0(key).increaseAndGet();
    }

    public int get(String key) {
        return get0(key).num;
    }

    public int getSet(String key, int v) {
        return get0(key).getSet(v);
    }

    private static class IntHolder {
        private int num;

        public int getAndIncrease() {
            return num++;
        }

        public int increaseAndGet() {
            return ++num;
        }

        public int getSet(int v) {
            int r = num;
            num = v;
            return r;
        }
    }
}