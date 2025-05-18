package com.dkd.manage.service.impl;

import com.dkd.manage.domain.TaskDetails;
import com.dkd.manage.mapper.TaskDetailsMapper;
import com.dkd.manage.service.ITaskDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工单详情Service业务层处理
 *
 * @author RQ
 * @date 2024-07-24
 */
@Service
public class TaskDetailsServiceImpl implements ITaskDetailsService
{
    @Autowired
    private TaskDetailsMapper taskDetailsMapper;

    /**
     * 查询工单详情
     *
     * @param detailsId 工单详情主键
     * @return 工单详情
     */
    @Override
    public TaskDetails selectTaskDetailsByDetailsId(Long detailsId)
    {
        return taskDetailsMapper.selectTaskDetailsByDetailsId(detailsId);
    }

    /**
     * 查询工单详情列表
     *
     * @param taskDetails 工单详情
     * @return 工单详情
     */
    @Override
    public List<TaskDetails> selectTaskDetailsList(TaskDetails taskDetails)
    {
        return taskDetailsMapper.selectTaskDetailsList(taskDetails);
    }

    /**
     * 新增工单详情
     *
     * @param taskDetails 工单详情
     * @return 结果
     */
    @Override
    public int insertTaskDetails(TaskDetails taskDetails)
    {
        return taskDetailsMapper.insertTaskDetails(taskDetails);
    }

    /**
     * 修改工单详情
     *
     * @param taskDetails 工单详情
     * @return 结果
     */
    @Override
    public int updateTaskDetails(TaskDetails taskDetails)
    {
        return taskDetailsMapper.updateTaskDetails(taskDetails);
    }

    /**
     * 批量删除工单详情
     *
     * @param detailsIds 需要删除的工单详情主键
     * @return 结果
     */
    @Override
    public int deleteTaskDetailsByDetailsIds(Long[] detailsIds)
    {
        return taskDetailsMapper.deleteTaskDetailsByDetailsIds(detailsIds);
    }

    /**
     * 删除工单详情信息
     *
     * @param detailsId 工单详情主键
     * @return 结果
     */
    @Override
    public int deleteTaskDetailsByDetailsId(Long detailsId)
    {
        return taskDetailsMapper.deleteTaskDetailsByDetailsId(detailsId);
    }

    /**
     * 批量新增工单详情
     * @param taskDetailsList
     * @return 结果
     */
    @Override
    public int batchInsertTaskDetails(List<TaskDetails> taskDetailsList) {
        return taskDetailsMapper.batchInsertTaskDetails(taskDetailsList);
    }
}
