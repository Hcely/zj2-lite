package org.zj2.lite.service.wrapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.function.PropGetter;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.service.BaseInnerService;
import org.zj2.lite.service.entity.request.PageRequest;
import org.zj2.lite.service.entity.request.wrapper.ZQueryWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 *
 * <br>CreateDate 六月 17,2022
 * @author peijie.ye
 */
@SuppressWarnings("unchecked")
public class ServiceQueryWrapper<T> extends ZQueryWrapper<T> {
    private static final long serialVersionUID = 4476746589280892543L;
    //
    private final transient BaseInnerService<T> service;

    public static <E> ServiceQueryWrapper<E> wrapper(BaseInnerService<E> service) {
        return new ServiceQueryWrapper<>(service);
    }

    private ServiceQueryWrapper(BaseInnerService<T> service) {
        this.service = service;
    }

    @Override
    public ServiceQueryWrapper<T> ignoreEmpty() {
        return (ServiceQueryWrapper<T>) super.ignoreEmpty();
    }

    @Override
    public ServiceQueryWrapper<T> ignoreEmpty(boolean ignoreEmpty) {
        return (ServiceQueryWrapper<T>) super.ignoreEmpty(ignoreEmpty);
    }

    @Override
    public ServiceQueryWrapper<T> select(PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.select(prop);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ServiceQueryWrapper<T> select(PropGetter<T, ?>... props) {
        return (ServiceQueryWrapper<T>) super.select(props);
    }

    @Override
    public ServiceQueryWrapper<T> eq(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.eq(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> eq(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.eq(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> ne(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.ne(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> ne(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.ne(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> isNull(PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.isNull(prop);
    }

    @Override
    public ServiceQueryWrapper<T> isNull(boolean b, PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.isNull(b, prop);
    }

    @Override
    public ServiceQueryWrapper<T> isNotNull(PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.isNotNull(prop);
    }

    @Override
    public ServiceQueryWrapper<T> isNotNull(boolean b, PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.isNotNull(b, prop);
    }

    @Override
    public ServiceQueryWrapper<T> in(PropGetter<T, ?> prop, Object... values) {
        return (ServiceQueryWrapper<T>) super.in(prop, values);
    }

    @Override
    public ServiceQueryWrapper<T> in(boolean b, PropGetter<T, ?> prop, Object... values) {
        return (ServiceQueryWrapper<T>) super.in(b, prop, values);
    }

    @Override
    public ServiceQueryWrapper<T> in(PropGetter<T, ?> prop, Collection<Object> values) {
        return (ServiceQueryWrapper<T>) super.in(prop, values);
    }

    @Override
    public ServiceQueryWrapper<T> in(boolean b, PropGetter<T, ?> prop, Collection<Object> values) {
        return (ServiceQueryWrapper<T>) super.in(b, prop, values);
    }

    @Override
    public ServiceQueryWrapper<T> notIn(PropGetter<T, ?> prop, Object... values) {
        return (ServiceQueryWrapper<T>) super.notIn(prop, values);
    }

    @Override
    public ServiceQueryWrapper<T> notIn(boolean b, PropGetter<T, ?> prop, Object... values) {
        return (ServiceQueryWrapper<T>) super.notIn(b, prop, values);
    }

    @Override
    public ServiceQueryWrapper<T> notIn(PropGetter<T, ?> prop, Collection<Object> values) {
        return (ServiceQueryWrapper<T>) super.notIn(prop, values);
    }

    @Override
    public ServiceQueryWrapper<T> notIn(boolean b, PropGetter<T, ?> prop, Collection<Object> values) {
        return (ServiceQueryWrapper<T>) super.notIn(b, prop, values);
    }

    @Override
    public ServiceQueryWrapper<T> gt(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.gt(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> gt(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.gt(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> lt(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.lt(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> lt(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.lt(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> gte(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.gte(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> gte(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.gte(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> lte(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.lte(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> lte(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.lte(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> like(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.like(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> like(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.like(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> likeRight(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.likeRight(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> likeRight(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.likeRight(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> likeLeft(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.likeLeft(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> likeLeft(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.likeLeft(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> notLike(PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.notLike(prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> notLike(boolean b, PropGetter<T, ?> prop, Object value) {
        return (ServiceQueryWrapper<T>) super.notLike(b, prop, value);
    }

    @Override
    public ServiceQueryWrapper<T> between(PropGetter<T, ?> prop, Object value1, Object value2) {
        return (ServiceQueryWrapper<T>) super.between(prop, value1, value2);
    }

    @Override
    public ServiceQueryWrapper<T> between(boolean b, PropGetter<T, ?> prop, Object value1, Object value2) {
        return (ServiceQueryWrapper<T>) super.between(b, prop, value1, value2);
    }

    @Override
    public ServiceQueryWrapper<T> notBetween(PropGetter<T, ?> prop, Object value1, Object value2) {
        return (ServiceQueryWrapper<T>) super.notBetween(prop, value1, value2);
    }

    @Override
    public ServiceQueryWrapper<T> notBetween(boolean b, PropGetter<T, ?> prop, Object value1, Object value2) {
        return (ServiceQueryWrapper<T>) super.notBetween(b, prop, value1, value2);
    }

    @Override
    public ServiceQueryWrapper<T> orderByAsc(PropGetter<T, ?>... props) {
        return (ServiceQueryWrapper<T>) super.orderByAsc(props);
    }

    @Override
    public ServiceQueryWrapper<T> orderByAsc(boolean b, PropGetter<T, ?>... props) {
        return (ServiceQueryWrapper<T>) super.orderByAsc(b, props);
    }

    @Override
    public ServiceQueryWrapper<T> orderByDesc(PropGetter<T, ?>... props) {
        return (ServiceQueryWrapper<T>) super.orderByDesc(props);
    }

    @Override
    public ServiceQueryWrapper<T> orderByDesc(boolean b, PropGetter<T, ?>... props) {
        return (ServiceQueryWrapper<T>) super.orderByDesc(b, props);
    }

    @Override
    public ServiceQueryWrapper<T> orderBy(boolean asc, PropGetter<T, ?>... props) {
        return (ServiceQueryWrapper<T>) super.orderBy(asc, props);
    }

    @Override
    public ServiceQueryWrapper<T> orderBy(boolean b, boolean asc, PropGetter<T, ?>... props) {
        return (ServiceQueryWrapper<T>) super.orderBy(b, asc, props);
    }

    @Override
    public ServiceQueryWrapper<T> orderByAsc(PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.orderByAsc(prop);
    }

    @Override
    public ServiceQueryWrapper<T> orderByAsc(boolean b, PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.orderByAsc(b, prop);
    }

    @Override
    public ServiceQueryWrapper<T> orderByDesc(PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.orderByDesc(prop);
    }

    @Override
    public ServiceQueryWrapper<T> orderByDesc(boolean b, PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.orderByDesc(b, prop);
    }

    @Override
    public ServiceQueryWrapper<T> orderBy(boolean asc, PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.orderBy(asc, prop);
    }

    @Override
    public ServiceQueryWrapper<T> orderBy(boolean b, boolean asc, PropGetter<T, ?> prop) {
        return (ServiceQueryWrapper<T>) super.orderBy(b, asc, prop);
    }

    @Override
    public ServiceQueryWrapper<T> limit(int size) {
        return (ServiceQueryWrapper<T>) super.limit(size);
    }

    @Override
    public ServiceQueryWrapper<T> limitPage(PageRequest request) {
        return (ServiceQueryWrapper<T>) super.limitPage(request);
    }

    @Override
    public ServiceQueryWrapper<T> limitPage(Integer pageNumber, Integer pageSize) {
        return (ServiceQueryWrapper<T>) super.limitPage(pageNumber, pageSize);
    }

    @Override
    public ServiceQueryWrapper<T> limit(int offset, int size) {
        return (ServiceQueryWrapper<T>) super.limit(offset, size);
    }

    @Override
    public ServiceQueryWrapper<T> forUpdate(boolean b) {
        return (ServiceQueryWrapper<T>) super.forUpdate(b);
    }

    public List<T> list() {
        return service.query(this);
    }

    public Map<String, T> listToMap(Function<T, String> getter) {
        return CollUtil.toMap(list(), getter);
    }

    public Map<String, List<T>> listGroupBy(Function<T, String> getter) {
        return CollUtil.groupingBy(list(), getter);
    }

    public Map<String, T> listMapReduce(Function<T, String> getter, BiConsumer<T, T> reduceFunc) {
        return CollUtil.mapReduce(list(), getter, reduceFunc);
    }

    public List<T> listMapReduceAsList(Function<T, String> getter, BiConsumer<T, T> reduceFunc) {
        return CollUtil.mapReduceAsList(list(), getter, reduceFunc);
    }

    public T one() {
        return service.getOne(this);
    }

    public Optional<T> oneOpt() {
        return Optional.ofNullable(one());
    }

    public int count() {
        return service.selectCount(this);
    }

    public boolean exists() {
        return service.exists(this);
    }

    public boolean notExists() {
        return !exists();
    }

    public ZListResp<T> page(PageRequest pageRequest) {
        return page(pageRequest.getPageNumber(), pageRequest.getPageSize());
    }

    public ZListResp<T> page(Integer pageNumber, Integer pageSize) {
        limit(-1).forUpdate(false);// 分页查询不允许配置该数据
        if (pageSize == null || pageSize < 1) {
            PageHelper.clearPage();
            List<T> models = list();
            return ZRBuilder.success().buildListResp(models);
        } else {
            try {
                pageNumber = pageNumber == null ? 1 : Math.max(pageNumber, 1);
                Page<T> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(this::list);
                return ZRBuilder.success().buildListResp(page, (int) page.getTotal(), pageNumber, pageSize);
            } finally {
                PageHelper.clearPage();
            }
        }
    }
}
