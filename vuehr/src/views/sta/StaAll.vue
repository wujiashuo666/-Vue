<template>
    <div>
        <el-row :gaps="20">
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="员工总数" :value="data.empCount || 0"/></el-card></el-col>
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="部门数" :value="data.depCount || 0"/></el-card></el-col>
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="男员工" :value="data.maleCount || 0"/></el-card></el-col>
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="女员工" :value="data.femaleCount || 0"/></el-card></el-col>
        </el-row>
        <el-row :gaps="20" style="margin-top: 20px;">
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="培训记录" :value="data.trainCount || 0"/></el-card></el-col>
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="奖惩记录" :value="data.ecCount || 0"/></el-card></el-col>
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="调薪记录" :value="data.asCount || 0"/></el-card></el-col>
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="调动记录" :value="data.mvCount || 0"/></el-card></el-col>
        </el-row>
    </div>
</template>
<script>
export default {
    name: "StaAll",
    data() { return { data: {} } },
    mounted() {
        this.getRequest('/system/stat/overview').then(resp => { if (resp) this.data = resp; });
    }
}
</script>