package com.dkd.manage.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dkd.manage.mapper.JobMapper;
import com.dkd.manage.domain.Job;
import com.dkd.manage.service.IJobService;

/**
 * 自动补货任务Service业务层处理
 * 
 * @author RQ
 * @date 2024-07-24
 */
@Service
public class JobServiceImpl implements IJobService 
{
    @Autowired
    private JobMapper jobMapper;

    /**
     * 查询自动补货任务
     * 
     * @param id 自动补货任务主键
     * @return 自动补货任务
     */
    @Override
    public Job selectJobById(Long id)
    {
        return jobMapper.selectJobById(id);
    }

    /**
     * 查询自动补货任务列表
     * 
     * @param job 自动补货任务
     * @return 自动补货任务
     */
    @Override
    public List<Job> selectJobList(Job job)
    {
        return jobMapper.selectJobList(job);
    }

    /**
     * 新增自动补货任务
     * 
     * @param job 自动补货任务
     * @return 结果
     */
    @Override
    public int insertJob(Job job)
    {
        return jobMapper.insertJob(job);
    }

    /**
     * 修改自动补货任务
     * 
     * @param job 自动补货任务
     * @return 结果
     */
    @Override
    public int updateJob(Job job)
    {
        return jobMapper.updateJob(job);
    }

    /**
     * 批量删除自动补货任务
     * 
     * @param ids 需要删除的自动补货任务主键
     * @return 结果
     */
    @Override
    public int deleteJobByIds(Long[] ids)
    {
        return jobMapper.deleteJobByIds(ids);
    }

    /**
     * 删除自动补货任务信息
     * 
     * @param id 自动补货任务主键
     * @return 结果
     */
    @Override
    public int deleteJobById(Long id)
    {
        return jobMapper.deleteJobById(id);
    }
}
