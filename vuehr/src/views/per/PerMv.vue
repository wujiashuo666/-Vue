<template>
    <div>
        <div style="margin-bottom: 15px;">
            <el-input v-model="searchName" placeholder="请输入选手姓名搜索" style="width: 250px; margin-right: 10px;" clearable @keyup.enter="initMv"></el-input>
            <el-button type="primary" @click="initMv"><el-icon><Search/></el-icon> 搜索</el-button>
            <el-button type="success" @click="showAddView" style="margin-left: 10px;"><el-icon><Plus/></el-icon> 添加转会记录</el-button>
        </div>

        <el-table :data="mvList" border stripe v-loading="loading">
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="empName" label="选手姓名"></el-table-column>
            <el-table-column prop="beforeDepName" label="原有战队"></el-table-column>
            <el-table-column prop="afterDepName" label="调入战队"></el-table-column>
            <el-table-column prop="afterJobName" label="调入位置"></el-table-column>
            <el-table-column label="转会日期" width="110">
                <template #default="scope">{{ scope.row.removeDate ? scope.row.removeDate.substring(0,10) : '' }}</template>
            </el-table-column>
            <el-table-column prop="reason" label="转会原因"></el-table-column>
            <el-table-column prop="remark" label="备注"></el-table-column>
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button size="small" type="primary" @click="showEditView(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="deleteMv(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination style="margin-top: 15px; justify-content: flex-end;" background layout="total, prev, pager, next, sizes"
            :total="total" :page-size="size" :current-page="page" @current-change="pageChange" @size-change="sizeChange">
        </el-pagination>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" append-to-body>
            <el-form :model="mvForm" label-width="90px">
                <el-form-item label="选手">
                    <el-select v-model="mvForm.eid" placeholder="请选择选手" style="width: 100%">
                        <el-option v-for="emp in empList" :key="emp.id" :label="emp.name" :value="emp.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="原有战队">
                    <el-select v-model="mvForm.beforeDepId" placeholder="请选择原有战队" style="width: 100%">
                        <el-option v-for="dep in depList" :key="dep.id" :label="dep.name" :value="dep.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="调入战队">
                    <el-select v-model="mvForm.afterdepid" placeholder="请选择调入战队" style="width: 100%">
                        <el-option v-for="dep in depList" :key="dep.id" :label="dep.name" :value="dep.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="调入位置">
                    <el-select v-model="mvForm.afterjobid" placeholder="请选择位置" style="width: 100%">
                        <el-option v-for="j in jobList" :key="j.id" :label="j.name" :value="j.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="转会日期">
                    <el-date-picker v-model="mvForm.removedate" type="date" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item label="转会原因">
                    <el-input v-model="mvForm.reason" type="textarea" :rows="2"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="mvForm.remark" type="textarea" :rows="2"></el-input>
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
    name: "PerMv",
    data() {
        return {
            mvList: [], empList: [], depList: [], jobList: [], searchName: '',
            page: 1, size: 10, total: 0, loading: false,
            dialogVisible: false, dialogTitle: '',
            mvForm: { id: null, eid: null, beforeDepId: null, afterdepid: null, afterjobid: null, removedate: null, reason: '', remark: '' }
        }
    },
    mounted() { this.initMv(); this.initEmps(); this.initDeps(); this.initJobs(); },
    methods: {
        initMv() {
            this.loading = true;
            this.getRequest('/personnel/remove/?page=' + this.page + '&size=' + this.size + '&name=' + this.searchName).then(resp => {
                if (resp) { this.mvList = resp.data; this.total = resp.total; }
                this.loading = false;
            });
        },
        initEmps() { this.getRequest('/personnel/remove/emps').then(resp => { if (resp) this.empList = resp; }); },
        initDeps() { this.getRequest('/personnel/remove/deps').then(resp => { if (resp) this.depList = resp; }); },
        initJobs() { this.getRequest('/personnel/remove/joblevels').then(resp => { if (resp) this.jobList = resp; }); },
        pageChange(p) { this.page = p; this.initMv(); },
        sizeChange(s) { this.size = s; this.page = 1; this.initMv(); },
        showAddView() {
            this.dialogTitle = '添加转会记录';
            this.mvForm = { id: null, eid: null, beforeDepId: null, afterdepid: null, afterjobid: null, removedate: null, reason: '', remark: '' };
            this.dialogVisible = true;
        },
        showEditView(row) {
            this.dialogTitle = '编辑转会记录';
            this.mvForm = { id: row.id, eid: row.eid, beforeDepId: row.beforeDepId, afterdepid: row.afterDepId, afterjobid: row.afterJobId, removedate: row.removeDate, reason: row.reason, remark: row.remark };
            this.dialogVisible = true;
        },
        submitForm() {
            if (this.mvForm.id) {
                this.putRequest('/personnel/remove/', this.mvForm).then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.dialogVisible = false; this.initMv(); }
                });
            } else {
                this.postRequest('/personnel/remove/', this.mvForm).then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.dialogVisible = false; this.initMv(); }
                });
            }
        },
        deleteMv(row) {
            this.$confirm('确认删除转会记录吗？', '提示', { type: 'warning' }).then(() => {
                this.deleteRequest('/personnel/remove/' + row.id).then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.initMv(); }
                });
            }).catch(() => {});
        }
    }
}
</script>
