# UserController 接口文档

## 1. 接口概览

UserController 提供用户管理相关的接口，包括用户列表查询、用户详情获取、用户创建、用户更新、用户删除、角色分配和用户状态更新等功能。

## 2. 接口列表

| 接口名称 | 请求方式 | 请求路径 | 功能描述 | 权限要求 |
| :--- | :--- | :--- | :--- | :--- |
| 获取用户列表 | GET | /api/users | 分页获取用户列表 | user:list |
| 获取用户详情 | GET | /api/users/{id} | 获取单个用户详情 | user:view |
| 创建用户 | POST | /api/users | 创建新用户 | user:create |
| 更新用户 | PUT | /api/users/{id} | 更新用户信息 | user:update |
| 删除用户 | DELETE | /api/users/{id} | 删除用户 | user:delete |
| 分配角色 | PUT | /api/users/{id}/roles | 为用户分配角色 | user:assign-role |
| 更新用户状态 | PUT | /api/users/{id}/status | 更新用户状态 | user:update |

## 3. 详细接口说明

### 3.1 获取用户列表

**请求方式**: GET
**请求路径**: /api/users
**功能描述**: 分页获取用户列表
**权限要求**: user:list

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| current | Long | 否 | 当前页码，默认1 |
| size | Long | 否 | 每页大小，默认10 |
| keyword | String | 否 | 搜索关键词 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "user1",
        "email": "user1@example.com",
        "avatar": null,
        "status": 1,
        "lastLoginAt": "2026-04-03T10:00:00Z",
        "createdAt": "2026-04-02T10:00:00Z"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  },
  "timestamp": 1712119200000
}
```

### 3.2 获取用户详情

**请求方式**: GET
**请求路径**: /api/users/{id}
**功能描述**: 获取单个用户详情
**权限要求**: user:view

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 用户ID |

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
    "lastLoginAt": "2026-04-03T10:00:00Z",
    "createdAt": "2026-04-02T10:00:00Z",
    "roles": [
      {
        "id": 1,
        "name": "admin",
        "label": "后台管理员"
      }
    ]
  },
  "timestamp": 1712119200000
}
```

### 3.3 创建用户

**请求方式**: POST
**请求路径**: /api/users
**功能描述**: 创建新用户
**权限要求**: user:create

**请求体**:
```json
{
  "username": "user2",
  "email": "user2@example.com",
  "password": "password123",
  "status": 1
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| username | String | 是 | 用户名 |
| email | String | 是 | 邮箱 |
| password | String | 是 | 密码 |
| status | Integer | 是 | 状态：1=正常 0=禁用 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 2,
    "username": "user2",
    "email": "user2@example.com",
    "avatar": null,
    "status": 1,
    "lastLoginAt": null,
    "createdAt": "2026-04-03T10:00:00Z"
  },
  "timestamp": 1712119200000
}
```

### 3.4 更新用户

**请求方式**: PUT
**请求路径**: /api/users/{id}
**功能描述**: 更新用户信息
**权限要求**: user:update

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 用户ID |

**请求体**:
```json
{
  "username": "user1_updated",
  "email": "user1_updated@example.com",
  "status": 1
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| username | String | 是 | 用户名 |
| email | String | 是 | 邮箱 |
| status | Integer | 是 | 状态：1=正常 0=禁用 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "user1_updated",
    "email": "user1_updated@example.com",
    "avatar": null,
    "status": 1,
    "lastLoginAt": "2026-04-03T10:00:00Z",
    "createdAt": "2026-04-02T10:00:00Z"
  },
  "timestamp": 1712119200000
}
```

### 3.5 删除用户

**请求方式**: DELETE
**请求路径**: /api/users/{id}
**功能描述**: 删除用户
**权限要求**: user:delete

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 用户ID |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1712119200000
}
```

### 3.6 分配角色

**请求方式**: PUT
**请求路径**: /api/users/{id}/roles
**功能描述**: 为用户分配角色
**权限要求**: user:assign-role

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 用户ID |

**请求体**:
```json
[1, 2]
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| roleIds | List<Long> | 是 | 角色ID列表 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1712119200000
}
```

### 3.7 更新用户状态

**请求方式**: PUT
**请求路径**: /api/users/{id}/status
**功能描述**: 更新用户状态
**权限要求**: user:update

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 用户ID |

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| status | Integer | 是 | 状态：1=正常 0=禁用 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1712119200000
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

### 6.1 使用curl调用获取用户列表

```bash
curl -X GET "http://localhost:8080/api/users?current=1&size=10&keyword=user" \
  -H "Authorization: Bearer {token}"
```

### 6.2 使用curl调用创建用户

```bash
curl -X POST "http://localhost:8080/api/users" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"username": "user2", "email": "user2@example.com", "password": "password123", "status": 1}'
```

## 7. 注意事项

1. 所有接口都需要在请求头中携带有效的token
2. 操作用户时需要对应的权限
3. 创建用户时，密码会自动进行加密处理
4. 分配角色时，会覆盖用户原有的角色
5. 删除用户时，会级联删除用户与角色的关联关系
