package com.dkd.manage.domain.vo;

import com.dkd.manage.domain.Channel;
import com.dkd.manage.domain.Sku;
import lombok.Data;

@Data
public class ChannelVo extends Channel {

    // 商品对象
    private Sku sku;
}
