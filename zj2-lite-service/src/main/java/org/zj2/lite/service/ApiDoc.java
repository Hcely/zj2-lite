package org.zj2.lite.service;

/**
 * Doc
 *
 * @author peijie.ye
 * @date 2023/2/16 11:55
 */
public interface ApiDoc {
    default String getTitle() {
        return null;
    }

    default String getVersion() {
        return null;
    }

    String getGroupName();

    String getBasePackage();
}
