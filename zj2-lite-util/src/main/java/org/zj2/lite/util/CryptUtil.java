package org.zj2.lite.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.zj2.lite.codec.AesCrypto;
import org.zj2.lite.common.function.PropGetter;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.PropertyUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * <br>CreateDate 七月 27,2022
 * @author peijie.ye
 */
public class CryptUtil {
    private static final AesCryptProvider DEF_PROVIDER = new AesCryptProvider("enXJat%n@akl1er1");
    private static final Set<String> AES_SECRET_KEYS = new HashSet<>(32);
    private static final CopyOnWriteArrayList<CryptProvider> CRYPT_PROVIDERS = new CopyOnWriteArrayList<>();
    private static volatile CryptProvider defCryptProvider = DEF_PROVIDER;//NOSONAR

    public static void setDefCryptProvider(String key) {
        setDefCryptProvider(new AesCryptProvider(key));
    }

    public static void setDefCryptProvider(CryptProvider cryptProvider) {
        defCryptProvider = cryptProvider;//NOSONAR
    }

    public static void addCryptProvider(String key) {
        if (StringUtils.isNotEmpty(key) && !AES_SECRET_KEYS.contains(key)) {
            addCryptProvider(new AesCryptProvider(key));
        }
    }

    public static synchronized void addCryptProvider(CryptProvider cryptProvider) {
        if (cryptProvider instanceof AesCryptProvider) {
            if (AES_SECRET_KEYS.add(((AesCryptProvider) cryptProvider).secretKey)) {
                CRYPT_PROVIDERS.add(cryptProvider);
            }
        } else {
            CRYPT_PROVIDERS.add(cryptProvider);
        }
    }

    public static String crypt(boolean isEncrypt, String val) {
        return isEncrypt ? encrypt(val) : decrypt(val);
    }

    public static String encrypt(String val) {
        if (StringUtils.isEmpty(val)) { return val; }
        CryptProvider provider = getDefCryptProvider();
        return provider.supportsEncrypt(val) ? provider.encrypt(val) : val;
    }

    private static CryptProvider getDefCryptProvider() {
        CryptProvider provider = defCryptProvider;
        return provider == null ? DEF_PROVIDER : provider;
    }

    public static String decrypt(String val) {
        if (StringUtils.isNotEmpty(val)) {
            String newVal;
            // 优先使用默认的
            CryptProvider provider = getDefCryptProvider();
            if (provider.supportsDecrypt(val) && (newVal = provider.decrypt(val)) != null) {
                return newVal;
            }
            CopyOnWriteArrayList<CryptProvider> providers = CRYPT_PROVIDERS;
            int size = providers.size();
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < size; ++i) {
                CryptProvider p = providers.get(i);
                if (p != null && p.supportsDecrypt(val) && (newVal = p.decrypt(val)) != null) { return newVal; }
            }
        }
        return val;
    }

    @SafeVarargs
    public static <T> void decryptList(Collection<? extends T> beans, PropGetter<T, String>... getters) {
        if (getters == null || getters.length == 0 || CollUtil.isEmpty(beans)) { return; }
        for (PropGetter<T, String> getter : getters) {
            Field field = null;
            for (T e : beans) {
                if (e != null) {
                    if (field == null && (field = getBeanField(e.getClass(), getter)) == null) { break; }
                    decryptBean(e, getter, field);
                }
            }
        }
    }

    @SafeVarargs
    public static <T> void decryptBean(T bean, PropGetter<T, String>... getters) {
        if (bean != null && getters != null && getters.length > 0) {
            for (PropGetter<T, String> getter : getters) {
                decryptBean(bean, getter, getBeanField(bean.getClass(), getter));
            }
        }
    }

    private static <T> void decryptBean(T bean, PropGetter<T, String> getter, Field field) {
        if (field == null) { return; }
        try {
            String value = getter.apply(bean);
            String newValue = decrypt(value);
            //noinspection StringEquality
            if (value != newValue) {// NOSONAR
                FieldUtils.writeField(field, bean, newValue);
            }
        } catch (Throwable ignored) { }// NOSONAR
    }

    @SuppressWarnings("all")
    private static Field getBeanField(Class<?> beanType, PropGetter<?, String> getter) {
        try {
            String fieldName = PropertyUtil.getLambdaFieldName(getter);
            return StringUtils.isEmpty(fieldName) ? null : FieldUtils.getField(beanType, fieldName, true);
        } catch (Throwable ignored) {// NOSONAR
            return null;
        }
    }

    public interface CryptProvider {
        boolean supportsEncrypt(String value);

        boolean supportsDecrypt(String value);

        String encrypt(String value);

        String decrypt(String value);
    }

    public static class AesCryptProvider implements CryptProvider {
        private static final String PREFIX = "_$<";
        private static final int CRYPTO_SIZE = 1 << 4;
        private final int version;
        private final String versionPrefix;
        private final String secretKey;
        private final AesCrypto[] cryptos;

        private static int getKeyFlag(String secretKey) {
            return secretKey.hashCode() & 1023;
        }

        public AesCryptProvider(String secretKey) {
            this(getKeyFlag(secretKey), secretKey);
        }

        public AesCryptProvider(int version, String secretKey) {
            this.secretKey = secretKey;
            this.version = version;
            this.versionPrefix = StringUtils.upperCase(PREFIX + Integer.toString(version, 36) + ">:");
            this.cryptos = new AesCrypto[CRYPTO_SIZE];
            for (int i = 0; i < CRYPTO_SIZE; ++i) {
                cryptos[i] = new AesCrypto();
                cryptos[i].init(secretKey);
            }
        }


        @Override
        public boolean supportsEncrypt(String value) {
            return StringUtils.isNotEmpty(value) && !StringUtils.startsWith(value, PREFIX);
        }

        public boolean supportsDecrypt(String value) {
            return StringUtils.length(value) > 24 && StringUtils.startsWith(value, versionPrefix);
        }

        @Override
        public String encrypt(String value) {
            final AesCrypto crypto = getCrypto();
            StringBuilder sb = new StringBuilder(128);
            sb.append(versionPrefix);
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (crypto) {
                try {
                    return crypto.encrypt64(sb, value).toString();
                } catch (Throwable e) {//NOSONAR
                    return value;
                }
            }
        }

        public String decrypt(String value) {
            final AesCrypto crypto = getCrypto();
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized (crypto) {
                try {
                    return crypto.decrypt64(value, versionPrefix.length(), value.length() - versionPrefix.length());
                } catch (Throwable e) {//NOSONAR
                    return value;
                }
            }
        }

        private AesCrypto getCrypto() {
            return cryptos[(int) (Thread.currentThread().getId() & (CRYPTO_SIZE - 1))];
        }

        public int getVersion() {
            return version;
        }

        public String getSecretKey() {
            return secretKey;
        }
    }
}
