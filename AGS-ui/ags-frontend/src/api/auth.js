import service from './axios'

// 认证相关接口
export const authApi = {
  // 登录
  login: (data) => {
    return service.post('/auth/login', data)
  },
  
  // 注册
  register: (data) => {
    return service.post('/auth/register', data)
  },
  
  // 刷新token
  refreshToken: () => {
    return service.post('/auth/refresh')
  },
  
  // 登出
  logout: () => {
    return service.post('/auth/logout')
  },
  
  // 获取当前用户信息
  getCurrentUser: () => {
    return service.get('/auth/me')
  }
}