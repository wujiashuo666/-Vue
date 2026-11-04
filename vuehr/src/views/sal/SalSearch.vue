<template>
    <div>
        <div style="margin-bottom: 15px;display:flex;align-items:center;flex-wrap:wrap;gap:10px;">
            <el-input v-model="searchName" placeholder="请输入选手姓名搜索" style="width: 250px" clearable @keyup.enter="loadAdvice"></el-input>
            <el-button type="primary" @click="loadAdvice"><el-icon><Search/></el-icon> 搜索</el-button>
            <el-tag type="warning" effect="plain" style="margin-left:8px;">
                建议规则：奖惩 1 分 = ¥500 · 战队 1 胜 = ¥200，可在调薪时手动修改
            </el-tag>
        </div>

        <el-table :data="paged" border stripe v-loading="loading">
            <el-table-column prop="id" label="ID" width="60"></el-table-column>
            <el-table-column prop="name" label="选手" width="120"></el-table-column>
            <el-table-column prop="teamName" label="所属战队" width="150">
                <template #default="scope">{{ scope.row.teamName || '自由选手' }}</template>
            </el-table-column>
            <el-table-column prop="salaryName" label="薪资方案" width="140">
                <template #default="scope">
                    <el-tag v-if="scope.row.salaryName" size="small">{{ scope.row.salaryName }}</el-tag>
                    <el-tag v-else size="small" type="info">未签约</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="baseSalary" label="基础月薪" width="100" align="right">
                <template #default="scope">¥{{ scope.row.baseSalary }}</template>
            </el-table-column>
            <el-table-column prop="ecScore" label="奖惩分" width="90" align="center">
                <template #default="scope">
                    <span :style="{color: scope.row.ecScore > 0 ? '#67c23a' : scope.row.ecScore < 0 ? '#f56c6c' : '#909399', fontWeight: 600}">
                        {{ scope.row.ecScore > 0 ? '+' : '' }}{{ scope.row.ecScore }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="战队战绩" width="110" align="center">
                <template #default="scope">
                    <span v-if="scope.row.played > 0" style="color:#67c23a;font-weight:600">{{ scope.row.wins }}</span>
                    <span v-if="scope.row.played > 0" style="color:#909399"> 胜 / {{ scope.row.played }} 场</span>
                    <span v-else style="color:#909399">暂无战绩</span>
                </template>
            </el-table-column>
            <el-table-column prop="advice" label="建议调整" width="110" align="right">
                <template #default="scope">
                    <span :style="{color: scope.row.advice > 0 ? '#67c23a' : scope.row.advice < 0 ? '#f56c6c' : '#909399', fontWeight: 700}">
                        {{ scope.row.advice > 0 ? '+' : '' }}¥{{ scope.row.advice }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column prop="afterSalary" label="调整后月薪" width="110" align="right">
                <template #default="scope">
                    <span style="font-weight:600">¥{{ scope.row.afterSalary }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="160" fixed="right">
                <template #default="scope">
                    <el-button size="small" type="primary" :disabled="scope.row.advice === 0 && !scope.row.salaryName"
                        @click="showAdjustDialog(scope.row)">调薪</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div style="display:flex;justify-content:flex-end;margin-top:15px;">
            <el-pagination background layout="total, prev, pager, next, sizes"
                :total="filtered.length" :page-size="pageSize" :current-page="page"
                @current-change="p => page = p" @size-change="s => { pageSize = s; page = 1 }">
            </el-pagination>
        </div>

        <!-- 调薪弹窗：预填系统建议值，允许手动调整 -->
        <el-dialog v-model="adjustVisible" title="战绩调薪" width="480px" append-to-body>
            <el-form :model="adjustForm" label-width="100px">
                <el-form-item label="选手">
                    <el-input :value="adjustForm.empName" disabled></el-input>
                </el-form-item>
                <el-form-item label="调前薪资">
                    <el-input-number v-model="adjustForm.beforeSalary" :min="0" :step="500" disabled></el-input-number>
                </el-form-item>
                <el-form-item label="调整金额">
                    <el-input-number v-model="adjustForm.delta" :step="500"></el-input-number>
                    <div style="font-size:12px;color:#909399;margin-top:4px;">
                        系统建议：{{ currentAdvice > 0 ? '+' : '' }}¥{{ currentAdvice }}（可手动修改）
                    </div>
                </el-form-item>
                <el-form-item label="调后薪资">
                    <el-input-number v-model="adjustForm.afterSalary" :min="0" :step="500"></el-input-number>
                </el-form-item>
                <el-form-item label="调薪原因">
                    <el-input v-model="adjustForm.reason" type="textarea" :rows="2"
                        placeholder="默认按奖惩与战绩生成，可修改"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="adjustForm.remark" type="textarea" :rows="2"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="adjustVisible = false">取消</el-button>
                <el-button type="primary" @click="submitAdjust" :loading="submitting">确定调薪</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script>
//战绩调薪：根据奖惩积分 + 战队比赛战绩自动计算建议调整金额（后端 /personnel/salary/advice），
//操作员可在建议基础上手动修改金额与原因，确认后写入调薪记录（adjustsalary）
export default {
    name: "SalSearch",
    data() {
        return {
            searchName: '',
            adviceList: [],
            loading: false,
            page: 1,
            pageSize: 10,
            adjustVisible: false,
            submitting: false,
            currentAdvice: 0,
            adjustForm: {
                eid: null, empName: '', beforeSalary: 0, delta: 0, afterSalary: 0, reason: '', remark: ''
            }
        }
    },
    computed: {
        filtered() {
            return this.adviceList
        },
        //当前页数据由表格直接展示，分页仅做展示用
        paged() {
            const start = (this.page - 1) * this.pageSize
            return this.filtered.slice(start, start + this.pageSize)
        }
    },
    mounted() {
        this.loadAdvice()
    },
    methods: {
        loadAdvice() {
            this.loading = true
            this.getRequest('/personnel/salary/advice?name=' + (this.searchName || '')).then(resp => {
                if (resp) this.adviceList = resp
            }).finally(() => { this.loading = false })
        },
        showAdjustDialog(row) {
            this.currentAdvice = row.advice
            this.adjustForm = {
                eid: row.id,
                empName: row.name,
                beforeSalary: row.baseSalary,
                delta: row.advice,
                afterSalary: row.afterSalary,
                reason: this.buildReason(row),
                remark: ''
            }
            this.adjustVisible = true
        },
        buildReason(row) {
            const parts = []
            if (row.ecScore > 0) parts.push(`奖惩积分 +${row.ecScore}`)
            else if (row.ecScore < 0) parts.push(`奖惩积分 ${row.ecScore}`)
            if (row.wins > 0) parts.push(`战队战绩 ${row.wins} 胜 ${row.played - row.wins} 负`)
            if (!parts.length) return '手动调整'
            return '按' + parts.join('，') + '调整'
        },
        submitAdjust() {
            const f = this.adjustForm
            if (f.afterSalary === f.beforeSalary && f.delta === 0) {
                this.$message.warning('调整前后薪资一致，请确认调整金额')
                return
            }
            this.submitting = true
            this.postRequest('/personnel/salary/', {
                eid: f.eid,
                asdate: new Date().toISOString().substring(0, 10),
                beforesalary: f.beforeSalary,
                aftersalary: f.afterSalary,
                reason: f.reason,
                remark: f.remark
            }).then(resp => {
                if (resp) {
                    this.$message.success(resp.msg || '调薪成功')
                    this.adjustVisible = false
                }
            }).finally(() => { this.submitting = false })
        }
    },
    watch: {
        //调整金额变化时联动调后薪资，也可单独改调后薪资（不联动金额）
        'adjustForm.delta'(val) {
            this.adjustForm.afterSalary = this.adjustForm.beforeSalary + val
        }
    }
}
</script>
