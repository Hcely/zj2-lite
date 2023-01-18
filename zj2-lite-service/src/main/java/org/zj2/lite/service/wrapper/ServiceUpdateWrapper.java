package org.zj2.lite.service.wrapper;

import org.zj2.lite.common.function.PropGetter;
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
    public ServiceUpdateWrapper<T> eq(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.eq(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> eq(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.eq(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> ne(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.ne(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> ne(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.ne(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> isNull(PropGetter<T, ?> prop) {
        return (ServiceUpdateWrapper<T>) super.isNull(prop);
    }

    @Override
    public ServiceUpdateWrapper<T> isNull(boolean b, PropGetter<T, ?> prop) {
        return (ServiceUpdateWrapper<T>) super.isNull(b, prop);
    }

    @Override
    public ServiceUpdateWrapper<T> isNotNull(PropGetter<T, ?> prop) {
        return (ServiceUpdateWrapper<T>) super.isNotNull(prop);
    }

    @Override
    public ServiceUpdateWrapper<T> isNotNull(boolean b, PropGetter<T, ?> prop) {
        return (ServiceUpdateWrapper<T>) super.isNotNull(b, prop);
    }

    @Override
    public ServiceUpdateWrapper<T> in(PropGetter<T, ?> prop, Object... values) {
        return (ServiceUpdateWrapper<T>) super.in(prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> in(boolean b, PropGetter<T, ?> prop, Object... values) {
        return (ServiceUpdateWrapper<T>) super.in(b, prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> in(PropGetter<T, ?> prop, Collection<Object> values) {
        return (ServiceUpdateWrapper<T>) super.in(prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> in(boolean b, PropGetter<T, ?> prop, Collection<Object> values) {
        return (ServiceUpdateWrapper<T>) super.in(b, prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> notIn(PropGetter<T, ?> prop, Object... values) {
        return (ServiceUpdateWrapper<T>) super.notIn(prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> notIn(boolean b, PropGetter<T, ?> prop, Object... values) {
        return (ServiceUpdateWrapper<T>) super.notIn(b, prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> notIn(PropGetter<T, ?> prop, Collection<Object> values) {
        return (ServiceUpdateWrapper<T>) super.notIn(prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> notIn(boolean b, PropGetter<T, ?> prop, Collection<Object> values) {
        return (ServiceUpdateWrapper<T>) super.notIn(b, prop, values);
    }

    @Override
    public ServiceUpdateWrapper<T> gt(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.gt(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> gt(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.gt(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> lt(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.lt(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> lt(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.lt(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> gte(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.gte(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> gte(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.gte(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> lte(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.lte(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> lte(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.lte(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> like(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.like(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> like(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.like(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> likeRight(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.likeRight(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> likeRight(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.likeRight(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> likeLeft(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.likeLeft(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> likeLeft(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.likeLeft(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> notLike(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.notLike(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> notLike(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.notLike(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> between(PropGetter<T, ?> prop, Object value1, Object value2) {
        return (ServiceUpdateWrapper<T>) super.between(prop, value1, value2);
    }

    @Override
    public ServiceUpdateWrapper<T> between(boolean b, PropGetter<T, ?> prop, Object value1, Object value2) {
        return (ServiceUpdateWrapper<T>) super.between(b, prop, value1, value2);
    }

    @Override
    public ServiceUpdateWrapper<T> notBetween(PropGetter<T, ?> prop, Object value1, Object value2) {
        return (ServiceUpdateWrapper<T>) super.notBetween(prop, value1, value2);
    }

    @Override
    public ServiceUpdateWrapper<T> notBetween(boolean b, PropGetter<T, ?> prop, Object value1, Object value2) {
        return (ServiceUpdateWrapper<T>) super.notBetween(b, prop, value1, value2);
    }

    @Override
    public ServiceUpdateWrapper<T> set(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.set(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> set(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.set(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> setIfAbsent(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.setIfAbsent(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> setIfAbsent(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.setIfAbsent(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> setIfPresent(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.setIfPresent(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> setIfPresent(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.setIfPresent(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> min(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.min(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> min(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.min(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> max(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.max(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> max(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.max(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> increase(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.increase(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> increase(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.increase(b, prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> decrease(PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.decrease(prop, value);
    }

    @Override
    public ServiceUpdateWrapper<T> decrease(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceUpdateWrapper<T>) super.decrease(b, prop, value);
    }

    public int update() {
        return service.update(this);
    }
}
