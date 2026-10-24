package org.javaboy.vhr.controller.personnel;

import org.javaboy.vhr.mapper.EmployeeMapper;
import org.javaboy.vhr.mapper.EmployeeecMapper;
import org.javaboy.vhr.model.Employeeec;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personnel/ec")
public class EcController {

    @Autowired
    EmployeeecMapper ecMapper;
    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/")
    public RespPageBean getEcByPage(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String name) {
        int offset = (page - 1) * size;
        RespPageBean bean = new RespPageBean();
        bean.setData(ecMapper.getEcByPage(offset, size, name));
        bean.setTotal(ecMapper.getEcTotal(name));
        return bean;
    }

    @PostMapping("/")
    public RespBean addEc(@RequestBody Employeeec ec) {
        return ecMapper.insertSelective(ec) == 1 ? RespBean.ok("添加成功!") : RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateEc(@RequestBody Employeeec ec) {
        return ecMapper.updateByPrimaryKeySelective(ec) == 1 ? RespBean.ok("更新成功!") : RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteEc(@PathVariable Integer id) {
        return ecMapper.deleteByPrimaryKey(id) == 1 ? RespBean.ok("删除成功!") : RespBean.error("删除失败!");
    }

    @GetMapping("/emps")
    public List<Map<String, Object>> getAllEmps() {
        return employeeMapper.getAllEmpIdAndName();
    }
}
