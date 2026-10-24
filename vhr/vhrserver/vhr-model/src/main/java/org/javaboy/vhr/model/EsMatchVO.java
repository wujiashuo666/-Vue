package org.javaboy.vhr.model;

import java.util.List;

/**
 * 比赛接口返回对象。tournament 为赛事名（join 得到），startTime 序列化为 ISO 字符串，
 * 供前端 new Date(...) 直接解析。
 */
public class EsMatchVO {
    private Integer id;
    private String tournament;
    private String stage;
    private Integer bo;
    private String status;
    private Integer team1Id;
    private Integer team2Id;
    private String startTime;
    private List<EsMatchMap> maps;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTournament() { return tournament; }
    public void setTournament(String tournament) { this.tournament = tournament; }
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    public Integer getBo() { return bo; }
    public void setBo(Integer bo) { this.bo = bo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getTeam1Id() { return team1Id; }
    public void setTeam1Id(Integer team1Id) { this.team1Id = team1Id; }
    public Integer getTeam2Id() { return team2Id; }
    public void setTeam2Id(Integer team2Id) { this.team2Id = team2Id; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public List<EsMatchMap> getMaps() { return maps; }
    public void setMaps(List<EsMatchMap> maps) { this.maps = maps; }
}
