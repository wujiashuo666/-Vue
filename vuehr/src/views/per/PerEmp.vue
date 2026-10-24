<template>
    <div>
        <div style="margin-bottom: 15px;">
            <el-input v-model="searchName" placeholder="请输入选手姓名" style="width: 250px; margin-right: 10px;" clearable @keyup.enter="initList"></el-input>
            <el-button type="primary" @click="initList"><el-icon><Search/></el-icon> 搜索</el-button>
        </div>
        <el-table :data="empList" border stripe v-loading="loading">
            <el-table-column prop="id" label="工号" width="80"></el-table-column>
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="gender" label="性别" width="60"></el-table-column>
            <el-table-column prop="department.name" label="战队"></el-table-column>
            <el-table-column prop="position.name" label="职位"></el-table-column>
            <el-table-column prop="phone" label="手机号"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
        </el-table>
        <el-pagination style="margin-top: 15px; justify-content: flex-end;" background layout="total, prev, pager, next"
            :total="total" :page-size="size" :current-page="page" @current-change="pageChange">
        </el-pagination>
    </div>
</template>

<script>
export default {
    name: "PerEmp",
    data() {
        return { empList: [], searchName: '', page: 1, size: 10, total: 0, loading: false }
    },
    mounted() { this.initList(); },
    methods: {
        initList() {
            this.loading = true;
            this.getRequest('/employee/basic/?page=' + this.page + '&size=' + this.size + '&name=' + this.searchName).then(resp => {
                if (resp) { this.empList = resp.data; this.total = resp.total; }
                this.loading = false;
            });
        },
        pageChange(p) { this.page = p; this.initList(); }
    }
}
</script>
