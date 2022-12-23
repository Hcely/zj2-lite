package org.zj2.lite.service;

import org.slf4j.Logger;
import org.zj2.lite.service.entity.request.wrapper.ZQueryWrapper;
import org.zj2.lite.service.entity.request.wrapper.ZUpdateWrapper;

import java.util.Collection;
import java.util.List;

/**
 *
 * <br>CreateDate 三月 21,2022
 * @author peijie.ye
 */
public interface BaseInnerService<DTO> {// NOSONAR

    Logger logger();

    DTO getCache(String id);

    DTO get(String id);

    List<DTO> getByIds(Collection<String> ids);

    DTO getOne(ZQueryWrapper<DTO> condition);

    List<DTO> query(ZQueryWrapper<DTO> condition);

    int selectCount(ZQueryWrapper<DTO> condition);

    boolean exists(ZQueryWrapper<DTO> condition);

    boolean delete(String id);

    int delete(Collection<String> ids);

    int delete(ZQueryWrapper<DTO> wrapper);

    boolean updateById(DTO dto);

    int update(DTO updateDTO, ZQueryWrapper<DTO> wrapper);

    int update(ZUpdateWrapper<DTO> wrapper);

    DTO add(DTO dto);

    <C extends Collection<? extends DTO>> int batchAdd(C dtos);

    /**
     * 如果存在主键id，则更新,否则新增
     * @param dto
     * @return
     */
    boolean save(DTO dto);

    /**
     * 尝试更新，更新失败则新增
     * 可能存在逻辑删问题，请注意
     * @param dto
     * @return
     */
    boolean tryUpdateInsert(DTO dto);

    <C extends Collection<? extends DTO>> int batchSave(C dtos);
}
