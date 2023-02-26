package org.zj2.common.wx.app;

import java.util.List;

/**
 * WXAppProvider
 *
 * @author peijie.ye
 * @date 2023/2/19 21:18
 */
public interface WXAppBundle {
    List<WXApp> list();

    WXApp get(String wxAppId);
}
