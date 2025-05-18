package com.dkd.manage.service;

import com.dkd.manage.domain.Task;
import com.dkd.manage.domain.dto.TaskDto;
import com.dkd.manage.domain.vo.TaskVo;

import java.util.List;

/**
 * 工单Service接口
 *
 * @author RQ
 * @date 2024-07-24
 */
public interface ITaskService
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
     * 批量删除工单
     *
     * @param taskIds 需要删除的工单主键集合
     * @return 结果
     */
    public int deleteTaskByTaskIds(Long[] taskIds);

    /**
     * 删除工单信息
     *
     * @param taskId 工单主键
     * @return 结果
     */
    public int deleteTaskByTaskId(Long taskId);

    /**
     *  查询工单列表
     * @param task
     * @return TaskVo集合
     */
    List<TaskVo> selectTaskVoList(Task task);

    /**
     * 新增运营、运维工单
     * @param taskDto
     * @return 结果
     */
    int insertTaskDto(TaskDto taskDto);

    /**
     * 取消工单
     * @param task
     * @return 结果
     */
    int cancelTask(Task task);
}
