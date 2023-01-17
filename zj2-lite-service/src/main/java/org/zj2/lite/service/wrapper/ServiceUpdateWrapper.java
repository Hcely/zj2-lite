package org.zj2.lite.service.wrapper;

import org.zj2.lite.common.util.PropFunc;
import org.zj2.lite.service.BaseInnerService;
import org.zj2.lite.service.entity.request.wrapper.ZUpdateWrapper;

import java.util.Collection;

/**
 *
 * <br>CreateDate 六月 17,2022
 * @author peijie.ye
 */
public class ServiceUpdateWrapper<T> extends ZUpdateWrapper<T> {
    private static final long serialVersionUID = 4476746589280892543L;
    //
    private final transient BaseInnerService<T> service;

    public static <E> ServiceUpdateWrapper<E> wrapper(BaseInnerService<E> service) {
        return new ServiceUpdateWrapper<>(service);
    }

    private ServiceUpdateWrapper(BaseInnerService<T> service) {
        this.service = service;
    }

    @Override
    public ServiceUpdateWrapper<T> ignoreEmpty() {
        return (ServiceUpdateWrapper<T>) super.ignoreEmpty();
    }

    @Override
    public ServiceUpdateWrapper<T> ignoreEmpty(boolean ignoreEmpty) {
        return (ServiceUpdateWrapper<T>) super.ignoreEmpty(ignoreEmpty);
    }

    @Override
    public ServiceUpdateWrapper<T> eq(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.eq(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> eq(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.eq(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> ne(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.ne(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> ne(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.ne(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> isNull(PropFunc<T, ?> prop) {
        return (ServiceUpdateWrapper<T>) super.isNull(prop);
    }

    @Override
    public ServiceUpdateWrapper<T> isNull(boolean b, PropFunc<T, ?> prop) {
        return (ServiceUpdateWrapper<T>) super.isNull(b, prop);
    }

    @Override
    public ServiceUpdateWrapper<T> isNotNull(PropFunc<T, ?> prop) {
        return (ServiceUpdateWrapper<T>) super.isNotNull(prop);
    }

    @Override
    public ServiceUpdateWrapper<T> isNotNull(boolean b, PropFunc<T, ?> prop) {
        return (ServiceUpdateWrapper<T>) super.isNotNull(b, prop);
    }

    @Override
    public ServiceUpdateWrapper<T> in(PropFunc<T, ?> prop, Object... values) {
        return (ServiceUpdateWrapper<T>) super.in(prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> in(boolean b, PropFunc<T, ?> prop, Object... values) {
        return (ServiceUpdateWrapper<T>) super.in(b, prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> in(PropFunc<T, ?> prop, Collection<Object> values) {
        return (ServiceUpdateWrapper<T>) super.in(prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> in(boolean b, PropFunc<T, ?> prop, Collection<Object> values) {
        return (ServiceUpdateWrapper<T>) super.in(b, prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> notIn(PropFunc<T, ?> prop, Object... values) {
        return (ServiceUpdateWrapper<T>) super.notIn(prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> notIn(boolean b, PropFunc<T, ?> prop, Object... values) {
        return (ServiceUpdateWrapper<T>) super.notIn(b, prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> notIn(PropFunc<T, ?> prop, Collection<Object> values) {
        return (ServiceUpdateWrapper<T>) super.notIn(prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> notIn(boolean b, PropFunc<T, ?> prop, Collection<Object> values) {
        return (ServiceUpdateWrapper<T>) super.notIn(b, prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> gt(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.gt(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> gt(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.gt(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> lt(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.lt(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> lt(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.lt(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> gte(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.gte(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> gte(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.gte(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> lte(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.lte(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> lte(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.lte(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> like(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.like(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> like(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.like(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> likeRight(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.likeRight(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> likeRight(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.likeRight(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> likeLeft(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.likeLeft(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> likeLeft(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.likeLeft(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> notLike(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.notLike(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> notLike(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.notLike(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> between(PropFunc<T, ?> prop, Object value1, Object value2) {
        return (ServiceUpdateWrapper<T>) super.between(prop, value1, value2);
    }

    @Override
    public ServiceUpdateWrapper<T> between(boolean b, PropFunc<T, ?> prop, Object value1, Object value2) {
        return (ServiceUpdateWrapper<T>) super.between(b, prop, value1, value2);
    }

    @Override
    public ServiceUpdateWrapper<T> notBetween(PropFunc<T, ?> prop, Object value1, Object value2) {
        return (ServiceUpdateWrapper<T>) super.notBetween(prop, value1, value2);
    }

    @Override
    public ServiceUpdateWrapper<T> notBetween(boolean b, PropFunc<T, ?> prop, Object value1, Object value2) {
        return (ServiceUpdateWrapper<T>) super.notBetween(b, prop, value1, value2);
    }

    @Override
    public ServiceUpdateWrapper<T> set(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.set(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> set(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.set(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> setIfAbsent(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.setIfAbsent(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> setIfAbsent(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.setIfAbsent(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> setIfPresent(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.setIfPresent(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> setIfPresent(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.setIfPresent(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> min(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.min(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> min(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.min(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> max(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.max(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> max(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.max(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> increase(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.increase(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> increase(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.increase(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> decrease(PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.decrease(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> decrease(boolean b, PropFunc<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.decrease(b, prop, value);
    }

    public int update() {
        return service.update(this);
    }
}
