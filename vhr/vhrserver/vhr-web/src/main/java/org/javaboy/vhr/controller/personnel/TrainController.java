package org.javaboy.vhr.controller.personnel;

import org.javaboy.vhr.mapper.EmployeeMapper;
import org.javaboy.vhr.mapper.EmployeetrainMapper;
import org.javaboy.vhr.model.Employeetrain;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personnel/train")
public class TrainController {

    @Autowired
    EmployeetrainMapper trainMapper;
    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/")
    public RespPageBean getTrainByPage(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String name) {
        page = (page - 1) * size;
        List<Map<String, Object>> data = trainMapper.getTrainByPage(page, size, name);
        Long total = trainMapper.getTrainTotal(name);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    @PostMapping("/")
    public RespBean addTrain(@RequestBody Employeetrain train) {
        if (trainMapper.insertSelective(train) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PutMapping("/")
    public RespBean updateTrain(@RequestBody Employeetrain train) {
        if (trainMapper.updateByPrimaryKeySelective(train) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteTrain(@PathVariable Integer id) {
        if (trainMapper.deleteByPrimaryKey(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @GetMapping("/emps")
    public List<Map<String, Object>> getAllEmps() {
        return employeeMapper.getAllEmpIdAndName();
    }
}
