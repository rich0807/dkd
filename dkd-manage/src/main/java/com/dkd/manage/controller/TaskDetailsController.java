package com.dkd.manage.controller;

import com.dkd.common.annotation.Log;
import com.dkd.common.core.controller.BaseController;
import com.dkd.common.core.domain.AjaxResult;
import com.dkd.common.core.domain.R;
import com.dkd.common.core.page.TableDataInfo;
import com.dkd.common.enums.BusinessType;
import com.dkd.common.utils.poi.ExcelUtil;
import com.dkd.manage.domain.TaskDetails;
import com.dkd.manage.service.ITaskDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工单详情Controller
 *
 * @author RQ
 * @date 2024-07-24
 */
@RestController
@RequestMapping("/manage/taskDetails")
@Api(tags = "工单详情管理")
public class TaskDetailsController extends BaseController {
    @Autowired
    private ITaskDetailsService taskDetailsService;

    /**
     * 查询工单详情列表
     */
    @PreAuthorize("@ss.hasPermi('manage:taskDeatils:list')")
    @ApiOperation(value = "查询工单详情列表", notes = "返回工单详情列表数据")
    @GetMapping("/list")
    public TableDataInfo list(
            @ApiParam(value = "查询条件", required = false) TaskDetails taskDetails) {
        startPage();
        List<TaskDetails> list = taskDetailsService.selectTaskDetailsList(taskDetails);
        return getDataTable(list);
    }

    /**
     * 导出工单详情列表
     */
    @PreAuthorize("@ss.hasPermi('manage:taskDeatils:export')")
    @Log(title = "工单详情", businessType = BusinessType.EXPORT)
    @ApiOperation(value = "导出工单详情列表", notes = "导出工单详情列表为Excel文件")
    @PostMapping("/export")
    public void export(
            HttpServletResponse response,
            @ApiParam(value = "查询条件", required = false) TaskDetails taskDetails) {
        List<TaskDetails> list = taskDetailsService.selectTaskDetailsList(taskDetails);
        ExcelUtil<TaskDetails> util = new ExcelUtil<>(TaskDetails.class);
        util.exportExcel(response, list, "工单详情数据");
    }

    /**
     * 获取工单详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:taskDeatils:query')")
    @ApiOperation(value = "获取工单详情详细信息", notes = "根据ID获取工单详情详细信息")
    @GetMapping(value = "/{detailsId}")
    public AjaxResult getInfo(
            @ApiParam(value = "工单详情ID", required = true) @PathVariable("detailsId") Long detailsId) {
        return success(taskDetailsService.selectTaskDetailsByDetailsId(detailsId));
    }

    /**
     * 新增工单详情
     */
    @PreAuthorize("@ss.hasPermi('manage:taskDeatils:add')")
    @Log(title = "工单详情", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增工单详情", notes = "根据JSON字符串新增工单详情")
    @PostMapping
    public AjaxResult add(
            @ApiParam(value = "工单详情对象", required = true) @RequestBody TaskDetails taskDetails) {
        return toAjax(taskDetailsService.insertTaskDetails(taskDetails));
    }

    /**
     * 修改工单详情
     */
    @PreAuthorize("@ss.hasPermi('manage:taskDeatils:edit')")
    @Log(title = "工单详情", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改工单详情", notes = "根据JSON字符串修改工单详情")
    @PutMapping
    public AjaxResult edit(
            @ApiParam(value = "工单详情对象", required = true) @RequestBody TaskDetails taskDetails) {
        return toAjax(taskDetailsService.updateTaskDetails(taskDetails));
    }

    /**
     * 删除工单详情
     */
    @PreAuthorize("@ss.hasPermi('manage:taskDeatils:remove')")
    @Log(title = "工单详情", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除工单详情", notes = "根据ID批量删除工单详情")
    @DeleteMapping("/{detailsIds}")
    public AjaxResult remove(
            @ApiParam(value = "工单详情ID数组", required = true) @PathVariable Long[] detailsIds) {
        return toAjax(taskDetailsService.deleteTaskDetailsByDetailsIds(detailsIds));
    }

    /**
     * 查看工单补货详情
     */
    @PreAuthorize("@ss.hasPermi('manage:taskDetails:list')")
    @ApiOperation(value = "查看工单补货详情", notes = "根据工单ID获取工单补货详情")
    @GetMapping( "/byTaskId/{taskId}")
    public R<List<TaskDetails>> byTaskId(
            @ApiParam(value = "工单ID", required = true) @PathVariable Long taskId) {
        TaskDetails taskDetails = new TaskDetails();
        taskDetails.setTaskId(taskId);
        return R.ok(taskDetailsService.selectTaskDetailsList(taskDetails));
    }
}
