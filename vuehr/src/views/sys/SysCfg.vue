<template>
    <div>
        <el-card style="max-width: 500px;">
            <template #header>系统设置</template>
            <el-form label-width="100px">
                <el-form-item label="系统名称"><el-input v-model="cfg.name"></el-input></el-form-item>
                <el-form-item label="版权信息"><el-input v-model="cfg.copyright"></el-input></el-form-item>
                <el-form-item label="开启注册"><el-switch v-model="cfg.register"></el-switch></el-form-item>
                <el-form-item label="开启验证码"><el-switch v-model="cfg.captcha"></el-switch></el-form-item>
                <el-button type="primary" @click="save">保存设置</el-button>
            </el-form>
        </el-card>
    </div>
</template>
<script>
export default {
    name: "SysCfg",
    data() { return { cfg: { name: '我的系统', copyright: '© 2026', register: false, captcha: true } } },
    methods: { save() { this.$message.success('设置已保存（演示）'); } }
}
</script>