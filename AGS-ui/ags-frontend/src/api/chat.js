import service from './axios'

// AI对话相关接口
export const chatApi = {
  // 发送消息
  sendMessage: (message) => {
    return service.post(`/ai/chat?message=${encodeURIComponent(message)}`)
  }
}