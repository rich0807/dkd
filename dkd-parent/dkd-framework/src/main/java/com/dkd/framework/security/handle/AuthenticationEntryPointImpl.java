package com.dkd.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.dkd.common.constant.HttpStatus;
import com.dkd.common.core.domain.AjaxResult;
import com.dkd.common.utils.ServletUtils;
import com.dkd.common.utils.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author ruoyi
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable
{
    private static final long serialVersionUID = -8970718410437077606L;

    /**
     * 处理未通过身份验证的请求
     * 当用户尝试访问需要身份验证的资源但未通过认证时，此方法被调用
     * 它向客户端返回一个包含错误信息的HTTP响应，指示认证失败
     *
     * @param request  请求对象，包含用户尝试访问的URI等信息
     * @param response 响应对象，用于向客户端发送错误信息
     * @param e        身份验证异常对象，指示用户身份验证失败的原因
     * @throws IOException 如果在向客户端发送响应时发生输入输出错误
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        // HTTP状态码401，表示未授权
        int code = HttpStatus.UNAUTHORIZED;
        // 构建错误消息，包括尝试访问的URI
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        // 使用JSON格式向客户端返回错误信息
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }
}
