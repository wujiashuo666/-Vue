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

    /** 综合信息：选手总数、战队数、本月新增等（含电竞概览） */
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
        //电竞概览：比赛/进行中/赛事/战队/注册选手
        map.put("matchCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM es_match", Integer.class));
        map.put("liveMatchCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM es_match WHERE status = 'live'", Integer.class));
        map.put("tournamentCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM es_tournament", Integer.class));
        map.put("teamCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM es_team", Integer.class));
        map.put("esPlayerCount", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM es_player", Integer.class));
        return map;
    }

    /** 选手位置（specialty）分布统计 */
    @GetMapping("/position")
    public List<Map<String, Object>> positionStat() {
        return jdbcTemplate.queryForList(
            "SELECT IFNULL(NULLIF(specialty, ''), '未设置') AS specialty, COUNT(*) AS cnt " +
            "FROM employee GROUP BY IFNULL(NULLIF(specialty, ''), '未设置') ORDER BY cnt DESC");
    }

    /** 电竞战况：各赛事比赛场次 + 战队胜场榜 */
    @GetMapping("/esports")
    public Map<String, Object> esportsStat() {
        Map<String, Object> map = new HashMap<>();
        map.put("tournamentStat", jdbcTemplate.queryForList(
            "SELECT t.name AS tournament, COUNT(m.id) AS matchCount, " +
            "SUM(m.status = 'finished') AS finishedCount " +
            "FROM es_tournament t LEFT JOIN es_match m ON m.tournament_id = t.id " +
            "GROUP BY t.id ORDER BY matchCount DESC"));
        // es_match 本身没有比分，比分按图存在 es_match_map 中；
        // 按图汇总：图胜多于图负的队伍获得该场比赛胜利
        map.put("teamWinStat", jdbcTemplate.queryForList(
            "SELECT tm.name AS team, SUM(mw.win) AS wins, COUNT(*) AS played " +
            "FROM es_team tm JOIN (" +
            "  SELECT m.id, m.team1_id AS team_id, " +
            "         CASE WHEN SUM(mm.score1 > mm.score2) > SUM(mm.score2 > mm.score1) THEN 1 ELSE 0 END AS win " +
            "  FROM es_match m JOIN es_match_map mm ON mm.match_id = m.id GROUP BY m.id " +
            "  UNION ALL " +
            "  SELECT m.id, m.team2_id, " +
            "         CASE WHEN SUM(mm.score2 > mm.score1) > SUM(mm.score1 > mm.score2) THEN 1 ELSE 0 END " +
            "  FROM es_match m JOIN es_match_map mm ON mm.match_id = m.id GROUP BY m.id" +
            ") mw ON mw.team_id = tm.id " +
            "GROUP BY tm.id ORDER BY wins DESC, played DESC"));
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
