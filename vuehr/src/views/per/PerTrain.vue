<template>
    <div>
        <div style="margin-bottom: 15px;">
            <el-input v-model="searchName" placeholder="请输入员工姓名搜索" style="width: 250px; margin-right: 10px;" clearable @keyup.enter="initTrain"></el-input>
            <el-button type="primary" @click="initTrain">
                <el-icon><Search/></el-icon> 搜索
            </el-button>
            <el-button type="success" @click="showAddView" style="margin-left: 10px;">
                <el-icon><Plus/></el-icon> 添加培训记录
            </el-button>
        </div>

        <el-table :data="trainList" border stripe v-loading="loading">
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="empName" label="员工姓名"></el-table-column>
            <el-table-column prop="trainDate" label="培训日期" width="120">
                <template #default="scope">
                    {{ scope.row.trainDate ? scope.row.trainDate.substring(0, 10) : '' }}
                </template>
            </el-table-column>
            <el-table-column prop="trainContent" label="培训内容"></el-table-column>
            <el-table-column prop="remark" label="备注"></el-table-column>
            <el-table-column label="操作" width="180">
                <template #default="scope">
                    <el-button size="small" type="primary" @click="showEditView(scope.row)">编辑</el-button>
                    <el-button size="small" type="danger" @click="deleteTrain(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination
            style="margin-top: 15px; justify-content: flex-end;"
            background
            layout="total, prev, pager, next, sizes"
            :total="total"
            :page-size="size"
            :current-page="page"
            @current-change="pageChange"
            @size-change="sizeChange">
        </el-pagination>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" append-to-body>
            <el-form :model="trainForm" label-width="80px">
                <el-form-item label="员工">
                    <el-select v-model="trainForm.eid" placeholder="请选择员工" style="width: 100%">
                        <el-option v-for="emp in empList" :key="emp.id" :label="emp.name" :value="emp.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="培训日期">
                    <el-date-picker v-model="trainForm.traindate" type="date" placeholder="选择日期" style="width: 100%"></el-date-picker>
                </el-form-item>
                <el-form-item label="培训内容">
                    <el-input v-model="trainForm.traincontent" type="textarea" :rows="3" placeholder="请输入培训内容"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="trainForm.remark" type="textarea" :rows="2" placeholder="备注（可选）"></el-input>
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
    name: "PerTrain",
    data() {
        return {
            trainList: [],
            empList: [],
            searchName: '',
            page: 1,
            size: 10,
            total: 0,
            loading: false,
            dialogVisible: false,
            dialogTitle: '',
            trainForm: {
                id: null,
                eid: null,
                traindate: null,
                traincontent: '',
                remark: ''
            }
        }
    },
    mounted() {
        this.initTrain();
        this.initEmps();
    },
    methods: {
        initTrain() {
            this.loading = true;
            this.getRequest('/personnel/train/?page=' + this.page + '&size=' + this.size + '&name=' + this.searchName).then(resp => {
                if (resp) {
                    this.trainList = resp.data;
                    this.total = resp.total;
                }
                this.loading = false;
            });
        },
        initEmps() {
            this.getRequest('/personnel/train/emps').then(resp => {
                if (resp) this.empList = resp;
            });
        },
        pageChange(p) {
            this.page = p;
            this.initTrain();
        },
        sizeChange(s) {
            this.size = s;
            this.page = 1;
            this.initTrain();
        },
        showAddView() {
            this.dialogTitle = '添加培训记录';
            this.trainForm = { id: null, eid: null, traindate: null, traincontent: '', remark: '' };
            this.dialogVisible = true;
        },
        showEditView(row) {
            this.dialogTitle = '编辑培训记录';
            this.trainForm = {
                id: row.id,
                eid: row.eid,
                traindate: row.trainDate,
                traincontent: row.trainContent,
                remark: row.remark
            };
            this.dialogVisible = true;
        },
        submitForm() {
            if (this.trainForm.id) {
                this.putRequest('/personnel/train/', this.trainForm).then(resp => {
                    if (resp) {
                        this.$message.success(resp.msg);
                        this.dialogVisible = false;
                        this.initTrain();
                    }
                });
            } else {
                this.postRequest('/personnel/train/', this.trainForm).then(resp => {
                    if (resp) {
                        this.$message.success(resp.msg);
                        this.dialogVisible = false;
                        this.initTrain();
                    }
                });
            }
        },
        deleteTrain(row) {
            this.$confirm('确认删除培训记录【' + row.trainContent + '】吗？', '提示', {
                type: 'warning'
            }).then(() => {
                this.deleteRequest('/personnel/train/' + row.id).then(resp => {
                    if (resp) {
                        this.$message.success(resp.msg);
                        this.initTrain();
                    }
                });
            }).catch(() => {});
        }
    }
}
</script>

<style scoped>
</style>
