package org.zj2.lite.service.entity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * StrList
 *
 * @author peijie.ye
 * @date 2022/12/23 9:52
 */
public class IntList extends ArrayList<Integer> {
    private static final long serialVersionUID = -5194876759592543554L;

    public IntList(int initialCapacity) {
        super(initialCapacity);
    }

    public IntList() {
    }

    public IntList(Collection<Integer> c) {
        super(c);
    }
}
