package top.yifan.template;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContextHelper
 *
 * @author Star.Y.Zheng
 */
@Component
public final class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        ApplicationContextHelper.applicationContext = applicationContext;
    }

    /**
     * 根据一个bean的类型获取相应的bean
     */
    public static <T> T getBean(Class<T> requiredType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(requiredType);
    }
}
