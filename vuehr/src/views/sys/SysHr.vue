<template>
    <div>
        <div style="margin-top: 10px;display: flex;justify-content: center">
            <el-input v-model="keywords" placeholder="通过用户名搜索用户..."
                      style="width: 400px;margin-right: 10px" @keydown.enter="doSearch"><template #prefix><el-icon><Search/></el-icon></template></el-input>
            <el-button type="primary" @click="doSearch"><el-icon><Search/></el-icon>搜索</el-button>
            <el-button type="success" @click="showAddDialog"><el-icon><Plus/></el-icon>新增操作员</el-button>
        </div>
        <div class="hr-container">
            <el-card class="hr-card" v-for="(hr,index) in hrs" :key="index">
                <template #header>
            <div class="clearfix">
                    <span>{{hr.name}}</span>
                    <el-button style="float: right; padding: 3px 0;color: #e30007;" link @click="deleteHr(hr)"><el-icon><Delete/></el-icon></el-button>
                </div>
            </template>
                <div>
                    <div class="img-container">
                        <el-tooltip content="点击上传新头像" placement="bottom">
                            <el-upload
                                    class="avatar-uploader"
                                    action="/system/hr/userface"
                                    :data="{uid: hr.id}"
                                    name="file"
                                    :show-file-list="false"
                                    accept="image/*"
                                    :on-success="uploadSuccess"
                                    :on-error="uploadError">
                                <img :src="hr.userface" :alt="hr.name" class="userface-img">
                            </el-upload>
                        </el-tooltip>
                    </div>
                    <div class="userinfo-container">
                        <div>用户名：{{hr.name}}</div>
                        <div>手机号码：{{hr.phone}}</div>
                        <div>电话号码：{{hr.telephone}}</div>
                        <div>地址：{{hr.address}}</div>
                        <div>用户状态：
                            <el-switch
                                    v-model="hr.enabled"
                                    active-text="启用"
                                    @change="enabledChange(hr)"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949"
                                    inactive-text="禁用">
                            </el-switch>
                        </div>
                        <div>用户角色：
                            <el-tag type="success" style="margin-right: 4px" v-for="(role,indexj) in hr.roles"
                                    :key="indexj">{{role.nameZh}}
                            </el-tag>
                            <el-popover
                                    placement="right"
                                    title="角色列表"
                                    @show="showPop(hr)"
                                    @hide="hidePop(hr)"
                                    width="200"
                                    trigger="click">
                                <el-select v-model="selectedRoles" multiple placeholder="请选择">
                                    <el-option
                                            v-for="(r,indexk) in allroles"
                                            :key="indexk"
                                            :label="r.nameZh"
                                            :value="r.id">
                                    </el-option>
                                </el-select>
                                <template #reference><el-button link><el-icon><MoreFilled/></el-icon></el-button></template>
                            </el-popover>
                        </div>
                        <div>备注：{{hr.remark}}</div>
                    </div>
                </div>
            </el-card>

        </div>

        <!-- 新增操作员弹窗：登录名唯一、初始密码必填，角色可多选 -->
        <el-dialog v-model="addVisible" title="新增操作员" width="480px" :close-on-click-modal="false">
            <el-form ref="addFormRef" :model="addForm" :rules="addRules" label-width="90px">
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="addForm.name" placeholder="显示用姓名，如：玩机器"></el-input>
                </el-form-item>
                <el-form-item label="登录用户名" prop="username">
                    <el-input v-model="addForm.username" placeholder="登录账号，需唯一"></el-input>
                </el-form-item>
                <el-form-item label="初始密码" prop="password">
                    <el-input type="password" show-password v-model="addForm.password" placeholder="不少于 3 位"></el-input>
                </el-form-item>
                <el-form-item label="手机号码">
                    <el-input v-model="addForm.phone"></el-input>
                </el-form-item>
                <el-form-item label="电话号码">
                    <el-input v-model="addForm.telephone"></el-input>
                </el-form-item>
                <el-form-item label="联系地址">
                    <el-input v-model="addForm.address"></el-input>
                </el-form-item>
                <el-form-item label="用户角色">
                    <el-select v-model="addForm.roleIds" multiple placeholder="可多选角色" style="width: 100%">
                        <el-option v-for="r in allroles" :key="r.id" :label="r.nameZh" :value="r.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="addForm.remark"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="addVisible = false">取消</el-button>
                <el-button type="primary" :loading="adding" @click="doAddHr">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "SysHr",
        data() {
            return {
                keywords: '',
                hrs: [],
                selectedRoles: [],
                allroles: [],
                addVisible: false,
                adding: false,
                addForm: {},
                addRules: {
                    name: [{required: true, message: '请填写姓名', trigger: 'blur'}],
                    username: [{required: true, message: '请填写登录用户名', trigger: 'blur'}],
                    password: [
                        {required: true, message: '请填写初始密码', trigger: 'blur'},
                        {min: 3, message: '密码不少于 3 位', trigger: 'blur'}
                    ]
                }
            }
        },
        mounted() {
            this.initHrs();
        },
        methods: {
            showAddDialog() {
                this.addForm = {name: '', username: '', password: '', phone: '', telephone: '', address: '', remark: '', roleIds: []};
                this.addVisible = true;
                //角色候选顺带拉取，避免下拉为空
                this.initAllRoles();
                this.$nextTick(() => {
                    if (this.$refs.addFormRef) {
                        this.$refs.addFormRef.clearValidate();
                    }
                });
            },
            doAddHr() {
                if (this.adding) return;
                this.$refs.addFormRef.validate(valid => {
                    if (!valid) return;
                    this.adding = true;
                    this.postRequest("/system/hr/", this.addForm).then(resp => {
                        if (resp) {
                            this.addVisible = false;
                            this.initHrs();
                        }
                    }).finally(() => {
                        this.adding = false;
                    });
                });
            },
            deleteHr(hr) {
                this.$confirm('此操作将永久删除【'+hr.name+'】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/system/hr/"+hr.id).then(resp=>{
                        if (resp) {
                            this.initHrs();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            doSearch() {
                this.initHrs();
            },
            hidePop(hr) {
                let roles = [];
                Object.assign(roles, hr.roles);
                let flag = false;
                if (roles.length != this.selectedRoles.length) {
                    flag = true;
                } else {
                    for (let i = 0; i < roles.length; i++) {
                        let role = roles[i];
                        for (let j = 0; j < this.selectedRoles.length; j++) {
                            let sr = this.selectedRoles[j];
                            if (role.id == sr) {
                                roles.splice(i, 1);
                                i--;
                                break;
                            }
                        }
                    }
                    if (roles.length != 0) {
                        flag = true;
                    }
                }
                if (flag) {
                    let url = '/system/hr/role?hrid=' + hr.id;
                    this.selectedRoles.forEach(sr => {
                        url += '&rids=' + sr;
                    });
                    this.putRequest(url).then(resp => {
                        if (resp) {
                            this.initHrs();
                        }
                    });
                }
            },
            showPop(hr) {
                this.initAllRoles();
                let roles = hr.roles;
                this.selectedRoles = [];
                roles.forEach(r => {
                    this.selectedRoles.push(r.id);
                })
            },
            enabledChange(hr) {
                delete hr.roles;
                this.putRequest("/system/hr/", hr).then(resp => {
                    if (resp) {
                        this.initHrs();
                    }
                })
            },
            uploadSuccess(resp) {
                if (resp && resp.status === 200) {
                    this.$message({type: 'success', message: '头像更新成功!'});
                    this.initHrs();
                } else {
                    this.$message({type: 'error', message: (resp && resp.msg) || '头像更新失败!'});
                }
            },
            uploadError() {
                this.$message({type: 'error', message: '头像上传失败!'});
            },
            initAllRoles() {
                this.getRequest("/system/hr/roles").then(resp => {
                    if (resp) {
                        this.allroles = resp;
                    }
                })
            },
            initHrs() {
                this.getRequest("/system/hr/?keywords="+this.keywords).then(resp => {
                    if (resp) {
                        this.hrs = resp;
                    }
                })
            }
        }
    }
</script>

<style>
    .userinfo-container div {
        font-size: 12px;
        color: #409eff;
    }

    .userinfo-container {
        margin-top: 20px;
    }

    .img-container {
        width: 100%;
        display: flex;
        justify-content: center;
    }

    .userface-img {
        width: 72px;
        height: 72px;
        border-radius: 72px;
        cursor: pointer;
        transition: box-shadow .2s;
    }

    .userface-img:hover {
        box-shadow: 0 0 0 3px #409eff;
    }

    .avatar-uploader .el-upload {
        border-radius: 72px;
    }

    .hr-container {
        margin-top: 10px;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-around;
    }

    .hr-card {
        width: 350px;
        margin-bottom: 20px;
    }
</style>