package org.javaboy.vhr.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/log")
public class OpLogController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public Map<String, Object> getLogs(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size) {
        int offset = (page - 1) * size;
        List<Map<String, Object>> data = jdbcTemplate.queryForList(
            "SELECT o.id, o.addDate, o.operate, h.name AS hrName " +
            "FROM oplog o LEFT JOIN hr h ON o.hrid = h.id " +
            "ORDER BY o.id DESC LIMIT ?, ?", offset, size);
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM oplog", Long.class);
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        map.put("total", total);
        return map;
    }
}
