<template>
    <div>
        <h3>各部门人数统计</h3>
        <el-table :data="list" border stripe>
            <el-table-column prop="deptName" label="部门"></el-table-column>
            <el-table-column prop="empCount" label="人数" width="120"></el-table-column>
            <el-table-column label="占比" width="300">
                <template #default="scope">
                    <el-progress :percentage="total ? Math.round(scope.row.empCount * 100 / total) : 0" :stroke-width="18"></el-progress>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>
<script>
export default {
    name: "StaPers",
    data() { return { list: [], total: 0 } },
    mounted() {
        this.getRequest('/system/stat/dept').then(resp => {
            if (resp) { this.list = resp; this.total = resp.reduce((s, r) => s + r.empCount, 0); }
        });
    }
}
</script>