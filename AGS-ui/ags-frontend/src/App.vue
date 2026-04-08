<template>
  <router-view />
</template>

<script setup>
// App组件
</script>

<style>
/* 全局样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: #1a1a1a;
  color: #fff;
  line-height: 1.6;
}

/* 滚动条全局样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #1a1a1a;
}

::-webkit-scrollbar-thumb {
  background: #333;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #444;
}

/* 通用按钮样式 */
button {
  font-family: inherit;
}

/* 通用输入框样式 */
input, textarea, select {
  font-family: inherit;
}
</style>
