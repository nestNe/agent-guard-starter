<template>
  <div class="error-page">
    <div class="error-container">
      <h1 class="error-code">403</h1>
      <h2 class="error-title">无权限访问</h2>
      <p class="error-message">抱歉，您没有权限访问该页面</p>
      <router-link to="/" class="error-btn">返回首页</router-link>
    </div>
  </div>
</template>

<script setup>
// 403页面
</script>

<style scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #1a1a1a;
  color: #fff;
}

.error-container {
  text-align: center;
  padding: 40px;
}

.error-code {
  font-size: 120px;
  font-weight: 700;
  color: #ff4d4f;
  margin: 0 0 20px;
}

.error-title {
  font-size: 32px;
  font-weight: 600;
  margin: 0 0 16px;
  color: #ccc;
}

.error-message {
  font-size: 16px;
  color: #999;
  margin: 0 0 32px;
}

.error-btn {
  display: inline-block;
  padding: 12px 24px;
  background-color: #4A9EFF;
  color: #fff;
  text-decoration: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 500;
  transition: background-color 0.2s;
}

.error-btn:hover {
  background-color: #3A8EFF;
}
</style>