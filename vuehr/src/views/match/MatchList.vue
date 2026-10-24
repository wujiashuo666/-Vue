<template>
  <div class="match-page">
    <!-- 顶部标题与状态切换 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">比赛中心</h1>
        <span class="page-sub">共 {{ enrichedMatches.length }} 场 · {{ liveCount }} 场进行中</span>
      </div>
      <div class="tabs">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          class="tab"
          :class="{ active: currentTab === tab.key }"
          @click="currentTab = tab.key"
        >
          {{ tab.label }}
          <span class="tab-count">{{ tabCount(tab.key) }}</span>
        </button>
        <el-button class="manage-btn" size="small" @click="$router.push('/match/manage')">管理比赛</el-button>
      </div>
    </div>

    <!-- 搜索与筛选 -->
    <div class="filter-bar">
      <el-input
        v-model="keyword"
        placeholder="搜索战队 / 赛事名称"
        clearable
        class="filter-search"
      >
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>

      <el-select v-model="filterTournament" placeholder="全部赛事" clearable class="filter-item">
        <el-option v-for="t in tournamentOptions" :key="t" :label="t" :value="t" />
      </el-select>

      <el-select v-model="filterMap" placeholder="全部地图" clearable class="filter-item">
        <el-option v-for="m in mapOptions" :key="m" :label="m" :value="m" />
      </el-select>

      <el-select v-model="sortOrder" class="filter-item filter-sort">
        <el-option label="时间：由近到远" value="desc" />
        <el-option label="时间：由远到近" value="asc" />
      </el-select>

      <button v-if="hasFilter" class="reset-btn" @click="resetFilter">重置</button>
    </div>

    <!-- 比赛列表 -->
    <div v-if="filteredMatches.length" class="match-list">
      <div
        v-for="match in filteredMatches"
        :key="match.id"
        class="match-card"
        :class="{ 'is-live': match.status === 'live' }"
        @click="openDetail(match)"
      >
        <div class="card-top">
          <span class="status-tag" :style="statusStyle(match.status)">
            <i v-if="match.status === 'live'" class="live-dot"></i>
            {{ statusLabel(match.status) }}
          </span>
          <span class="tournament">{{ match.tournament }}</span>
          <span class="stage">BO{{ match.bo }} · {{ match.stage }}</span>
        </div>

        <div class="match-info">
          <div class="team team-left">
            <div class="team-badge" :style="badgeStyle(match.team1)">{{ match.team1.short }}</div>
            <span class="team-name">{{ match.team1.name }}</span>
          </div>

          <div class="score-area">
            <template v-if="match.status === 'upcoming'">
              <div class="score-time">{{ formatClock(match.startTime) }}</div>
              <div class="score-date">{{ formatDate(match.startTime) }}</div>
            </template>
            <template v-else>
              <div class="score">
                <span :class="{ winner: match.score1 > match.score2 }">{{ match.score1 }}</span>
                <span class="colon">:</span>
                <span :class="{ winner: match.score2 > match.score1 }">{{ match.score2 }}</span>
              </div>
              <div v-if="match.curMap" class="live-map">
                当前 {{ match.curMap.name }} {{ match.curMap.score1 }} - {{ match.curMap.score2 }}
              </div>
            </template>
          </div>

          <div class="team team-right">
            <span class="team-name">{{ match.team2.name }}</span>
            <div class="team-badge" :style="badgeStyle(match.team2)">{{ match.team2.short }}</div>
          </div>
        </div>

        <div class="card-bottom">
          <div class="meta-left">
            <span v-if="match.mapNames.length" class="maps">
              <span v-for="(mp, i) in match.mapNames" :key="i" class="map-chip">{{ mp }}</span>
            </span>
            <span v-else class="maps muted">图池待定</span>
          </div>
          <div class="meta-right">
            <span v-if="match.status === 'upcoming'" class="countdown">{{ countdown(match.startTime) }}</span>
            <span v-else class="match-time">{{ formatDateTime(match.startTime) }}</span>
            <span class="detail-hint">查看详情 ›</span>
          </div>
        </div>
      </div>
    </div>

    <el-empty v-else description="没有符合条件的比赛，试试调整筛选条件" />

    <!-- 比赛详情 -->
    <el-dialog
      v-model="detailVisible"
      :title="detail ? `${detail.team1.name}  VS  ${detail.team2.name}` : ''"
      width="860px"
      class="match-dialog"
      append-to-body
    >
      <div v-if="detail" class="detail-body">
        <div class="detail-hero">
          <div class="hero-team">
            <div class="team-badge lg" :style="badgeStyle(detail.team1)">{{ detail.team1.short }}</div>
            <div class="hero-name">{{ detail.team1.name }}</div>
            <div class="hero-rank">世界排名 #{{ detail.team1.worldRank }}</div>
          </div>
          <div class="hero-score">
            <template v-if="detail.status === 'upcoming'">
              <div class="hero-vs">VS</div>
              <div class="hero-sub">{{ formatDateTime(detail.startTime) }}</div>
            </template>
            <template v-else>
              <div class="hero-nums">
                <span :class="{ winner: detail.score1 > detail.score2 }">{{ detail.score1 }}</span>
                <span class="colon">:</span>
                <span :class="{ winner: detail.score2 > detail.score1 }">{{ detail.score2 }}</span>
              </div>
              <div class="hero-sub">{{ statusLabel(detail.status) }}</div>
            </template>
          </div>
          <div class="hero-team">
            <div class="team-badge lg" :style="badgeStyle(detail.team2)">{{ detail.team2.short }}</div>
            <div class="hero-name">{{ detail.team2.name }}</div>
            <div class="hero-rank">世界排名 #{{ detail.team2.worldRank }}</div>
          </div>
        </div>

        <div class="detail-meta">
          <span><b>赛事</b>{{ detail.tournament }}</span>
          <span><b>阶段</b>{{ detail.stage }}</span>
          <span><b>赛制</b>BO{{ detail.bo }}</span>
          <span><b>时间</b>{{ formatDateTime(detail.startTime) }}</span>
        </div>

        <div class="section-title">逐图比分</div>
        <div v-if="detail.maps.length" class="map-list">
          <div v-for="(mp, i) in detail.maps" :key="i" class="map-row">
            <div class="map-name">{{ mp.name }}</div>
            <div class="map-bar">
              <div class="bar-track">
                <div class="bar-fill left" :style="{ width: barWidth(mp.score1, mp.score2) }"></div>
                <div class="bar-fill right"></div>
              </div>
              <div class="map-score">
                <span :class="{ winner: mp.score1 > mp.score2 }">{{ mp.score1 }}</span>
                <span class="colon">:</span>
                <span :class="{ winner: mp.score2 > mp.score1 }">{{ mp.score2 }}</span>
              </div>
            </div>
            <el-tag v-if="mp.live" type="danger" size="small" effect="dark">进行中</el-tag>
          </div>
        </div>
        <div v-else class="empty-tip">暂无逐图比分数据</div>

        <div class="roster-grid">
          <div v-for="side in [{ team: detail.team1, key: 'left' }, { team: detail.team2, key: 'right' }]" :key="side.key" class="roster-col">
            <div class="section-title">{{ side.team.name }} 选手数据</div>
            <el-table :data="roster(side.team.id)" size="small" class="roster-table">
              <el-table-column prop="nick" label="选手" min-width="110" />
              <el-table-column prop="country" label="国籍" width="70" />
              <el-table-column prop="rating" label="Rating" width="80" sortable />
              <el-table-column prop="kd" label="K/D" width="70" />
              <el-table-column prop="adr" label="ADR" width="70" />
              <el-table-column prop="kast" label="KAST" width="75" />
            </el-table>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { Search } from '@element-plus/icons-vue'
import {
  MATCH_STATUS, STATUS_META, seriesScore, currentMap, fetchEsports
} from '@/mock/esports'

export default {
  name: 'MatchList',
  components: { Search },
  data() {
    return {
      currentTab: 'all',
      keyword: '',
      filterTournament: '',
      filterMap: '',
      sortOrder: 'desc',
      detailVisible: false,
      detail: null,
      now: Date.now(),
      timer: null,
      matchesData: [],
      playersData: [],
      teamsData: [],
      tournamentOptionsData: [],
      mapOptionsData: [],
      loading: false,
      tabs: [
        { key: 'all', label: '全部' },
        { key: MATCH_STATUS.LIVE, label: '进行中' },
        { key: MATCH_STATUS.UPCOMING, label: '未开始' },
        { key: MATCH_STATUS.FINISHED, label: '已结束' }
      ]
    }
  },
  computed: {
    // 模板无法直接访问模块导入，这里以 computed 暴露
    tournamentOptions() { return this.tournamentOptionsData },
    mapOptions() { return this.mapOptionsData },

    // 把队伍对象、系列赛比分、当前图等派生数据一次性算好，模板里不再做查找
    enrichedMatches() {
      return this.matchesData.map(m => {
        const { team1Score: s1, team2Score: s2 } = seriesScore(m)
        return {
          ...m,
          team1: this.getTeamById(m.team1Id),
          team2: this.getTeamById(m.team2Id),
          score1: s1,
          score2: s2,
          curMap: currentMap(m),
          mapNames: m.maps.map(x => x.name)
        }
      })
    },

    filteredMatches() {
      const kw = this.keyword.trim().toLowerCase()
      let list = this.enrichedMatches.filter(m => {
        if (this.currentTab !== 'all' && m.status !== this.currentTab) return false
        if (this.filterTournament && m.tournament !== this.filterTournament) return false
        // 未开始的比赛没有图池，选中地图时应排除，否则筛选结果不可预期
        if (this.filterMap && !m.mapNames.includes(this.filterMap)) return false
        if (kw) {
          const hay = `${m.team1.name} ${m.team2.name} ${m.team1.short} ${m.team2.short} ${m.tournament}`.toLowerCase()
          if (!hay.includes(kw)) return false
        }
        return true
      })
      list = list.sort((a, b) => {
        const diff = new Date(a.startTime) - new Date(b.startTime)
        return this.sortOrder === 'desc' ? -diff : diff
      })
      return list
    },

    liveCount() {
      return this.enrichedMatches.filter(m => m.status === MATCH_STATUS.LIVE).length
    },

    hasFilter() {
      return !!(this.keyword || this.filterTournament || this.filterMap)
    }
  },
  mounted() {
    this.loadData()
    // 倒计时每秒刷新
    this.timer = setInterval(() => { this.now = Date.now() }, 1000)
  },
  beforeUnmount() {
    clearInterval(this.timer)
  },
  methods: {
    tabCount(key) {
      if (key === 'all') return this.enrichedMatches.length
      return this.enrichedMatches.filter(m => m.status === key).length
    },
    statusLabel(status) {
      return STATUS_META[status] ? STATUS_META[status].label : status
    },
    statusStyle(status) {
      const meta = STATUS_META[status]
      if (!meta) return {}
      return { color: meta.color, borderColor: meta.color, background: `${meta.color}1a` }
    },
    badgeStyle(team) {
      const [c1, c2] = team.color || ['#555', '#222']
      return { background: `linear-gradient(135deg, ${c1}, ${c2})` }
    },
    barWidth(s1, s2) {
      const total = s1 + s2
      if (!total) return '50%'
      return `${(s1 / total) * 100}%`
    },
    roster(teamId) {
      return this.playersData.filter(p => p.teamId === teamId).sort((a, b) => b.rating - a.rating)
    },
    getTeamById(id) {
      return this.teamsData.find(t => t.id === id) || { id, name: '未知战队', short: '?', color: ['#555', '#222'] }
    },
    async loadData() {
      this.loading = true
      try {
        const d = await fetchEsports()
        this.matchesData = d.matches
        this.playersData = d.players
        this.teamsData = d.teams
        this.tournamentOptionsData = d.tournaments
        this.mapOptionsData = d.usedMaps
      } finally {
        this.loading = false
      }
    },
    openDetail(match) {
      this.detail = match
      this.detailVisible = true
    },
    resetFilter() {
      this.keyword = ''
      this.filterTournament = ''
      this.filterMap = ''
    },
    pad(n) { return n < 10 ? `0${n}` : `${n}` },
    formatDateTime(t) {
      const d = new Date(t)
      return `${d.getFullYear()}-${this.pad(d.getMonth() + 1)}-${this.pad(d.getDate())} ${this.pad(d.getHours())}:${this.pad(d.getMinutes())}`
    },
    formatDate(t) {
      const d = new Date(t)
      return `${this.pad(d.getMonth() + 1)}/${this.pad(d.getDate())}`
    },
    formatClock(t) {
      const d = new Date(t)
      return `${this.pad(d.getHours())}:${this.pad(d.getMinutes())}`
    },
    countdown(t) {
      const diff = new Date(t).getTime() - this.now
      if (diff <= 0) return '即将开始'
      const day = Math.floor(diff / 86400000)
      const h = Math.floor(diff % 86400000 / 3600000)
      const m = Math.floor(diff % 3600000 / 60000)
      const s = Math.floor(diff % 60000 / 1000)
      if (day > 0) return `${day}天 ${this.pad(h)}:${this.pad(m)}:${this.pad(s)}`
      return `${this.pad(h)}:${this.pad(m)}:${this.pad(s)}`
    }
  }
}
</script>

<style scoped>
.match-page {
  color: #e6edf3;
}

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
  background: linear-gradient(135deg, #ff4655, #ff8a00);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}
.page-sub {
  display: block;
  margin-top: 6px;
  font-size: 13px;
  color: #8b949e;
}
.tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.tab {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 18px;
  border-radius: 20px;
  background: #141a22;
  border: 1px solid #232b36;
  color: #8b949e;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}
.tab:hover { border-color: #ff4655; color: #e6edf3; }
.tab.active {
  background: linear-gradient(135deg, #ff4655, #ff8a00);
  border-color: transparent;
  color: #fff;
  font-weight: 600;
}
.tab-count {
  font-size: 12px;
  padding: 1px 7px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.12);
}
.manage-btn {
  align-self: center;
  margin-left: 4px;
}

/* 筛选栏 */
.filter-bar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: 20px;
}
.filter-search { width: 260px; }
.filter-item { width: 170px; }
.filter-sort { width: 160px; }
.reset-btn {
  padding: 8px 16px;
  border-radius: 8px;
  background: transparent;
  border: 1px solid #30363d;
  color: #8b949e;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}
.reset-btn:hover { border-color: #ff4655; color: #ff4655; }

/* 列表 */
.match-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(420px, 1fr));
  gap: 16px;
}
.match-card {
  background: #141a22;
  border: 1px solid #232b36;
  border-radius: 14px;
  padding: 18px 20px;
  cursor: pointer;
  transition: transform 0.25s, border-color 0.25s, box-shadow 0.25s;
}
.match-card:hover {
  border-color: #ff4655;
  transform: translateY(-3px);
  box-shadow: 0 10px 28px rgba(255, 70, 85, 0.14);
}
.match-card.is-live {
  border-color: rgba(255, 70, 85, 0.55);
  box-shadow: 0 0 0 1px rgba(255, 70, 85, 0.15), 0 8px 24px rgba(255, 70, 85, 0.1);
}

.card-top {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 14px;
  font-size: 12px;
}
.status-tag {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  padding: 3px 10px;
  border-radius: 11px;
  border: 1px solid;
  font-weight: 600;
}
.live-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #ff4655;
  animation: pulse 1.4s infinite;
}
@keyframes pulse {
  0% { opacity: 1; box-shadow: 0 0 0 0 rgba(255, 70, 85, 0.7); }
  70% { opacity: 0.6; box-shadow: 0 0 0 7px rgba(255, 70, 85, 0); }
  100% { opacity: 1; box-shadow: 0 0 0 0 rgba(255, 70, 85, 0); }
}
.tournament { color: #ffb020; font-weight: 600; }
.stage { color: #6e7681; margin-left: auto; }

.match-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 14px;
}
.team {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
  min-width: 0;
}
.team-right { justify-content: flex-end; }
.team-badge {
  width: 38px;
  height: 38px;
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
.team-badge.lg { width: 54px; height: 54px; font-size: 15px; border-radius: 14px; }
.team-name {
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.score-area {
  text-align: center;
  flex-shrink: 0;
  min-width: 108px;
}
.score {
  font-size: 26px;
  font-weight: 800;
  letter-spacing: 1px;
}
.score .winner { color: #ff4655; }
.colon { color: #6e7681; margin: 0 4px; }
.score-time { font-size: 20px; font-weight: 700; color: #e6edf3; }
.score-date { font-size: 12px; color: #8b949e; }
.live-map {
  margin-top: 2px;
  font-size: 11px;
  color: #ff4655;
  white-space: nowrap;
}

.card-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding-top: 12px;
  border-top: 1px solid #1c232c;
  font-size: 12px;
  color: #8b949e;
}
.maps { display: flex; gap: 6px; flex-wrap: wrap; }
.map-chip {
  padding: 2px 8px;
  border-radius: 5px;
  background: #1c232c;
  border: 1px solid #2a323d;
  color: #a9b3bf;
}
.muted { color: #6e7681; }
.meta-right {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}
.countdown {
  color: #ffb020;
  font-weight: 700;
  font-variant-numeric: tabular-nums;
}
.detail-hint { color: #ff4655; }

/* 详情弹窗 */
.detail-hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 20px;
  border-radius: 12px;
  background: linear-gradient(135deg, #161c25, #10151c);
  border: 1px solid #232b36;
}
.hero-team {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  flex: 1;
}
.hero-name { font-size: 16px; font-weight: 700; }
.hero-rank { font-size: 12px; color: #8b949e; }
.hero-score { text-align: center; flex-shrink: 0; }
.hero-vs { font-size: 20px; font-weight: 800; color: #8b949e; }
.hero-nums { font-size: 34px; font-weight: 800; }
.hero-nums .winner { color: #ff4655; }
.hero-sub { font-size: 12px; color: #8b949e; margin-top: 4px; }

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 16px;
  font-size: 13px;
  color: #a9b3bf;
}
.detail-meta b { color: #6e7681; font-weight: 500; margin-right: 6px; }

.section-title {
  margin: 22px 0 12px;
  font-size: 14px;
  font-weight: 700;
  color: #e6edf3;
  padding-left: 10px;
  border-left: 3px solid #ff4655;
}
.map-list { display: flex; flex-direction: column; gap: 10px; }
.map-row {
  display: flex;
  align-items: center;
  gap: 14px;
}
.map-name { width: 82px; font-size: 13px; color: #a9b3bf; }
.map-bar { flex: 1; display: flex; align-items: center; gap: 12px; }
.bar-track {
  flex: 1;
  height: 8px;
  border-radius: 4px;
  overflow: hidden;
  display: flex;
  background: #2a323d;
}
.bar-fill.left { background: linear-gradient(90deg, #ff4655, #ff8a00); }
.bar-fill.right { flex: 1; background: #3b444f; }
.map-score {
  width: 66px;
  text-align: right;
  font-size: 14px;
  font-weight: 700;
  font-variant-numeric: tabular-nums;
}
.map-score .winner { color: #ff4655; }
.empty-tip { font-size: 13px; color: #6e7681; }

.roster-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}
.roster-col { min-width: 0; }
.roster-table { width: 100%; }

@media (max-width: 900px) {
  .match-list { grid-template-columns: 1fr; }
  .roster-grid { grid-template-columns: 1fr; }
}
</style>
