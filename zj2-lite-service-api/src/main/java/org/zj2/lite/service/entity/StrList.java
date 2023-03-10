package org.zj2.lite.service.entity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * StrList
 *
 * @author peijie.ye
 * @date 2022/12/23 9:52
 */
public class StrList extends ArrayList<String> {
    private static final long serialVersionUID = -5194876759592543554L;

    public StrList(int initialCapacity) {
        super(initialCapacity);
    }

    public StrList() {
    }

    public StrList(Collection<String> c) {
        super(c);
    }
}
