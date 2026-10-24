package org.javaboy.vhr.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 电竞选手
 */
public class EsPlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer teamId;
    private String nick;
    private String name;
    private String country;
    private BigDecimal rating;
    private BigDecimal kd;
    private BigDecimal adr;
    private BigDecimal kast;
    private Integer maps;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }
    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public BigDecimal getKd() { return kd; }
    public void setKd(BigDecimal kd) { this.kd = kd; }
    public BigDecimal getAdr() { return adr; }
    public void setAdr(BigDecimal adr) { this.adr = adr; }
    public BigDecimal getKast() { return kast; }
    public void setKast(BigDecimal kast) { this.kast = kast; }
    public Integer getMaps() { return maps; }
    public void setMaps(Integer maps) { this.maps = maps; }
}
