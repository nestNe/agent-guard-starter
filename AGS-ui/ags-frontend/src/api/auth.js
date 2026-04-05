import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    // 统一处理错误
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 未授权，跳转到登录页
          break
        case 403:
          // 禁止访问
          break
        case 404:
          // 接口不存在
          break
        case 500:
          // 服务器错误
          break
        default:
          // 其他错误
          break
      }
    }
    return Promise.reject(error)
  }
)

// 认证相关API
export const authApi = {
  // 注册
  register: (data) => {
    return api.post('/auth/register', data)
  },
  
  // 登录
  login: (data) => {
    return api.post('/auth/login', data)
  },
  
  // 刷新token
  refreshToken: (refreshToken) => {
    return api.post('/auth/refresh', {}, {
      headers: {
        'X-Refresh-Token': refreshToken
      }
    })
  },
  
  // 登出
  logout: () => {
    return api.post('/auth/logout')
  },
  
  // 获取当前用户信息
  getCurrentUser: () => {
    return api.get('/auth/me')
  }
}

// 权限相关API
export const permissionApi = {
  // 获取权限列表
  getPermissions: (params) => {
    return api.get('/permissions', { params })
  },
  
  // 获取所有权限
  getAllPermissions: () => {
    return api.get('/permissions/all')
  },
  
  // 按模块获取权限
  getPermissionsByModule: () => {
    return api.get('/permissions/by-module')
  },
  
  // 获取权限详情
  getPermissionById: (id) => {
    return api.get(`/permissions/${id}`)
  },
  
  // 创建权限
  createPermission: (data) => {
    return api.post('/permissions', data)
  },
  
  // 更新权限
  updatePermission: (id, data) => {
    return api.put(`/permissions/${id}`, data)
  },
  
  // 删除权限
  deletePermission: (id) => {
    return api.delete(`/permissions/${id}`)
  },
  
  // 更新权限状态
  updatePermissionStatus: (id, status) => {
    return api.put(`/permissions/${id}/status`, { status })
  }
}

// 角色相关API
export const roleApi = {
  // 获取角色列表
  getRoles: (params) => {
    return api.get('/roles', { params })
  },
  
  // 获取所有角色
  getAllRoles: () => {
    return api.get('/roles/all')
  },
  
  // 获取角色详情
  getRoleById: (id) => {
    return api.get(`/roles/${id}`)
  },
  
  // 创建角色
  createRole: (data) => {
    return api.post('/roles', data)
  },
  
  // 更新角色
  updateRole: (id, data) => {
    return api.put(`/roles/${id}`, data)
  },
  
  // 删除角色
  deleteRole: (id) => {
    return api.delete(`/roles/${id}`)
  },
  
  // 分配权限
  assignPermissions: (id, permissionIds) => {
    return api.put(`/roles/${id}/permissions`, permissionIds)
  },
  
  // 更新角色状态
  updateRoleStatus: (id, status) => {
    return api.put(`/roles/${id}/status`, { status })
  }
}

// 用户相关API
export const userApi = {
  // 获取用户列表
  getUsers: (params) => {
    return api.get('/users', { params })
  },
  
  // 获取用户详情
  getUserById: (id) => {
    return api.get(`/users/${id}`)
  },
  
  // 创建用户
  createUser: (data) => {
    return api.post('/users', data)
  },
  
  // 更新用户
  updateUser: (id, data) => {
    return api.put(`/users/${id}`, data)
  },
  
  // 删除用户
  deleteUser: (id) => {
    return api.delete(`/users/${id}`)
  },
  
  // 分配角色
  assignRoles: (id, roleIds) => {
    return api.put(`/users/${id}/roles`, roleIds)
  },
  
  // 更新用户状态
  updateUserStatus: (id, status) => {
    return api.put(`/users/${id}/status`, { status })
  }
}

export default api