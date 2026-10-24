package org.javaboy.vhr.controller.personnel;

import org.javaboy.vhr.mapper.EmployeeMapper;
import org.javaboy.vhr.mapper.EmployeeremoveMapper;
import org.javaboy.vhr.model.Department;
import org.javaboy.vhr.model.Employeeremove;
import org.javaboy.vhr.model.JobLevel;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.DepartmentService;
import org.javaboy.vhr.service.JobLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personnel/remove")
public class RemoveController {

    @Autowired
    EmployeeremoveMapper mvMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    JobLevelService jobLevelService;

    @GetMapping("/")
    public RespPageBean getMvByPage(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String name) {
        int offset = (page - 1) * size;
        RespPageBean bean = new RespPageBean();
        bean.setData(mvMapper.getMvByPage(offset, size, name));
        bean.setTotal(mvMapper.getMvTotal(name));
        return bean;
    }

    @PostMapping("/")
    public RespBean addMv(@RequestBody Employeeremove mv) {
        return mvMapper.insertSelective(mv) == 1 ? RespBean.ok("添加成功!") : RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateMv(@RequestBody Employeeremove mv) {
        return mvMapper.updateByPrimaryKeySelective(mv) == 1 ? RespBean.ok("更新成功!") : RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteMv(@PathVariable Integer id) {
        return mvMapper.deleteByPrimaryKey(id) == 1 ? RespBean.ok("删除成功!") : RespBean.error("删除失败!");
    }

    @GetMapping("/emps")
    public List<Map<String, Object>> getAllEmps() {
        return employeeMapper.getAllEmpIdAndName();
    }

    @GetMapping("/deps")
    public List<Department> getAllDeps() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/joblevels")
    public List<JobLevel> getAllJobLevels() {
        return jobLevelService.getAllJobLevels();
    }
}
