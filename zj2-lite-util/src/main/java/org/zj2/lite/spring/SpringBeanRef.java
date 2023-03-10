package org.zj2.lite.spring;


/**
 * <br>CreateDate 七月 06,2022
 *
 * @author peijie.ye
 */
public class SpringBeanRef<T> extends InstanceHolder<T> {
    private final String beanName;
    private final Class<?> beanType;

    public SpringBeanRef(String beanName) {
        super(null);
        this.beanName = beanName;
        this.beanType = null;
    }

    public SpringBeanRef(Class<T> beanType) {
        super(null);
        this.beanName = null;
        this.beanType = beanType;
    }

    @Override
    protected Object getImpl() {
        Object result;
        try {
            if(beanName != null) {
                result = SpringUtil.getBean(beanName);
            } else {
                result = SpringUtil.getBean(beanType);
            }
        } catch(Throwable e) {//NOSONAR
            result = null;
        }
        return result;
    }
}
