# ChatController 接口文档

## 1. 接口概览

ChatController 提供与 AI 模型的对话接口，允许用户发送消息并获取 AI 的响应。

## 2. 接口列表

| 接口名称 | 请求方式 | 请求路径 | 功能描述 |
| :--- | :--- | :--- | :--- |
| 对话接口 | POST | /api/ai/chat | 与 AI 模型进行对话 |

## 3. 详细接口说明

### 3.1 对话接口

**请求方式**: POST
**请求路径**: /api/ai/chat
**功能描述**: 与 AI 模型进行对话，发送消息并获取 AI 的响应

**请求参数**:  

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| message | String | 是 | 对话消息内容（通过URL参数传递） |

**请求示例**:

```
http://localhost:8080/api/ai/chat?message=查看user用户都有什么权限
```

**响应体**:

```json
{
  "code": 200,
  "message": "success",
  "data": "你好！你提到\"查看 user 用户都有什么权限\"，但目前缺少具体的用户标识（如用户名、邮箱或用户ID），我无法直接定位该用户。\n\n此外，\"user\"可能是一个泛指，也可能是某个具体用户名。为了准确查询，请你提供以下任一信息：\n\n- 用户名（`username`）  \n- 用户邮箱（`email`）  \n- 或者用户ID（`userId`，若已知）\n\n一旦确认用户身份，我可以：\n1. 先查询该用户所属的角色（`userRoles`）  \n2. 再根据角色查询对应权限（`rolePermissions`）  \n\n请补充信息，我马上为你查 👇",
  "timestamp": 1775659191145
}
```

## 4. 响应格式说明

所有接口的响应格式统一为以下结构：

```json
{
  "code": 200, // 状态码，200表示成功
  "message": "success", // 响应消息
  "data": "AI 模型的响应内容", // 响应数据，AI模型的文本响应
  "timestamp": 1775659191145 // 响应时间戳
}
```

## 5. 错误响应

当请求失败时，响应格式如下：

```json
{
  "code": 400, // 错误状态码
  "message": "错误消息", // 错误描述
  "data": null,
  "timestamp": 1712119200000
}
```

常见错误状态码：
- 400: 请求参数错误
- 401: 未授权，token无效或过期
- 403: 禁止访问，权限不足
- 500: 服务器内部错误

## 6. 接口调用示例

### 6.1 使用curl调用对话接口

```bash
curl -X POST "http://localhost:8080/api/ai/chat?message=你好，AI！" \
  -H "Authorization: Bearer {token}"
```

### 6.2 使用Postman调用对话接口

1. 设置请求方式为 POST
2. 设置请求 URL 为 `http://localhost:8080/api/ai/chat?message=你好，AI！`
3. 在 Headers 中添加 `Authorization: Bearer {token}`
4. 点击发送按钮

## 7. 注意事项

1. 该接口需要 `agent:chat` 权限才能访问
2. 请求需要在头部携带有效的 token
3. 消息内容通过 URL 参数传递，使用 `message` 参数
4. 消息内容应该符合 AI 模型的输入要求
5. 接口会记录请求和响应日志，便于问题排查