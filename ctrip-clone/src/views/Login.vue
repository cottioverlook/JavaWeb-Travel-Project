<template>
  <div class="login-container">
    <div class="login-bg"></div>

    <div class="login-box">
      <div class="login-banner">
        <img src="https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=80" alt="Travel">
        <div class="banner-text">
          <h2>探索世界</h2>
          <p>开启您的精彩旅程</p>
        </div>
      </div>

      <div class="login-form">
        <div class="form-header">
          <span :class="{ active: loginType === 'password' }" @click="loginType = 'password'">账号登录</span>
          <span :class="{ active: loginType === 'mobile' }" @click="loginType = 'mobile'">手机动态码</span>
        </div>

        <div v-if="loginType === 'password'" class="form-content">
          <el-input v-model="form.username" placeholder="手机号/邮箱/用户名" size="large" prefix-icon="User" />
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" prefix-icon="Lock" show-password style="margin-top: 20px;" />
        </div>

        <div v-if="loginType === 'mobile'" class="form-content">
          <el-input v-model="form.phone" placeholder="请输入手机号" size="large" prefix-icon="Iphone" />
          <div style="display: flex; margin-top: 20px; gap: 10px;">
            <el-input v-model="form.code" placeholder="验证码" size="large" prefix-icon="Message" />
            <el-button size="large" :disabled="timer > 0" @click="sendCode">
              {{ timer > 0 ? `${timer}s后重发` : '获取验证码' }}
            </el-button>
          </div>
        </div>

        <div class="form-footer">
          <span class="forgot" @click="handleForgot">忘记密码？</span>
          <span class="register" @click="handleRegister">免费注册</span>
        </div>

        <el-button type="primary" class="submit-btn" size="large" @click="handleLogin">登 录</el-button>
        
        <div class="other-login">
          <p>其他登录方式</p>
          <div class="icons">
            <div class="icon-bg wechat"><el-icon><ChatDotRound /></el-icon></div>
            <div class="icon-bg alipay"><el-icon><Money /></el-icon></div>
          </div>
        </div>
        
        <div class="agreement">
          <el-checkbox v-model="agreed">我已阅读并同意《服务协议》和《隐私政策》</el-checkbox>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Iphone, Message, ChatDotRound, Money } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { login, register } from '@/api/auth'

const router = useRouter()
const loginType = ref('password') // 'password' | 'mobile'
const agreed = ref(false)
const timer = ref(0)

const form = ref({
  username: '',
  password: '',
  phone: '',
  code: ''
})

// 模拟发送验证码
const sendCode = () => {
  if (!form.value.phone) return ElMessage.warning('请输入手机号')
  timer.value = 60
  const interval = setInterval(() => {
    timer.value--
    if (timer.value <= 0) clearInterval(interval)
  }, 1000)
  ElMessage.success('验证码已发送：1234')
}

// 处理忘记密码
const handleForgot = () => {
  ElMessageBox.alert('请联系客服重置密码，或者验证手机号找回。', '忘记密码', {
    confirmButtonText: '知道了'
  })
}

// 处理注册
const handleRegister = () => {
  ElMessageBox.prompt('请输入手机号进行注册', '免费注册', {
    confirmButtonText: '注册',
    cancelButtonText: '取消',
    inputPattern: /^1[3-9]\d{9}$/,
    inputErrorMessage: '手机号格式不正确'
  }).then(async ({ value }) => {
    // 注册逻辑：默认密码设为手机号后6位（仅演示）
    const password = value.slice(-6)
    try {
      await register({
        regType: 'phone',
        identification: value,
        password: password
      })
      ElMessage.success(`注册成功！账号：${value}，默认密码：${password}`)
    } catch (error) {
      // 错误已在拦截器处理
    }
  }).catch(() => {})
}

// 处理登录逻辑
const handleLogin = async () => {
  if (!agreed.value) {
    return ElMessage.warning('请先勾选同意服务协议')
  }
  
  try {
    const token = await login({
      loginType: loginType.value === 'password' ? (form.value.username.includes('@') ? 'email' : 'phone') : 'phone', // 简单判断类型，实际项目应更严谨
      identification: loginType.value === 'password' ? form.value.username : form.value.phone,
      password: form.value.password // 手机验证码登录逻辑后端暂未实现，这里暂只支持密码登录
    })

    // 保存 Token
    localStorage.setItem('token', token)
    localStorage.setItem('isLoggedIn', 'true')
    
    ElMessage.success('登录成功！')
    
    // 跳转到首页
    router.push('/')
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  width: 100vw;
  background-image: linear-gradient(135deg, #0086f6 0%, #00a0f0 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 900px;
  height: 550px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 50px rgba(0,0,0,0.2);
  display: flex;
  overflow: hidden;
}

.login-banner {
  width: 400px;
  position: relative;
  background: #f0f2f5;
}
.login-banner img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.banner-text {
  position: absolute;
  top: 40%;
  left: 40px;
  color: #fff;
  text-shadow: 0 2px 4px rgba(0,0,0,0.5);
}
.banner-text h2 { font-size: 32px; margin: 0; }
.banner-text p { font-size: 18px; margin-top: 10px; }

.login-form {
  flex: 1;
  padding: 40px 50px;
  display: flex;
  flex-direction: column;
}

.form-header {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
  font-size: 18px;
  cursor: pointer;
  color: #666;
}
.form-header span.active {
  color: #0086f6;
  font-weight: bold;
  border-bottom: 3px solid #0086f6;
  padding-bottom: 5px;
}

.form-footer {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
  font-size: 14px;
  color: #666;
}
.forgot, .register { cursor: pointer; }
.register { color: #0086f6; }

.submit-btn {
  width: 100%;
  margin-top: 30px;
  font-size: 16px;
  letter-spacing: 2px;
}

.other-login {
  margin-top: auto;
  text-align: center;
}
.other-login p { color: #999; font-size: 12px; margin-bottom: 10px; }
.icons { display: flex; justify-content: center; gap: 20px; }
.icon-bg {
  width: 40px; height: 40px;
  border-radius: 50%;
  background: #f0f2f5;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer;
  color: #666;
  font-size: 20px;
}
.wechat:hover { color: #07c160; background: #e7fbf0; }
.alipay:hover { color: #1677ff; background: #e6f1ff; }

.agreement {
  margin-top: 20px;
}
</style>