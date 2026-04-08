<template>
  <div class="main-layout">
    <!-- 左侧导航栏 -->
    <aside class="sidebar" :class="{ collapsed: isSidebarCollapsed }">
      <div class="sidebar-header">
        <h1 class="logo">AGS</h1>
        <button class="collapse-btn" @click="toggleSidebar">
          {{ isSidebarCollapsed ? '→' : '←' }}
        </button>
      </div>
      <nav class="sidebar-nav">
        <router-link to="/dashboard" class="nav-item" active-class="active">
          <span class="nav-icon">🏠</span>
          <span class="nav-text">首页</span>
        </router-link>
        <router-link to="/users" class="nav-item" active-class="active">
          <span class="nav-icon">👥</span>
          <span class="nav-text">用户管理</span>
        </router-link>
        <router-link to="/roles" class="nav-item" active-class="active">
          <span class="nav-icon">🎭</span>
          <span class="nav-text">角色管理</span>
        </router-link>
        <router-link to="/permissions" class="nav-item" active-class="active">
          <span class="nav-icon">🔒</span>
          <span class="nav-text">权限管理</span>
        </router-link>
        <router-link to="/chat" class="nav-item" active-class="active">
          <span class="nav-icon">💬</span>
          <span class="nav-text">AI对话</span>
        </router-link>
      </nav>
      <div class="sidebar-footer">
        <div class="user-info" v-if="userStore.userInfo">
          <span class="user-name">{{ userStore.userInfo.username }}</span>
          <button class="logout-btn" @click="logout">退出</button>
        </div>
      </div>
    </aside>
    
    <!-- 主内容区域 -->
    <main class="content">
      <!-- 顶部导航 -->
      <header class="top-nav">
        <div class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span v-if="$route.name !== 'Dashboard'"> / {{ $route.meta.title }}</span>
        </div>
        <div class="user-actions">
          <span class="user-email">{{ userStore.userInfo?.email }}</span>
        </div>
      </header>
      
      <!-- 页面内容 -->
      <div class="page-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-slide" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { authApi } from '../api/auth'

const router = useRouter()
const userStore = useUserStore()
const isSidebarCollapsed = ref(false)

// 切换侧边栏
const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value
}

// 退出登录
const logout = async () => {
  try {
    await authApi.logout()
  } catch (error) {
    console.error('登出失败:', error)
  } finally {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.main-layout {
  display: flex;
  height: 100vh;
  background-color: #1a1a1a;
  color: #fff;
}

/* 侧边栏 */
.sidebar {
  width: 220px;
  background-color: #111;
  border-right: 1px solid #333;
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-bottom: 1px solid #333;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #4A9EFF;
  margin: 0;
}

.collapse-btn {
  background: none;
  border: none;
  color: #fff;
  font-size: 16px;
  cursor: pointer;
  padding: 5px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.collapse-btn:hover {
  background-color: #333;
}

.sidebar-nav {
  flex: 1;
  padding: 20px 0;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #ccc;
  text-decoration: none;
  transition: all 0.2s;
  border-left: 3px solid transparent;
}

.nav-item:hover {
  background-color: #333;
  color: #4A9EFF;
}

.nav-item.active {
  background-color: #333;
  color: #4A9EFF;
  border-left-color: #4A9EFF;
}

.nav-icon {
  margin-right: 12px;
  font-size: 18px;
}

.sidebar.collapsed .nav-text {
  display: none;
}

.sidebar.collapsed .nav-icon {
  margin-right: 0;
}

.sidebar-footer {
  padding: 20px;
  border-top: 1px solid #333;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
}

.logout-btn {
  background-color: #333;
  border: 1px solid #444;
  color: #fff;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 14px;
}

.logout-btn:hover {
  background-color: #444;
  border-color: #555;
}

/* 主内容 */
.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 64px;
  background-color: #1a1a1a;
  border-bottom: 1px solid #333;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.breadcrumb a {
  color: #4A9EFF;
  text-decoration: none;
}

.breadcrumb span {
  color: #ccc;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-email {
  font-size: 14px;
  color: #ccc;
}

.page-content {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

/* 页面切换动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.2s ease;
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(8px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    left: 0;
    top: 0;
    height: 100vh;
    z-index: 1000;
    transform: translateX(0);
  }
  
  .sidebar.collapsed {
    transform: translateX(-100%);
  }
  
  .content {
    margin-left: 0;
  }
}
</style>