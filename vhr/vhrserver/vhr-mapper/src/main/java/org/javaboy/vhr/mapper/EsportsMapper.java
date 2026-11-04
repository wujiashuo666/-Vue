package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.EsMatch;
import org.javaboy.vhr.model.EsMatchMap;
import org.javaboy.vhr.model.EsMatchVO;
import org.javaboy.vhr.model.EsPlayer;
import org.javaboy.vhr.model.EsTeam;
import org.javaboy.vhr.model.EsTournament;

import java.util.List;
import java.util.Map;

public interface EsportsMapper {
    List<EsTeam> selectTeams();

    List<EsPlayer> selectPlayers();

    List<EsTournament> selectTournaments();

    /** 比赛列表（含赛事名与逐图比分），start_time 已格式化为 ISO 字符串 */
    List<EsMatchVO> selectMatches();

    /* ------------------- 赛事晋级/淘汰标记（玩大手操纵比赛） ------------------- */

    /** 清除某赛事某战队的旧标记，重新判定时覆盖写入 */
    int deleteProgress(@Param("tournamentId") Integer tournamentId, @Param("teamId") Integer teamId);

    int insertProgress(@Param("tournamentId") Integer tournamentId,
                       @Param("teamId") Integer teamId,
                       @Param("result") String result);

    /** 某赛事全部晋级/淘汰标记（带战队名称、图标等展示信息） */
    List<Map<String, Object>> selectProgress(@Param("tournamentId") Integer tournamentId);

    /** 已结束且带逐图比分的比赛，供战绩统计推导 */
    List<EsMatch> selectFinishedWithMaps();

    int addTeam(EsTeam team);

    int updateTeam(EsTeam team);

    int deleteTeam(Integer id);

    /** 战队被多少场比赛引用（team1 或 team2），删除前做完整性检查 */
    int countMatchesByTeamId(@Param("teamId") Integer teamId);

    int addMatch(EsMatch match);

    int updateMatch(EsMatch match);

    int deleteMatch(Integer id);

    int deleteMapsByMatchId(@Param("matchId") Integer matchId);

    int addMatchMap(EsMatchMap map);

    /* ------------------------- 赛事维护 ------------------------- */

    /** 按名称统计同名赛事数，excludeId 用于更新时排除自身 */
    int countTournamentByName(@Param("name") String name, @Param("excludeId") Integer excludeId);

    /** 该赛事下的比赛场次，删除前做引用检查 */
    int countMatchesByTournamentId(@Param("tournamentId") Integer tournamentId);

    int addTournament(EsTournament tournament);

    int updateTournament(EsTournament tournament);

    int deleteTournament(Integer id);

    /* ------------------------- 选手维护 ------------------------- */

    /** 校验选手所属战队是否存在 */
    int countTeamById(@Param("id") Integer id);

    /** 重复游戏 ID 的数量（游戏 ID 全局唯一），excludeId 用于更新时排除自身 */
    int countPlayerByNick(@Param("nick") String nick, @Param("excludeId") Integer excludeId);

    int addPlayer(EsPlayer player);

    int updatePlayer(EsPlayer player);

    int deletePlayer(Integer id);
}
