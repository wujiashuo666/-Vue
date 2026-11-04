<template>
  <div class="login-page">
    <!-- 左侧：CS 官方主视觉品牌区 -->
    <div class="brand-side">
      <div class="brand-inner">
        <div class="game-art">
          <img src="/cs2_header.jpg" alt="Counter-Strike 2" />
        </div>
        <h1 class="brand-title">职业选手管理系统</h1>
        <p class="brand-tagline">COUNTER-STRIKE PLAYER MANAGEMENT SYSTEM</p>
        <div class="brand-meta">
          <span class="meta-item">TOURNAMENT · TEAM · PLAYER · STATS</span>
        </div>
      </div>
      <div class="brand-footer">CS-ESPORTS OPS / v2.0</div>
    </div>

    <!-- 右侧：登录区（垂直居中，极简风格） -->
    <div class="login-side">
      <div class="login-panel">
        <img src="/cs2_logo.png" class="cs-logo" alt="Counter-Strike" />
        <div class="panel-heading">
          <span class="sign-en">SIGN IN</span>
          <span class="sign-zh">登录操作台</span>
        </div>

        <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="form-area">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="操作员账号" size="large" @keyup.enter="submitLogin">
              <template #prefix>
                <!-- CS 十字准星图标 -->
                <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" class="field-icon">
                  <circle cx="12" cy="12" r="7" />
                  <line x1="12" y1="1.5" x2="12" y2="5" />
                  <line x1="12" y1="19" x2="12" y2="22.5" />
                  <line x1="1.5" y1="12" x2="5" y2="12" />
                  <line x1="19" y1="12" x2="22.5" y2="12" />
                </svg>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" placeholder="密码" size="large" type="password" show-password @keyup.enter="submitLogin">
              <template #prefix>
                <!-- 盾牌图标 -->
                <svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" class="field-icon">
                  <path d="M12 2 L20 5.5 V11 C20 16.5 16.5 20.5 12 22 C7.5 20.5 4 16.5 4 11 V5.5 Z" />
                  <polyline points="9 11.5 11.2 13.7 15 9.5" />
                </svg>
              </template>
            </el-input>
          </el-form-item>
          <button class="login-btn" :disabled="loading" @click="submitLogin">
            {{ loading ? '验证中…' : '进 入 系 统' }}
          </button>
        </el-form>

        <div class="panel-foot">
          <span>AUTHORIZED PERSONNEL ONLY</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
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
        username: [{ required: true, message: '请输入操作员账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
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
            this.$router.replace('/player')
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
  position: relative;
  display: flex;
}

/* 背景图压暗后作底纹（保持原图不变，仅压暗） */
.login-page::before {
  content: '';
  position: absolute;
  inset: 0;
  background: url('/login-bg.jpg') no-repeat center center fixed;
  background-size: cover;
  opacity: 0.18;
  filter: saturate(0.8);
  pointer-events: none;
}

/* ---------------- 左侧品牌区 ---------------- */
.brand-side {
  position: relative;
  z-index: 1;
  flex: 1.4;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 60px 72px;
}
.brand-inner {
  max-width: 560px;
}
.game-art {
  width: 100%;
  border: 1px solid rgba(255, 255, 255, 0.12);
  box-shadow: 0 24px 70px rgba(0, 0, 0, 0.6);
}
.game-art img {
  display: block;
  width: 100%;
  height: auto;
}
.brand-title {
  margin: 34px 0 10px;
  font-size: 30px;
  font-weight: 600;
  letter-spacing: 6px;
  color: #eef2f6;
}
.brand-tagline {
  margin: 0;
  font-size: 12px;
  letter-spacing: 3.5px;
  color: #ff8a00;
  font-weight: 600;
}
.brand-meta {
  margin-top: 26px;
  padding-top: 18px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}
.meta-item {
  font-size: 11px;
  letter-spacing: 2px;
  color: #6e7681;
  font-family: Consolas, monospace;
}
.brand-footer {
  position: absolute;
  left: 72px;
  bottom: 28px;
  font-size: 11px;
  letter-spacing: 2px;
  color: #4d5560;
  font-family: Consolas, monospace;
}

/* ---------------- 右侧登录区 ---------------- */
.login-side {
  position: relative;
  z-index: 1;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  border-left: 1px solid rgba(255, 255, 255, 0.08);
  background: rgba(13, 17, 23, 0.55);
  backdrop-filter: blur(6px);
  padding: 40px;
}
.login-panel {
  width: 320px;
}
/* 官方 logo 为白底深字，反相 + screen 混合后呈现为黑底白字，
   在深色页面上等效于透明底白字 */
.cs-logo {
  display: block;
  width: 190px;
  margin-bottom: 46px;
  filter: invert(1);
  mix-blend-mode: screen;
}
.panel-heading {
  display: flex;
  align-items: baseline;
  gap: 10px;
  margin-bottom: 30px;
  padding-bottom: 14px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
.sign-en {
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 4px;
  color: #eef2f6;
}
.sign-zh {
  font-size: 11px;
  letter-spacing: 2px;
  color: #6e7681;
}

/* ---------------- 表单 ---------------- */
.form-area {
  margin-bottom: 0;
}
.field-icon {
  color: #ff8a00;
  opacity: 0.9;
}
.form-area :deep(.el-input__wrapper) {
  background: rgba(0, 0, 0, 0.35);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 2px;
  box-shadow: none;
  height: 46px;
}
.form-area :deep(.el-input__wrapper:hover) {
  border-color: rgba(255, 138, 0, 0.45);
}
.form-area :deep(.el-input__wrapper.is-focus) {
  border-color: #ff8a00;
  box-shadow: none;
}
.form-area :deep(.el-input__inner) {
  color: #eef2f6;
  letter-spacing: 1px;
}
.form-area :deep(.el-input__inner::placeholder) {
  color: #58606a;
  letter-spacing: 2px;
}
.form-area :deep(.el-input__prefix) {
  color: #ff8a00;
}
.form-area :deep(.el-form-item__error) {
  color: #ff8a00;
}

.login-btn {
  width: 100%;
  height: 46px;
  margin-top: 6px;
  border: 1px solid #ff8a00;
  background: transparent;
  color: #ff8a00;
  font-size: 14px;
  font-weight: 600;
  letter-spacing: 5px;
  cursor: pointer;
  border-radius: 2px;
  transition: all 0.2s;
  font-family: inherit;
}
.login-btn:hover:not(:disabled) {
  background: #ff8a00;
  color: #0b0e14;
}
.login-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.panel-foot {
  margin-top: 34px;
  text-align: center;
  font-size: 10px;
  letter-spacing: 2.5px;
  color: #3d444d;
  font-family: Consolas, monospace;
}

/* 小屏降级：上下堆叠 */
@media (max-width: 900px) {
  .login-page {
    flex-direction: column;
  }
  .brand-side {
    padding: 40px 28px 12px;
  }
  .login-side {
    border-left: none;
    border-top: 1px solid rgba(255, 255, 255, 0.08);
  }
  .brand-footer {
    display: none;
  }
}
</style>
