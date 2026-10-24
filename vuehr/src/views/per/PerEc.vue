<template>
    <div class="page">
        <div class="toolbar">
            <el-input v-model="searchName" placeholder="搜索选手姓名" style="width: 250px; margin-right: 10px;" clearable @keyup.enter="initEc"></el-input>
            <el-button type="primary" @click="initEc">搜索</el-button>
            <el-button type="success" @click="showAddView" style="margin-left: 10px;">+ 添加奖惩</el-button>
        </div>

        <el-table :data="ecList" v-loading="loading" class="ec-table">
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="empName" label="选手"></el-table-column>
            <el-table-column label="类型" width="80">
                <template #default="scope">
                    <el-tag :type="scope.row.ecType === 1 ? 'success' : 'danger'">
                        {{ scope.row.ecType === 1 ? '奖励' : '惩罚' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="日期" width="110">
                <template #default="scope">{{ formatDate(scope.row.ecDate) }}</template>
            </el-table-column>
            <el-table-column prop="ecReason" label="原因">
                <template #default="scope">{{ scope.row.ecReason || '-' }}</template>
            </el-table-column>
            <el-table-column prop="ecPoint" label="分值" width="70">
                <template #default="scope">{{ scope.row.ecPoint ?? '-' }}</template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" width="220" show-overflow-tooltip>
                <template #default="scope">{{ scope.row.remark || '-' }}</template>
            </el-table-column>
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button size="small" type="primary" @click="showEditView(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="deleteEc(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination style="margin-top: 15px; justify-content: flex-end;" background layout="total, prev, pager, next"
            :total="total" :page-size="size" :current-page="page" @current-change="pageChange">
        </el-pagination>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" append-to-body top="6vh"
            :close-on-click-modal="false" class="ec-dialog">
            <el-form ref="ecFormRef" :model="ecForm" :rules="rules" label-width="80px">
                <el-form-item label="选手" prop="eid">
                    <el-select v-model="ecForm.eid" placeholder="请选择选手" style="width: 100%">
                        <el-option v-for="emp in empList" :key="emp.id" :label="emp.name" :value="emp.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="类型" prop="ecType">
                    <el-radio-group v-model="ecForm.ecType">
                        <el-radio :value="1">奖励</el-radio>
                        <el-radio :value="2">惩罚</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="日期" prop="ecDate">
                    <el-date-picker v-model="ecForm.ecDate" type="date" value-format="YYYY-MM-DD"
                        placeholder="请选择日期" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item label="原因" prop="ecReason">
                    <el-input v-model="ecForm.ecReason" type="textarea" :rows="2" maxlength="200" show-word-limit></el-input>
                </el-form-item>
                <el-form-item label="分值">
                    <el-input-number v-model="ecForm.ecPoint" :min="-100" :max="100"></el-input-number>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="ecForm.remark" type="textarea" :rows="2"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitForm">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: "PerEc",
    data() {
        return {
            ecList: [], empList: [], searchName: '',
            page: 1, size: 10, total: 0, loading: false,
            dialogVisible: false, dialogTitle: '',
            ecForm: { id: null, eid: null, ecDate: null, ecReason: '', ecPoint: 0, ecType: 1, remark: '' },
            submitting: false,
            rules: {
                eid: [{ required: true, message: '请选择选手', trigger: 'change' }],
                ecType: [{ required: true, message: '请选择类型', trigger: 'change' }],
                ecDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
                ecReason: [{ required: true, message: '请填写原因', trigger: 'blur' }]
            }
        }
    },
    mounted() { this.initEc(); this.initEmps(); },
    methods: {
        initEc() {
            this.loading = true;
            this.getRequest('/personnel/ec/?page=' + this.page + '&size=' + this.size + '&name=' + this.searchName).then(resp => {
                if (resp) { this.ecList = resp.data; this.total = resp.total; }
                this.loading = false;
            });
        },
        initEmps() { this.getRequest('/personnel/ec/emps').then(resp => { if (resp) this.empList = resp; }); },
        pageChange(p) { this.page = p; this.initEc(); },
        showAddView() {
            this.dialogTitle = '添加奖惩记录';
            this.ecForm = { id: null, eid: null, ecDate: this.today(), ecReason: '', ecPoint: 0, ecType: 1, remark: '' };
            this.dialogVisible = true;
            this.resetValidate();
        },
        showEditView(row) {
            this.dialogTitle = '编辑奖惩记录';
            this.ecForm = { id: row.id, eid: row.eid, ecDate: row.ecDate, ecReason: row.ecReason, ecPoint: row.ecPoint, ecType: row.ecType, remark: row.remark };
            this.dialogVisible = true;
            this.resetValidate();
        },
        today() {
            const d = new Date();
            const m = String(d.getMonth() + 1).padStart(2, '0');
            const day = String(d.getDate()).padStart(2, '0');
            return `${d.getFullYear()}-${m}-${day}`;
        },
        formatDate(value) {
            if (!value) return '';
            const str = String(value);
            if (/^\d{4}-\d{2}-\d{2}$/.test(str)) return str;
            const d = new Date(str);
            if (isNaN(d.getTime())) return str.substring(0, 10);
            const m = String(d.getMonth() + 1).padStart(2, '0');
            const day = String(d.getDate()).padStart(2, '0');
            return `${d.getFullYear()}-${m}-${day}`;
        },
        resetValidate() {
            this.$nextTick(() => {
                if (this.$refs.ecFormRef) this.$refs.ecFormRef.clearValidate();
            });
        },
        submitForm() {
            if (this.submitting) return;
            this.$refs.ecFormRef.validate(valid => {
                if (!valid) return;
                this.submitting = true;
                const req = this.ecForm.id
                    ? this.putRequest('/personnel/ec/', this.ecForm)
                    : this.postRequest('/personnel/ec/', this.ecForm);
                req.then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.dialogVisible = false; this.initEc(); }
                }).finally(() => { this.submitting = false; });
            });
        },
        deleteEc(row) {
            this.$confirm('确认删除吗？', '提示', { type: 'warning' }).then(() => {
                this.deleteRequest('/personnel/ec/' + row.id).then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.initEc(); }
                });
            }).catch(() => {});
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
}
.ec-table :deep(.el-table) {
    background: transparent !important;
    color: #fff !important;
}
.ec-table :deep(.el-table__inner-wrapper) {
    background: transparent !important;
}
.ec-table :deep(.el-table__cell) {
    background: transparent !important;
    color: #fff !important;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1) !important;
}
.ec-table :deep(.el-table__header-wrapper) {
    background: transparent !important;
}
.ec-table :deep(.el-table__header th) {
    background: rgba(255, 255, 255, 0.1) !important;
    color: #fff !important;
}
</style>

<style>
/* 弹窗通过 append-to-body 挂到 body 上，避免被页面容器的 backdrop-filter / overflow 裁剪；
   内容过高时弹窗体可滚动，保证底部「取消 / 确定」按钮始终可见 */
.ec-dialog .el-dialog__body {
    max-height: 62vh;
    overflow-y: auto;
}
</style>
