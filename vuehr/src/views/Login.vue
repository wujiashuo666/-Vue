<template>
  <div class="login-page">
    <div class="login-box">
      <div class="brand">
        <span class="brand-mark">CS</span>
        <h2 class="title">职业选手管理系统</h2>
      </div>
      <p class="subtitle">欢迎回来，请登录</p>

      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="form-area">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            size="large"
            :prefix-icon="User"
            @keyup.enter="submitLogin"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            placeholder="密码"
            size="large"
            type="password"
            show-password
            :prefix-icon="Lock"
            @keyup.enter="submitLogin"
          ></el-input>
        </el-form-item>
        <el-button
          type="primary"
          size="large"
          class="login-btn"
          :loading="loading"
          @click="submitLogin"
        >登 录</el-button>
      </el-form>
    </div>
  </div>
</template>

<script>
import { User, Lock } from '@element-plus/icons-vue'

export default {
  name: 'Login',
  data() {
    return {
      loading: false,
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  setup() {
    // 模板里直接引用图标组件
    return { User, Lock }
  },
  methods: {
    submitLogin() {
      this.$refs.loginFormRef.validate((valid) => {
        if (!valid) return
        this.loading = true
        this.postRequest('/doLogin', this.loginForm).then(resp => {
          this.loading = false
          if (resp) {
            this.$store.commit('INIT_CURRENTHR', resp.obj)
            window.sessionStorage.setItem('user', JSON.stringify(resp.obj))
            this.$router.replace('/emp/basic')
          }
        }).catch(() => {
          // 请求失败或校验不通过时必须复位 loading，否则按钮会一直转圈
          this.loading = false
        })
      })
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: #0b0e14;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

/* 背景图压暗后作底纹 */
.login-page::before {
  content: '';
  position: absolute;
  inset: 0;
  background: url('/login-bg.jpg') no-repeat center center fixed;
  background-size: cover;
  opacity: 0.22;
  filter: saturate(0.85);
}

.login-box {
  position: relative;
  z-index: 1;
  width: 380px;
  padding: 40px;
  background: rgba(20, 26, 34, 0.92);
  border: 1px solid #232b36;
  border-radius: 16px;
  box-shadow: 0 18px 50px rgba(0, 0, 0, 0.55);
}

.brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}
.brand-mark {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border-radius: 9px;
  background: linear-gradient(135deg, #ff4655, #ff8a00);
  color: #fff;
  font-weight: 800;
  font-size: 14px;
}
.title {
  margin: 0;
  font-size: 21px;
  font-weight: 700;
  color: #e6edf3;
  text-align: center;
}
.subtitle {
  margin: 10px 0 28px;
  font-size: 13px;
  color: #8b949e;
  text-align: center;
}
.form-area {
  margin-bottom: 0;
}

/* Vue 3 已废弃 >>>，改用 :deep()，否则深层样式不生效 */
.form-area :deep(.el-input__wrapper) {
  background: #0f141b;
  border: 1px solid #2a323d;
  border-radius: 8px;
  box-shadow: none;
  height: 44px;
}
.form-area :deep(.el-input__wrapper:hover) {
  border-color: #3f4a59;
}
.form-area :deep(.el-input__wrapper.is-focus) {
  border-color: #ff4655;
  box-shadow: 0 0 0 1px rgba(255, 70, 85, 0.35);
}
.form-area :deep(.el-input__inner) {
  color: #e6edf3;
}
.form-area :deep(.el-input__inner::placeholder) {
  color: #6e7681;
}
.form-area :deep(.el-form-item__error) {
  color: #ff4655;
}

.login-btn {
  width: 100%;
  height: 44px;
  margin-top: 4px;
  background: linear-gradient(135deg, #ff4655, #ff8a00);
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 2px;
}
.login-btn:hover {
  background: linear-gradient(135deg, #ff5c68, #ff9a1f);
}
</style>
