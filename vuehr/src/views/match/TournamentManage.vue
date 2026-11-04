<template>
    <div class="page">
        <div class="toolbar">
            <el-input v-model="keyword" placeholder="搜索赛事名称 / 赛区" clearable style="width: 250px; margin-right: 10px;"></el-input>
            <el-button type="success" @click="openAdd">+ 新增赛事</el-button>
            <el-button @click="load">刷新</el-button>
            <span class="total-tip">共 {{ filteredList.length }} 项赛事</span>
        </div>

        <el-table :data="filteredList" v-loading="loading" class="tournament-table" stripe>
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="name" label="赛事名称" min-width="200"></el-table-column>
            <el-table-column prop="region" label="赛区" width="110">
                <template #default="scope">
                    <el-tag v-if="scope.row.region" size="small" effect="dark">{{ scope.row.region }}</el-tag>
                    <span v-else class="muted">-</span>
                </template>
            </el-table-column>
            <el-table-column label="赛程" width="200">
                <template #default="scope">
                    <span v-if="scope.row.startDate || scope.row.endDate">{{ scope.row.startDate || '?' }} ~ {{ scope.row.endDate || '?' }}</span>
                    <span v-else class="muted">待定</span>
                </template>
            </el-table-column>
            <el-table-column label="比赛场次" width="100" sortable :sort-method="(a, b) => (a.matchCount || 0) - (b.matchCount || 0)">
                <template #default="scope">
                    <span class="count">{{ scope.row.matchCount || 0 }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="170">
                <template #default="scope">
                    <el-button size="small" type="primary" @click="openEdit(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="remove(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-empty v-if="!loading && !filteredList.length" description="暂无赛事记录"></el-empty>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="460px" append-to-body
            :close-on-click-modal="false">
            <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
                <el-form-item label="赛事名称" prop="name">
                    <el-input v-model="form.name" placeholder="如：ESL Pro League S24" maxlength="128"></el-input>
                </el-form-item>
                <el-form-item label="赛区" prop="region">
                    <el-select v-model="form.region" allow-create filterable clearable placeholder="如：欧洲" style="width: 100%">
                        <el-option v-for="r in regionOptions" :key="r" :label="r" :value="r"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="赛程" prop="dateRange">
                    <el-date-picker v-model="form.dateRange" type="daterange" range-separator="至"
                        start-placeholder="开始日期" end-placeholder="结束日期"
                        format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%"></el-date-picker>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="submit">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: 'TournamentManage',
    data() {
        return {
            list: [],
            keyword: '',
            loading: false,
            submitting: false,
            dialogVisible: false,
            dialogTitle: '',
            form: { id: null, name: '', region: '', dateRange: null },
            // 与战队管理保持一致：赛区可选可填
            baseRegions: ['欧洲', '独联体', '美洲', '亚洲', '中东', '土耳其'],
            rules: {
                name: [{ required: true, message: '请填写赛事名称', trigger: 'blur' }]
            }
        }
    },
    computed: {
        filteredList() {
            const kw = this.keyword.trim().toLowerCase()
            if (!kw) return this.list
            return this.list.filter(t =>
                (t.name || '').toLowerCase().includes(kw) || (t.region || '').toLowerCase().includes(kw))
        },
        regionOptions() {
            return [...new Set([...this.baseRegions, ...this.list.map(t => t.region).filter(Boolean)])]
        }
    },
    mounted() { this.load() },
    methods: {
        load() {
            this.loading = true
            this.getRequest('/esports/tournament-options').then(resp => {
                if (resp) this.list = resp
            }).finally(() => { this.loading = false })
        },
        emptyForm() {
            return { id: null, name: '', region: '', dateRange: null }
        },
        openAdd() {
            this.dialogTitle = '新增赛事'
            this.form = this.emptyForm()
            this.dialogVisible = true
            this.resetValidate()
        },
        openEdit(row) {
            this.dialogTitle = '编辑赛事'
            //后端日期为 yyyy-MM-dd 字符串或 null，daterange 控件用数组绑定
            const range = (row.startDate || row.endDate)
                ? [row.startDate || null, row.endDate || null]
                : null
            this.form = { id: row.id, name: row.name, region: row.region || '', dateRange: range }
            this.dialogVisible = true
            this.resetValidate()
        },
        resetValidate() {
            this.$nextTick(() => {
                if (this.$refs.formRef) this.$refs.formRef.clearValidate()
            })
        },
        submit() {
            if (this.submitting) return
            this.$refs.formRef.validate(valid => {
                if (!valid) return
                this.submitting = true
                const [startDate, endDate] = this.form.dateRange || [null, null]
                const payload = {
                    id: this.form.id,
                    name: this.form.name.trim(),
                    region: this.form.region,
                    startDate: startDate,
                    endDate: endDate
                }
                const req = payload.id
                    ? this.putRequest('/esports/tournament', payload)
                    : this.postRequest('/esports/tournament', payload)
                req.then(resp => {
                    if (resp) {
                        this.dialogVisible = false
                        this.load()
                    }
                }).finally(() => { this.submitting = false })
            })
        },
        remove(row) {
            this.$confirm(`确认删除赛事「${row.name}」吗？`, '提示', { type: 'warning' }).then(() => {
                this.deleteRequest('/esports/tournament/' + row.id).then(resp => {
                    if (resp) this.load()
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
    margin-bottom: 15px;
    display: flex;
    align-items: center;
}
.total-tip {
    margin-left: auto;
    font-size: 12px;
    color: #8b949e;
}
.count {
    font-weight: 700;
    color: #ff4655;
    font-variant-numeric: tabular-nums;
}
.muted { color: #6e7681; }
.tournament-table :deep(.el-table__cell) {
    background: transparent !important;
    color: #e8e8e8 !important;
    border-bottom: 1px solid rgba(255, 255, 255, 0.08) !important;
}
.tournament-table :deep(.el-table__header th) {
    background: rgba(255, 255, 255, 0.1) !important;
    color: #fff !important;
}
</style>
