package org.javaboy.vhr.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 战队接口返回对象。color 由 color1/color2 组装，short 用 @JsonProperty 输出为 "short"，
 * 与前端 mock 结构保持一致（Java 关键字 short 不能作为字段名）。
 */
public class EsTeamVO {
    private Integer id;
    private String name;
    private String shortName;
    private String region;
    private Integer worldRank;
    private String[] color;

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
    public String[] getColor() { return color; }
    public void setColor(String[] color) { this.color = color; }
}
