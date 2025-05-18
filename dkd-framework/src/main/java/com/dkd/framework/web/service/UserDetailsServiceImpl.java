package com.dkd.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.dkd.common.core.domain.entity.SysUser;
import com.dkd.common.core.domain.model.LoginUser;
import com.dkd.common.enums.UserStatus;
import com.dkd.common.exception.ServiceException;
import com.dkd.common.utils.MessageUtils;
import com.dkd.common.utils.StringUtils;
import com.dkd.system.service.ISysUserService;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        // 通过用户名查询用户信息
        SysUser user = userService.selectUserByUserName(username);

        // 检查用户是否存在
        if (StringUtils.isNull(user))
        {
            // 记录用户不存在的日志
            log.info("登录用户：{} 不存在.", username);
            // 抛出异常，提示用户不存在
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        }
        // 检查用户是否已被删除
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            // 记录用户已被删除的日志
            log.info("登录用户：{} 已被删除.", username);
            // 抛出异常，提示用户已被删除
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        // 检查用户是否已被停用
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            // 记录用户已被停用的日志
            log.info("登录用户：{} 已被停用.", username);
            // 抛出异常，提示用户已被停用
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }

        // 验证用户密码是否正确
        passwordService.validate(user);

        // 创建并返回登录用户对象
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        // 根据提供的SysUser对象创建LoginUser对象，其中包括用户ID、部门ID、用户信息和用户角色权限信息
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
