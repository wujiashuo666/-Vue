package org.javaboy.vhr.model;

import java.io.Serializable;

/**
 * 电竞战队
 */
public class EsTeam implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String shortName;
    private String region;
    private Integer worldRank;
    private String color1;
    private String color2;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public Integer getWorldRank() { return worldRank; }
    public void setWorldRank(Integer worldRank) { this.worldRank = worldRank; }
    public String getColor1() { return color1; }
    public void setColor1(String color1) { this.color1 = color1; }
    public String getColor2() { return color2; }
    public void setColor2(String color2) { this.color2 = color2; }
}
