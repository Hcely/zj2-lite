package org.zj2.lite.service;

import org.zj2.lite.service.entity.request.wrapper.ZQueryWrapper;

import java.util.Collection;
import java.util.List;

/**
 * <br>CreateDate 三月 21,2022
 *
 * @author peijie.ye
 */
public interface BaseApi<DTO> {// NOSONAR

    DTO getCache(String id);

    DTO get(String id);

    List<DTO> getByIds(Collection<String> ids);

    DTO getOne(ZQueryWrapper<DTO> condition);

    List<DTO> query(ZQueryWrapper<DTO> condition);

    int selectCount(ZQueryWrapper<DTO> condition);

    boolean exists(ZQueryWrapper<DTO> condition);
}
