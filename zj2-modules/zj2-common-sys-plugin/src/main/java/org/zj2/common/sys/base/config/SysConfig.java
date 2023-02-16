package org.zj2.common.sys.base.config;

import lombok.Getter;
import org.zj2.common.sys.base.dto.SysConfigDTO;
import org.zj2.lite.common.util.NumUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  SysConfig
 *
 * @author peijie.ye
 * @date 2022/12/24 23:57
 */
@Getter
public abstract class SysConfig<T> {
    public static SysConfig<Integer> ofIntConfig(String configCode) {
        return ofIntConfig(configCode, null);
    }

    public static SysConfig<Integer> ofIntConfig(String configCode, Integer defaultValue) {
        return new IntConfig(configCode, defaultValue);
    }

    public static SysConfig<String> ofStrConfig(String configCode) {
        return ofStrConfig(configCode, null);
    }

    public static SysConfig<String> ofStrConfig(String configCode, String defaultValue) {
        return new StrConfig(configCode, defaultValue);
    }

    public static SysConfig<BigDecimal> ofNumConfig(String configCode) {
        return ofNumConfig(configCode, null);
    }

    public static SysConfig<BigDecimal> ofNumConfig(String configCode, Number defaultValue) {
        return new NumConfig(configCode, defaultValue);
    }

    public static SysConfig<Boolean> ofBooleanConfig(String configCode) {
        return ofBooleanConfig(configCode, null);
    }

    public static SysConfig<Boolean> ofBooleanConfig(String configCode, Boolean defaultValue) {
        return new BooleanConfig(configCode, defaultValue);
    }

    public static SysConfig<LocalDateTime> ofDateConfig(String configCode) {
        return ofDateConfig(configCode, null);
    }

    public static SysConfig<LocalDateTime> ofDateConfig(String configCode, String defaultValue) {
        return new DateConfig(configCode, defaultValue);
    }

    final String configCode;

    private SysConfig(String configCode) {
        this.configCode = configCode;
    }

    public SysConfigDTO data() {
        return SysConfigUtil.config(configCode);
    }

    public abstract T value();

    private static class IntConfig extends SysConfig<Integer> {
        private final Integer defaultValue;

        private IntConfig(String configCode, Integer defaultValue) {
            super(configCode);
            this.defaultValue = defaultValue;
        }

        @Override
        public Integer value() {
            return data().valueInt(defaultValue);
        }
    }

    private static class StrConfig extends SysConfig<String> {
        private final String defaultValue;

        private StrConfig(String configCode, String defaultValue) {
            super(configCode);
            this.defaultValue = defaultValue;
        }

        @Override
        public String value() {
            return data().valueStr(defaultValue);
        }
    }

    private static class NumConfig extends SysConfig<BigDecimal> {
        private final BigDecimal defaultValue;

        private NumConfig(String configCode, Number defaultValue) {
            super(configCode);
            this.defaultValue = NumUtil.of(defaultValue, false);
        }

        @Override
        public BigDecimal value() {
            return data().valueNum(defaultValue);
        }
    }

    private static class BooleanConfig extends SysConfig<Boolean> {
        private final Boolean defaultValue;

        private BooleanConfig(String configCode, Boolean defaultValue) {
            super(configCode);
            this.defaultValue = defaultValue;
        }

        @Override
        public Boolean value() {
            return data().valueBoolean(defaultValue);
        }
    }

    private static class DateConfig extends SysConfig<LocalDateTime> {
        private final String defaultValue;

        private DateConfig(String configCode, String defaultValue) {
            super(configCode);
            this.defaultValue = defaultValue;
        }

        @Override
        public LocalDateTime value() {
            return data().valueDate(defaultValue);
        }
    }
}
