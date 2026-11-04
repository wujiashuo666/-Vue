<template>
  <div class="control-page">
    <!-- 顶部：玩大手标识 + 赛事选择 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <span class="hand-icon">✋</span> 玩大手操纵比赛
        </h1>
        <span class="page-sub">由玩机器亲自下场：选择赛事 → 操纵比分 → 判定晋级或淘汰</span>
      </div>
      <div class="header-right">
        <el-select v-model="currentTournament" placeholder="选择要操纵的赛事" class="tourney-select"
          size="large" @change="onTournamentChange">
          <el-option v-for="t in tournamentOptions" :key="t.id" :label="t.name" :value="t.id" />
        </el-select>
      </div>
    </div>

    <el-empty v-if="!currentTournament" description="先选择一个赛事，才能开始你的操纵大计" />

    <template v-else>
      <!-- 对阵列表 -->
      <div class="section-title">对阵操控（{{ matches.length }} 场）</div>
      <div v-if="matches.length" class="match-grid">
        <div v-for="m in matches" :key="m.id" class="control-card" :class="cardClass(m)">
          <div class="card-top">
            <span class="status-tag" :class="m.status">{{ statusLabel(m.status) }}</span>
            <span class="stage">{{ m.stage }} · BO{{ m.bo }}</span>
          </div>

          <!-- 对阵双方 -->
          <div class="vs-row">
            <div class="vs-team" :class="{ dim: winnerOf(m) === 2 }">
              <div class="team-badge" :style="badgeStyle(teamOf(m.team1Id))">
                <img v-if="teamOf(m.team1Id).logo" :src="teamOf(m.team1Id).logo" alt="" />
                <span v-else>{{ shortOf(m.team1Id) }}</span>
              </div>
              <div class="team-name">{{ teamOf(m.team1Id).name }}</div>
            </div>
            <div class="vs-score">
              <span class="num" :class="{ winner: winnerOf(m) === 1 }">{{ mapScore(m).s1 }}</span>
              <span class="colon">:</span>
              <span class="num" :class="{ winner: winnerOf(m) === 2 }">{{ mapScore(m).s2 }}</span>
            </div>
            <div class="vs-team right" :class="{ dim: winnerOf(m) === 1 }">
              <div class="team-name">{{ teamOf(m.team2Id).name }}</div>
              <div class="team-badge" :style="badgeStyle(teamOf(m.team2Id))">
                <img v-if="teamOf(m.team2Id).logo" :src="teamOf(m.team2Id).logo" alt="" />
                <span v-else>{{ shortOf(m.team2Id) }}</span>
              </div>
            </div>
          </div>

          <!-- 每图比分操纵 -->
          <div class="map-edit">
            <div v-for="(mp, i) in m.editMaps" :key="i" class="map-edit-row">
              <el-select v-model="mp.name" size="small" class="map-select" placeholder="选择地图">
                <el-option v-for="name in mapNames(m.bo)" :key="name" :label="name" :value="name" />
              </el-select>
              <el-input-number v-model="mp.score1" :min="0" :max="30" size="small" controls-position="right" class="score-input" />
              <span class="dash">:</span>
              <el-input-number v-model="mp.score2" :min="0" :max="30" size="small" controls-position="right" class="score-input" />
              <el-button v-if="m.editMaps.length > 1" size="small" text type="danger" @click="m.editMaps.splice(i, 1)">✕</el-button>
            </div>
            <el-button v-if="m.editMaps.length < m.bo" size="small" text type="primary" @click="m.editMaps.push(newMap())">+ 加一张图</el-button>
          </div>

          <!-- 操纵动作 -->
          <div class="action-bar">
            <el-tooltip content="自动填入比分让左侧战队拿下这场比赛" placement="top">
              <button class="quick-btn left" @click="quickWin(m, 1)">⚠ {{ shortOf(m.team1Id) }} 获胜</button>
            </el-tooltip>
            <el-tooltip content="自动填入比分让右侧战队拿下这场比赛" placement="top">
              <button class="quick-btn right" @click="quickWin(m, 2)">⚠ {{ shortOf(m.team2Id) }} 获胜</button>
            </el-tooltip>
            <button class="save-btn" :disabled="savingId === m.id" @click="saveControl(m)">
              {{ savingId === m.id ? '写入中…' : '保存操纵结果' }}
            </button>
          </div>

          <!-- 晋级 / 淘汰判定 -->
          <div v-if="winnerOf(m)" class="verdict-bar">
            <span class="verdict-tip">
              {{ winnerOf(m) === 1 ? teamOf(m.team1Id).name : teamOf(m.team2Id).name }} 已被操纵赢下本场：
            </span>
            <button class="verdict-btn advance" @click="judge(m, winnerOf(m), 'advanced')">标记晋级 ▲</button>
            <button class="verdict-btn eliminate" @click="judge(m, loserOf(m), 'eliminated')">标记败者淘汰 ▼</button>
          </div>
        </div>
      </div>
      <el-empty v-else description="该赛事下还没有比赛，去比赛管理里先安排对阵" />

      <!-- 晋级 / 淘汰榜 -->
      <div class="section-title">晋级 / 淘汰榜</div>
      <div class="progress-board">
        <div class="progress-col advanced">
          <div class="col-head">▲ 晋级</div>
          <div class="col-body">
            <div v-for="p in progressList.filter(x => x.result === 'advanced')" :key="p.teamId" class="progress-item">
              <div class="team-badge sm" :style="badgeStyle(p)">
                <img v-if="p.logo" :src="p.logo" alt="" />
                <span v-else>{{ p.shortName }}</span>
              </div>
              <span class="pi-name">{{ p.teamName }}</span>
              <el-button size="small" text type="danger" @click="revokeJudge(p)">撤销</el-button>
            </div>
            <div v-if="!progressList.some(x => x.result === 'advanced')" class="col-empty">暂无晋级队伍</div>
          </div>
        </div>
        <div class="progress-col eliminated">
          <div class="col-head">▼ 淘汰</div>
          <div class="col-body">
            <div v-for="p in progressList.filter(x => x.result === 'eliminated')" :key="p.teamId" class="progress-item">
              <div class="team-badge sm" :style="badgeStyle(p)">
                <img v-if="p.logo" :src="p.logo" alt="" />
                <span v-else>{{ p.shortName }}</span>
              </div>
              <span class="pi-name">{{ p.teamName }}</span>
              <el-button size="small" text type="danger" @click="revokeJudge(p)">撤销</el-button>
            </div>
            <div v-if="!progressList.some(x => x.result === 'eliminated')" class="col-empty">暂无淘汰队伍</div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { getRequest, putRequest, postRequest } from '@/utils/api'
import { CS_MAPS } from '@/mock/esports'

/** 快速操纵时填充的败方地图比分池 */
const LOSE_SCORES = [7, 9, 5, 11]

export default {
  name: 'MatchControl',
  data() {
    return {
      currentTournament: null,
      tournamentOptions: [],
      matches: [],
      teams: [],
      progressList: [],
      savingId: null,
      judging: false
    }
  },
  mounted() {
    this.loadTournaments()
    this.loadTeams()
  },
  methods: {
    loadTournaments() {
      this.getRequest('/esports/tournament-options').then(resp => {
        if (resp) this.tournamentOptions = resp
      })
    },
    loadTeams() {
      this.getRequest('/esports/teams').then(resp => {
        if (resp) this.teams = resp
      })
    },
    loadMatches() {
      if (!this.currentTournament) { this.matches = []; return }
      this.getRequest('/esports/matches').then(resp => {
        if (!resp) return
        this.matches = resp
          .filter(m => m.tournamentId === this.currentTournament)
          .map(m => Object.assign({}, m, {
            editMaps: (m.maps && m.maps.length)
              ? m.maps.map(mp => ({ name: mp.name, score1: mp.score1, score2: mp.score2, live: !!mp.live }))
              : [this.newMap()]
          }))
      })
    },
    loadProgress() {
      if (!this.currentTournament) { this.progressList = []; return }
      this.getRequest('/esports/control/progress/' + this.currentTournament).then(resp => {
        if (resp) this.progressList = resp
      })
    },
    onTournamentChange() {
      this.loadMatches()
      this.loadProgress()
    },
    /* ---------------- 操纵 ---------------- */
    newMap() {
      return { name: '', score1: 0, score2: 0, live: false }
    },
    mapNames() {
      return CS_MAPS
    },
    // 系列赛比分（图数汇总）
    mapScore(m) {
      let s1 = 0, s2 = 0
      m.editMaps.forEach(mp => {
        if (mp.score1 > mp.score2) s1++
        else if (mp.score2 > mp.score1) s2++
      })
      return { s1, s2 }
    },
    winnerOf(m) {
      const { s1, s2 } = this.mapScore(m)
      if (m.editMaps.length < Math.ceil(m.bo / 2)) return 0
      if (s1 > s2 && s1 >= Math.ceil(m.bo / 2)) return 1
      if (s2 > s1 && s2 >= Math.ceil(m.bo / 2)) return 2
      return 0
    },
    loserOf(m) {
      return this.winnerOf(m) === 1 ? 2 : 1
    },
    // 一键操纵：让 side(1/2) 战队赢下系列赛
    quickWin(m, side) {
      const need = Math.ceil(m.bo / 2)
      const maps = []
      for (let i = 0; i < m.bo; i++) {
        const win = i < need
        const loser = LOSE_SCORES[i % LOSE_SCORES.length]
        maps.push({
          name: CS_MAPS[i % CS_MAPS.length],
          score1: side === 1 ? (win ? 13 : loser) : (win ? loser : 13),
          score2: side === 2 ? (win ? 13 : loser) : (win ? loser : 13),
          live: false
        })
      }
      m.editMaps = maps
    },
    // 保存操纵结果：比分写库并把比赛置为已结束
    saveControl(m) {
      const maps = m.editMaps.filter(mp => mp.name)
      if (!maps.length) {
        this.$message.warning('至少要有一张地图的比分')
        return
      }
      for (const mp of maps) {
        if (mp.score1 === mp.score2) {
          this.$message.warning(`地图 ${mp.name} 的比分不能相同，必须分出胜负`)
          return
        }
      }
      this.savingId = m.id
      const payload = {
        id: m.id,
        tournamentId: m.tournamentId,
        stage: m.stage,
        bo: m.bo,
        status: 'finished',
        team1Id: m.team1Id,
        team2Id: m.team2Id,
        startTime: m.startTime,
        maps: maps.map(mp => ({ name: mp.name, score1: mp.score1, score2: mp.score2, live: false }))
      }
      this.putRequest('/esports/match', payload).then(resp => {
        if (resp) {
          this.$message.success('操纵成功，比赛已按剧本结束')
          this.loadMatches()
        }
      }).finally(() => { this.savingId = null })
    },
    // 晋级 / 淘汰判定
    judge(m, side, result) {
      const teamId = side === 1 ? m.team1Id : m.team2Id
      const teamName = side === 1 ? this.teamOf(m.team1Id).name : this.teamOf(m.team2Id).name
      const action = result === 'advanced' ? '晋级' : '淘汰'
      this.$confirm(`确认将 ${teamName} 标记为本赛事${action}？`, '玩大手判定', { type: 'warning' }).then(() => {
        return postRequest('/esports/control/progress', {
          tournamentId: this.currentTournament, teamId, result
        })
      }).then(resp => {
        if (resp) {
          this.$message.success(`${teamName} 已${action}`)
          this.loadProgress()
        }
      }).catch(() => {})
    },
    // 撤销判定：删除该战队在本赛事的晋级/淘汰标记
    revokeJudge(p) {
      this.deleteRequest('/esports/control/progress?tournamentId=' + this.currentTournament + '&teamId=' + p.teamId)
        .then(resp => {
          if (resp) this.loadProgress()
        })
    },
    /* ---------------- 展示辅助 ---------------- */
    teamOf(id) {
      return this.teams.find(t => t.id === id) || { id, name: '未知战队', shortName: '?', color: ['#555', '#222'] }
    },
    shortOf(id) {
      return this.teamOf(id).shortName || '?'
    },
    badgeStyle(team) {
      const c = team.color || ['#555', '#222']
      return { background: `linear-gradient(135deg, ${c[0]}, ${c[1]})` }
    },
    statusLabel(s) {
      return { upcoming: '未开始', live: '进行中', finished: '已结束' }[s] || s
    },
    cardClass(m) {
      return {
        'is-live': m.status === 'live',
        'is-manipulated': m.status === 'finished'
      }
    }
  }
}
</script>

<style scoped>
.control-page { color: #e6edf3; }

/* 头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 18px;
  padding-bottom: 18px;
  border-bottom: 1px solid #232b36;
}
.page-title {
  margin: 0;
  font-size: 26px;
  font-weight: 800;
  letter-spacing: 0.5px;
  background: linear-gradient(135deg, #ffb020, #ff4655);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}
.hand-icon { -webkit-text-fill-color: initial; }
.page-sub { display: block; margin-top: 6px; font-size: 13px; color: #8b949e; }
.tourney-select { width: 280px; }

.section-title {
  margin: 22px 0 12px;
  font-size: 14px;
  font-weight: 700;
  color: #e6edf3;
  padding-left: 10px;
  border-left: 3px solid #ffb020;
}

/* 对阵卡片 */
.match-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(460px, 1fr));
  gap: 16px;
}
.control-card {
  background: #141a22;
  border: 1px solid #232b36;
  border-radius: 14px;
  padding: 16px 18px;
  transition: border-color 0.25s, box-shadow 0.25s;
}
.control-card.is-manipulated {
  border-color: rgba(255, 176, 32, 0.5);
  box-shadow: 0 0 0 1px rgba(255, 176, 32, 0.12);
}
.card-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 12px;
}
.status-tag {
  padding: 3px 10px;
  border-radius: 11px;
  border: 1px solid;
  font-weight: 600;
}
.status-tag.live { color: #ff4655; border-color: #ff4655; background: #ff46551a; }
.status-tag.finished { color: #3fb950; border-color: #3fb950; background: #3fb9501a; }
.status-tag.upcoming { color: #8b949e; border-color: #30363d; background: #30363d1a; }
.stage { color: #6e7681; }

.vs-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
}
.vs-team {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
  min-width: 0;
  transition: opacity 0.25s;
}
.vs-team.right { justify-content: flex-end; }
.vs-team.dim { opacity: 0.45; }
.team-name {
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.team-badge {
  width: 40px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 800;
  color: #fff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}
.team-badge.sm { width: 30px; height: 30px; border-radius: 8px; font-size: 11px; }
.team-badge img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  padding: 3px;
  filter: drop-shadow(0 1px 3px rgba(0, 0, 0, 0.6));
}
.vs-score { font-size: 24px; font-weight: 800; flex-shrink: 0; font-variant-numeric: tabular-nums; }
.vs-score .winner { color: #ff4655; }
.colon { color: #6e7681; margin: 0 4px; }

/* 每图操纵 */
.map-edit { border-top: 1px dashed #1c232c; padding-top: 10px; }
.map-edit-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}
.map-select { width: 150px; }
.score-input { width: 96px; }
.dash { color: #6e7681; }

/* 动作条 */
.action-bar {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-top: 10px;
}
.quick-btn, .save-btn, .verdict-btn {
  padding: 7px 14px;
  border-radius: 8px;
  border: 1px solid;
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
  transition: all 0.2s;
  color: #fff;
}
.quick-btn.left {
  background: linear-gradient(135deg, #1f6feb33, #1f6feb22);
  border-color: #1f6feb;
  color: #79c0ff;
}
.quick-btn.right {
  background: linear-gradient(135deg, #ff465533, #ff465522);
  border-color: #ff4655;
  color: #ff8a99;
}
.quick-btn:hover { filter: brightness(1.3); }
.save-btn {
  margin-left: auto;
  background: linear-gradient(135deg, #ffb020, #ff8a00);
  border-color: transparent;
}
.save-btn:disabled { opacity: 0.5; cursor: not-allowed; }

/* 判定条 */
.verdict-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
  margin-top: 12px;
  padding: 10px 12px;
  border-radius: 10px;
  background: rgba(255, 176, 32, 0.08);
  border: 1px solid rgba(255, 176, 32, 0.35);
}
.verdict-tip { font-size: 13px; color: #ffb020; flex: 1; min-width: 160px; }
.verdict-btn { font-size: 12px; padding: 6px 12px; }
.verdict-btn.advance {
  background: linear-gradient(135deg, #238636, #2ea043);
  border-color: transparent;
}
.verdict-btn.eliminate {
  background: transparent;
  border-color: #ff4655;
  color: #ff8a99;
}

/* 晋级/淘汰榜 */
.progress-board {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}
.progress-col {
  background: #141a22;
  border: 1px solid #232b36;
  border-radius: 14px;
  overflow: hidden;
}
.progress-col.advanced { border-color: rgba(63, 185, 80, 0.4); }
.progress-col.eliminated { border-color: rgba(255, 70, 85, 0.4); }
.col-head {
  padding: 10px 16px;
  font-weight: 700;
  font-size: 14px;
}
.progress-col.advanced .col-head { background: #3fb95022; color: #3fb950; }
.progress-col.eliminated .col-head { background: #ff465522; color: #ff8a99; }
.col-body { padding: 10px 16px 16px; display: flex; flex-direction: column; gap: 10px; }
.progress-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 8px;
  border-radius: 8px;
  background: #1c232c;
}
.pi-name { font-size: 14px; font-weight: 600; flex: 1; }
.col-empty { color: #6e7681; font-size: 13px; padding: 8px; }

@media (max-width: 1000px) {
  .match-grid, .progress-board { grid-template-columns: 1fr; }
}
</style>
