<template>
    <div>
        <el-card style="max-width: 600px; margin: 20px auto;">
            <template #header>
                <span>月末处理 - 工资结算</span>
            </template>
            <el-steps :active="step" finish-status="success" process-status="process" simple>
                <el-step title="读取员工账套"></el-step>
                <el-step title="计算应发工资"></el-step>
                <el-step title="扣除社保公积金"></el-step>
                <el-step title="生成工资单"></el-step>
            </el-steps>
            <div style="text-align: center; margin: 30px 0;">
                <el-button type="primary" size="large" :loading="generating" @click="generate">
                    {{ generating ? '正在计算...' : '生成本月工资表' }}
                </el-button>
            </div>
            <el-alert v-if="result" :title="result" type="success" :closable="false" style="margin-top: 10px;"></el-alert>
            <el-descriptions v-if="summary" :column="2" border style="margin-top: 20px;">
                <el-descriptions-item label="员工人数">{{ summary.count }} 人</el-descriptions-item>
                <el-descriptions-item label="应发总额">¥{{ summary.totalShould }}</el-descriptions-item>
                <el-descriptions-item label="扣款总额">¥{{ summary.totalDeduct }}</el-descriptions-item>
                <el-descriptions-item label="实发总额">¥{{ summary.totalActual }}</el-descriptions-item>
            </el-descriptions>
        </el-card>
    </div>
</template>

<script>
export default {
    name: "SalMonth",
    data() {
        return { generating: false, step: 0, result: '', summary: null }
    },
    methods: {
        generate() {
            this.generating = true;
            this.step = 1;
            this.result = '';
            setTimeout(() => { this.step = 2; }, 400);
            setTimeout(() => { this.step = 3; }, 800);
            setTimeout(() => {
                this.getRequest('/salary/table/').then(resp => {
                    if (resp) {
                        let totalShould = 0, totalActual = 0, totalDeduct = 0;
                        resp.forEach(r => {
                            totalShould += Number(r.shouldPay);
                            totalActual += Number(r.actualPay);
                            totalDeduct += Number(r.pension) + Number(r.medical) + Number(r.fund);
                        });
                        this.summary = {
                            count: resp.length,
                            totalShould: totalShould.toFixed(2),
                            totalActual: totalActual.toFixed(2),
                            totalDeduct: totalDeduct.toFixed(2)
                        };
                    }
                    this.step = 4;
                    this.result = '本月工资表已生成！请到「工资表管理」查看明细。';
                    this.generating = false;
                });
            }, 1200);
        }
    }
}
</script>
