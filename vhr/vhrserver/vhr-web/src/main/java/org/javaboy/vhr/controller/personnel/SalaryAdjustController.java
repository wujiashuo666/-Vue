package org.javaboy.vhr.controller.personnel;

import org.javaboy.vhr.mapper.AdjustSalaryMapper;
import org.javaboy.vhr.mapper.EmployeeMapper;
import org.javaboy.vhr.model.AdjustSalary;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personnel/salary")
public class SalaryAdjustController {

    /** 奖惩分换算：每 1 分 = 500 元 */
    private static final int EC_SCORE_YUAN = 500;
    /** 战队胜场换算：每 1 胜 = 200 元 */
    private static final int WIN_YUAN = 200;

    @Autowired
    AdjustSalaryMapper asMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;

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

    /**
     * 战绩调薪建议：按奖惩积分 + 战队比赛战绩计算每位选手的建议调整金额。
     * 选手与电竞战队通过 employee.name = es_player.nick 关联（选手库的姓名即游戏 ID），
     * 战队胜场按已结束比赛的逐图比分汇总推导。前端可在此建议基础上手动调整后落库。
     */
    @GetMapping("/advice")
    public List<Map<String, Object>> salaryAdvice(@RequestParam(required = false) String name) {
        // es_match 没有系列赛比分，按图汇总：图胜多于图负的队伍获得该场比赛胜利
        String winSql =
            "SELECT tm.id AS team_id, SUM(mw.win) AS wins, COUNT(*) AS played " +
            "FROM es_team tm JOIN ( " +
            "  SELECT m.id, m.team1_id AS team_id, " +
            "         CASE WHEN SUM(mm.score1 > mm.score2) > SUM(mm.score2 > mm.score1) THEN 1 ELSE 0 END AS win " +
            "  FROM es_match m JOIN es_match_map mm ON mm.match_id = m.id GROUP BY m.id " +
            "  UNION ALL " +
            "  SELECT m.id, m.team2_id, " +
            "         CASE WHEN SUM(mm.score2 > mm.score1) > SUM(mm.score1 > mm.score2) THEN 1 ELSE 0 END " +
            "  FROM es_match m JOIN es_match_map mm ON mm.match_id = m.id GROUP BY m.id " +
            ") mw ON mw.team_id = tm.id GROUP BY tm.id";

        String sql =
            "SELECT e.id, e.name, d.name AS teamName, s.name AS salaryName, " +
            "       IFNULL(s.basicSalary, 0) AS baseSalary, " +
            "       IFNULL(ec.ecScore, 0) AS ecScore, " +
            "       IFNULL(w.wins, 0) AS wins, IFNULL(w.played, 0) AS played " +
            "FROM employee e " +
            "LEFT JOIN department d ON d.id = e.departmentId " +
            "LEFT JOIN empsalary es ON es.eid = e.id " +
            "LEFT JOIN salary s ON s.id = es.sid " +
            "LEFT JOIN (SELECT eid, SUM(ecPoint) AS ecScore FROM employeeec GROUP BY eid) ec ON ec.eid = e.id " +
            "LEFT JOIN es_player p ON p.nick = e.name " +
            "LEFT JOIN (" + winSql + ") w ON w.team_id = p.team_id " +
            "WHERE (? IS NULL OR e.name LIKE CONCAT('%', ?, '%')) " +
            "ORDER BY e.id";

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, name, name);
        for (Map<String, Object> row : list) {
            int ecScore = ((Number) row.get("ecScore")).intValue();
            int wins = ((Number) row.get("wins")).intValue();
            int base = ((Number) row.get("baseSalary")).intValue();
            int advice = ecScore * EC_SCORE_YUAN + wins * WIN_YUAN;
            row.put("advice", advice);
            row.put("afterSalary", base + advice);
        }
        return list;
    }
}
