package org.javaboy.vhr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 战队战绩统计（后端依据已结束比赛实时推导）。
 */
public class EsTeamStatVO {
    private Integer id;
    private String name;
    private String shortName;
    private String region;
    private Integer worldRank;
    private Integer wins;
    private Integer losses;
    private Integer winRate;
    private Integer mapDiff;
    private String[] color;
    private List<RecentItem> recent;

    public static class RecentItem {
        private String opponent;
        private String result; // W / L
        private String score;

        public RecentItem() {}
        public RecentItem(String opponent, String result, String score) {
            this.opponent = opponent;
            this.result = result;
            this.score = score;
        }

        public String getOpponent() { return opponent; }
        public void setOpponent(String opponent) { this.opponent = opponent; }
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
        public String getScore() { return score; }
        public void setScore(String score) { this.score = score; }
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    @JsonProperty("short")
    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public Integer getWorldRank() { return worldRank; }
    public void setWorldRank(Integer worldRank) { this.worldRank = worldRank; }
    public Integer getWins() { return wins; }
    public void setWins(Integer wins) { this.wins = wins; }
    public Integer getLosses() { return losses; }
    public void setLosses(Integer losses) { this.losses = losses; }
    public Integer getWinRate() { return winRate; }
    public void setWinRate(Integer winRate) { this.winRate = winRate; }
    public Integer getMapDiff() { return mapDiff; }
    public void setMapDiff(Integer mapDiff) { this.mapDiff = mapDiff; }
    public String[] getColor() { return color; }
    public void setColor(String[] color) { this.color = color; }
    public List<RecentItem> getRecent() { return recent; }
    public void setRecent(List<RecentItem> recent) { this.recent = recent; }
}
