package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.EsMatch;
import org.javaboy.vhr.model.EsMatchMap;
import org.javaboy.vhr.model.EsMatchVO;
import org.javaboy.vhr.model.EsPlayer;
import org.javaboy.vhr.model.EsTeam;
import org.javaboy.vhr.model.EsTournament;

import java.util.List;

public interface EsportsMapper {
    List<EsTeam> selectTeams();

    List<EsPlayer> selectPlayers();

    List<EsTournament> selectTournaments();

    /** 比赛列表（含赛事名与逐图比分），start_time 已格式化为 ISO 字符串 */
    List<EsMatchVO> selectMatches();

    /** 已结束且带逐图比分的比赛，供战绩统计推导 */
    List<EsMatch> selectFinishedWithMaps();

    int addTeam(EsTeam team);

    int updateTeam(EsTeam team);

    int deleteTeam(Integer id);

    int addMatch(EsMatch match);

    int updateMatch(EsMatch match);

    int deleteMatch(Integer id);

    int deleteMapsByMatchId(@Param("matchId") Integer matchId);

    int addMatchMap(EsMatchMap map);
}
