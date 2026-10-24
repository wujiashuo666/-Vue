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
}
