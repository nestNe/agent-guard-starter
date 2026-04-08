<template>
  <div class="dashboard">
    <h2 class="page-title">首页</h2>
    <div class="dashboard-cards">
      <div class="card">
        <div class="card-icon">👥</div>
        <div class="card-content">
          <h3 class="card-title">用户数量</h3>
          <p class="card-value">100+</p>
        </div>
      </div>
      <div class="card">
        <div class="card-icon">🎭</div>
        <div class="card-content">
          <h3 class="card-title">角色数量</h3>
          <p class="card-value">10+</p>
        </div>
      </div>
      <div class="card">
        <div class="card-icon">🔒</div>
        <div class="card-content">
          <h3 class="card-title">权限数量</h3>
          <p class="card-value">50+</p>
        </div>
      </div>
      <div class="card">
        <div class="card-icon">💬</div>
        <div class="card-content">
          <h3 class="card-title">对话次数</h3>
          <p class="card-value">1000+</p>
        </div>
      </div>
    </div>
    <div class="dashboard-info">
      <h3 class="info-title">系统信息</h3>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">系统版本</span>
          <span class="info-value">v1.0.0</span>
        </div>
        <div class="info-item">
          <span class="info-label">最后更新</span>
          <span class="info-value">2026-04-08</span>
        </div>
        <div class="info-item">
          <span class="info-label">服务器状态</span>
          <span class="info-value status-online">在线</span>
        </div>
        <div class="info-item">
          <span class="info-label">API状态</span>
          <span class="info-value status-online">正常</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 首页组件
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #fff;
  margin: 0;
}

.dashboard-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
}

.card {
  background-color: #111;
  border: 1px solid #333;
  border-radius: 6px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.2s;
}

.card:hover {
  border-color: #4A9EFF;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-icon {
  font-size: 32px;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #222;
  border-radius: 4px;
}

.card-content {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #ccc;
  margin: 0 0 8px;
  font-weight: 500;
}

.card-value {
  font-size: 24px;
  font-weight: 600;
  color: #4A9EFF;
  margin: 0;
}

.dashboard-info {
  background-color: #111;
  border: 1px solid #333;
  border-radius: 6px;
  padding: 24px;
}

.info-title {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 20px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-label {
  font-size: 14px;
  color: #999;
}

.info-value {
  font-size: 16px;
  font-weight: 500;
  color: #ccc;
}

.status-online {
  color: #52c41a;
}

.status-offline {
  color: #ff4d4f;
}
</style>