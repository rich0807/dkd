package com.dkd.framework.security.filter;

import com.dkd.common.core.domain.model.LoginUser;
import com.dkd.common.utils.SecurityUtils;
import com.dkd.common.utils.StringUtils;
import com.dkd.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 * @author ruoyi
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private TokenService tokenService;

    /**
     * 执行过滤器的内部方法该方法主要用于身份验证
     * 它通过令牌服务获取当前用户，如果用户已登录且当前没有认证对象，则创建一个认证对象并设置到上下文中
     * 这样其他地方就可以获取到用户信息
     *
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        // 通过令牌服务获取登录用户信息
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 检查是否已登录且SS当前没有认证对象
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication()))
        {
            // 验证用户令牌是否有效
            tokenService.verifyToken(loginUser);
            // 创建认证对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            // 设置认证对象的详细信息，这些详细信息是基于web的认证细节
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 将认证对象设置到安全上下文中，这样应用的其他部分可以访问到用户信息
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        // 继续执行过滤器链中的下一个过滤器或目标servlet
        chain.doFilter(request, response);
    }
}
