<template>
    <div>
        <h2>选手信息统计</h2>
        <el-divider/>
        <el-row :gutter="20">
            <el-col :span="12">
                <el-card class="box-card">
                    <template #header>各战队选手人数统计</template>
                    <el-table :data="deps" size="small" border stripe height="420">
                        <el-table-column type="index" label="#" width="50"/>
                        <el-table-column prop="deptName" label="战队"></el-table-column>
                        <el-table-column prop="empCount" label="选手人数" width="100"></el-table-column>
                    </el-table>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card class="box-card">
                    <template #header>选手位置分布</template>
                    <el-table :data="positions" size="small" border stripe height="420">
                        <el-table-column type="index" label="#" width="50"/>
                        <el-table-column prop="specialty" label="位置"></el-table-column>
                        <el-table-column prop="cnt" label="人数" width="100"></el-table-column>
                    </el-table>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "StaPers",
        data() {
            return {
                deps: [],
                positions: []
            }
        },
        mounted() {
            this.initData();
        },
        methods: {
            initData() {
                this.getRequest("/system/stat/dept").then(resp => {
                    if (resp) {
                        this.deps = resp;
                    }
                });
                this.getRequest("/system/stat/position").then(resp => {
                    if (resp) {
                        this.positions = resp;
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .box-card {
        min-height: 480px;
    }
</style>
