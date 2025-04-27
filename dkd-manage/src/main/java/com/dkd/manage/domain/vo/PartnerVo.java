package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Partner;
import lombok.Data;

@Data
public class PartnerVo extends Partner {
    //点位数
    private int nodeCount;
}
