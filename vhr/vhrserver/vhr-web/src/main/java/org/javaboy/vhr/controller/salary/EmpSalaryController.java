package org.javaboy.vhr.controller.salary;

import org.javaboy.vhr.mapper.EmpSalaryMapper;
import org.javaboy.vhr.model.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/salary/table")
public class EmpSalaryController {

    @Autowired
    EmpSalaryMapper empSalaryMapper;

    @GetMapping("/")
    public List<Map<String, Object>> getSalaryTable(@RequestParam(required = false) String name) {
        return empSalaryMapper.getSalaryTable(name);
    }

    @PostMapping("/generate")
    public RespBean generate() {
        // 月末处理：实际项目里会生成月度工资单记录，这里简化为提示成功
        return RespBean.ok("本月工资表已生成！");
    }
}
