package org.javaboy.vhr.model;

import java.io.Serializable;

/**
 * 比赛逐图比分。name 直接对应前端 maps[].name，避免二次映射。
 */
public class EsMatchMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer matchId;
    private Integer mapOrder;
    private String name;
    private Integer score1;
    private Integer score2;
    private Boolean live;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }
    public Integer getMapOrder() { return mapOrder; }
    public void setMapOrder(Integer mapOrder) { this.mapOrder = mapOrder; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getScore1() { return score1; }
    public void setScore1(Integer score1) { this.score1 = score1; }
    public Integer getScore2() { return score2; }
    public void setScore2(Integer score2) { this.score2 = score2; }
    public Boolean getLive() { return live; }
    public void setLive(Boolean live) { this.live = live; }
}
