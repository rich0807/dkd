package com.dkd.manage.mapper;

import com.dkd.manage.domain.Task;
import com.dkd.manage.domain.vo.TaskVo;

import java.util.List;

/**
 * 工单Mapper接口
 *
 * @author RQ
 * @date 2024-07-24
 */
public interface TaskMapper
{
    /**
     * 查询工单
     *
     * @param taskId 工单主键
     * @return 工单
     */
    public Task selectTaskByTaskId(Long taskId);

    /**
     * 查询工单列表
     *
     * @param task 工单
     * @return 工单集合
     */
    public List<Task> selectTaskList(Task task);

    /**
     * 新增工单
     *
     * @param task 工单
     * @return 结果
     */
    public int insertTask(Task task);

    /**
     * 修改工单
     *
     * @param task 工单
     * @return 结果
     */
    public int updateTask(Task task);

    /**
     * 删除工单
     *
     * @param taskId 工单主键
     * @return 结果
     */
    public int deleteTaskByTaskId(Long taskId);

    /**
     * 批量删除工单
     *
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskByTaskIds(Long[] taskIds);

    /**
     *  查询工单列表
     * @param task
     * @return TaskVo集合
     */
    List<TaskVo> selectTaskVoList(Task task);
}
