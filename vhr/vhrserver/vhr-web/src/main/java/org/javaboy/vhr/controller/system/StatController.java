package org.javaboy.vhr.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/stat")
public class StatController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /** 综合信息：员工总数、部门数、本月新增等 */
    @GetMapping("/overview")
    public Map<String, Object> overview() {
        Map<String, Object> map = new HashMap<>();
        map.put("empCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee", Integer.class));
        map.put("depCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM department", Integer.class));
        map.put("trainCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employeetrain", Integer.class));
        map.put("ecCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employeeec", Integer.class));
        map.put("asCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM adjustsalary", Integer.class));
        map.put("mvCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employeeremove", Integer.class));
        map.put("maleCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee WHERE gender='男'", Integer.class));
        map.put("femaleCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee WHERE gender='女'", Integer.class));
        return map;
    }

    /** 按部门统计人数 */
    @GetMapping("/dept")
    public List<Map<String, Object>> deptStat() {
        return jdbcTemplate.queryForList(
            "SELECT d.name AS deptName, COUNT(e.id) AS empCount " +
            "FROM department d LEFT JOIN employee e ON d.id = e.departmentId " +
            "GROUP BY d.id ORDER BY empCount DESC");
    }

    /** 员工积分排行（奖惩分汇总） */
    @GetMapping("/score")
    public List<Map<String, Object>> scoreRank() {
        return jdbcTemplate.queryForList(
            "SELECT e.id, e.name, IFNULL(SUM(c.ecPoint),0) AS totalScore " +
            "FROM employee e LEFT JOIN employeeec c ON e.id = c.eid " +
            "GROUP BY e.id ORDER BY totalScore DESC LIMIT 20");
    }
}
