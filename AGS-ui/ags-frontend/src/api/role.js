import service from './axios'

// 角色管理相关接口
export const roleApi = {
  // 获取角色列表
  getRoles: (params) => {
    return service.get('/roles', { params })
  },
  
  // 获取所有角色
  getAllRoles: () => {
    return service.get('/roles/all')
  },
  
  // 获取角色详情
  getRoleById: (id) => {
    return service.get(`/roles/${id}`)
  },
  
  // 创建角色
  createRole: (roleData) => {
    return service.post('/roles', roleData)
  },
  
  // 更新角色
  updateRole: (id, roleData) => {
    return service.put(`/roles/${id}`, roleData)
  },
  
  // 删除角色
  deleteRole: (id) => {
    return service.delete(`/roles/${id}`)
  },
  
  // 分配权限
  assignPermissions: (id, permissionIds) => {
    return service.put(`/roles/${id}/permissions`, permissionIds)
  },
  
  // 更新角色状态
  updateStatus: (id, status) => {
    return service.put(`/roles/${id}/status`, { status })
  }
}