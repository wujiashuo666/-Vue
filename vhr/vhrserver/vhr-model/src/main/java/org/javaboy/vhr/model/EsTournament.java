package org.javaboy.vhr.model;

import java.io.Serializable;

/**
 * 电竞赛事（锦标赛）。
 * startDate / endDate 为 yyyy-MM-dd 字符串（SQL 中 date_format 得到），
 * 与 EsMatchVO 的 startTime 处理方式保持一致；matchCount 仅在列表查询时统计。
 */
public class EsTournament implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String region;
    private String startDate;
    private String endDate;
    /** 该赛事下的比赛场次，仅 selectTournaments 统计查询时填充 */
    private Integer matchCount;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public Integer getMatchCount() { return matchCount; }
    public void setMatchCount(Integer matchCount) { this.matchCount = matchCount; }
}
