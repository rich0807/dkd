package com.dkd.manage.controller;

import com.dkd.common.annotation.Log;
import com.dkd.common.constant.DkdContants;
import com.dkd.common.core.controller.BaseController;
import com.dkd.common.core.domain.AjaxResult;
import com.dkd.common.core.page.TableDataInfo;
import com.dkd.common.enums.BusinessType;
import com.dkd.common.utils.poi.ExcelUtil;
import com.dkd.manage.domain.Emp;
import com.dkd.manage.domain.VendingMachine;
import com.dkd.manage.service.IEmpService;
import com.dkd.manage.service.IVendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 人员列表Controller
 *
 * @author itheima
 * @date 2024-06-15
 */
@RestController
@RequestMapping("/manage/emp")
public class EmpController extends BaseController {
    @Autowired
    private IEmpService empService;

    @Autowired
    private IVendingMachineService vendingMachineService;

    /**
     * 查询人员列表列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:list')")
    @GetMapping("/list")
    public TableDataInfo list(Emp emp) {
        startPage();
        List<Emp> list = empService.selectEmpList(emp);
        return getDataTable(list);
    }

    /**
     * 导出人员列表列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:export')")
    @Log(title = "人员列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Emp emp) {
        List<Emp> list = empService.selectEmpList(emp);
        ExcelUtil<Emp> util = new ExcelUtil<Emp>(Emp.class);
        util.exportExcel(response, list, "人员列表数据");
    }

    /**
     * 获取人员列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(empService.selectEmpById(id));
    }

    /**
     * 新增人员列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:add')")
    @Log(title = "人员列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Emp emp) {
        return toAjax(empService.insertEmp(emp));
    }

    /**
     * 修改人员列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:edit')")
    @Log(title = "人员列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Emp emp) {
        return toAjax(empService.updateEmp(emp));
    }

    /**
     * 删除人员列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:remove')")
    @Log(title = "人员列表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(empService.deleteEmpByIds(ids));
    }

    /**
     * 根据售货机获取运营人员列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:list')")
    @GetMapping("/businessList/{innerCode}")
    public AjaxResult businessList(@PathVariable("innerCode") String innerCode) {
        //1. 根据innerCode查询售货机信息
        VendingMachine vendingMachine = vendingMachineService.selectVendingMachineByInnerCode(innerCode);
        if (vendingMachine == null) {
            return error("售货机不存在");
        }
        //2. 根据区域id、角色编号、员工状态查询运营人员列表
        Emp emp = new Emp();
        emp.setRegionId(vendingMachine.getRegionId());// 设备所属区域id
        emp.setRoleCode(DkdContants.ROLE_CODE_BUSINESS);// 角色编码：运营员
        emp.setStatus(DkdContants.EMP_STATUS_NORMAL);// 员工启用
        return success(empService.selectEmpList(emp));
    }

    /**
     * 根据售货机编码获取运维人员列表
     */
    @PreAuthorize("@ss.hasPermi('manage:emp:list')")
    @GetMapping("/operationList/{innerCode}")
    public AjaxResult operationList(@PathVariable("innerCode") String innerCode) {
        //1. 查询售货机信息
        VendingMachine vendingMachine = vendingMachineService.selectVendingMachineByInnerCode(innerCode);
        if(vendingMachine==null){
            return error("售货机不存在");
        }
        //2. 根据区域id、角色编号、员工状态查询运维人员列表
        Emp emp = new Emp();
        emp.setRegionId(vendingMachine.getRegionId());// 设备所属区域id
        emp.setRoleCode(DkdContants.ROLE_CODE_OPERATOR);// 角色编码：运维员
        emp.setStatus(DkdContants.EMP_STATUS_NORMAL);// 员工启用
        return success(empService.selectEmpList(emp));
    }
}

