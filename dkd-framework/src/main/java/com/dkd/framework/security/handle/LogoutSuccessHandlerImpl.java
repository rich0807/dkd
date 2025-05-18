package com.dkd.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.dkd.common.constant.Constants;
import com.dkd.common.core.domain.AjaxResult;
import com.dkd.common.core.domain.model.LoginUser;
import com.dkd.common.utils.MessageUtils;
import com.dkd.common.utils.ServletUtils;
import com.dkd.common.utils.StringUtils;
import com.dkd.framework.manager.AsyncManager;
import com.dkd.framework.manager.factory.AsyncFactory;
import com.dkd.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        // 获取当前登录用户
        LoginUser loginUser = tokenService.getLoginUser(request);
        // 检查用户是否已登录
        if (StringUtils.isNotNull(loginUser))
        {
            // 获取用户名
            String userName = loginUser.getUsername();
            // 删除用户缓存记录，实现退出
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志，使用异步执行
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        // 向客户端返回退出成功的信息
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}
