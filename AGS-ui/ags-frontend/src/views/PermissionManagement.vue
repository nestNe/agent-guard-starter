<template>
  <div class="permission-management">
    <h2 class="page-title">权限管理</h2>
    <div class="page-actions">
      <button class="action-btn primary" @click="openAddDialog">添加权限</button>
      <div class="search-box">
        <input type="text" v-model="keyword" placeholder="搜索权限" @keyup.enter="searchPermissions" />
        <button class="search-btn" @click="searchPermissions">🔍</button>
      </div>
    </div>
    <div class="permission-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>权限名称</th>
            <th>权限标识</th>
            <th>模块</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="permission in permissions" :key="permission.id">
            <td>{{ permission.id }}</td>
            <td>{{ permission.label }}</td>
            <td>{{ permission.name }}</td>
            <td>{{ permission.module }}</td>
            <td>
              <span class="status-badge" :class="permission.status === '启用' ? 'active' : 'inactive'">
                {{ permission.status }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <button class="btn edit" @click="openEditDialog(permission)">编辑</button>
                <button class="btn delete" @click="openDeleteDialog(permission.id)">删除</button>
              </div>
            </td>
          </tr>
          <tr v-if="loading">
            <td colspan="6" class="loading">加载中...</td>
          </tr>
          <tr v-else-if="permissions.length === 0">
            <td colspan="6" class="empty">暂无权限数据</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="pagination">
      <button class="page-btn" :disabled="currentPage === 1" @click="changePage(currentPage - 1)">上一页</button>
      <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
      <button class="page-btn" :disabled="currentPage === totalPages" @click="changePage(currentPage + 1)">下一页</button>
    </div>

    <!-- 消息提示 -->
    <div class="message" :class="messageType" v-if="showMessage">
      {{ message }}
    </div>

    <!-- 添加权限对话框 -->
    <div class="dialog" v-if="showAddDialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>添加权限</h3>
          <button class="close-btn" @click="showAddDialog = false">&times;</button>
        </div>
        <div class="dialog-body">
          <div class="form-item">
            <label>权限名称</label>
            <input type="text" v-model="formData.label" placeholder="请输入权限名称" />
          </div>
          <div class="form-item">
            <label>权限标识</label>
            <input type="text" v-model="formData.name" placeholder="请输入权限标识" />
          </div>
          <div class="form-item">
            <label>模块</label>
            <input type="text" v-model="formData.module" placeholder="请输入模块" />
          </div>
          <div class="form-item">
            <label>描述</label>
            <textarea v-model="formData.description" placeholder="请输入权限描述" rows="3"></textarea>
          </div>
          <div class="form-item">
            <label>状态</label>
            <select v-model="formData.status">
              <option value="1">启用</option>
              <option value="0">禁用</option>
            </select>
          </div>
        </div>
        <div class="dialog-footer">
          <button class="btn cancel" @click="showAddDialog = false">取消</button>
          <button class="btn confirm" @click="addPermission">确定</button>
        </div>
      </div>
    </div>

    <!-- 编辑权限对话框 -->
    <div class="dialog" v-if="showEditDialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>编辑权限</h3>
          <button class="close-btn" @click="showEditDialog = false">&times;</button>
        </div>
        <div class="dialog-body">
          <div class="form-item">
            <label>权限名称</label>
            <input type="text" v-model="formData.label" placeholder="请输入权限名称" />
          </div>
          <div class="form-item">
            <label>权限标识</label>
            <input type="text" v-model="formData.name" placeholder="请输入权限标识" />
          </div>
          <div class="form-item">
            <label>模块</label>
            <input type="text" v-model="formData.module" placeholder="请输入模块" />
          </div>
          <div class="form-item">
            <label>描述</label>
            <textarea v-model="formData.description" placeholder="请输入权限描述" rows="3"></textarea>
          </div>
          <div class="form-item">
            <label>状态</label>
            <select v-model="formData.status">
              <option value="1">启用</option>
              <option value="0">禁用</option>
            </select>
          </div>
        </div>
        <div class="dialog-footer">
          <button class="btn cancel" @click="showEditDialog = false">取消</button>
          <button class="btn confirm" @click="updatePermission">确定</button>
        </div>
      </div>
    </div>

    <!-- 删除权限对话框 -->
    <div class="dialog" v-if="showDeleteDialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>删除权限</h3>
          <button class="close-btn" @click="showDeleteDialog = false">&times;</button>
        </div>
        <div class="dialog-body">
          <p>确定要删除该权限吗？</p>
        </div>
        <div class="dialog-footer">
          <button class="btn cancel" @click="showDeleteDialog = false">取消</button>
          <button class="btn confirm danger" @click="deletePermission">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { permissionApi } from '../api/permission'

// 权限数据
const permissions = ref([])
const currentPage = ref(1)
const totalPages = ref(1)
const loading = ref(false)
const keyword = ref('')
const module = ref('')

// 对话框状态
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)

// 表单数据
const formData = ref({
  name: '',
  label: '',
  module: '',
  description: '',
  status: '1'
})

// 选中的权限
const selectedPermission = ref(null)
const selectedPermissionId = ref(null)

// 消息提示
const message = ref('')
const showMessage = ref(false)
const messageType = ref('success') // success, error

// 显示消息提示
const showMsg = (msg, type = 'success') => {
  message.value = msg
  messageType.value = type
  showMessage.value = true
  setTimeout(() => {
    showMessage.value = false
  }, 3000)
}

// 获取权限列表
const getPermissions = async () => {
  loading.value = true
  try {
    const response = await permissionApi.getPermissions({
      current: currentPage.value,
      size: 10,
      keyword: keyword.value,
      module: module.value
    })
    
    permissions.value = response.data.records || []
    totalPages.value = response.data.pages || 1
  } catch (error) {
    console.error('获取权限列表失败:', error)
    showMsg('获取权限列表失败', 'error')
  } finally {
    loading.value = false
  }
}

// 搜索权限
const searchPermissions = () => {
  currentPage.value = 1
  getPermissions()
}

// 分页
const changePage = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
  getPermissions()
}

// 打开添加对话框
const openAddDialog = () => {
  formData.value = {
    name: '',
    label: '',
    module: '',
    description: '',
    status: '1'
  }
  showAddDialog.value = true
}

// 打开编辑对话框
const openEditDialog = (permission) => {
  selectedPermission.value = { ...permission }
  formData.value = {
    name: permission.name,
    label: permission.label,
    module: permission.module,
    description: permission.description || '',
    status: permission.status === '启用' ? '1' : '0'
  }
  showEditDialog.value = true
}

// 打开删除对话框
const openDeleteDialog = (permissionId) => {
  selectedPermissionId.value = permissionId
  showDeleteDialog.value = true
}

// 添加权限
const addPermission = async () => {
  try {
    await permissionApi.createPermission(formData.value)
    showMsg('添加权限成功')
    showAddDialog.value = false
    getPermissions()
  } catch (error) {
    console.error('添加权限失败:', error)
    showMsg('添加权限失败', 'error')
  }
}

// 更新权限
const updatePermission = async () => {
  try {
    await permissionApi.updatePermission(selectedPermission.value.id, formData.value)
    showMsg('更新权限成功')
    showEditDialog.value = false
    getPermissions()
  } catch (error) {
    console.error('更新权限失败:', error)
    showMsg('更新权限失败', 'error')
  }
}

// 删除权限
const deletePermission = async () => {
  try {
    await permissionApi.deletePermission(selectedPermissionId.value)
    showMsg('删除权限成功')
    showDeleteDialog.value = false
    getPermissions()
  } catch (error) {
    console.error('删除权限失败:', error)
    showMsg('删除权限失败', 'error')
  }
}

// 组件挂载时获取数据
onMounted(() => {
  getPermissions()
})
</script>

<style scoped>
.permission-management {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #fff;
  margin: 0;
}

.page-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.action-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.action-btn.primary {
  background-color: #4A9EFF;
  color: #fff;
}

.action-btn.primary:hover {
  background-color: #3A8EFF;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-box input {
  padding: 10px 16px;
  background-color: #222;
  border: 1px solid #333;
  border-radius: 4px;
  color: #fff;
  font-size: 14px;
  width: 240px;
}

.search-box input:focus {
  outline: none;
  border-color: #4A9EFF;
}

.search-btn {
  padding: 10px 16px;
  background-color: #333;
  border: 1px solid #444;
  border-radius: 4px;
  color: #fff;
  cursor: pointer;
  transition: all 0.2s;
}

.search-btn:hover {
  background-color: #444;
}

.permission-table {
  background-color: #111;
  border: 1px solid #333;
  border-radius: 6px;
  overflow: hidden;
}

.permission-table table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.permission-table th,
.permission-table td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.permission-table th:nth-child(1),
.permission-table td:nth-child(1) {
  width: 60px;
}

.permission-table th:nth-child(2),
.permission-table td:nth-child(2) {
  width: 120px;
}

.permission-table th:nth-child(3),
.permission-table td:nth-child(3) {
  width: 150px;
}

.permission-table th:nth-child(4),
.permission-table td:nth-child(4) {
  width: 100px;
}

.permission-table th:nth-child(5),
.permission-table td:nth-child(5) {
  width: 100px;
}

.permission-table th:nth-child(6),
.permission-table td:nth-child(6) {
  width: 120px;
}

.permission-table th {
  background-color: #1a1a1a;
  color: #ccc;
  font-weight: 600;
  font-size: 14px;
}

.permission-table td {
  color: #fff;
  font-size: 14px;
}

.permission-table tr:hover {
  background-color: #1a1a1a;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.btn {
  padding: 6px 12px;
  border: 1px solid #333;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.btn.edit {
  background-color: #1a1a1a;
  color: #4A9EFF;
  border-color: #4A9EFF;
}

.btn.edit:hover {
  background-color: rgba(74, 158, 255, 0.1);
}

.btn.delete {
  background-color: #1a1a1a;
  color: #ff4d4f;
  border-color: #ff4d4f;
}

.btn.delete:hover {
  background-color: rgba(255, 77, 79, 0.1);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 20px;
}

.page-btn {
  padding: 8px 16px;
  background-color: #1a1a1a;
  border: 1px solid #333;
  border-radius: 4px;
  color: #fff;
  cursor: pointer;
  transition: all 0.2s;
}

.page-btn:hover:not(:disabled) {
  background-color: #333;
}

.page-btn:disabled {
  color: #666;
  cursor: not-allowed;
}

.page-info {
  color: #ccc;
  font-size: 14px;
}

.loading,
.empty {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 14px;
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.active {
  background-color: rgba(82, 196, 26, 0.2);
  color: #52c41a;
  border: 1px solid rgba(82, 196, 26, 0.4);
}

.status-badge.inactive {
  background-color: rgba(255, 77, 79, 0.2);
  color: #ff4d4f;
  border: 1px solid rgba(255, 77, 79, 0.4);
}

/* 消息提示 */
.message {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 12px 20px;
  border-radius: 4px;
  color: #fff;
  font-size: 14px;
  z-index: 1000;
  animation: slideIn 0.3s ease-out;
}

.message.success {
  background-color: #52c41a;
}

.message.error {
  background-color: #ff4d4f;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 对话框 */
.dialog {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog-content {
  background-color: #1a1a1a;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.5);
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #333;
}

.dialog-header h3 {
  color: #fff;
  margin: 0;
  font-size: 16px;
}

.close-btn {
  background: none;
  border: none;
  color: #999;
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  color: #fff;
}

.dialog-body {
  padding: 16px;
}

.form-item {
  margin-bottom: 16px;
}

.form-item label {
  display: block;
  color: #ccc;
  font-size: 14px;
  margin-bottom: 8px;
}

.form-item input,
.form-item select,
.form-item textarea {
  width: 100%;
  padding: 8px 12px;
  background-color: #2a2a2a;
  border: 1px solid #333;
  border-radius: 4px;
  color: #fff;
  font-size: 14px;
}

.form-item input:focus,
.form-item select:focus,
.form-item textarea:focus {
  outline: none;
  border-color: #4A9EFF;
}

.form-item textarea {
  resize: vertical;
  min-height: 80px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px;
  border-top: 1px solid #333;
}

.btn.cancel {
  background-color: #1a1a1a;
  color: #ccc;
  border-color: #333;
}

.btn.cancel:hover {
  background-color: #333;
}

.btn.confirm {
  background-color: #4A9EFF;
  color: #fff;
  border-color: #4A9EFF;
}

.btn.confirm:hover {
  background-color: #69b1ff;
}

.btn.confirm.danger {
  background-color: #ff4d4f;
  border-color: #ff4d4f;
}

.btn.confirm.danger:hover {
  background-color: #ff7875;
}
</style>