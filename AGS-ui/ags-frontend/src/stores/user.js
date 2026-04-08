import { defineStore } from 'pinia'

// 用户状态管理
export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('accessToken') || null,
    refreshToken: localStorage.getItem('refreshToken') || null,
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || null,
    permissions: JSON.parse(localStorage.getItem('permissions')) || []
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    hasPermission: (state) => (permission) => state.permissions.includes(permission)
  },
  
  actions: {
    // 登录成功后设置用户信息
    setUserInfo(userInfo, token, refreshToken, permissions) {
      this.userInfo = userInfo
      this.token = token
      this.refreshToken = refreshToken
      this.permissions = permissions
      
      // 持久化存储
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
      localStorage.setItem('accessToken', token)
      localStorage.setItem('refreshToken', refreshToken)
      localStorage.setItem('permissions', JSON.stringify(permissions))
    },
    
    // 更新token
    updateToken(token, refreshToken) {
      this.token = token
      this.refreshToken = refreshToken
      localStorage.setItem('accessToken', token)
      localStorage.setItem('refreshToken', refreshToken)
    },
    
    // 登出
    logout() {
      this.userInfo = null
      this.token = null
      this.refreshToken = null
      this.permissions = []
      
      // 清空本地存储
      localStorage.removeItem('userInfo')
      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')
      localStorage.removeItem('permissions')
    }
  }
})