package com.dkd.manage.service.impl;

import com.dkd.common.exception.ServiceException;
import com.dkd.common.utils.DateUtils;
import com.dkd.manage.domain.Sku;
import com.dkd.manage.mapper.SkuMapper;
import com.dkd.manage.service.IChannelService;
import com.dkd.manage.service.ISkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品管理Service业务层处理
 *
 * @author RQ
 * @date 2024-07-15
 */
@Service
public class SkuServiceImpl implements ISkuService
{
    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private IChannelService channelService;

    /**
     * 查询商品管理
     *
     * @param skuId 商品管理主键
     * @return 商品管理
     */
    @Override
    public Sku selectSkuBySkuId(Long skuId)
    {
        return skuMapper.selectSkuBySkuId(skuId);
    }

    /**
     * 查询商品管理列表
     *
     * @param sku 商品管理
     * @return 商品管理
     */
    @Override
    public List<Sku> selectSkuList(Sku sku)
    {
        return skuMapper.selectSkuList(sku);
    }

    /**
     * 新增商品管理
     *
     * @param sku 商品管理
     * @return 结果
     */
    @Override
    public int insertSku(Sku sku)
    {
        sku.setCreateTime(DateUtils.getNowDate());
        return skuMapper.insertSku(sku);
    }

    /**
     * 修改商品管理
     *
     * @param sku 商品管理
     * @return 结果
     */
    @Override
    public int updateSku(Sku sku)
    {
        sku.setUpdateTime(DateUtils.getNowDate());
        return skuMapper.updateSku(sku);
    }

/**
 * 批量删除商品管理
 *
 * @param skuIds 需要删除的商品管理主键
 * @return 结果
 */
@Override
public int deleteSkuBySkuIds(Long[] skuIds)
{   //1. 判断商品的id集合是否有关联货道
    int count = channelService.countChannelBySkuIds(skuIds);
    if(count>0){
        throw new ServiceException("此商品被货道关联，无法删除");
    }
    //2. 没有关联货道才能删除
    return skuMapper.deleteSkuBySkuIds(skuIds);
}

    /**
     * 删除商品管理信息
     *
     * @param skuId 商品管理主键
     * @return 结果
     */
    @Override
    public int deleteSkuBySkuId(Long skuId)
    {
        return skuMapper.deleteSkuBySkuId(skuId);
    }

/**
 * 批量新增商品管理
 * @param skuList
 * @return 结果
 */
@Override
public int insertSkus(List<Sku> skuList) {
    return skuMapper.insertSkus(skuList);
}
}
