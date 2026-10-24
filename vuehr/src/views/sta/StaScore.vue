<template>
    <div>
        <h3>员工积分排行（按奖惩分汇总，TOP 20）</h3>
        <el-table :data="list" border stripe>
            <el-table-column label="排名" width="80">
                <template #default="scope">{{ scope.$index + 1 }}</template>
            </el-table-column>
            <el-table-column prop="name" label="员工姓名"></el-table-column>
            <el-table-column prop="totalScore" label="总积分" width="120">
                <template #default="scope">
                    <el-tag :type="scope.row.totalScore >= 0 ? 'success' : 'danger'">{{ scope.row.totalScore }}</el-tag>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>
<script>
export default {
    name: "StaScore",
    data() { return { list: [] } },
    mounted() { this.getRequest('/system/stat/score').then(resp => { if (resp) this.list = resp; }); }
}
</script>