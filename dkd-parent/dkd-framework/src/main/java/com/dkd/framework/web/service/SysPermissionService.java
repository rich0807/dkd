package com.dkd.framework.web.service;

import com.dkd.common.core.domain.entity.SysRole;
import com.dkd.common.core.domain.entity.SysUser;
import com.dkd.system.service.ISysMenuService;
import com.dkd.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author ruoyi
 */
@Component
public class SysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user)
    {
        // 初始化一个字符串集合，用于存储用户角色字符串
        Set<String> roles = new HashSet<String>();
        // 判断用户是否为管理员
        if (user.isAdmin())
        {
            roles.add("admin");// 管理员拥有所有权限，直接添加"admin"角色
        }
        else
        {
            // 非管理员用户，通过用户ID查询其角色权限，并添加到集合中
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }

        // 返回用户角色权限集合
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        // 初始化权限集合
        Set<String> perms = new HashSet<String>();
        // 判断用户是否为管理员，管理员拥有所有权限
        if (user.isAdmin())
        {
            perms.add("*:*:*");// 将所有权限添加到集合中
        }
        else
        {
            // 获取用户的所有角色
            List<SysRole> roles = user.getRoles();
            // 如果角色列表不为空
            if (!CollectionUtils.isEmpty(roles))
            {
                // 遍历角色列表，为每个角色设置权限，并添加到总的权限集合中
                for (SysRole role : roles)
                {
                    // 通过角色ID查询并获取每个角色的权限集合
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                    // 将角色权限设置到角色对象中，以便于数据权限匹配使用
                    role.setPermissions(rolePerms);
                    // 将角色权限合并到总的权限集合中
                    perms.addAll(rolePerms);
                }
            }
            else
            {
                // 如果用户没有角色，则直接获取用户的权限
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }

        // 返回用户的所有权限集合
        return perms;
    }
}
