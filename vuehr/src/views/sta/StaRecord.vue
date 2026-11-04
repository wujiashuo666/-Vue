<template>
    <div>
        <h2>选手变动记录统计</h2>
        <el-divider/>
        <el-row :gutter="20">
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="选手培训记录" :value="data.trainCount || 0"/></el-card></el-col>
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="选手奖惩记录" :value="data.ecCount || 0"/></el-card></el-col>
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="选手调薪记录" :value="data.asCount || 0"/></el-card></el-col>
            <el-col :span="6"><el-card shadow="hover"><el-statistic title="选手转会/调动记录" :value="data.mvCount || 0"/></el-card></el-col>
        </el-row>

        <h2 style="margin-top: 30px">最新奖惩明细</h2>
        <el-divider/>
        <el-table :data="ecRecords" size="small" border stripe v-loading="loading">
            <el-table-column prop="empName" label="选手" width="140"/>
            <el-table-column prop="ecDate" label="日期" width="120"/>
            <el-table-column prop="ecReason" label="事由"/>
            <el-table-column prop="ecPoint" label="分值" width="80">
                <template #default="scope">
                    <el-tag :type="(scope.row.ecPoint || 0) >= 0 ? 'success' : 'danger'" size="small">
                        {{ scope.row.ecPoint }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注"/>
        </el-table>
    </div>
</template>
<script>
export default {
    name: "StaRecord",
    data() {
        return {
            data: {},
            ecRecords: [],
            loading: false
        }
    },
    mounted() {
        this.getRequest('/system/stat/overview').then(resp => { if (resp) this.data = resp; });
        this.initEcRecords();
    },
    methods: {
        initEcRecords() {
            this.loading = true;
            this.getRequest('/personnel/ec/?page=1&size=10').then(resp => {
                if (resp && resp.data) {
                    this.ecRecords = resp.data;
                }
            }).finally(() => { this.loading = false; });
        }
    }
}
</script>
