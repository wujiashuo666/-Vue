<template>
  <div class="rating-page">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">选手数据</h1>
        <span class="page-sub">Rating 2.1 口径 · 共 {{ players.length }} 名选手</span>
      </div>
    </div>

    <div class="overview">
      <div class="ov-card">
        <div class="ov-label">选手总数</div>
        <div class="ov-value">{{ players.length }}</div>
      </div>
      <div class="ov-card">
        <div class="ov-label">平均 Rating</div>
        <div class="ov-value">{{ avgRating }}</div>
      </div>
      <div class="ov-card">
        <div class="ov-label">Rating 榜首</div>
        <div class="ov-value small">{{ topPlayer ? topPlayer.nick : '-' }}</div>
        <div class="ov-extra" v-if="topPlayer">{{ fmt(topPlayer.rating, 2) }} · {{ teamName(topPlayer.teamId) }}</div>
      </div>
      <div class="ov-card">
        <div class="ov-label">ADR 榜首</div>
        <div class="ov-value small">{{ topAdr ? topAdr.nick : '-' }}</div>
        <div class="ov-extra" v-if="topAdr">{{ topAdr.adr }} · {{ teamName(topAdr.teamId) }}</div>
      </div>
    </div>

    <div class="filter-bar">
      <el-input v-model="keyword" placeholder="搜索选手 ID / 姓名" clearable class="filter-search">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-select v-model="teamId" placeholder="全部战队" clearable class="filter-item">
        <el-option v-for="t in teams" :key="t.id" :label="t.name" :value="t.id" />
      </el-select>
      <el-select v-model="minRating" placeholder="Rating 下限" clearable class="filter-item">
        <el-option label="1.00 以上" :value="1.0" />
        <el-option label="1.10 以上" :value="1.1" />
        <el-option label="1.20 以上" :value="1.2" />
      </el-select>
      <button class="reset-btn" @click="keyword = ''; teamId = ''; minRating = ''">重置</button>
    </div>

    <el-table :data="filteredPlayers" class="player-table" :default-sort="{ prop: 'rating', order: 'descending' }">
      <el-table-column label="#" width="62" align="center">
        <template #default="{ $index }">
          <span class="rank" :class="{ top: $index < 3 }">{{ $index + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column label="选手" min-width="170">
        <template #default="{ row }">
          <div class="player-cell">
            <span class="nick">{{ row.nick }}</span>
            <span class="real-name">{{ row.name }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="战队" min-width="150">
        <template #default="{ row }">
          <div class="team-cell">
            <div class="team-badge" :style="badgeStyle(row.teamId)">{{ teamShort(row.teamId) }}</div>
            <span>{{ teamName(row.teamId) }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="country" label="国籍" width="80" />

      <el-table-column label="Rating" width="170" prop="rating" sortable>
        <template #default="{ row }">
          <div class="rate-cell">
            <div class="rate-track">
              <div class="rate-fill" :style="{ width: ratingWidth(row.rating) }"></div>
            </div>
            <span class="rate-num" :class="ratingClass(row.rating)">{{ fmt(row.rating, 2) }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="kd" label="K/D" width="90" sortable>
        <template #default="{ row }">
          <span :class="Number(row.kd) >= 1 ? 'pos' : 'neg'">{{ fmt(row.kd, 2) }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="adr" label="ADR" width="90" sortable>
        <template #default="{ row }">{{ fmt(row.adr, 2) }}</template>
      </el-table-column>
      <el-table-column prop="kast" label="KAST" width="90" sortable>
        <template #default="{ row }">{{ fmt(row.kast, 1) }}%</template>
      </el-table-column>
      <el-table-column prop="maps" label="地图数" width="90" sortable />
    </el-table>

    <el-empty v-if="!filteredPlayers.length" description="没有符合条件的选手" />
  </div>
</template>

<script>
import { Search } from '@element-plus/icons-vue'
import { fetchEsports } from '@/mock/esports'

export default {
  name: 'PlayerRating',
  components: { Search },
    data() {
      return {
        keyword: '',
        teamId: '',
        minRating: '',
        players: [],
        teams: [],
        loading: false
      }
    },
  computed: {
    filteredPlayers() {
      const kw = this.keyword.trim().toLowerCase()
      return this.players.filter(p => {
        if (this.teamId !== '' && p.teamId !== this.teamId) return false
        if (this.minRating !== '' && p.rating < this.minRating) return false
        if (kw && !(`${p.nick} ${p.name}`.toLowerCase().includes(kw))) return false
        return true
      })
    },
    sorted() {
      return [...this.filteredPlayers].sort((a, b) => b.rating - a.rating)
    },
    avgRating() {
      if (!this.players.length) return '0.00'
      const sum = this.players.reduce((s, p) => s + (Number(p.rating) || 0), 0)
      return (sum / this.players.length).toFixed(2)
    },
    topPlayer() { return this.sorted.length ? this.sorted[0] : null },
    topAdr() {
      if (!this.players.length) return null
      return this.players.reduce((a, b) => (b.adr > a.adr ? b : a))
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const d = await fetchEsports()
        this.players = d.players
        this.teams = d.teams
      } finally {
        this.loading = false
      }
    },
    teamName(id) {
      const t = this.teams.find(x => x.id === id)
      return t ? t.name : '未知战队'
    },
    teamShort(id) {
      const t = this.teams.find(x => x.id === id)
      return t ? t.short : '?'
    },
    badgeStyle(id) {
      const t = this.teams.find(x => x.id === id)
      const [c1, c2] = (t && t.color) || ['#555', '#222']
      return { background: `linear-gradient(135deg, ${c1}, ${c2})` }
    },
    // 统一数值展示：空值或非数字一律显示 "-"，避免 toFixed 报错
    fmt(value, digits) {
      const n = Number(value)
      return Number.isFinite(n) ? n.toFixed(digits) : '-'
    },
    // Rating 0.8~1.5 映射到进度条宽度，低于 0.8 按 0 处理
    ratingWidth(rating) {
      const n = Number(rating)
      if (!Number.isFinite(n)) return '0%'
      const pct = ((n - 0.8) / 0.7) * 100
      return `${Math.min(100, Math.max(0, pct))}%`
    },
    ratingClass(rating) {
      if (rating >= 1.2) return 'elite'
      if (rating >= 1.1) return 'good'
      if (rating >= 1.0) return 'ok'
      return 'poor'
    }
  }
}
</script>

<style scoped>
.rating-page { color: #e6edf3; }
.page-header {
  margin-bottom: 18px;
  padding-bottom: 18px;
  border-bottom: 1px solid #232b36;
}
.page-title {
  margin: 0;
  font-size: 26px;
  font-weight: 800;
  background: linear-gradient(135deg, #ff4655, #ff8a00);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}
.page-sub { display: block; margin-top: 6px; font-size: 13px; color: #8b949e; }

.overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(170px, 1fr));
  gap: 14px;
  margin-bottom: 22px;
}
.ov-card {
  background: #141a22;
  border: 1px solid #232b36;
  border-radius: 12px;
  padding: 16px 18px;
}
.ov-label { font-size: 12px; color: #8b949e; }
.ov-value {
  margin-top: 8px;
  font-size: 28px;
  font-weight: 800;
  color: #ff4655;
  font-variant-numeric: tabular-nums;
}
.ov-value.small { font-size: 19px; color: #e6edf3; }
.ov-extra { margin-top: 4px; font-size: 12px; color: #8b949e; }

.filter-bar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 18px;
}
.filter-search { width: 250px; }
.filter-item { width: 160px; }
.reset-btn {
  padding: 8px 16px;
  border-radius: 8px;
  background: transparent;
  border: 1px solid #30363d;
  color: #8b949e;
  cursor: pointer;
  font-size: 13px;
}
.reset-btn:hover { border-color: #ff4655; color: #ff4655; }

.player-cell { display: flex; flex-direction: column; }
.nick { font-weight: 700; font-size: 14px; }
.real-name { font-size: 12px; color: #8b949e; }

.team-cell { display: flex; align-items: center; gap: 8px; }
.team-badge {
  width: 28px;
  height: 28px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 800;
  color: #fff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  flex-shrink: 0;
}

.rank {
  display: inline-block;
  width: 24px;
  height: 24px;
  line-height: 24px;
  border-radius: 7px;
  background: #1c232c;
  color: #8b949e;
  font-weight: 700;
  font-size: 12px;
}
.rank.top {
  background: linear-gradient(135deg, #ff4655, #ff8a00);
  color: #fff;
}

.rate-cell { display: flex; align-items: center; gap: 10px; }
.rate-track {
  flex: 1;
  height: 7px;
  border-radius: 4px;
  background: #2a323d;
  overflow: hidden;
}
.rate-fill {
  height: 100%;
  border-radius: 4px;
  background: linear-gradient(90deg, #ff4655, #ff8a00);
}
.rate-num {
  width: 40px;
  text-align: right;
  font-weight: 700;
  font-variant-numeric: tabular-nums;
}
.rate-num.elite { color: #ff4655; }
.rate-num.good { color: #ffb020; }
.rate-num.ok { color: #22c55e; }
.rate-num.poor { color: #8b949e; }

.pos { color: #22c55e; font-weight: 700; }
.neg { color: #ef4444; font-weight: 700; }
</style>
