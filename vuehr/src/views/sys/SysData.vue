<template>
    <div>
        <el-card>
            <template #header>备份恢复数据库</template>
            <el-alert type="info" :closable="false" title="备份功能：将当前 vhr 数据库导出为 SQL 文件。恢复功能：选择备份文件恢复。"></el-alert>
            <div style="margin-top: 20px;">
                <el-button type="primary" @click="doBackup">立即备份</el-button>
                <el-button type="warning">选择备份文件恢复</el-button>
            </div>
        </el-card>
    </div>
</template>
<script>
export default {
    name: "SysData",
    methods: { doBackup() { this.$message.success('备份成功（演示）'); } }
}
</script>