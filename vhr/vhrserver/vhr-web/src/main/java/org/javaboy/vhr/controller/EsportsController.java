package org.javaboy.vhr.controller;

import org.javaboy.vhr.model.EsMatch;
import org.javaboy.vhr.model.EsMatchVO;
import org.javaboy.vhr.model.EsPlayer;
import org.javaboy.vhr.model.EsTeam;
import org.javaboy.vhr.model.EsTeamStatVO;
import org.javaboy.vhr.model.EsTeamVO;
import org.javaboy.vhr.model.EsTournament;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.EsportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/esports")
public class EsportsController {

    @Autowired
    EsportsService esportsService;

    @GetMapping("/teams")
    public List<EsTeamVO> teams() {
        return esportsService.getTeams();
    }

    @GetMapping("/players")
    public List<EsPlayer> players() {
        return esportsService.getPlayers();
    }

    @GetMapping("/tournaments")
    public List<String> tournaments() {
        return esportsService.getTournaments();
    }

    /** 赛事下拉选项（含 id），供比赛维护表单使用 */
    @GetMapping("/tournament-options")
    public List<EsTournament> tournamentOptions() {
        return esportsService.getTournamentOptions();
    }

    @GetMapping("/matches")
    public List<EsMatchVO> matches() {
        return esportsService.getMatches();
    }

    /* ------------------------- 玩大手操纵比赛 ------------------------- */

    /** 某赛事的晋级/淘汰标记列表 */
    @GetMapping("/control/progress/{tournamentId}")
    public List<java.util.Map<String, Object>> progress(@PathVariable Integer tournamentId) {
        return esportsService.getProgress(tournamentId);
    }

    /**
     * 操纵比赛判定：标记某战队在该赛事中晋级(advanced)或淘汰(eliminated)。
     * 比赛比分本身通过已有的 PUT /esports/match 操纵。
     */
    @PostMapping("/control/progress")
    public RespBean setProgress(@RequestBody java.util.Map<String, Object> body) {
        Integer tournamentId = body.get("tournamentId") == null ? null : Integer.valueOf(body.get("tournamentId").toString());
        Integer teamId = body.get("teamId") == null ? null : Integer.valueOf(body.get("teamId").toString());
        String result = (String) body.get("result");
        return esportsService.setProgress(tournamentId, teamId, result);
    }

    /** 撤销某战队在该赛事中的晋级/淘汰判定 */
    @DeleteMapping("/control/progress")
    public RespBean revokeProgress(@RequestParam Integer tournamentId, @RequestParam Integer teamId) {
        return esportsService.revokeProgress(tournamentId, teamId);
    }

    @GetMapping("/team-stats")
    public List<EsTeamStatVO> teamStats() {
        return esportsService.getTeamStats();
    }

    @PostMapping("/team")
    public RespBean addTeam(@RequestBody EsTeam team) {
        return esportsService.addTeam(team);
    }

    @PutMapping("/team")
    public RespBean updateTeam(@RequestBody EsTeam team) {
        return esportsService.updateTeam(team);
    }

    @DeleteMapping("/team/{id}")
    public RespBean deleteTeam(@PathVariable Integer id) {
        return esportsService.deleteTeam(id);
    }

    @PostMapping("/player")
    public RespBean addPlayer(@RequestBody EsPlayer player) {
        return esportsService.addPlayer(player);
    }

    @PutMapping("/player")
    public RespBean updatePlayer(@RequestBody EsPlayer player) {
        return esportsService.updatePlayer(player);
    }

    @DeleteMapping("/player/{id}")
    public RespBean deletePlayer(@PathVariable Integer id) {
        return esportsService.deletePlayer(id);
    }

    @PostMapping("/match")
    public RespBean addMatch(@RequestBody EsMatch match) {
        return esportsService.addMatch(match);
    }

    @PutMapping("/match")
    public RespBean updateMatch(@RequestBody EsMatch match) {
        return esportsService.updateMatch(match);
    }

    @DeleteMapping("/match/{id}")
    public RespBean deleteMatch(@PathVariable Integer id) {
        return esportsService.deleteMatch(id);
    }

    @PostMapping("/tournament")
    public RespBean addTournament(@RequestBody EsTournament tournament) {
        return esportsService.addTournament(tournament);
    }

    @PutMapping("/tournament")
    public RespBean updateTournament(@RequestBody EsTournament tournament) {
        return esportsService.updateTournament(tournament);
    }

    @DeleteMapping("/tournament/{id}")
    public RespBean deleteTournament(@PathVariable Integer id) {
        return esportsService.deleteTournament(id);
    }
}
