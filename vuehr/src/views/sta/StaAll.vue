<template>
    <div>
        <h2>综合信息统计</h2>
        <el-divider/>
        <el-row :gutter="20">
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>选手总数（选手库档案）</template>
                    <div class="stat-num">{{ overview.empCount || 0 }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>战队数量</template>
                    <div class="stat-num">{{ overview.depCount || 0 }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>男选手数</template>
                    <div class="stat-num">{{ overview.maleCount || 0 }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>女选手数</template>
                    <div class="stat-num">{{ overview.femaleCount || 0 }}</div>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 20px">
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>比赛总数</template>
                    <div class="stat-num">{{ overview.matchCount || 0 }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>进行中比赛</template>
                    <div class="stat-num live">{{ overview.liveMatchCount || 0 }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>赛事数量</template>
                    <div class="stat-num">{{ overview.tournamentCount || 0 }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>注册选手（比赛中心）</template>
                    <div class="stat-num">{{ overview.esPlayerCount || 0 }}</div>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 20px">
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>选手培训记录</template>
                    <div class="stat-num">{{ overview.trainCount || 0 }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>选手奖惩记录</template>
                    <div class="stat-num">{{ overview.ecCount || 0 }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>选手调薪记录</template>
                    <div class="stat-num">{{ overview.asCount || 0 }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <template #header>选手转会/调动记录</template>
                    <div class="stat-num">{{ overview.mvCount || 0 }}</div>
                </el-card>
            </el-col>
        </el-row>

        <h2 style="margin-top: 30px">电竞战况</h2>
        <el-divider/>
        <el-row :gutter="20">
            <el-col :span="12">
                <el-card class="box-card">
                    <template #header>各赛事比赛场次</template>
                    <el-table :data="esData.tournamentStat" size="small" border stripe height="260">
                        <el-table-column prop="tournament" label="赛事"/>
                        <el-table-column prop="matchCount" label="比赛场次" width="100"/>
                        <el-table-column prop="finishedCount" label="已结束" width="80"/>
                    </el-table>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card class="box-card">
                    <template #header>战队胜场榜</template>
                    <el-table :data="esData.teamWinStat" size="small" border stripe height="260">
                        <el-table-column type="index" label="#" width="50"/>
                        <el-table-column prop="team" label="战队"/>
                        <el-table-column prop="wins" label="胜场" width="70"/>
                        <el-table-column prop="played" label="总场次" width="80"/>
                    </el-table>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "StaAll",
        data() {
            return {
                overview: {},
                esData: {tournamentStat: [], teamWinStat: []}
            }
        },
        mounted() {
            this.initData();
        },
        methods: {
            initData() {
                this.getRequest("/system/stat/overview").then(resp => {
                    if (resp) {
                        this.overview = resp;
                    }
                });
                this.getRequest("/system/stat/esports").then(resp => {
                    if (resp) {
                        this.esData = resp;
                    }
                });
            }
        }
    }
</script>

<style scoped>
    .stat-num {
        font-size: 36px;
        font-weight: bold;
        color: #409eff;
        text-align: center;
        font-variant-numeric: tabular-nums;
    }

    .stat-num.live {
        color: #f56c6c;
    }
</style>
