package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Node;
import com.dkd.manage.domain.Partner;
import com.dkd.manage.domain.Region;
import lombok.Data;

@Data
public class NodeVo extends Node {

    // 设备数量
    private int vmCount;

    // 区域
    private Region region;

    // 合作商
    private Partner partner;
}