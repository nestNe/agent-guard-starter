<template>
  <div class="login-page">
    <div class="login-container">
      <h1 class="login-title">登录</h1>
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="account">账号</label>
          <input
            type="text"
            id="account"
            v-model="form.account"
            placeholder="请输入用户名或邮箱"
            required
          />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input
            type="password"
            id="password"
            v-model="form.password"
            placeholder="请输入密码"
            required
          />
        </div>
        <button type="submit" class="login-btn" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
        <div class="form-footer">
          <span>还没有账号？</span>
          <router-link to="/register">立即注册</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { authApi } from '../api/auth'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const loading = ref(false)
const form = reactive({
  account: '',
  password: ''
})

// 处理登录
const handleLogin = async () => {
  try {
    loading.value = true
    const response = await authApi.login(form)
    
    const { accessToken, refreshToken, userInfo } = response.data
    const permissions = userInfo.permissions || []
    
    // 保存用户信息
    userStore.setUserInfo(userInfo, accessToken, refreshToken, permissions)
    
    // 跳转到原页面或首页
    const redirect = route.query.redirect || '/dashboard'
    router.push(redirect)
  } catch (error) {
    console.error('登录失败:', error)
    alert('登录失败，请检查账号和密码')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #1a1a1a;
}

.login-container {
  width: 100%;
  max-width: 400px;
  padding: 40px;
  background-color: #111;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.login-title {
  text-align: center;
  color: #4A9EFF;
  margin-bottom: 30px;
  font-size: 24px;
  font-weight: 600;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  color: #ccc;
  font-size: 14px;
  font-weight: 500;
}

.form-group input {
  padding: 12px 16px;
  background-color: #222;
  border: 1px solid #333;
  border-radius: 4px;
  color: #fff;
  font-size: 14px;
  transition: border-color 0.2s;
}

.form-group input:focus {
  outline: none;
  border-color: #4A9EFF;
}

.login-btn {
  padding: 12px;
  background-color: #4A9EFF;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
  margin-top: 10px;
}

.login-btn:hover {
  background-color: #3A8EFF;
}

.login-btn:disabled {
  background-color: #666;
  cursor: not-allowed;
}

.form-footer {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-top: 20px;
  font-size: 14px;
  color: #ccc;
}

.form-footer a {
  color: #4A9EFF;
  text-decoration: none;
  transition: color 0.2s;
}

.form-footer a:hover {
  color: #3A8EFF;
}
</style>