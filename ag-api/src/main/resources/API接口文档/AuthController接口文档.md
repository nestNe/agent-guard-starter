# AuthController 接口文档

## 1. 接口概览

AuthController 提供用户认证相关的接口，包括用户注册、登录、刷新token、登出和获取当前用户信息等功能。

## 2. 接口列表

| 接口名称 | 请求方式 | 请求路径 | 功能描述 |
| :--- | :--- | :--- | :--- |
| 用户注册 | POST | /api/auth/register | 注册新用户 |
| 用户登录 | POST | /api/auth/login | 用户登录并获取token |
| 刷新token | POST | /api/auth/refresh | 使用刷新token获取新的访问token |
| 用户登出 | POST | /api/auth/logout | 用户登出 |
| 获取当前用户 | GET | /api/auth/me | 获取当前登录用户信息 |

## 3. 详细接口说明

### 3.1 用户注册

**请求方式**: POST
**请求路径**: /api/auth/register
**功能描述**: 注册新用户

**请求体**:
```json
{
  "username": "user1",
  "email": "user1@example.com",
  "password": "password123"
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| username | String | 是 | 用户名，长度3-64个字符 |
| email | String | 是 | 邮箱，格式正确 |
| password | String | 是 | 密码，长度6-20个字符 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "user1",
    "email": "user1@example.com",
    "avatar": null,
    "status": 1,
    "lastLoginAt": null,
    "createdAt": "2026-04-03T10:00:00Z"
  },
  "timestamp": 1712119200000
}
```

### 3.2 用户登录

**请求方式**: POST
**请求路径**: /api/auth/login
**功能描述**: 用户登录并获取token

**请求体**:
```json
{
  "account": "user1",
  "password": "password123"
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| account | String | 是 | 用户名或邮箱 |
| password | String | 是 | 密码 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "user1",
      "email": "user1@example.com",
      "avatar": null,
      "status": 1
    }
  },
  "timestamp": 1712119200000
}
```

### 3.3 刷新token

**请求方式**: POST
**请求路径**: /api/auth/refresh
**功能描述**: 使用刷新token获取新的访问token

**请求头**:

| 头部名称 | 值 | 描述 |
| :--- | :--- | :--- |
| X-Refresh-Token | 刷新token | 用于获取新的访问token |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "username": "user1",
      "email": "user1@example.com",
      "avatar": null,
      "status": 1
    }
  },
  "timestamp": 1712119200000
}
```

### 3.4 用户登出

**请求方式**: POST
**请求路径**: /api/auth/logout
**功能描述**: 用户登出

**请求头**:

| 头部名称 | 值 | 描述 |
| :--- | :--- | :--- |
| Authorization | Bearer {token} | 访问token |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1712119200000
}
```

### 3.5 获取当前用户

**请求方式**: GET
**请求路径**: /api/auth/me
**功能描述**: 获取当前登录用户信息

**请求头**:

| 头部名称 | 值 | 描述 |
| :--- | :--- | :--- |
| Authorization | Bearer {token} | 访问token |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "seehold@gmail.com",
    "avatar": null,
    "status": "启用",
    "lastLoginAt": "2026-04-06 23:47:26",
    "createdAt": "2026-04-03 03:16:07",
    "roles": [
      "super_admin"
    ],
    "permissions": [
      "user:list",
      "user:create",
      "user:update",
      "user:delete",
      "user:view",
      "user:assign-role",
      "role:list",
      "role:create",
      "role:update",
      "role:delete",
      "role:view",
      "role:assign-perm",
      "perm:list",
      "perm:create",
      "perm:update",
      "perm:delete",
      "perm:view",
      "agent:chat"
    ]
  },
  "timestamp": 1775490889340
}
```

## 4. 响应格式说明

所有接口的响应格式统一为以下结构：

```json
{
  "code": 200, // 状态码，200表示成功
  "message": "success", // 响应消息
  "data": {}, // 响应数据，根据接口不同而不同
  "timestamp": 1712119200000 // 响应时间戳
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
- 404: 资源不存在
- 500: 服务器内部错误

## 6. 接口调用示例

### 6.1 使用curl调用登录接口

```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"account": "user1", "password": "password123"}'
```

### 6.2 使用curl调用获取当前用户接口

```bash
curl -X GET "http://localhost:8080/api/auth/me" \
  -H "Authorization: Bearer {token}"
```

## 7. 注意事项

1. 所有需要认证的接口（如登出、获取当前用户）都需要在请求头中携带有效的token
2. 密码必须符合长度要求（6-20个字符）
3. 用户名和邮箱必须唯一
4. 刷新token的有效期通常比访问token长，用于在访问token过期后获取新的访问token
5. 登出后，token将被失效，需要重新登录获取新的token
