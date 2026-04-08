import axios from 'axios'
import { useUserStore } from '../stores/user'

// 创建axios实例
const service = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求队列
let requestQueue = []
// 是否正在刷新token
let isRefreshing = false

// 请求拦截器
service.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    // 添加token
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    return response.data
  },
  async error => {
    const userStore = useUserStore()
    const originalRequest = error.config
    
    // 处理401错误（token过期）
    if (error.response && error.response.status === 401) {
      // 如果不是刷新token的请求
      if (!originalRequest._retry) {
        // 如果正在刷新token，将请求加入队列
        if (isRefreshing) {
          return new Promise(resolve => {
            requestQueue.push(() => {
              resolve(service(originalRequest))
            })
          })
        }
        
        originalRequest._retry = true
        isRefreshing = true
        
        try {
          // 调用刷新token接口
          const response = await service.post('/auth/refresh', {}, {
            headers: {
              'X-Refresh-Token': userStore.refreshToken
            }
          })
          
          const { accessToken, refreshToken } = response.data
          
          // 更新token
          userStore.updateToken(accessToken, refreshToken)
          
          // 重试队列中的请求
          requestQueue.forEach(cb => cb())
          requestQueue = []
          
          // 重试原请求
          return service(originalRequest)
        } catch (refreshError) {
          // 刷新token失败，跳转到登录页
          userStore.logout()
          window.location.href = '/login'
          return Promise.reject(refreshError)
        } finally {
          isRefreshing = false
        }
      }
    }
    
    return Promise.reject(error)
  }
)

export default service