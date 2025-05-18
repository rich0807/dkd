package com.dkd.framework.web.service;

import com.dkd.common.constant.CacheConstants;
import com.dkd.common.core.domain.entity.SysUser;
import com.dkd.common.core.redis.RedisCache;
import com.dkd.common.exception.user.UserPasswordNotMatchException;
import com.dkd.common.exception.user.UserPasswordRetryLimitExceedException;
import com.dkd.common.utils.SecurityUtils;
import com.dkd.framework.security.context.AuthenticationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 *
 * @author ruoyi
 */
@Component
public class SysPasswordService
{
    @Autowired
    private RedisCache redisCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    /**
     * 验证用户登录信息
     * 本方法主要功能是验证传入的用户对象的密码是否正确，同时处理密码重试逻辑
     * 如果密码错误，会增加重试计数并在达到最大重试次数后锁定用户
     * 如果密码正确，则清除之前的登录记录缓存
     *
     * @param user 待验证的用户对象，包含用户信息和密码
     * @throws UserPasswordRetryLimitExceedException 如果用户密码重试次数超过限定次数，则抛出此异常
     * @throws UserPasswordNotMatchException 如果用户密码不匹配，则抛出此异常
     */
    public void validate(SysUser user)
    {
        // 获取当前的认证信息
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        // 从认证信息中提取用户名
        String username = usernamePasswordAuthenticationToken.getName();
        // 从认证信息中提取密码
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        // 尝试从缓存中获取当前用户的密码重试次数
        Integer retryCount = redisCache.getCacheObject(getCacheKey(username));

        // 如果缓存中没有重试次数，初始化为0
        if (retryCount == null)
        {
            retryCount = 0;
        }

        // 检查重试次数是否已经达到或超过最大重试次数
        if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        {
            // 如果达到最大重试次数，抛出异常，并指定锁定时间
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        // 检查用户提供的密码是否与数据库中的密码匹配
        if (!matches(user, password))
        {
            // 如果密码不匹配，增加重试计数
            retryCount = retryCount + 1;
            // 更新缓存中的重试次数，并设置锁定时间
            redisCache.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            // 抛出密码不匹配异常
            throw new UserPasswordNotMatchException();
        }
        else
        {
            // 如果密码匹配，清除登录记录缓存
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (redisCache.hasKey(getCacheKey(loginName)))
        {
            redisCache.deleteObject(getCacheKey(loginName));
        }
    }
}
