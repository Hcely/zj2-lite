package org.zj2.lite.common.text;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class StrFormatterManager {
    private static final int MAX_FORMATTER_LENGTH = 256;
    public static final StrFormatterManager DEFAULT = new StrFormatterManager();
    private boolean useCache;
    private boolean nullAsEmpty;
    private final Map<String, StrFormatter> formatterCache;
    protected final CopyOnWriteArrayList<ValueSerializer> serializers;

    public StrFormatterManager() {
        this(true);
    }

    public StrFormatterManager(boolean useCache) {
        this.useCache = useCache;
        this.nullAsEmpty = false;
        this.formatterCache = useCache ? new HashMap<>(1024) : new HashMap<>();
        this.serializers = new CopyOnWriteArrayList<>();
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
        if(!useCache) { clearCache(); }
    }

    public boolean isNullAsEmpty() {
        return nullAsEmpty;
    }

    public void setNullAsEmpty(boolean nullAsEmpty) {
        this.nullAsEmpty = nullAsEmpty;
    }

    public void clearCache() {
        synchronized(formatterCache) { formatterCache.clear(); }
    }

    public StrFormatter getFormatter(String format) {
        if(!useCache || StringUtils.length(format) > MAX_FORMATTER_LENGTH) { return new StrFormatter(this, format); }
        StrFormatter formatter = formatterCache.get(format);//NOSONAR
        if(formatter == null) {
            synchronized(formatterCache) {
                if((formatter = formatterCache.get(format)) == null) {
                    formatter = new StrFormatter(this, format);
                    formatterCache.put(format, formatter);
                }
            }
        }
        return formatter;
    }

    public void addValueSerializer(ValueSerializer serializer) {
        if(serializer != null) { serializers.add(serializer); }
    }
}
