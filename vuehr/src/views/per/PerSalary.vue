<template>
    <div>
        <div style="margin-bottom: 15px;">
            <el-input v-model="searchName" placeholder="请输入员工姓名搜索" style="width: 250px; margin-right: 10px;" clearable @keyup.enter="initAs"></el-input>
            <el-button type="primary" @click="initAs"><el-icon><Search/></el-icon> 搜索</el-button>
            <el-button type="success" @click="showAddView" style="margin-left: 10px;"><el-icon><Plus/></el-icon> 添加调薪记录</el-button>
        </div>

        <el-table :data="asList" border stripe v-loading="loading">
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="empName" label="员工姓名"></el-table-column>
            <el-table-column label="调薪日期" width="110">
                <template #default="scope">{{ scope.row.asDate ? scope.row.asDate.substring(0,10) : '' }}</template>
            </el-table-column>
            <el-table-column prop="beforeSalary" label="调前薪资" width="100">
                <template #default="scope">¥{{ scope.row.beforeSalary }}</template>
            </el-table-column>
            <el-table-column prop="afterSalary" label="调后薪资" width="100">
                <template #default="scope">
                    <span :style="{color: scope.row.afterSalary > scope.row.beforeSalary ? '#67c23a' : '#f56c6c'}">
                        ¥{{ scope.row.afterSalary }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column prop="reason" label="调薪原因"></el-table-column>
            <el-table-column prop="remark" label="备注"></el-table-column>
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button size="small" type="primary" @click="showEditView(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="deleteAs(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination style="margin-top: 15px; justify-content: flex-end;" background layout="total, prev, pager, next, sizes"
            :total="total" :page-size="size" :current-page="page" @current-change="pageChange" @size-change="sizeChange">
        </el-pagination>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" append-to-body>
            <el-form :model="asForm" label-width="90px">
                <el-form-item label="员工">
                    <el-select v-model="asForm.eid" placeholder="请选择员工" style="width: 100%">
                        <el-option v-for="emp in empList" :key="emp.id" :label="emp.name" :value="emp.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="调薪日期">
                    <el-date-picker v-model="asForm.asdate" type="date" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item label="调前薪资">
                    <el-input-number v-model="asForm.beforesalary" :min="0" :step="100"></el-input-number>
                </el-form-item>
                <el-form-item label="调后薪资">
                    <el-input-number v-model="asForm.aftersalary" :min="0" :step="100"></el-input-number>
                </el-form-item>
                <el-form-item label="调薪原因">
                    <el-input v-model="asForm.reason" type="textarea" :rows="2"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="asForm.remark" type="textarea" :rows="2"></el-input>
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
    name: "PerSalary",
    data() {
        return {
            asList: [], empList: [], searchName: '',
            page: 1, size: 10, total: 0, loading: false,
            dialogVisible: false, dialogTitle: '',
            asForm: { id: null, eid: null, asdate: null, beforesalary: 0, aftersalary: 0, reason: '', remark: '' }
        }
    },
    mounted() { this.initAs(); this.initEmps(); },
    methods: {
        initAs() {
            this.loading = true;
            this.getRequest('/personnel/salary/?page=' + this.page + '&size=' + this.size + '&name=' + this.searchName).then(resp => {
                if (resp) { this.asList = resp.data; this.total = resp.total; }
                this.loading = false;
            });
        },
        initEmps() { this.getRequest('/personnel/salary/emps').then(resp => { if (resp) this.empList = resp; }); },
        pageChange(p) { this.page = p; this.initAs(); },
        sizeChange(s) { this.size = s; this.page = 1; this.initAs(); },
        showAddView() {
            this.dialogTitle = '添加调薪记录';
            this.asForm = { id: null, eid: null, asdate: null, beforesalary: 0, aftersalary: 0, reason: '', remark: '' };
            this.dialogVisible = true;
        },
        showEditView(row) {
            this.dialogTitle = '编辑调薪记录';
            this.asForm = { id: row.id, eid: row.eid, asdate: row.asDate, beforesalary: row.beforeSalary, aftersalary: row.afterSalary, reason: row.reason, remark: row.remark };
            this.dialogVisible = true;
        },
        submitForm() {
            if (this.asForm.id) {
                this.putRequest('/personnel/salary/', this.asForm).then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.dialogVisible = false; this.initAs(); }
                });
            } else {
                this.postRequest('/personnel/salary/', this.asForm).then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.dialogVisible = false; this.initAs(); }
                });
            }
        },
        deleteAs(row) {
            this.$confirm('确认删除调薪记录吗？', '提示', { type: 'warning' }).then(() => {
                this.deleteRequest('/personnel/salary/' + row.id).then(resp => {
                    if (resp) { this.$message.success(resp.msg); this.initAs(); }
                });
            }).catch(() => {});
        }
    }
}
</script>
