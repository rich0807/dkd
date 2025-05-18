package com.dkd.framework.manager.factory;

import com.dkd.common.constant.Constants;
import com.dkd.common.utils.LogUtils;
import com.dkd.common.utils.ServletUtils;
import com.dkd.common.utils.StringUtils;
import com.dkd.common.utils.ip.AddressUtils;
import com.dkd.common.utils.ip.IpUtils;
import com.dkd.common.utils.spring.SpringUtils;
import com.dkd.system.domain.SysLogininfor;
import com.dkd.system.domain.SysOperLog;
import com.dkd.system.service.ISysLogininforService;
import com.dkd.system.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录用户登录信息的定时任务
     *
     * @param username 用户名
     * @param status 登录状态（成功、失败等）
     * @param message 登录的提示信息
     * @param args 其他可选参数
     * @return 返回一个实现了定时任务的TimerTask对象，用于异步记录登录信息
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
                                             final Object... args)
    {
        // 解析用户代理信息，用于获取客户端操作系统和浏览器信息
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        // 获取客户端IP地址
        final String ip = IpUtils.getIpAddr();
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 通过IP地址获取用户所在地理位置信息
                String address = AddressUtils.getRealAddressByIP(ip);
                // 构建日志信息字符串
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);

                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);

                // 根据登录状态设置记录状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    logininfor.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(Constants.FAIL);
                }

                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                // 插入数据
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}
