import service from './axios'

// 权限管理相关接口
export const permissionApi = {
  // 获取权限列表
  getPermissions: (params) => {
    return service.get('/permissions', { params })
  },
  
  // 获取所有权限
  getAllPermissions: () => {
    return service.get('/permissions/all')
  },
  
  // 按模块获取权限
  getPermissionsByModule: () => {
    return service.get('/permissions/by-module')
  },
  
  // 获取权限详情
  getPermissionById: (id) => {
    return service.get(`/permissions/${id}`)
  },
  
  // 创建权限
  createPermission: (permissionData) => {
    return service.post('/permissions', permissionData)
  },
  
  // 更新权限
  updatePermission: (id, permissionData) => {
    return service.put(`/permissions/${id}`, permissionData)
  },
  
  // 删除权限
  deletePermission: (id) => {
    return service.delete(`/permissions/${id}`)
  },
  
  // 更新权限状态
  updateStatus: (id, status) => {
    return service.put(`/permissions/${id}/status`, { status })
  }
}