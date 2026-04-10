<template>
  <div class="user-management">
    <h2 class="page-title">用户管理</h2>
    <div class="page-actions">
      <button class="action-btn primary" @click="openAddDialog">添加用户</button>
      <div class="search-box">
        <input type="text" v-model="keyword" placeholder="搜索用户" @keyup.enter="searchUsers" />
        <button class="search-btn" @click="searchUsers">🔍</button>
      </div>
    </div>
    <div class="user-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>邮箱</th>
            <th>状态</th>
            <th>角色</th>
            <th>最后登录时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>
              <span class="status-badge" :class="user.status === '启用' ? 'active' : 'inactive'">
                {{ user.status }}
              </span>
            </td>
            <td>
              <span class="role-tag" v-for="role in user.roles" :key="role">{{ role }}</span>
            </td>
            <td>{{ user.lastLoginAt || '-' }}</td>
            <td>
              <div class="action-buttons">
                <button class="btn edit" @click="openEditDialog(user)">编辑</button>
                <button class="btn delete" @click="openDeleteDialog(user.id)">删除</button>
                <button class="btn assign-role" @click="openAssignRoleDialog(user)">分配角色</button>
              </div>
            </td>
          </tr>
          <tr v-if="loading">
            <td colspan="8" class="loading">加载中...</td>
          </tr>
          <tr v-else-if="users.length === 0">
            <td colspan="8" class="empty">暂无用户数据</td>
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

    <!-- 添加用户对话框 -->
    <div class="dialog" v-if="showAddDialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>添加用户</h3>
          <button class="close-btn" @click="showAddDialog = false">&times;</button>
        </div>
        <div class="dialog-body">
          <div class="form-item">
            <label>用户名</label>
            <input type="text" v-model="formData.username" placeholder="请输入用户名" />
          </div>
          <div class="form-item">
            <label>邮箱</label>
            <input type="email" v-model="formData.email" placeholder="请输入邮箱" />
          </div>
          <div class="form-item">
            <label>密码</label>
            <input type="password" v-model="formData.password" placeholder="请输入密码" />
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
          <button class="btn confirm" @click="addUser">确定</button>
        </div>
      </div>
    </div>

    <!-- 编辑用户对话框 -->
    <div class="dialog" v-if="showEditDialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>编辑用户</h3>
          <button class="close-btn" @click="showEditDialog = false">&times;</button>
        </div>
        <div class="dialog-body">
          <div class="form-item">
            <label>用户名</label>
            <input type="text" v-model="formData.username" placeholder="请输入用户名" />
          </div>
          <div class="form-item">
            <label>邮箱</label>
            <input type="email" v-model="formData.email" placeholder="请输入邮箱" />
          </div>
          <div class="form-item">
            <label>密码</label>
            <input type="password" v-model="formData.password" placeholder="请输入密码（留空则不修改）" />
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
          <button class="btn confirm" @click="updateUser">确定</button>
        </div>
      </div>
    </div>

    <!-- 删除用户对话框 -->
    <div class="dialog" v-if="showDeleteDialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>删除用户</h3>
          <button class="close-btn" @click="showDeleteDialog = false">&times;</button>
        </div>
        <div class="dialog-body">
          <p>确定要删除该用户吗？</p>
        </div>
        <div class="dialog-footer">
          <button class="btn cancel" @click="showDeleteDialog = false">取消</button>
          <button class="btn confirm danger" @click="deleteUser">确定</button>
        </div>
      </div>
    </div>

    <!-- 分配角色对话框 -->
    <div class="dialog" v-if="showAssignRoleDialog">
      <div class="dialog-content">
        <div class="dialog-header">
          <h3>分配角色 - {{ selectedUser?.username }}</h3>
          <button class="close-btn" @click="showAssignRoleDialog = false">&times;</button>
        </div>
        <div class="dialog-body">
          <div class="role-list">
            <div class="role-item" v-for="role in roles" :key="role.id">
              <input 
                type="checkbox" 
                :id="`role-${role.id}`" 
                :value="role.id" 
                v-model="selectedRoleIds"
              />
              <label :for="`role-${role.id}`">{{ role.label }}</label>
            </div>
          </div>
        </div>
        <div class="dialog-footer">
          <button class="btn cancel" @click="showAssignRoleDialog = false">取消</button>
          <button class="btn confirm" @click="assignRoles">确定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '../api/user'
import { roleApi } from '../api/role'

// 用户数据
const users = ref([])
const currentPage = ref(1)
const totalPages = ref(1)
const loading = ref(false)
const keyword = ref('')

// 对话框状态
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)
const showAssignRoleDialog = ref(false)

// 表单数据
const formData = ref({
  username: '',
  email: '',
  password: '',
  status: '1'
})

// 选中的用户
const selectedUser = ref(null)
const selectedUserId = ref(null)

// 角色数据
const roles = ref([])
const selectedRoleIds = ref([])

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

// 获取用户列表
const getUsers = async () => {
  loading.value = true
  try {
    const response = await userApi.getUsers({
      current: currentPage.value,
      size: 10,
      keyword: keyword.value
    })
    
    users.value = response.data.records || []
    totalPages.value = response.data.pages || 1
  } catch (error) {
    console.error('获取用户列表失败:', error)
    showMsg('获取用户列表失败', 'error')
  } finally {
    loading.value = false
  }
}

// 搜索用户
const searchUsers = () => {
  currentPage.value = 1
  getUsers()
}

// 分页
const changePage = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
  getUsers()
}

// 打开添加对话框
const openAddDialog = () => {
  formData.value = {
    username: '',
    email: '',
    password: '',
    status: '1'
  }
  showAddDialog.value = true
}

// 打开编辑对话框
const openEditDialog = (user) => {
  selectedUser.value = { ...user }
  formData.value = {
    username: user.username,
    email: user.email,
    password: '',
    status: user.status === '启用' ? '1' : '0'
  }
  showEditDialog.value = true
}

// 打开删除对话框
const openDeleteDialog = (userId) => {
  selectedUserId.value = userId
  showDeleteDialog.value = true
}

// 打开分配角色对话框
const openAssignRoleDialog = async (user) => {
  selectedUser.value = { ...user }
  selectedUserId.value = user.id
  
  try {
    // 获取所有角色
    const response = await roleApi.getAllRoles()
    roles.value = response.data || []
    
    // 初始化选中的角色
    if (user.roles && user.roles.length > 0) {
      selectedRoleIds.value = roles.value
        .filter(role => user.roles.includes(role.name))
        .map(role => role.id)
    } else {
      selectedRoleIds.value = []
    }
    showAssignRoleDialog.value = true
  } catch (error) {
    console.error('获取角色列表失败:', error)
    showMsg('获取角色列表失败', 'error')
  }
}

// 添加用户
const addUser = async () => {
  try {
    await userApi.createUser(formData.value)
    showMsg('添加用户成功')
    showAddDialog.value = false
    getUsers()
  } catch (error) {
    console.error('添加用户失败:', error)
    showMsg('添加用户失败', 'error')
  }
}

// 更新用户
const updateUser = async () => {
  try {
    await userApi.updateUser(selectedUser.value.id, formData.value)
    showMsg('更新用户成功')
    showEditDialog.value = false
    getUsers()
  } catch (error) {
    console.error('更新用户失败:', error)
    showMsg('更新用户失败', 'error')
  }
}

// 删除用户
const deleteUser = async () => {
  try {
    await userApi.deleteUser(selectedUserId.value)
    showMsg('删除用户成功')
    showDeleteDialog.value = false
    getUsers()
  } catch (error) {
    console.error('删除用户失败:', error)
    showMsg('删除用户失败', 'error')
  }
}

// 分配角色
const assignRoles = async () => {
  try {
    await userApi.assignRoles(selectedUserId.value, selectedRoleIds.value)
    showMsg('分配角色成功')
    showAssignRoleDialog.value = false
    getUsers()
  } catch (error) {
    console.error('分配角色失败:', error)
    showMsg('分配角色失败', 'error')
  }
}

// 组件挂载时获取数据
onMounted(() => {
  getUsers()
})
</script>

<style scoped>
.user-management {
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

.user-table {
  background-color: #111;
  border: 1px solid #333;
  border-radius: 6px;
  overflow: hidden;
}

.user-table table {
  width: 100%;
  border-collapse: collapse;
}

.user-table th,
.user-table td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #333;
}

.user-table th {
  background-color: #1a1a1a;
  color: #ccc;
  font-weight: 600;
  font-size: 14px;
}

.user-table td {
  color: #fff;
  font-size: 14px;
}

.user-table tr:hover {
  background-color: #1a1a1a;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.active {
  background-color: rgba(82, 196, 26, 0.2);
  color: #52c41a;
}

.status-badge.inactive {
  background-color: rgba(255, 77, 79, 0.2);
  color: #ff4d4f;
}

/* 角色标签 */
.role-tag {
  display: inline-block;
  padding: 2px 8px;
  background-color: rgba(74, 144, 255, 0.2);
  color: #4A9EFF;
  border: 1px solid rgba(74, 144, 255, 0.4);
  border-radius: 4px;
  font-size: 12px;
  margin-right: 4px;
  margin-bottom: 4px;
}

.pagination {
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

.btn.assign-role {
  background-color: #1a1a1a;
  color: #faad14;
  border-color: #faad14;
}

.btn.assign-role:hover {
  background-color: rgba(250, 173, 20, 0.1);
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
.form-item select {
  width: 100%;
  padding: 8px 12px;
  background-color: #2a2a2a;
  border: 1px solid #333;
  border-radius: 4px;
  color: #fff;
  font-size: 14px;
}

.form-item input:focus,
.form-item select:focus {
  outline: none;
  border-color: #4A9EFF;
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

/* 角色列表 */
.role-list {
  max-height: 300px;
  overflow-y: auto;
}

.role-item {
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.role-item input[type="checkbox"] {
  margin-right: 8px;
}

.role-item label {
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}
</style>