<template>
  <div class="stats-page">
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">战队战绩</h1>
        <span class="page-sub">依据 {{ finishedCount }} 场已结束比赛实时统计</span>
      </div>
    </div>

    <!-- 概览卡片 -->
    <div class="overview">
      <div class="ov-card">
        <div class="ov-label">参赛战队</div>
        <div class="ov-value">{{ stats.length }}</div>
      </div>
      <div class="ov-card">
        <div class="ov-label">已结束比赛</div>
        <div class="ov-value">{{ finishedCount }}</div>
      </div>
      <div class="ov-card">
        <div class="ov-label">胜率第一</div>
        <div class="ov-value small">{{ topTeam ? topTeam.name : '-' }}</div>
        <div class="ov-extra" v-if="topTeam">{{ topTeam.winRate }}%</div>
      </div>
      <div class="ov-card">
        <div class="ov-label">净胜图最多</div>
        <div class="ov-value small">{{ bestDiffTeam ? bestDiffTeam.name : '-' }}</div>
        <div class="ov-extra" v-if="bestDiffTeam">+{{ bestDiffTeam.mapDiff }}</div>
      </div>
    </div>

    <div class="filter-bar">
      <el-input v-model="keyword" placeholder="搜索战队" clearable class="filter-search">
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-select v-model="region" placeholder="全部赛区" clearable class="filter-item">
        <el-option v-for="r in regions" :key="r" :label="r" :value="r" />
      </el-select>
      <button class="reset-btn" @click="region = ''; keyword = ''">重置</button>
    </div>

    <el-table :data="filteredStats" class="team-table" :default-sort="{ prop: 'winRate', order: 'descending' }">
      <el-table-column label="#" width="60" align="center">
        <template #default="{ $index }">
          <span class="rank" :class="{ top: $index < 3 }">{{ $index + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column label="战队" min-width="180">
        <template #default="{ row }">
          <div class="team-cell">
            <div class="team-badge" :style="badgeStyle(row)">{{ row.short }}</div>
            <span class="team-name">{{ row.name }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="region" label="赛区" width="90" />
      <el-table-column prop="worldRank" label="世界排名" width="90" sortable />

      <el-table-column label="胜负" width="100" prop="wins" sortable>
        <template #default="{ row }">
          <span class="w">{{ row.wins }}</span>
          <span class="sep">-</span>
          <span class="l">{{ row.losses }}</span>
        </template>
      </el-table-column>

      <el-table-column label="胜率" width="170" prop="winRate" sortable>
        <template #default="{ row }">
          <div class="rate-cell">
            <div class="rate-track">
              <div class="rate-fill" :style="{ width: row.winRate + '%' }"></div>
            </div>
            <span class="rate-num">{{ row.winRate }}%</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="净胜图" width="100" prop="mapDiff" sortable>
        <template #default="{ row }">
          <span :class="row.mapDiff >= 0 ? 'pos' : 'neg'">
            {{ row.mapDiff >= 0 ? '+' : '' }}{{ row.mapDiff }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="近期战绩" min-width="200">
        <template #default="{ row }">
          <div class="recent">
            <span
              v-for="(r, i) in row.recent"
              :key="i"
              class="recent-chip"
              :class="r.result === 'W' ? 'w' : 'l'"
              :title="`${r.result} ${r.score} vs ${r.opponent}`"
            >{{ r.result }}</span>
            <span v-if="!row.recent.length" class="muted">暂无</span>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!filteredStats.length" description="没有符合条件的战队" />
  </div>
</template>

<script>
import { Search } from '@element-plus/icons-vue'
import { MATCH_STATUS, fetchEsports } from '@/mock/esports'

export default {
  name: 'TeamStats',
  components: { Search },
    data() {
      return {
        keyword: '',
        region: '',
        stats: [],
        matchesData: [],
        loading: false
      }
    },
  computed: {
    regions() { return [...new Set(this.stats.map(t => t.region))] },
    finishedCount() { return this.matchesData.filter(m => m.status === MATCH_STATUS.FINISHED).length },
    filteredStats() {
      const kw = this.keyword.trim().toLowerCase()
      return this.stats.filter(t => {
        if (this.region && t.region !== this.region) return false
        if (kw && !(`${t.name} ${t.short}`.toLowerCase().includes(kw))) return false
        return true
      })
    },
    topTeam() { return this.stats.length ? this.stats[0] : null },
    bestDiffTeam() {
      if (!this.stats.length) return null
      return this.stats.reduce((a, b) => (b.mapDiff > a.mapDiff ? b : a))
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
        this.stats = d.teamStats
        this.matchesData = d.matches
      } finally {
        this.loading = false
      }
    },
    badgeStyle(team) {
      const [c1, c2] = team.color || ['#555', '#222']
      return { background: `linear-gradient(135deg, ${c1}, ${c2})` }
    }
  }
}
</script>

<style scoped>
.stats-page { color: #e6edf3; }
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
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
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
}
.ov-value.small { font-size: 19px; color: #e6edf3; }
.ov-extra { margin-top: 4px; font-size: 12px; color: #ffb020; font-weight: 700; }

.filter-bar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 18px;
}
.filter-search { width: 240px; }
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

.team-cell { display: flex; align-items: center; gap: 10px; }
.team-badge {
  width: 32px;
  height: 32px;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 800;
  color: #fff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  flex-shrink: 0;
}
.team-name { font-weight: 600; }

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

.w { color: #22c55e; font-weight: 700; }
.l { color: #ef4444; font-weight: 700; }
.sep { color: #6e7681; margin: 0 4px; }

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
  width: 42px;
  text-align: right;
  font-weight: 700;
  font-variant-numeric: tabular-nums;
}

.pos { color: #22c55e; font-weight: 700; }
.neg { color: #ef4444; font-weight: 700; }

.recent { display: flex; gap: 5px; }
.recent-chip {
  width: 22px;
  height: 22px;
  line-height: 22px;
  text-align: center;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 800;
}
.recent-chip.w { background: rgba(34, 197, 94, 0.18); color: #22c55e; }
.recent-chip.l { background: rgba(239, 68, 68, 0.18); color: #ef4444; }
.muted { color: #6e7681; }
</style>
