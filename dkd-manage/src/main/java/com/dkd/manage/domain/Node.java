package com.dkd.manage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dkd.common.annotation.Excel;
import com.dkd.common.core.domain.BaseEntity;

/**
 * 点位管理对象 tb_node
 * 
 * @author ruoyi
 * @date 2025-04-22
 */
public class Node extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 点位ID */
    private Long id;

    /** 点位名称 */
    @Excel(name = "点位名称")
    private String nodeName;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String address;

    /** 商圈类型 */
    @Excel(name = "商圈类型")
    private Long businessDistrictType;

    /** 关联区域ID */
    @Excel(name = "关联区域ID")
    private Long regionId;

    /** 关联合作商ID */
    @Excel(name = "关联合作商ID")
    private Long partnerId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setNodeName(String nodeName) 
    {
        this.nodeName = nodeName;
    }

    public String getNodeName() 
    {
        return nodeName;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setBusinessDistrictType(Long businessDistrictType) 
    {
        this.businessDistrictType = businessDistrictType;
    }

    public Long getBusinessDistrictType() 
    {
        return businessDistrictType;
    }
    public void setRegionId(Long regionId) 
    {
        this.regionId = regionId;
    }

    public Long getRegionId() 
    {
        return regionId;
    }
    public void setPartnerId(Long partnerId) 
    {
        this.partnerId = partnerId;
    }

    public Long getPartnerId() 
    {
        return partnerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("nodeName", getNodeName())
            .append("address", getAddress())
            .append("businessDistrictType", getBusinessDistrictType())
            .append("regionId", getRegionId())
            .append("partnerId", getPartnerId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }
}
