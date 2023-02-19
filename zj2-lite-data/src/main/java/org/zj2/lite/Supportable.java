package org.zj2.lite;

/**
 *
 * <br>CreateDate 三月 27,2022
 * @author peijie.ye
 */
public interface Supportable<T> {
    boolean supports(T obj);
}
