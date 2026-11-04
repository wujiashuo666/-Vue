<template>
    <div>
        <div>
            <el-table :data="emps" border stripe size="small">
                <el-table-column type="selection" align="left" width="55"></el-table-column>
                <el-table-column prop="name" label="选手" fixed width="120" align="left"></el-table-column>
                <el-table-column prop="workID" label="编号" width="120" align="left"></el-table-column>
                <el-table-column prop="email" label="电子邮件" width="200" align="left"></el-table-column>
                <el-table-column prop="phone" label="电话号码" width="120" align="left"></el-table-column>
                <el-table-column prop="department.name" label="所属战队" width="140" align="left"></el-table-column>
                <el-table-column label="薪资方案" align="center">
                    <template #default="scope">
                        <el-tooltip placement="right" v-if="scope.row.salary">
                            <template #content><div>
                                <table>
                                    <tr>
                                        <td>方案名称</td>
                                        <td>{{scope.row.salary.name}}</td>
                                    </tr>
                                    <tr>
                                        <td>基础月薪</td>
                                        <td>{{scope.row.salary.basicSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>出场费</td>
                                        <td>{{scope.row.salary.trafficSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>直播补贴</td>
                                        <td>{{scope.row.salary.lunchSalary}}</td>
                                    </tr>
                                    <tr>
                                        <td>夺冠奖金</td>
                                        <td>{{scope.row.salary.bonus}}</td>
                                    </tr>
                                    <tr>
                                        <td>启用时间</td>
                                        <td>{{scope.row.salary.createDate ? scope.row.salary.createDate.substring(0,10) : ''}}</td>
                                    </tr>
                                </table>
                            </div></template>
                            <el-tag>{{scope.row.salary.name}}</el-tag>
                        </el-tooltip>
                        <el-tag v-else type="info">暂未签约方案</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                    <template #default="scope">
                        <el-popover
                                placement="left"
                                title="更换薪资方案"
                                @show="showPop(scope.row.salary)"
                                @hide="hidePop(scope.row)"
                                width="200"
                                trigger="click">
                            <div>
                                <el-select v-model="currentSalary" placeholder="请选择" size="small">
                                    <el-option
                                            v-for="item in salaries"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </div>
                            <template #reference><el-button type="danger">更换薪资方案</el-button></template>
                        </el-popover>
                    </template>
                </el-table-column>
            </el-table>
            <div style="display: flex;justify-content: flex-end">
                <el-pagination
                        background
                        @size-change="sizeChange"
                        @current-change="currentChange"
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
    //选手薪资设置：为每位选手绑定薪资方案（原五险一金展示项与 CS 战队合同无关，已移除）
    export default {
        name: "SalSobCfg",
        data() {
            return {
                emps: [],
                total: 0,
                currentPage: 1,
                currentSize: 10,
                currentSalary: null,
                salaries: []
            }
        },
        mounted() {
            this.initEmps();
            this.initSalaries();
        },
        methods: {
            sizeChange(size) {
                this.currentSize = size;
                this.initEmps();
            },
            currentChange(page) {
                this.currentPage = page;
                this.initEmps();
            },
            hidePop(data) {
                if (this.currentSalary) {
                    this.putRequest('/salary/sobcfg/?eid=' + data.id + '&sid=' + this.currentSalary).then(resp => {
                        if (resp) {
                            this.initEmps()
                        }
                    });
                }
            },
            showPop(data) {
                if (data) {
                    this.currentSalary = data.id;
                } else {
                    this.currentSalary = null;
                }
            },
            initSalaries() {
                this.getRequest("/salary/sobcfg/salaries").then(resp => {
                    if (resp) {
                        this.salaries = resp;
                    }
                })
            },
            initEmps() {
                this.getRequest("/salary/sobcfg/?page=" + this.currentPage + '&size=' + this.currentSize).then(resp => {
                    if (resp) {
                        this.emps = resp.data;
                        this.total = resp.total;
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
