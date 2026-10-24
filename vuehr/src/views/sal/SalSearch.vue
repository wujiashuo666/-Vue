<template>
    <div>
        <div style="margin-bottom: 15px;">
            <el-input v-model="searchName" placeholder="请输入员工姓名搜索" style="width: 250px; margin-right: 10px;" clearable @keyup.enter="initList"></el-input>
            <el-button type="primary" @click="initList"><el-icon><Search/></el-icon> 查询</el-button>
        </div>
        <el-table :data="salList" border stripe v-loading="loading">
            <el-table-column prop="empName" label="员工姓名" width="100"></el-table-column>
            <el-table-column prop="depName" label="部门" width="120"></el-table-column>
            <el-table-column prop="sobName" label="工资账套" width="140"></el-table-column>
            <el-table-column prop="shouldPay" label="应发" width="100">
                <template #default="scope">¥{{ scope.row.shouldPay }}</template>
            </el-table-column>
            <el-table-column prop="pension" label="社保扣款" width="100">
                <template #default="scope">-¥{{ Number(scope.row.pension) + Number(scope.row.medical) + Number(scope.row.fund) }}</template>
            </el-table-column>
            <el-table-column prop="actualPay" label="实发工资" width="120">
                <template #default="scope">
                    <el-tag type="success" size="large">¥{{ scope.row.actualPay }}</el-tag>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
export default {
    name: "SalSearch",
    data() {
        return { salList: [], searchName: '', loading: false }
    },
    mounted() { this.initList(); },
    methods: {
        initList() {
            this.loading = true;
            this.getRequest('/salary/table/?name=' + this.searchName).then(resp => {
                if (resp) this.salList = resp;
                this.loading = false;
            });
        }
    }
}
</script>
