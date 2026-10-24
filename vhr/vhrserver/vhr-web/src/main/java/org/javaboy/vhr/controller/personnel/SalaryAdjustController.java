package org.javaboy.vhr.controller.personnel;

import org.javaboy.vhr.mapper.AdjustSalaryMapper;
import org.javaboy.vhr.mapper.EmployeeMapper;
import org.javaboy.vhr.model.AdjustSalary;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personnel/salary")
public class SalaryAdjustController {

    @Autowired
    AdjustSalaryMapper asMapper;
    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/")
    public RespPageBean getAsByPage(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String name) {
        int offset = (page - 1) * size;
        RespPageBean bean = new RespPageBean();
        bean.setData(asMapper.getAsByPage(offset, size, name));
        bean.setTotal(asMapper.getAsTotal(name));
        return bean;
    }

    @PostMapping("/")
    public RespBean addAs(@RequestBody AdjustSalary as) {
        return asMapper.insertSelective(as) == 1 ? RespBean.ok("添加成功!") : RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateAs(@RequestBody AdjustSalary as) {
        return asMapper.updateByPrimaryKeySelective(as) == 1 ? RespBean.ok("更新成功!") : RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteAs(@PathVariable Integer id) {
        return asMapper.deleteByPrimaryKey(id) == 1 ? RespBean.ok("删除成功!") : RespBean.error("删除失败!");
    }

    @GetMapping("/emps")
    public List<Map<String, Object>> getAllEmps() {
        return employeeMapper.getAllEmpIdAndName();
    }
}
