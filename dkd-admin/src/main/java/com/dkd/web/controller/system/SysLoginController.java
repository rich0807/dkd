package com.dkd.web.controller.system;

import com.dkd.common.constant.Constants;
import com.dkd.common.core.domain.AjaxResult;
import com.dkd.common.core.domain.entity.SysMenu;
import com.dkd.common.core.domain.entity.SysUser;
import com.dkd.common.core.domain.model.LoginBody;
import com.dkd.common.utils.SecurityUtils;
import com.dkd.framework.web.service.SysLoginService;
import com.dkd.framework.web.service.SysPermissionService;
import com.dkd.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        // 初始化一个成功的AjaxResult对象
        AjaxResult ajax = AjaxResult.success();
        // 调用serivce登录认证，返回token令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);// 将生成的令牌放入AjaxResult对象中
        return ajax; // 返回包含令牌的AjaxResult对象
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        // 获取当前登录用户对象
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 调用service查询当前用户的角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 调用service查询当前用户的权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        // 创建并配置一个成功的AjaxResult对象
        AjaxResult ajax = AjaxResult.success();
        // 向AjaxResult对象中放入用户信息、角色和权限
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        // 返回配置好的AjaxResult对象
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();

        // 根据用户ID查询该用户的所有菜单（权限），以树状结构返回
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);

        // 将菜单树转换为前端所需的路由格式并返回
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
