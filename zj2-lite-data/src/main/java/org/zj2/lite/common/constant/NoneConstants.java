package org.zj2.lite.common.constant;

import org.apache.commons.lang3.ArrayUtils;
import org.zj2.lite.common.util.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  NoneConstants
 *
 * @author peijie.ye
 * @date 2022/11/24 2:08
 */
public interface NoneConstants {
    NoneNumber NONE_NUM = new NoneNumber();
    LocalDateTime NONE_DATE = DateUtil.$INVALID;
    Object NONE_OBJ = new Object();
    String NONE_STR = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";
    String[] EMPTY_STRINGS = ArrayUtils.EMPTY_STRING_ARRAY;//NOSONAR
    Object[] EMPTY_OBJECTS = ArrayUtils.EMPTY_OBJECT_ARRAY;//NOSONAR

    final class NoneNumber extends BigDecimal {
        private static final long serialVersionUID = -6427710248936140416L;

        private NoneNumber() {
            super(0);
        }
    }
}
