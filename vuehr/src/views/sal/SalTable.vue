<template>
    <div>
        <el-alert title="工资表管理：展示每个员工当前的工资明细（基本工资、奖金、补贴、社保扣款、实发）" type="info" :closable="false" style="margin-bottom: 15px;"></el-alert>
        <el-table :data="salList" border stripe v-loading="loading">
            <el-table-column prop="empName" label="员工姓名" width="100"></el-table-column>
            <el-table-column prop="depName" label="部门" width="120"></el-table-column>
            <el-table-column prop="sobName" label="工资账套" width="140"></el-table-column>
            <el-table-column prop="basicSalary" label="基本工资" width="90">
                <template #default="scope">¥{{ scope.row.basicSalary }}</template>
            </el-table-column>
            <el-table-column prop="bonus" label="奖金" width="80">
                <template #default="scope">¥{{ scope.row.bonus }}</template>
            </el-table-column>
            <el-table-column prop="lunchSalary" label="午餐补贴" width="90">
                <template #default="scope">¥{{ scope.row.lunchSalary }}</template>
            </el-table-column>
            <el-table-column prop="trafficSalary" label="交通补贴" width="90">
                <template #default="scope">¥{{ scope.row.trafficSalary }}</template>
            </el-table-column>
            <el-table-column prop="shouldPay" label="应发合计" width="100">
                <template #default="scope"><b>¥{{ scope.row.shouldPay }}</b></template>
            </el-table-column>
            <el-table-column prop="pension" label="养老金" width="80">
                <template #default="scope"><span style="color:#f56c6c">-¥{{ scope.row.pension }}</span></template>
            </el-table-column>
            <el-table-column prop="medical" label="医疗金" width="80">
                <template #default="scope"><span style="color:#f56c6c">-¥{{ scope.row.medical }}</span></template>
            </el-table-column>
            <el-table-column prop="fund" label="公积金" width="80">
                <template #default="scope"><span style="color:#f56c6c">-¥{{ scope.row.fund }}</span></template>
            </el-table-column>
            <el-table-column prop="actualPay" label="实发工资" width="110">
                <template #default="scope">
                    <el-tag type="success" size="large">¥{{ scope.row.actualPay }}</el-tag>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
export default {
    name: "SalTable",
    data() {
        return { salList: [], loading: false }
    },
    mounted() { this.initList(); },
    methods: {
        initList() {
            this.loading = true;
            this.getRequest('/salary/table/').then(resp => {
                if (resp) this.salList = resp;
                this.loading = false;
            });
        }
    }
}
</script>
