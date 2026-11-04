<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <el-button type="primary" @click="showAddSalaryView"><el-icon><Plus/></el-icon>添加选手薪资方案</el-button>
            <el-button type="success" @click="initSalaries"><el-icon><Refresh/></el-icon></el-button>
        </div>
        <div style="margin-top: 10px">
            <el-table :data="salaries" border stripe>
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column width="140" prop="name" label="方案名称"></el-table-column>
                <el-table-column width="90" prop="basicSalary" label="基础月薪"></el-table-column>
                <el-table-column width="90" prop="trafficSalary" label="出场费"></el-table-column>
                <el-table-column width="90" prop="lunchSalary" label="直播补贴"></el-table-column>
                <el-table-column width="90" prop="bonus" label="夺冠奖金"></el-table-column>
                <el-table-column width="120" prop="createDate" label="启用时间">
                    <template #default="scope">{{ scope.row.createDate ? scope.row.createDate.substring(0,10) : '' }}</template>
                </el-table-column>
                <el-table-column label="方案总额" align="center">
                    <el-table-column width="90" label="月薪合计">
                        <template #default="scope">{{ scope.row.basicSalary + scope.row.trafficSalary + scope.row.lunchSalary }}</template>
                    </el-table-column>
                    <el-table-column width="90" label="含夺冠奖金">
                        <template #default="scope">
                            <span style="color:#67c23a;font-weight:600">{{ scope.row.basicSalary + scope.row.trafficSalary + scope.row.lunchSalary + scope.row.bonus }}</span>
                        </template>
                    </el-table-column>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button @click="showEditSalaryView(scope.row)">编辑</el-button>
                        <el-button type="danger" @click="deleteSalary(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-dialog
                :title="dialogTitle"
                v-model="dialogVisible"
                width="50%"
                append-to-body>
            <div style="display: flex;justify-content: space-around;align-items: center">
                <el-steps direction="vertical" :active="activeItemIndex">
                    <el-step :title="itemName" v-for="(itemName,index) in salaryItemName" :key="index"></el-step>
                </el-steps>
                <el-input v-model="salary[title]" :placeholder="'请输入'+salaryItemName[index]+'...'"
                          v-for="(value,title,index) in salary"
                          :key="index" v-show="activeItemIndex==index" style="width: 200px"></el-input>
            </div>
            <template #footer>
                <span class="dialog-footer">
    <el-button @click="preStep">{{activeItemIndex==4?'取消':'上一步'}}</el-button>
    <el-button type="primary" @click="nextStep">{{activeItemIndex==4?'完成':'下一步'}}</el-button>
  </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    //薪资方案 CS 化：只保留基础月薪 / 出场费 / 直播补贴 / 夺冠奖金四个金额项，
    //原五险一金字段（养老金、医保、公积金）与 CS 战队合同无关，前端不再录入展示，
    //提交时仍按 0 写入数据库列以兼容既有表结构
    export default {
        name: "SalSob",
        data() {
            return {
                dialogVisible: false,
                dialogTitle: '添加薪资方案',
                salaries: [],
                activeItemIndex: 0,
                salaryItemName: [
                    '基础月薪',
                    '出场费',
                    '直播补贴',
                    '夺冠奖金',
                    '方案名称'
                ],
                salary: {
                    basicSalary: 0,
                    trafficSalary: 0,
                    lunchSalary: 0,
                    bonus: 0,
                    name: ''
                }
            }
        },
        mounted() {
            this.initSalaries();
        },
        methods: {
            showEditSalaryView(data) {
                this.dialogTitle = '修改薪资方案';
                this.dialogVisible = true;
                this.salary.basicSalary = data.basicSalary;
                this.salary.trafficSalary = data.trafficSalary;
                this.salary.lunchSalary = data.lunchSalary;
                this.salary.bonus = data.bonus;
                this.salary.name = data.name;
                this.salary.id = data.id;
                this.activeItemIndex = 0;
            },
            deleteSalary(data) {
                this.$confirm('此操作将删除【' + data.name + '】薪资方案，是否继续？', '提示', {
                    cancelButtonText: '取消',
                    confirmButtonText: '确定'
                }).then(() => {
                    this.deleteRequest("/salary/sob/" + data.id).then(resp => {
                        if (resp) {
                            this.initSalaries();
                        }
                    })
                }).catch(() => {
                    this.$message.info("取消删除!");
                })
            },
            preStep() {
                if (this.activeItemIndex == 0) {
                    return;
                } else if (this.activeItemIndex == 4) {
                    //最后一步的按钮文案是"取消"，直接关闭对话框
                    this.dialogVisible = false;
                    return;
                }
                this.activeItemIndex--;
            },
            nextStep() {
                if (this.activeItemIndex == 4) {
                    //提交时补齐兼容字段，与既有 salary 表结构对齐
                    const payload = Object.assign({
                        pensionPer: 0, pensionBase: 0,
                        medicalPer: 0, medicalBase: 0,
                        accumulationFundPer: 0, accumulationFundBase: 0
                    }, this.salary);
                    if (this.salary.id) {
                        this.putRequest("/salary/sob/", payload).then(resp => {
                            if (resp) {
                                this.initSalaries();
                                this.dialogVisible = false;
                            }
                        })
                    } else {
                        this.postRequest("/salary/sob/", payload).then(resp => {
                            if (resp) {
                                this.initSalaries();
                                this.dialogVisible = false;
                            }
                        });
                    }
                    return;
                }
                this.activeItemIndex++;
            },
            showAddSalaryView() {
                this.salary = {
                    basicSalary: 0,
                    trafficSalary: 0,
                    lunchSalary: 0,
                    bonus: 0,
                    name: 'S级主力合同'
                }
                this.dialogTitle = '添加薪资方案';
                this.activeItemIndex = 0;
                this.dialogVisible = true;
            },
            initSalaries() {
                this.getRequest("/salary/sob/").then(resp => {
                    if (resp) {
                        this.salaries = resp;
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
