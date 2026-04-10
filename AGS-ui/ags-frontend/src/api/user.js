import service from './axios'

// 用户管理相关接口
export const userApi = {
  // 获取用户列表
  getUsers: (params) => {
    return service.get('/users', { params })
  },
  
  // 获取用户详情
  getUserById: (id) => {
    return service.get(`/users/${id}`)
  },
  
  // 创建用户
  createUser: (userData) => {
    return service.post('/users', userData)
  },
  
  // 更新用户
  updateUser: (id, userData) => {
    return service.put(`/users/${id}`, userData)
  },
  
  // 删除用户
  deleteUser: (id) => {
    return service.delete(`/users/${id}`)
  },
  
  // 分配角色
  assignRoles: (id, roleIds) => {
    return service.put(`/users/${id}/roles`, roleIds)
  },
  
  // 更新用户状态
  updateStatus: (id, status) => {
    return service.put(`/users/${id}/status`, { status })
  }
}