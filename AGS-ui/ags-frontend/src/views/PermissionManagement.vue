<template>
  <div class="permission-management">
    <h2 class="page-title">权限管理</h2>
    <div class="page-actions">
      <button class="action-btn primary">添加权限</button>
      <div class="search-box">
        <input type="text" placeholder="搜索权限" />
        <button class="search-btn">🔍</button>
      </div>
    </div>
    <div class="permission-table">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>权限名称</th>
            <th>权限标识</th>
            <th>描述</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="permission in permissions" :key="permission.id">
            <td>{{ permission.id }}</td>
            <td>{{ permission.name }}</td>
            <td>{{ permission.code }}</td>
            <td>{{ permission.description || '-' }}</td>
            <td>{{ permission.createdAt }}</td>
            <td>
              <div class="action-buttons">
                <button class="btn edit">编辑</button>
                <button class="btn delete">删除</button>
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

// 模拟权限数据
const permissions = ref([
  {
    id: 1,
    name: '用户列表',
    code: 'user:list',
    description: '查看用户列表',
    createdAt: '2026-04-01 00:00:00'
  },
  {
    id: 2,
    name: '用户创建',
    code: 'user:create',
    description: '创建新用户',
    createdAt: '2026-04-01 00:00:00'
  },
  {
    id: 3,
    name: '用户更新',
    code: 'user:update',
    description: '更新用户信息',
    createdAt: '2026-04-01 00:00:00'
  },
  {
    id: 4,
    name: '用户删除',
    code: 'user:delete',
    description: '删除用户',
    createdAt: '2026-04-01 00:00:00'
  },
  {
    id: 5,
    name: 'AI对话',
    code: 'agent:chat',
    description: '与AI模型对话',
    createdAt: '2026-04-01 00:00:00'
  }
])

const currentPage = ref(1)
const totalPages = ref(2)
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
}

.permission-table th,
.permission-table td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #333;
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
</style>