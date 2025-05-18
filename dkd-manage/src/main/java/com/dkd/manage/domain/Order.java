package com.dkd.manage.domain;

import com.dkd.common.annotation.Excel;
import com.dkd.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 订单管理对象 tb_order
 *
 * @author RQ
 * @date 2024-08-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity
{   private static final long serialVersionUID = 1L;
    /** 主键 */
    private Long id;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 第三方平台单号 */
    @Excel(name = "第三方平台单号")
    private String thirdNo;

    /** 机器编号 */
    @Excel(name = "机器编号")
    private String innerCode;

    /** 货道编号 */
    @Excel(name = "货道编号")
    private String channelCode;

    /** skuId */
    @Excel(name = "skuId")
    private Long skuId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String skuName;

    /** 商品类别Id */
    @Excel(name = "商品类别Id")
    private Long classId;

    /** 订单状态:0-待支付;1-支付完成;2-出货成功;3-出货失败;4-已取消 */
    @Excel(name = "订单状态:0-待支付;1-支付完成;2-出货成功;3-出货失败;4-已取消")
    private Long status;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private Long amount;

    /** 商品金额 */
    @Excel(name = "商品金额")
    private Long price;

    /** 支付类型，1支付宝 2微信 */
    @Excel(name = "支付类型，1支付宝 2微信")
    private String payType;

    /** 支付状态，0-未支付;1-支付完成;2-退款中;3-退款完成 */
    @Excel(name = "支付状态，0-未支付;1-支付完成;2-退款中;3-退款完成")
    private Long payStatus;

    /** 合作商账单金额 */
    @Excel(name = "合作商账单金额")
    private Long bill;

    /** 点位地址 */
    @Excel(name = "点位地址")
    private String addr;

    /** 所属区域Id */
    @Excel(name = "所属区域Id")
    private Long regionId;

    /** 区域名称 */
    @Excel(name = "区域名称")
    private String regionName;

    /** 所属商圈 */
    @Excel(name = "所属商圈")
    private Long businessType;

    /** 合作商Id */
    @Excel(name = "合作商Id")
    private Long partnerId;

    /** 跨站身份验证 */
    @Excel(name = "跨站身份验证")
    private String openId;

    /** 点位Id */
    @Excel(name = "点位Id")
    private Long nodeId;

    /** 点位名称 */
    @Excel(name = "点位名称")
    private String nodeName;

    /** 取消原因 */
    @Excel(name = "取消原因")
    private String cancelDesc;




}
