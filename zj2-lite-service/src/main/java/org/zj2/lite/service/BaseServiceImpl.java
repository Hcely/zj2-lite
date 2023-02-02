package org.zj2.lite.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.util.BeanUtil;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.PropertyUtil;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.entity.request.PageRequest;
import org.zj2.lite.service.entity.request.wrapper.ZQueryWrapper;
import org.zj2.lite.service.entity.request.wrapper.ZUpdateWrapper;
import org.zj2.lite.service.util.SafeLogUtil;
import org.zj2.lite.spring.SpringUtil;
import org.zj2.lite.util.ZRBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * BaseServiceImpl
 * <br>CreateDate 三月 16,2022
 * @author peijie.ye
 * @since 1.0
 */
@SuppressWarnings("all")
public class BaseServiceImpl<M extends BaseMapper<DO>, DO, DTO> //NOSONAR
        implements BaseInnerService<DTO>, InitializingBean {
    private static final Set<String> UPDATE_IGNORED_FIELDS = Set.of(ServiceConstants.APP_CODE,
            ServiceConstants.ORG_CODE, ServiceConstants.ORG_GROUP_CODE, ServiceConstants.CREATE_TIME,
            ServiceConstants.CREATE_USER, ServiceConstants.CREATE_USER_NAME);
    protected final Logger log = SafeLogUtil.getLogger(this.getClass());
    @Autowired
    protected M mapper;
    protected final Class<DO> entityType;
    protected final Class<DTO> dtoType;
    protected TableInfo tableInfo;
    protected Map<String, String> tableFieldMap;

    public BaseServiceImpl() {
        entityType = (Class<DO>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseServiceImpl.class, 1);
        dtoType = (Class<DTO>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseServiceImpl.class, 2);
    }

    protected final ZQueryWrapper<DTO> wrapper() {
        return new ZQueryWrapper<>();
    }

    protected final ZQueryWrapper<DTO> wrapper(boolean ignoreEmpty) {
        return new ZQueryWrapper<DTO>().ignoreEmpty(ignoreEmpty);
    }

    protected final ZUpdateWrapper<DTO> updateWrapper() {
        return new ZUpdateWrapper<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        tableInfo = TableInfoHelper.getTableInfo(entityType);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        tableFieldMap = new HashMap<>();
        for (TableFieldInfo e : CollUtil.of(tableInfo.getFieldList())) {
            tableFieldMap.put(e.getProperty(), e.getColumn());
        }
        tableFieldMap.put(tableInfo.getKeyProperty(), tableInfo.getKeyColumn());
        SafeLogUtil.addSensitiveProperty(entityType);
        SafeLogUtil.addSensitiveProperty(dtoType);
    }

    protected <T extends BaseServiceImpl> T self(T instance) {
        return (T) SpringUtil.self(instance);
    }

    protected String getPrimaryId(DTO dto) {
        if (dto == null) { return null; }
        Object idVal = PropertyUtil.getProperty(dto, tableInfo.getKeyProperty());
        return idVal == null ? null : idVal.toString();
    }

    @Override
    public DTO getCache(String id) {
        return CacheUtil.DEF_CACHE.get(dtoType, id, this::get);
    }

    @Override
    public DTO get(String id) {
        return StringUtils.isEmpty(id) ? null : BeanUtil.toBean(mapper.selectById(id), dtoType);
    }

    @Override
    public List<DTO> getByIds(Collection<String> ids) {
        return CollUtil.isEmpty(ids) ? new ArrayList<>() : BeanUtil.copyToList(mapper.selectBatchIds(ids), dtoType);
    }

    @Override
    public DTO getOne(ZQueryWrapper<DTO> wrapper) {
        wrapper.limit(1);
        return BeanUtil.toBean(
                mapper.selectOne(WrapperUtil.buildQueryCondition(tableInfo.getKeyColumn(), tableFieldMap, wrapper)),
                dtoType);
    }

    @Override
    public List<DTO> query(ZQueryWrapper<DTO> wrapper) {
        return BeanUtil.copyToList(
                mapper.selectList(WrapperUtil.buildQueryCondition(tableInfo.getKeyColumn(), tableFieldMap, wrapper)),
                dtoType);
    }

    @Override
    public int selectCount(ZQueryWrapper<DTO> wrapper) {
        Long i = mapper.selectCount(WrapperUtil.buildQueryCondition(tableInfo.getKeyColumn(), tableFieldMap, wrapper));
        return i == null ? 0 : i.intValue();
    }

    @Override
    public boolean exists(ZQueryWrapper<DTO> wrapper) {
        return mapper.exists(WrapperUtil.buildQueryCondition(tableInfo.getKeyColumn(), tableFieldMap, wrapper));
    }

    @Override
    public Logger logger() {
        return log;
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        log.info("[delete]-id:" + id);
        if (StringUtils.isEmpty(id)) { return false; }
        return mapper.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public int delete(Collection<String> ids) {
        log.info("[delete]-ids:" + ids);
        if (CollUtil.isEmpty(ids)) { return 0; }
        return mapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public int delete(ZQueryWrapper<DTO> wrapper) {
        log.info("[delete]-wrapper:" + SafeLogUtil.toJSONStr(wrapper));
        if (CollUtil.isEmpty(wrapper.getConditions())) { throw new ZError(entityType + ":禁止全表删除"); }
        return mapper.delete(WrapperUtil.buildCondition(tableFieldMap, wrapper));
    }

    @Override
    @Transactional
    public boolean updateById(DTO dto) {
        if (dto == null) { return false; }
        String primaryId = getPrimaryId(dto);
        DO entity = BeanUtil.copyProperties(dto, tableInfo.newInstance(), UPDATE_IGNORED_FIELDS);
        log.info("[updateById]-entity:" + SafeLogUtil.toJSONStr(entity));
        if (StringUtils.isEmpty(primaryId)) { return false; }
        boolean b = mapper.updateById(entity) > 0;
        if (b) { BeanUtil.copyProperties(entity, dto, UPDATE_IGNORED_FIELDS); }
        return b;
    }

    @Override
    @Transactional
    public int update(DTO updateDTO, ZQueryWrapper<DTO> wrapper) {
        if (updateDTO == null) { return 0; }
        if (CollUtil.isEmpty(wrapper.getConditions())) { throw new ZError(entityType + ":禁止全表更新"); }
        DO entity = BeanUtil.copyProperties(updateDTO, tableInfo.newInstance(), UPDATE_IGNORED_FIELDS);
        log.info("[update]-entity:" + SafeLogUtil.toJSONStr(entity) + ",wrapper:" + SafeLogUtil.toJSONStr(wrapper));
        return mapper.update(entity, WrapperUtil.buildCondition(tableFieldMap, wrapper));
    }

    @Override
    @Transactional
    public int update(ZUpdateWrapper<DTO> wrapper) {
        log.info("[update]-wrapper:" + JSON.toJSONString(wrapper));
        if (CollUtil.isEmpty(wrapper.getConditions())) { throw new ZError(entityType + ":禁止全表更新"); }
        UpdateWrapper<DO> updateWrapper = WrapperUtil.buildUpdate(tableFieldMap, wrapper);
        return mapper.update(null, updateWrapper);
    }

    @Override
    @Transactional
    public DTO add(DTO dto) {
        if (dto == null) { return dto; }
        try {
            DO entity = BeanUtil.copyProperties(dto, tableInfo.newInstance());
            mapper.insert(entity);
            log.info("[add]-entity:" + SafeLogUtil.toJSONStr(entity));
            BeanUtil.copyProperties(entity, dto);
            return dto;
        } catch (Throwable e) {
            log.error("[add]-dto:" + SafeLogUtil.toJSONStr(dto), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public <C extends Collection<? extends DTO>> int batchAdd(C dtos) {
        if (CollUtil.isEmpty(dtos)) { return 0; }
        int i = 0;
        int count = 0;
        for (DTO dto : dtos) {
            try {
                DO entity = BeanUtil.copyProperties(dto, tableInfo.newInstance());
                boolean b = mapper.insert(entity) > 0;
                log.info("[batchAdd]-entity[" + i + "]:" + SafeLogUtil.toJSONStr(entity));
                BeanUtil.copyProperties(entity, dto);
                if (b) { ++count; }
            } catch (Throwable e) {
                log.error("[batchAdd]-dto[" + i + "]:" + SafeLogUtil.toJSONStr(dto), e);
                throw e;
            }
            ++i;
        }
        return count;
    }

    @Override
    @Transactional
    public boolean save(DTO dto) {
        if (dto == null) { return false; }
        final String primaryId = getPrimaryId(dto);
        try {
            boolean b;
            if (StringUtils.isNotEmpty(primaryId)) {
                DO entity = BeanUtil.copyProperties(dto, tableInfo.newInstance(), UPDATE_IGNORED_FIELDS);
                b = mapper.updateById(entity) > 0;
                BeanUtil.copyProperties(entity, dto, UPDATE_IGNORED_FIELDS);
                log.info("[save(u:" + b + ")]-entity:" + SafeLogUtil.toJSONStr(entity));
            } else {
                DO entity = BeanUtil.copyProperties(dto, tableInfo.newInstance());
                b = mapper.insert(entity) > 0;
                BeanUtil.copyProperties(entity, dto);
                log.info("[save(i)]-entity:" + SafeLogUtil.toJSONStr(entity));
            }
            return b;
        } catch (Throwable e) {
            log.error("[save]-dto:" + SafeLogUtil.toJSONStr(dto), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean tryUpdateInsert(DTO dto) {
        if (dto == null) { return false; }
        final String primaryId = getPrimaryId(dto);
        try {
            if (StringUtils.isNotEmpty(primaryId)) {
                DO entity = BeanUtil.copyProperties(dto, tableInfo.newInstance(), UPDATE_IGNORED_FIELDS);
                boolean b = mapper.updateById(entity) > 0;
                if (b) {
                    BeanUtil.copyProperties(entity, dto, UPDATE_IGNORED_FIELDS);
                    log.info("[upsert(u)]-entity:" + SafeLogUtil.toJSONStr(entity));
                    return true;
                }
            }
            DO entity = BeanUtil.copyProperties(dto, tableInfo.newInstance());
            mapper.insert(entity);
            log.info("[upsert(i)]-entity:" + SafeLogUtil.toJSONStr(entity));
            BeanUtil.copyProperties(entity, dto);
            return true;
        } catch (Throwable e) {
            log.error("[upsert]-dto:" + SafeLogUtil.toJSONStr(dto), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public <C extends Collection<? extends DTO>> int batchSave(C dtos) {
        if (CollUtil.isEmpty(dtos)) { return 0; }
        int i = 0;
        int count = 0;
        for (DTO dto : dtos) {
            try {
                String primaryId = getPrimaryId(dto);
                boolean b;
                if (StringUtils.isNotEmpty(primaryId)) {
                    DO entity = BeanUtil.copyProperties(dto, tableInfo.newInstance(), UPDATE_IGNORED_FIELDS);
                    b = mapper.updateById(entity) > 0;
                    BeanUtil.copyProperties(entity, dto, UPDATE_IGNORED_FIELDS);
                    log.info("[batchSave(u:" + b + ")]-entity[" + i + "]:" + SafeLogUtil.toJSONStr(entity));
                } else {
                    DO entity = BeanUtil.copyProperties(dto, tableInfo.newInstance());
                    b = mapper.insert(entity) > 0;
                    BeanUtil.copyProperties(entity, dto);
                    log.info("[batchSave(i)]-entity[" + i + "]:" + SafeLogUtil.toJSONStr(entity));
                }
                if (b) { ++count; }
            } catch (Throwable e) {
                log.error("[batchSave]-dto[" + i + "]:" + SafeLogUtil.toJSONStr(dto), e);
                throw e;
            }
            ++i;
        }
        return count;
    }

    protected <R extends PageRequest, E> ZListResp<E> pageQuery(R request, Function<R, List<E>> query) {
        Integer pageNumber = request.getPageNumber();
        Integer pageSize = request.getPageSize();
        if (pageSize == null || pageSize < 1) {
            PageHelper.clearPage();
            List<E> models = query.apply(request);
            return ZRBuilder.success().buildListResp(models);
        } else {
            try {
                pageNumber = pageNumber == null ? 1 : Math.max(pageNumber, 1);
                Page<E> page = PageHelper.startPage(pageNumber, pageSize);
                List<E> list = query.apply(request);
                return ZRBuilder.success().buildListResp(page, (int) page.getTotal(), pageNumber, pageSize);
            } finally {
                PageHelper.clearPage();
            }
        }
    }

    protected <E> ZListResp<E> pageQuery(Integer pageNumber, Integer pageSize, Supplier<List<E>> query) {
        if (pageSize == null || pageSize < 1) {
            PageHelper.clearPage();
            List<E> models = query.get();
            return ZRBuilder.success().buildListResp(models);
        } else {
            try {
                pageNumber = pageNumber == null ? 1 : Math.max(pageNumber, 1);
                Page<E> page = PageHelper.startPage(pageNumber, pageSize);
                List<E> list = query.get();
                return ZRBuilder.success().buildListResp(list, (int) page.getTotal(), pageNumber, pageSize);
            } finally {
                PageHelper.clearPage();
            }
        }
    }
}
