<template>
    <div class="page">
        <div class="toolbar">
            <el-input v-model="keyword" placeholder="搜索战队名称" clearable style="width: 250px; margin-right: 10px;"></el-input>
            <el-button type="success" @click="openAdd">+ 添加战队</el-button>
        </div>

        <el-table :data="filteredList" v-loading="loading" class="team-table" stripe>
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column label="队标" width="70">
                <template #default="scope">
                    <span class="badge" :style="badgeStyle(scope.row)">{{ scope.row.shortName || '??' }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="战队名称"></el-table-column>
            <el-table-column prop="region" label="赛区" width="110"></el-table-column>
            <el-table-column prop="worldRank" label="世界排名" width="100" sortable></el-table-column>
            <el-table-column label="主题色" width="120">
                <template #default="scope">
                    <span class="dot" :style="{ background: scope.row.color && scope.row.color[0] }"></span>
                    <span class="dot" :style="{ background: scope.row.color && scope.row.color[1] }"></span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button size="small" type="primary" @click="openEdit(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="del(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="460px" append-to-body>
            <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
                <el-form-item label="战队名称" prop="name">
                    <el-input v-model="form.name" placeholder="如：Vitality"></el-input>
                </el-form-item>
                <el-form-item label="简称" prop="shortName">
                    <el-input v-model="form.shortName" placeholder="如：VIT" maxlength="6"></el-input>
                </el-form-item>
                <el-form-item label="赛区" prop="region">
                    <el-select v-model="form.region" allow-create filterable style="width: 100%">
                        <el-option v-for="r in regionOptions" :key="r" :label="r" :value="r"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="世界排名" prop="worldRank">
                    <el-input-number v-model="form.worldRank" :min="1" :max="500"></el-input-number>
                </el-form-item>
                <el-form-item label="主题色">
                    <el-color-picker v-model="form.color1"></el-color-picker>
                    <el-color-picker v-model="form.color2" style="margin-left: 8px"></el-color-picker>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submit">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "SysTeam",
    data() {
        return {
            list: [], keyword: '', loading: false,
            dialogVisible: false, dialogTitle: '',
            form: {},
            rules: {
                name: [{ required: true, message: '请填写战队名称', trigger: 'blur' }],
                shortName: [{ required: true, message: '请填写简称', trigger: 'blur' }]
            }
        }
    },
    computed: {
        filteredList() {
            const k = this.keyword.trim().toLowerCase()
            if (!k) return this.list
            return this.list.filter(t => (t.name || '').toLowerCase().includes(k) || (t.shortName || '').toLowerCase().includes(k))
        },
        regionOptions() {
            return [...new Set(this.list.map(t => t.region).filter(Boolean))]
        }
    },
    mounted() { this.load() },
    methods: {
        load() {
            this.loading = true
            this.getRequest('/esports/teams').then(resp => {
                if (resp) this.list = resp
                this.loading = false
            }).catch(() => { this.loading = false })
        },
        badgeStyle(t) {
            const c = t.color && t.color.length === 2 ? t.color : ['#555', '#333']
            return { background: `linear-gradient(135deg, ${c[0]}, ${c[1]})` }
        },
        openAdd() {
            this.dialogTitle = '添加战队'
            this.form = { name: '', shortName: '', region: '欧洲', worldRank: 100, color1: '#ff4757', color2: '#2f3542' }
            this.dialogVisible = true
        },
        openEdit(row) {
            this.dialogTitle = '编辑战队'
            this.form = {
                id: row.id, name: row.name, shortName: row.shortName, region: row.region,
                worldRank: row.worldRank,
                color1: row.color && row.color[0], color2: row.color && row.color[1]
            }
            this.dialogVisible = true
        },
        submit() {
            this.$refs.formRef.validate(valid => {
                if (!valid) return
                const req = this.form.id
                    ? this.putRequest('/esports/team', this.form)
                    : this.postRequest('/esports/team', this.form)
                req.then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.dialogVisible = false; this.load() }
                })
            })
        },
        del(row) {
            this.$confirm(`确认删除战队「${row.name}」吗？`, '提示', { type: 'warning' }).then(() => {
                this.deleteRequest('/esports/team/' + row.id).then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.load() }
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
    border-radius: 12px;
    padding: 20px;
    color: #fff;
}
.toolbar {
    margin-bottom: 15px;
    display: flex;
    align-items: center;
}
.badge {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 42px;
    height: 42px;
    border-radius: 8px;
    color: #fff;
    font-weight: 700;
    font-size: 13px;
}
.dot {
    display: inline-block;
    width: 14px;
    height: 14px;
    border-radius: 50%;
    margin-right: 6px;
}
.team-table :deep(.el-table__cell) {
    background: transparent !important;
    color: #e8e8e8 !important;
    border-bottom: 1px solid rgba(255, 255, 255, 0.08) !important;
}
.team-table :deep(.el-table__header th) {
    background: rgba(255, 255, 255, 0.08) !important;
    color: #fff !important;
}
</style>
