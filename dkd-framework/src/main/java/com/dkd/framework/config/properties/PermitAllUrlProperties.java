package com.dkd.framework.config.properties;

import com.dkd.common.annotation.Anonymous;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 设置Anonymous注解允许匿名访问的url
 *
 * @author ruoyi
 */
@Configuration
public class PermitAllUrlProperties implements InitializingBean, ApplicationContextAware
{
    // 定义一个静态的正则表达式模式，用于匹配路径变量
    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    // 应用上下文对象，用于获取Spring Bean
    private ApplicationContext applicationContext;

    // 存储允许匿名访问的URL模式的列表
    private List<String> urls = new ArrayList<>();

    // 通配符，用于替换路径中的变量部分
    public String ASTERISK = "*";

    /**
     * 在属性设置之后执行初始化操作
     * 主要用于解析请求映射，找出标记为匿名访问的方法和控制器，并将其URL模式添加到urls列表中
     */
    @Override
    public void afterPropertiesSet()
    {
        // 从应用上下文中获取RequestMappingHandlerMapping的实例
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取所有处理器方法及其对应的请求映射信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        // 遍历所有请求映射信息
        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);

            // 检查方法上是否有Anonymous注解
            Anonymous method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class);
            // 如果方法上有Anonymous注解
            Optional.ofNullable(method).ifPresent(anonymous ->
                    Objects.requireNonNull(info.getPatternsCondition().getPatterns())
                            .forEach(url -> urls.add(RegExUtils.replaceAll(url, PATTERN, ASTERISK))));

            // 检查控制器类上是否有Anonymous注解
            Anonymous controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Anonymous.class);
            // 如果控制器类上有Anonymous注解
            Optional.ofNullable(controller).ifPresent(anonymous ->
                    Objects.requireNonNull(info.getPatternsCondition().getPatterns())
                            .forEach(url -> urls.add(RegExUtils.replaceAll(url, PATTERN, ASTERISK))));
        });
    }

    /**
     * 设置应用上下文
     * @param context 应用上下文对象
     * @throws BeansException 如果应用上下文设置失败
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.applicationContext = context;
    }

    /**
     * 获取允许匿名访问的URL模式列表
     * @return URL模式列表
     */
    public List<String> getUrls()
    {
        return urls;
    }

    /**
     * 设置允许匿名访问的URL模式列表
     * @param urls URL模式列表
     */
    public void setUrls(List<String> urls)
    {
        this.urls = urls;
    }
}
