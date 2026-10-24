package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EsportsMapper;
import org.javaboy.vhr.model.EsMatch;
import org.javaboy.vhr.model.EsMatchMap;
import org.javaboy.vhr.model.EsMatchVO;
import org.javaboy.vhr.model.EsPlayer;
import org.javaboy.vhr.model.EsTeam;
import org.javaboy.vhr.model.EsTeamStatVO;
import org.javaboy.vhr.model.EsTeamVO;
import org.javaboy.vhr.model.EsTournament;
import org.javaboy.vhr.model.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EsportsService {

    /** 比赛状态，与前端 MATCH_STATUS 保持一致 */
    private static final List<String> MATCH_STATUS = Arrays.asList("live", "upcoming", "finished");

    /** BO 赛制只允许 1/3/5 局 */
    private static final List<Integer> BO_OPTIONS = Arrays.asList(1, 3, 5);

    @Autowired
    EsportsMapper esportsMapper;

    public List<EsTeamVO> getTeams() {
        return esportsMapper.selectTeams().stream().map(t -> {
            EsTeamVO vo = new EsTeamVO();
            vo.setId(t.getId());
            vo.setName(t.getName());
            vo.setShortName(t.getShortName());
            vo.setRegion(t.getRegion());
            vo.setWorldRank(t.getWorldRank());
            vo.setColor(new String[]{t.getColor1(), t.getColor2()});
            return vo;
        }).collect(Collectors.toList());
    }

    public List<EsPlayer> getPlayers() {
        return esportsMapper.selectPlayers();
    }

    public List<String> getTournaments() {
        return esportsMapper.selectTournaments().stream()
                .map(EsTournament::getName)
                .collect(Collectors.toList());
    }

    /** 赛事下拉选项（含 id），供比赛维护表单使用 */
    public List<EsTournament> getTournamentOptions() {
        return esportsMapper.selectTournaments();
    }

    public List<EsMatchVO> getMatches() {
        return esportsMapper.selectMatches();
    }

    /**
     * 战队战绩：依据已结束比赛的逐图结果实时推导，避免存储冗余且易矛盾的汇总数据。
     */
    public List<EsTeamStatVO> getTeamStats() {
        List<EsTeam> teams = esportsMapper.selectTeams();
        List<EsMatch> finished = esportsMapper.selectFinishedWithMaps();

        Map<Integer, EsTeam> teamMap = teams.stream()
                .collect(Collectors.toMap(EsTeam::getId, t -> t));
        Map<Integer, StatHolder> stats = new HashMap<>();
        for (EsTeam t : teams) {
            stats.put(t.getId(), new StatHolder());
        }

        for (EsMatch m : finished) {
            int s1 = 0, s2 = 0;
            for (EsMatchMap mp : m.getMaps()) {
                if (Boolean.TRUE.equals(mp.getLive())) continue;
                if (mp.getScore1() > mp.getScore2()) s1++;
                else if (mp.getScore2() > mp.getScore1()) s2++;
            }
            boolean win1 = s1 > s2;
            StatHolder a = stats.get(m.getTeam1Id());
            StatHolder b = stats.get(m.getTeam2Id());
            if (a == null || b == null) continue;

            if (win1) { a.wins++; b.losses++; } else { b.wins++; a.losses++; }

            for (EsMatchMap mp : m.getMaps()) {
                a.mapsWon += mp.getScore1();
                a.mapsLost += mp.getScore2();
                b.mapsWon += mp.getScore2();
                b.mapsLost += mp.getScore1();
            }

            String score = s1 + "-" + s2;
            String opp1 = teamName(teamMap, m.getTeam2Id());
            String opp2 = teamName(teamMap, m.getTeam1Id());
            a.recent.add(new EsTeamStatVO.RecentItem(opp1, win1 ? "W" : "L", score));
            b.recent.add(new EsTeamStatVO.RecentItem(opp2, win1 ? "L" : "W", score));
        }

        List<EsTeamStatVO> result = new ArrayList<>();
        for (EsTeam t : teams) {
            StatHolder h = stats.get(t.getId());
            EsTeamStatVO vo = new EsTeamStatVO();
            vo.setId(t.getId());
            vo.setName(t.getName());
            vo.setShortName(t.getShortName());
            vo.setRegion(t.getRegion());
            vo.setWorldRank(t.getWorldRank());
            vo.setColor(new String[]{t.getColor1(), t.getColor2()});
            int total = h.wins + h.losses;
            vo.setWins(h.wins);
            vo.setLosses(h.losses);
            vo.setWinRate(total > 0 ? Math.round(h.wins * 100.0f / total) : 0);
            vo.setMapDiff(h.mapsWon - h.mapsLost);
            // 仅保留最近 5 场
            int from = Math.max(0, h.recent.size() - 5);
            vo.setRecent(h.recent.subList(from, h.recent.size()));
            result.add(vo);
        }

        result.sort(Comparator
                .comparing(EsTeamStatVO::getWinRate, Comparator.reverseOrder())
                .thenComparing(EsTeamStatVO::getMapDiff, Comparator.reverseOrder()));
        return result;
    }

    private String teamName(Map<Integer, EsTeam> teamMap, Integer id) {
        EsTeam t = teamMap.get(id);
        return t == null ? "未知战队" : t.getName();
    }

    private static class StatHolder {
        int wins = 0;
        int losses = 0;
        int mapsWon = 0;
        int mapsLost = 0;
        List<EsTeamStatVO.RecentItem> recent = new ArrayList<>();
    }

    public RespBean addTeam(EsTeam team) {
        if (esportsMapper.addTeam(team) == 1) {
            return RespBean.ok("添加成功", team);
        }
        return RespBean.error("添加失败");
    }

    public RespBean updateTeam(EsTeam team) {
        if (esportsMapper.updateTeam(team) == 1) {
            return RespBean.ok("更新成功");
        }
        return RespBean.error("更新失败，记录不存在");
    }

    public RespBean deleteTeam(Integer id) {
        try {
            if (esportsMapper.deleteTeam(id) == 1) {
                return RespBean.ok("删除成功");
            }
            return RespBean.error("删除失败，记录不存在");
        } catch (Exception e) {
            //战队下仍有选手时外键约束会抛异常
            return RespBean.error("该战队下仍有选手，请先移除或转移选手");
        }
    }

    /* ------------------------- 比赛维护 ------------------------- */

    @Transactional(rollbackFor = Exception.class)
    public RespBean addMatch(EsMatch match) {
        String error = validateMatch(match);
        if (error != null) {
            return RespBean.error(error);
        }
        if (esportsMapper.addMatch(match) != 1) {
            return RespBean.error("添加失败");
        }
        saveMaps(match);
        return RespBean.ok("添加成功", match.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public RespBean updateMatch(EsMatch match) {
        if (match.getId() == null) {
            return RespBean.error("缺少比赛 ID");
        }
        String error = validateMatch(match);
        if (error != null) {
            return RespBean.error(error);
        }
        if (esportsMapper.updateMatch(match) != 1) {
            return RespBean.error("更新失败，记录不存在");
        }
        //逐图比分整体替换：先清空再按提交顺序重建
        esportsMapper.deleteMapsByMatchId(match.getId());
        saveMaps(match);
        return RespBean.ok("更新成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public RespBean deleteMatch(Integer id) {
        //es_match_map 没有外键级联，先删逐图再删比赛
        esportsMapper.deleteMapsByMatchId(id);
        if (esportsMapper.deleteMatch(id) == 1) {
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败，记录不存在");
    }

    /**
     * 校验比赛主体字段，返回 null 表示通过，否则返回给前端的错误提示。
     */
    private String validateMatch(EsMatch match) {
        if (match.getTournamentId() == null) {
            return "请选择赛事";
        }
        if (match.getTeam1Id() == null || match.getTeam2Id() == null) {
            return "请选择对阵双方战队";
        }
        if (match.getTeam1Id().equals(match.getTeam2Id())) {
            return "对阵双方不能是同一支战队";
        }
        if (match.getBo() == null || !BO_OPTIONS.contains(match.getBo())) {
            return "BO 赛制只能是 1 / 3 / 5";
        }
        if (match.getStatus() == null || !MATCH_STATUS.contains(match.getStatus())) {
            return "比赛状态不合法";
        }
        if (match.getStartTime() == null) {
            return "请选择开始时间";
        }
        List<EsMatchMap> maps = match.getMaps();
        if (maps != null) {
            if (maps.size() > match.getBo()) {
                return "逐图比分数量不能超过 BO" + match.getBo();
            }
            long liveCount = maps.stream().filter(mp -> Boolean.TRUE.equals(mp.getLive())).count();
            if (liveCount > 1) {
                return "只能有一张地图处于进行中";
            }
            if (liveCount > 0 && !"live".equals(match.getStatus())) {
                return "只有进行中的比赛才能标记进行中的地图";
            }
            for (EsMatchMap mp : maps) {
                if (mp.getName() == null || mp.getName().trim().isEmpty()) {
                    return "请选择每张地图的名称";
                }
                if (mp.getScore1() == null || mp.getScore2() == null
                        || mp.getScore1() < 0 || mp.getScore2() < 0) {
                    return "地图比分必须是不小于 0 的整数";
                }
            }
        }
        return null;
    }

    /** 按提交顺序重排 map_order 后写入，忽略前端传入的 matchId / mapOrder */
    private void saveMaps(EsMatch match) {
        List<EsMatchMap> maps = match.getMaps();
        if (maps == null || maps.isEmpty()) {
            return;
        }
        for (int i = 0; i < maps.size(); i++) {
            EsMatchMap mp = maps.get(i);
            mp.setMatchId(match.getId());
            mp.setMapOrder(i + 1);
            mp.setLive(Boolean.TRUE.equals(mp.getLive()));
            esportsMapper.addMatchMap(mp);
        }
    }
}
