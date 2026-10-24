<template>
  <div class="page">
    <!-- 顶部导航 -->
    <div class="top-bar">
      <div class="logo">
        <span class="logo-icon">CS2</span>
        <span class="logo-text">职业选手库</span>
      </div>
      <div class="top-right">
        <el-input v-model="keywords" placeholder="搜索选手..." class="search" @keyup.enter="searchPlayer" clearable>
          <i slot="prefix" class="el-icon-search"></i>
        </el-input>
        <el-button type="primary" round @click="openAddDialog">+ 添加</el-button>
      </div>
    </div>

    <!-- 筛选 -->
    <div class="filter-bar">
      <span class="filter-item" :class="{ active: filterPosition === '' }" @click="filterPosition = ''">全部</span>
      <span class="filter-item" :class="{ active: filterPosition === '狙击手' }" @click="filterPosition = '狙击手'">狙击手</span>
      <span class="filter-item" :class="{ active: filterPosition === '步枪手' }" @click="filterPosition = '步枪手'">步枪手</span>
      <span class="filter-item" :class="{ active: filterPosition === '指挥' }" @click="filterPosition = '指挥'">指挥</span>
      <span class="filter-item" :class="{ active: filterPosition === '突破手' }" @click="filterPosition = '突破手'">突破手</span>
      <span class="filter-item" :class="{ active: filterPosition === '自由人' }" @click="filterPosition = '自由人'">自由人</span>
    </div>

    <!-- 战队分组 -->
    <div v-for="(players, teamName) in groupedPlayers" :key="teamName" class="team-section">
      <div class="team-header">
        <span class="team-dot"></span>
        <span class="team-name">{{ teamName }}</span>
        <span class="team-count">{{ players.length }}</span>
      </div>
      <div class="player-grid">
        <div v-for="player in players" :key="player.id" class="player-card" @click="openEditDialog(player)">
          <div class="card-img">
            <img v-if="player.userface" :src="player.userface" :alt="player.name" />
            <div v-else class="img-placeholder">{{ player.name.charAt(0) }}</div>
            <span class="pos-tag" :class="getPositionClass(player.specialty)">{{ player.specialty }}</span>
          </div>
          <div class="card-body">
            <div class="p-name">{{ player.name }}</div>
            <div class="p-real">{{ player.nativePlace }}</div>
          </div>
        </div>
      </div>
    </div>

    <el-empty v-if="playerList.length === 0" description="没有找到选手"></el-empty>

    <!-- 编辑弹窗 -->
    <el-dialog title="选手信息" v-model="editDialogVisible" width="480px" append-to-body>
      <el-form :model="currentPlayer" label-width="80px" size="small">
        <el-form-item label="选手ID"><el-input v-model="currentPlayer.name"></el-input></el-form-item>
        <el-form-item label="真实姓名"><el-input v-model="currentPlayer.nativePlace"></el-input></el-form-item>
        <el-form-item label="位置">
          <el-select v-model="currentPlayer.specialty" style="width: 100%">
            <el-option label="狙击手" value="狙击手"></el-option>
            <el-option label="步枪手" value="步枪手"></el-option>
            <el-option label="指挥" value="指挥"></el-option>
            <el-option label="突破手" value="突破手"></el-option>
            <el-option label="自由人" value="自由人"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属战队">
          <el-select v-model="currentPlayer.departmentId" style="width: 100%">
            <el-option v-for="team in teamList" :key="team.id" :label="team.name" :value="team.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="头像地址"><el-input v-model="currentPlayer.userface"></el-input></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="deletePlayer" v-if="currentPlayer.id">删除</el-button>
        <el-button type="primary" @click="savePlayer">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'EmpBasic',
  data() {
    return {
      keywords: '',
      filterPosition: '',
      playerList: [],
      editDialogVisible: false,
      currentPlayer: {},
      teamList: [
        { id: 1, name: 'Vitality' },
        { id: 2, name: 'MOUZ' },
        { id: 3, name: 'Team Spirit' },
        { id: 4, name: 'Aurora' },
        { id: 5, name: 'FURIA' },
        { id: 6, name: 'Falcons' },
        { id: 7, name: 'Astralis' },
        { id: 8, name: 'G2 Esports' },
        { id: 9, name: 'The MongolZ' },
        { id: 10, name: 'NAVI' },
        { id: 11, name: 'Team Liquid' },
        { id: 12, name: 'Fnatic' },
        { id: 13, name: 'Legacy' },
        { id: 14, name: 'TYLOO' },
        { id: 15, name: 'Lynn Vision' },
        { id: 16, name: 'FaZe Clan' },
        { id: 17, name: 'paiN' }
      ]
    }
  },
  computed: {
    filteredPlayers() {
      let list = this.playerList
      if (this.filterPosition) {
        list = list.filter(p => p.specialty === this.filterPosition)
      }
      return list
    },
    groupedPlayers() {
      const groups = {}
      this.filteredPlayers.forEach(p => {
        const team = p.department ? p.department.name : '未分配战队'
        if (!groups[team]) groups[team] = []
        groups[team].push(p)
      })
      return groups
    }
  },
  mounted() {
    this.loadPlayers()
  },
  methods: {
    loadPlayers() {
      this.getRequest('/employee/basic/?page=1&size=200').then(resp => {
        if (resp && resp.data) this.playerList = resp.data
      })
    },
    searchPlayer() {
      this.getRequest('/employee/basic/?page=1&size=200&name=' + this.keywords).then(resp => {
        if (resp && resp.data) this.playerList = resp.data
      })
    },
    openEditDialog(p) {
      this.currentPlayer = JSON.parse(JSON.stringify(p))
      this.editDialogVisible = true
    },
    openAddDialog() {
      this.currentPlayer = { name: '', nativePlace: '', specialty: '步枪手', departmentId: null, userface: '' }
      this.editDialogVisible = true
    },
    savePlayer() {
      if (this.currentPlayer.id) {
        this.putRequest('/employee/basic/', this.currentPlayer).then(resp => {
          if (resp) {
            this.$message.success('修改成功')
            this.editDialogVisible = false
            this.loadPlayers()
          }
        })
      } else {
        this.postRequest('/employee/basic/', this.currentPlayer).then(resp => {
          if (resp) {
            this.$message.success('添加成功')
            this.editDialogVisible = false
            this.loadPlayers()
          }
        })
      }
    },
    deletePlayer() {
      this.$confirm('确定要删除这个选手吗？', '提示', { type: 'warning' }).then(() => {
        this.deleteRequest('/employee/basic/' + this.currentPlayer.id).then(resp => {
          this.$message.success('删除成功')
          this.editDialogVisible = false
          this.loadPlayers()
        })
      })
    },
    getPositionClass(pos) {
      const map = { '狙击手':'pos-awp', '步枪手':'pos-rifle', '指挥':'pos-igl', '突破手':'pos-entry', '自由人':'pos-lurker' }
      return map[pos] || 'pos-default'
    }
  }
}
</script>

<style scoped>
.page {
  background: transparent;
  min-height: calc(100vh - 100px);
  padding: 20px;
  color: #fff;
}

/* 顶部导航 */
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #2d2d2d;
}
.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}
.logo-icon {
  font-size: 20px;
  font-weight: 800;
  color: #ff4757;
}
.logo-text {
  font-size: 18px;
  font-weight: 600;
}
.top-right {
  display: flex;
  gap: 12px;
  align-items: center;
}
.search {
  width: 240px;
}
.search :deep(.el-input__inner) {
  background: rgba(255, 255, 255, 0.9) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  border-radius: 8px;
  color: #000 !important;
}
.search :deep(.el-input__inner::placeholder) {
  color: #999;
}

/* 筛选 */
.filter-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
}
.filter-item {
  padding: 6px 16px;
  border-radius: 6px;
  background: #2d2d2d;
  color: #999;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}
.filter-item:hover {
  background: #3d3d3d;
  color: #fff;
}
.filter-item.active {
  background: #ff4757;
  color: #fff;
}

/* 战队分组 */
.team-section {
  margin-bottom: 32px;
}
.team-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}
.team-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ff4757;
}
.team-name {
  font-size: 16px;
  font-weight: 600;
}
.team-count {
  font-size: 12px;
  color: #666;
}

/* 选手卡片 */
.player-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 12px;
}
.player-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.2s;
}
.player-card:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}
.card-img {
  position: relative;
  width: 100%;
  aspect-ratio: 3/4;
  background: rgba(255, 255, 255, 0.05);
}
.card-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.img-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  font-weight: 700;
  color: #666;
}
.pos-tag {
  position: absolute;
  top: 8px;
  right: 8px;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
}
.pos-awp { background: #ef4444; }
.pos-rifle { background: #3b82f6; }
.pos-igl { background: #eab308; color: #000; }
.pos-entry { background: #22c55e; }
.pos-lurker { background: #a855f7; }
.pos-default { background: #666; }
.card-body {
  padding: 10px 12px;
}
.p-name {
  font-size: 14px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 2px;
}
.p-real {
  font-size: 12px;
  color: #999;
}
</style>
