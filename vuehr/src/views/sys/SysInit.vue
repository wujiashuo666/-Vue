<template>
    <div>
        <el-card>
            <template #header>初始化数据库</template>
            <el-alert type="warning" :closable="false" title="警告：此操作将清空所有数据并重新导入初始 SQL，不可恢复！"></el-alert>
            <div style="margin-top: 20px;">
                <el-popconfirm title="确认要初始化数据库吗？所有数据将丢失！" @confirm="doInit">
                    <el-button type="danger">初始化数据库</el-button>
                </el-popconfirm>
            </div>
        </el-card>
    </div>
</template>
<script>
export default {
    name: "SysInit",
    methods: { doInit() { this.$message.success('数据库已初始化（演示）'); } }
}
</script>