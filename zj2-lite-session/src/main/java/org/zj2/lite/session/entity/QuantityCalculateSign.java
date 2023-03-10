package org.zj2.lite.session.entity;

/**
 * 运算符合
 * <br>CreateDate 三月 23,2022
 *
 * @author peijie.ye
 */
public interface QuantityCalculateSign {
    void calculate(QuantitiesView view, Quantity quantity);
}
