<template>
    <div class="page">
        <div class="toolbar">
            <el-input v-model="keyword" placeholder="搜索战队 / 赛事" clearable style="width: 210px"></el-input>
            <el-select v-model="filterStatus" placeholder="全部状态" clearable style="width: 130px">
                <el-option v-for="(meta, key) in statusMeta" :key="key" :label="meta.label" :value="key"></el-option>
            </el-select>
            <el-select v-model="filterTournament" placeholder="全部赛事" clearable style="width: 190px">
                <el-option v-for="t in tournamentNames" :key="t" :label="t" :value="t"></el-option>
            </el-select>
            <el-button type="success" @click="openAdd">+ 新增比赛</el-button>
            <el-button @click="loadMatches">刷新</el-button>
            <span class="total-tip">共 {{ filteredList.length }} 场</span>
        </div>

        <el-table :data="filteredList" v-loading="loading" class="match-table" stripe>
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="tournament" label="赛事" min-width="150"></el-table-column>
            <el-table-column prop="stage" label="阶段" width="90"></el-table-column>
            <el-table-column label="赛制" width="70">
                <template #default="scope">BO{{ scope.row.bo }}</template>
            </el-table-column>
            <el-table-column label="对阵" min-width="220">
                <template #default="scope">
                    <span class="team-name">{{ teamName(scope.row.team1Id) }}</span>
                    <span class="vs">vs</span>
                    <span class="team-name">{{ teamName(scope.row.team2Id) }}</span>
                </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
                <template #default="scope">
                    <el-tag :type="tagType(scope.row.status)" effect="dark" size="small">
                        {{ statusLabel(scope.row.status) }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="开始时间" width="150">
                <template #default="scope">{{ formatTime(scope.row.startTime) }}</template>
            </el-table-column>
            <el-table-column label="比分" width="90">
                <template #default="scope">
                    <span v-if="scope.row.maps && scope.row.maps.length" class="score">{{ seriesText(scope.row) }}</span>
                    <span v-else class="muted">-</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="170">
                <template #default="scope">
                    <el-button size="small" type="primary" @click="openEdit(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="remove(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-empty v-if="!loading && !filteredList.length" description="没有符合条件的比赛"></el-empty>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="720px" append-to-body top="5vh"
            :close-on-click-modal="false" class="match-manage-dialog">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="赛事" prop="tournamentId">
                            <el-select v-model="form.tournamentId" placeholder="请选择赛事" style="width: 100%">
                                <el-option v-for="t in tournamentOptions" :key="t.id" :label="t.name" :value="t.id"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="阶段" prop="stage">
                            <el-input v-model="form.stage" placeholder="如：小组赛 / 淘汰赛 / 决赛"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="赛制" prop="bo">
                            <el-select v-model="form.bo" style="width: 100%">
                                <el-option v-for="b in boOptions" :key="b" :label="'BO' + b" :value="b"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态" prop="status">
                            <el-select v-model="form.status" style="width: 100%">
                                <el-option v-for="(meta, key) in statusMeta" :key="key" :label="meta.label" :value="key"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="战队 1" prop="team1Id">
                            <el-select v-model="form.team1Id" placeholder="请选择战队" filterable style="width: 100%">
                                <el-option v-for="t in teams" :key="t.id" :label="t.name" :value="t.id"
                                    :disabled="t.id === form.team2Id"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="战队 2" prop="team2Id">
                            <el-select v-model="form.team2Id" placeholder="请选择战队" filterable style="width: 100%">
                                <el-option v-for="t in teams" :key="t.id" :label="t.name" :value="t.id"
                                    :disabled="t.id === form.team1Id"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="开始时间" prop="startTime">
                            <el-date-picker v-model="form.startTime" type="datetime" placeholder="请选择开始时间"
                                format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DDTHH:mm:ss" style="width: 100%">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>

                <div class="map-editor">
                    <div class="map-editor-head">
                        <span class="section-title">逐图比分（最多 {{ form.bo }} 张图，可以不填）</span>
                        <el-button size="small" @click="addMapRow" :disabled="form.maps.length >= form.bo">+ 添加一张</el-button>
                    </div>
                    <div v-if="!form.maps.length" class="empty-tip">暂未录入逐图比分</div>
                    <div v-for="(mp, index) in form.maps" :key="index" class="map-row">
                        <span class="map-order">图 {{ index + 1 }}</span>
                        <el-select v-model="mp.name" placeholder="地图" style="width: 130px">
                            <el-option v-for="m in mapOptions" :key="m" :label="m" :value="m"></el-option>
                        </el-select>
                        <el-input-number v-model="mp.score1" :min="0" :max="30" controls-position="right"
                            style="width: 110px"></el-input-number>
                        <span class="colon">:</span>
                        <el-input-number v-model="mp.score2" :min="0" :max="30" controls-position="right"
                            style="width: 110px"></el-input-number>
                        <el-checkbox v-model="mp.live" :disabled="form.status !== 'live'">进行中</el-checkbox>
                        <el-button size="small" type="danger" link @click="form.maps.splice(index, 1)">移除</el-button>
                    </div>
                </div>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submit">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { MATCH_STATUS, STATUS_META, CS_MAPS } from '@/mock/esports'

const STATUS_TAG = {
    live: 'danger',
    upcoming: 'warning',
    finished: 'success'
}

export default {
    name: 'MatchManage',
    data() {
        return {
            list: [],
            teams: [],
            tournamentOptions: [],
            loading: false,
            submitting: false,
            keyword: '',
            filterStatus: '',
            filterTournament: '',
            dialogVisible: false,
            dialogTitle: '',
            boOptions: [1, 3, 5],
            mapOptions: CS_MAPS,
            // 初始值与 emptyForm() 保持一致；data() 里不调用 methods，避免初始化顺序依赖
            form: {
                id: null, tournamentId: null, stage: '小组赛', bo: 3,
                team1Id: null, team2Id: null, status: MATCH_STATUS.UPCOMING,
                startTime: '', maps: []
            },
            rules: {
                tournamentId: [{ required: true, message: '请选择赛事', trigger: 'change' }],
                stage: [{ required: true, message: '请填写比赛阶段', trigger: 'blur' }],
                bo: [{ required: true, message: '请选择赛制', trigger: 'change' }],
                status: [{ required: true, message: '请选择状态', trigger: 'change' }],
                team1Id: [{ required: true, message: '请选择战队 1', trigger: 'change' }],
                team2Id: [{ required: true, message: '请选择战队 2', trigger: 'change' }],
                startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }]
            }
        }
    },
    computed: {
        statusMeta() { return STATUS_META },
        tournamentNames() {
            return [...new Set(this.list.map(m => m.tournament).filter(Boolean))]
        },
        teamMap() {
            return new Map(this.teams.map(t => [t.id, t]))
        },
        // 与后端一致：按开始时间倒序，未开始的排在前面由状态筛选控制
        filteredList() {
            const kw = this.keyword.trim().toLowerCase()
            return this.list
                .filter(m => {
                    if (this.filterStatus && m.status !== this.filterStatus) return false
                    if (this.filterTournament && m.tournament !== this.filterTournament) return false
                    if (!kw) return true
                    const hay = `${this.teamName(m.team1Id)} ${this.teamName(m.team2Id)} ${m.tournament}`.toLowerCase()
                    return hay.includes(kw)
                })
                .slice()
                .sort((a, b) => new Date(b.startTime) - new Date(a.startTime))
        }
    },
    mounted() { this.loadAll() },
    watch: {
        // 赛制改小后裁掉多余的逐图行，避免提交时超出 BO 数量
        'form.bo'(val) {
            if (this.form.maps.length > val) {
                this.form.maps = this.form.maps.slice(0, val)
            }
        }
    },
    methods: {
        emptyForm() {
            return {
                id: null, tournamentId: null, stage: '小组赛', bo: 3,
                team1Id: null, team2Id: null, status: MATCH_STATUS.UPCOMING,
                startTime: '', maps: []
            }
        },
        loadAll() {
            this.loadMatches()
            this.loadTeams()
            this.loadTournaments()
        },
        loadMatches() {
            this.loading = true
            this.getRequest('/esports/matches').then(resp => {
                if (resp) this.list = resp
            }).finally(() => { this.loading = false })
        },
        loadTeams() {
            this.getRequest('/esports/teams').then(resp => { if (resp) this.teams = resp })
        },
        loadTournaments() {
            this.getRequest('/esports/tournament-options').then(resp => { if (resp) this.tournamentOptions = resp })
        },
        teamName(id) {
            const t = this.teamMap.get(id)
            return t ? t.name : '未知战队'
        },
        statusLabel(status) {
            return STATUS_META[status] ? STATUS_META[status].label : status
        },
        tagType(status) {
            return STATUS_TAG[status] || 'info'
        },
        pad(n) { return n < 10 ? `0${n}` : `${n}` },
        formatTime(t) {
            if (!t) return '-'
            const d = new Date(String(t).replace(' ', 'T'))
            if (isNaN(d.getTime())) return t
            return `${d.getFullYear()}-${this.pad(d.getMonth() + 1)}-${this.pad(d.getDate())} ${this.pad(d.getHours())}:${this.pad(d.getMinutes())}`
        },
        // 与列表页口径一致：逐图比分推导系列赛比分，进行中的图不计入
        seriesText(row) {
            let s1 = 0
            let s2 = 0
            row.maps.forEach(mp => {
                if (mp.live) return
                if (mp.score1 > mp.score2) s1++
                else if (mp.score2 > mp.score1) s2++
            })
            return `${s1} : ${s2}`
        },
        openAdd() {
            this.dialogTitle = '新增比赛'
            this.form = this.emptyForm()
            this.dialogVisible = true
            this.resetValidate()
        },
        openEdit(row) {
            this.dialogTitle = '编辑比赛'
            this.form = {
                id: row.id,
                tournamentId: this.tournamentIdOf(row.tournament),
                stage: row.stage,
                bo: row.bo,
                team1Id: row.team1Id,
                team2Id: row.team2Id,
                status: row.status,
                startTime: row.startTime,
                maps: (row.maps || []).map(mp => ({
                    name: mp.name, score1: mp.score1, score2: mp.score2, live: !!mp.live
                }))
            }
            this.dialogVisible = true
            this.resetValidate()
        },
        // 比赛列表只带赛事名，编辑时按名称反查赛事 id
        tournamentIdOf(name) {
            const t = this.tournamentOptions.find(x => x.name === name)
            return t ? t.id : null
        },
        resetValidate() {
            this.$nextTick(() => {
                if (this.$refs.formRef) this.$refs.formRef.clearValidate()
            })
        },
        addMapRow() {
            if (this.form.maps.length >= this.form.bo) return
            // 已录入的地图不再重复预选
            const used = new Set(this.form.maps.map(mp => mp.name))
            const next = this.mapOptions.find(m => !used.has(m)) || this.mapOptions[0]
            this.form.maps.push({ name: next, score1: 0, score2: 0, live: false })
        },
        submit() {
            if (this.submitting) return
            this.$refs.formRef.validate(valid => {
                if (!valid) return
                if (this.form.team1Id === this.form.team2Id) {
                    this.$message.error('对阵双方不能是同一支战队')
                    return
                }
                this.submitting = true
                const payload = {
                    ...this.form,
                    maps: this.form.maps.map(mp => ({
                        name: mp.name,
                        score1: Number(mp.score1) || 0,
                        score2: Number(mp.score2) || 0,
                        live: this.form.status === 'live' && !!mp.live
                    }))
                }
                const req = payload.id
                    ? this.putRequest('/esports/match', payload)
                    : this.postRequest('/esports/match', payload)
                req.then(resp => {
                    if (resp) {
                        this.dialogVisible = false
                        this.loadMatches()
                    }
                }).finally(() => { this.submitting = false })
            })
        },
        remove(row) {
            const label = `${this.teamName(row.team1Id)} vs ${this.teamName(row.team2Id)}`
            this.$confirm(`确认删除「${label}」这场比赛及其逐图比分吗？`, '提示', { type: 'warning' }).then(() => {
                this.deleteRequest('/esports/match/' + row.id).then(resp => {
                    if (resp) this.loadMatches()
                })
            }).catch(() => {})
        }
    }
}
</script>

<style scoped>
.page {
    background: rgba(0, 0, 0, 0.3) !important;
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    border-radius: 12px;
    padding: 20px;
    color: #fff;
}
.toolbar {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 15px;
}
.total-tip {
    margin-left: auto;
    font-size: 12px;
    color: #8b949e;
}
.team-name { font-weight: 600; }
.vs {
    margin: 0 8px;
    font-size: 12px;
    color: #8b949e;
}
.score {
    font-weight: 700;
    color: #ff4655;
    font-variant-numeric: tabular-nums;
}
.muted { color: #6e7681; }

.match-table :deep(.el-table),
.match-table :deep(.el-table__inner-wrapper),
.match-table :deep(.el-table__cell) {
    background: transparent !important;
    color: #fff !important;
}
.match-table :deep(.el-table__header th) {
    background: rgba(255, 255, 255, 0.1) !important;
    color: #fff !important;
}
.match-table :deep(.el-table__cell) {
    border-bottom: 1px solid rgba(255, 255, 255, 0.08) !important;
}

.map-editor {
    margin-top: 4px;
    padding: 14px 16px;
    border: 1px solid #232b36;
    border-radius: 10px;
    background: rgba(255, 255, 255, 0.03);
}
.map-editor-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
}
.section-title {
    font-size: 13px;
    font-weight: 600;
    color: #e6edf3;
}
.empty-tip {
    font-size: 12px;
    color: #6e7681;
}
.map-row {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
}
.map-row:last-child { margin-bottom: 0; }
.map-order {
    width: 40px;
    font-size: 12px;
    color: #8b949e;
}
.colon {
    color: #8b949e;
    font-weight: 700;
}
</style>

<style>
/* 弹窗挂到 body 上，内容过高时正文可滚动，底部按钮始终可见 */
.match-manage-dialog .el-dialog__body {
    max-height: 66vh;
    overflow-y: auto;
}
</style>
