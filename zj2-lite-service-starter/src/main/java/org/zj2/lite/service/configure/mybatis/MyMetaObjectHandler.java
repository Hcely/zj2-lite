package org.zj2.lite.service.configure.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.zj2.lite.service.constant.ServiceConstants;
import org.zj2.lite.service.context.AuthContext;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        final LocalDateTime now = LocalDateTime.now();
        final AuthContext context = AuthContext.current();
        //
        this.setFieldValByName(ServiceConstants.APP_CODE, context.getAppCode(), metaObject);
        this.setFieldValByName(ServiceConstants.ORG_CODE, context.getOrgCode(), metaObject);
        //
        this.setFieldValByName(ServiceConstants.CREATE_TIME, now, metaObject);
        this.setFieldValByName(ServiceConstants.CREATE_USER, context.getUserId(), metaObject);
        this.setFieldValByName(ServiceConstants.CREATE_USER_NAME, context.getUserName(), metaObject);
        //
        this.setFieldValByName(ServiceConstants.UPDATE_TIME, now, metaObject);
        this.setFieldValByName(ServiceConstants.UPDATE_USER, context.getUserId(), metaObject);
        this.setFieldValByName(ServiceConstants.UPDATE_USER_NAME, context.getUserName(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        final LocalDateTime now = LocalDateTime.now();
        final AuthContext context = AuthContext.current();
        this.setFieldValByName(ServiceConstants.UPDATE_TIME, now, metaObject);
        this.setFieldValByName(ServiceConstants.UPDATE_USER, context.getUserId(), metaObject);
        this.setFieldValByName(ServiceConstants.UPDATE_USER_NAME, context.getUserName(), metaObject);
    }
}