package org.zj2.lite.service.broadcast;

import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.constant.NoneConstants;

/**
 * StandardServerSignalListener
 *
 * @author peijie.ye
 * @date 2022/12/13 0:53
 */
public abstract class StandardServerSignalListener implements ServerSignalListener {
    private final String[] tags;

    public StandardServerSignalListener(String... tags) {
        this.tags = tags == null || tags.length == 0 ? NoneConstants.EMPTY_STRINGS : tags;
    }

    @Override
    public boolean supports(String tag) {
        return StringUtils.containsAnyIgnoreCase(tag, tags);
    }
}
