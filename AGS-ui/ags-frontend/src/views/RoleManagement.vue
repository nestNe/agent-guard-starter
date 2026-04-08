<template>
  <div class="role-management">
    <h2 class="page-title">角色管理</h2>
    <div class="page-actions">
      <button class="action-btn primary">添加角色</button>
      <div class="search-box">
        <input type="text" placeholder="搜索角色" />
        <button class="search-btn">🔍</button>
      </div>
    </div>
    <div class="role-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>角色名称</th>
            <th>角色标识</th>
            <th>描述</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="role in roles" :key="role.id">
            <td>{{ role.id }}</td>
            <td>{{ role.name }}</td>
            <td>{{ role.code }}</td>
            <td>{{ role.description || '-' }}</td>
            <td>{{ role.createdAt }}</td>
            <td>
              <div class="action-buttons">
                <button class="btn edit">编辑</button>
                <button class="btn delete">删除</button>
                <button class="btn assign-perm">分配权限</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="pagination">
      <button class="page-btn" :disabled="currentPage === 1">上一页</button>
      <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
      <button class="page-btn" :disabled="currentPage === totalPages">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 模拟角色数据
const roles = ref([
  {
    id: 1,
    name: '超级管理员',
    code: 'super_admin',
    description: '拥有所有权限',
    createdAt: '2026-04-01 00:00:00'
  },
  {
    id: 2,
    name: '管理员',
    code: 'admin',
    description: '拥有管理权限',
    createdAt: '2026-04-02 00:00:00'
  },
  {
    id: 3,
    name: '普通用户',
    code: 'user',
    description: '拥有基础权限',
    createdAt: '2026-04-03 00:00:00'
  }
])

const currentPage = ref(1)
const totalPages = ref(1)
</script>

<style scoped>
.role-management {
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

.role-table {
  background-color: #111;
  border: 1px solid #333;
  border-radius: 6px;
  overflow: hidden;
}

.role-table table {
  width: 100%;
  border-collapse: collapse;
}

.role-table th,
.role-table td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #333;
}

.role-table th {
  background-color: #1a1a1a;
  color: #ccc;
  font-weight: 600;
  font-size: 14px;
}

.role-table td {
  color: #fff;
  font-size: 14px;
}

.role-table tr:hover {
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

.btn.assign-perm {
  background-color: #1a1a1a;
  color: #faad14;
  border-color: #faad14;
}

.btn.assign-perm:hover {
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
</style>