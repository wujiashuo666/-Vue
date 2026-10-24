package org.javaboy.vhr.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 电竞比赛（系列赛，BO 赛制）
 */
public class EsMatch implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer tournamentId;
    private String stage;
    private Integer bo;
    private Integer team1Id;
    private Integer team2Id;
    private String status; // live / upcoming / finished
    private LocalDateTime startTime;
    private List<EsMatchMap> maps;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getTournamentId() { return tournamentId; }
    public void setTournamentId(Integer tournamentId) { this.tournamentId = tournamentId; }
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    public Integer getBo() { return bo; }
    public void setBo(Integer bo) { this.bo = bo; }
    public Integer getTeam1Id() { return team1Id; }
    public void setTeam1Id(Integer team1Id) { this.team1Id = team1Id; }
    public Integer getTeam2Id() { return team2Id; }
    public void setTeam2Id(Integer team2Id) { this.team2Id = team2Id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public List<EsMatchMap> getMaps() { return maps; }
    public void setMaps(List<EsMatchMap> maps) { this.maps = maps; }
}
