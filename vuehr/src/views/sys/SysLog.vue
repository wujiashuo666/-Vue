<template>
    <div>
        <el-table :data="logs" border stripe v-loading="loading">
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="addDate" label="操作日期" width="150">
                <template #default="scope">{{ scope.row.addDate ? scope.row.addDate.substring(0,10) : '' }}</template>
            </el-table-column>
            <el-table-column prop="operate" label="操作内容"></el-table-column>
            <el-table-column prop="hrName" label="操作人" width="150"></el-table-column>
        </el-table>
        <el-pagination style="margin-top: 15px; justify-content: flex-end;" background layout="total, prev, pager, next"
            :total="total" :page-size="size" :current-page="page" @current-change="pageChange">
        </el-pagination>
    </div>
</template>
<script>
export default {
    name: "SysLog",
    data() { return { logs: [], page: 1, size: 10, total: 0, loading: false } },
    mounted() { this.init(); },
    methods: {
        init() {
            this.loading = true;
            this.getRequest('/system/log/?page=' + this.page + '&size=' + this.size).then(resp => {
                if (resp) { this.logs = resp.data; this.total = resp.total; }
                this.loading = false;
            });
        },
        pageChange(p) { this.page = p; this.init(); }
    }
}
</script>