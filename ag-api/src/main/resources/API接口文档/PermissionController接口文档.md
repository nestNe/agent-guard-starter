# PermissionController 接口文档

## 1. 接口概览

PermissionController 提供权限管理相关的接口，包括权限列表查询、权限详情获取、按模块获取权限、权限创建、权限更新、权限删除和权限状态更新等功能。

## 2. 接口列表

| 接口名称 | 请求方式 | 请求路径 | 功能描述 | 权限要求 |
| :--- | :--- | :--- | :--- | :--- |
| 获取权限列表 | GET | /api/permissions | 分页获取权限列表 | perm:list |
| 获取所有权限 | GET | /api/permissions/all | 获取所有权限（不分页） | perm:list |
| 按模块获取权限 | GET | /api/permissions/by-module | 按模块分组获取权限 | perm:list |
| 获取权限详情 | GET | /api/permissions/{id} | 获取单个权限详情 | perm:view |
| 创建权限 | POST | /api/permissions | 创建新权限 | perm:create |
| 更新权限 | PUT | /api/permissions/{id} | 更新权限信息 | perm:update |
| 删除权限 | DELETE | /api/permissions/{id} | 删除权限 | perm:delete |
| 更新权限状态 | PUT | /api/permissions/{id}/status | 更新权限状态 | perm:update |

## 3. 详细接口说明

### 3.1 获取权限列表

**请求方式**: GET
**请求路径**: /api/permissions
**功能描述**: 分页获取权限列表
**权限要求**: perm:list

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| current | Long | 否 | 当前页码，默认1 |
| size | Long | 否 | 每页大小，默认10 |
| keyword | String | 否 | 搜索关键词 |
| module | String | 否 | 模块名称 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "user:list",
        "label": "用户列表",
        "module": "user",
        "status": 1,
        "createdAt": "2026-04-02T10:00:00Z"
      }
    ],
    "total": 17,
    "size": 10,
    "current": 1,
    "pages": 2
  },
  "timestamp": 1712119200000
}
```

### 3.2 获取所有权限

**请求方式**: GET
**请求路径**: /api/permissions/all
**功能描述**: 获取所有权限（不分页）
**权限要求**: perm:list

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "user:list",
      "label": "用户列表",
      "module": "user",
      "status": 1,
      "createdAt": "2026-04-02T10:00:00Z"
    },
    {
      "id": 2,
      "name": "user:create",
      "label": "创建用户",
      "module": "user",
      "status": 1,
      "createdAt": "2026-04-02T10:00:00Z"
    }
  ],
  "timestamp": 1712119200000
}
```

### 3.3 按模块获取权限

**请求方式**: GET
**请求路径**: /api/permissions/by-module
**功能描述**: 按模块分组获取权限
**权限要求**: perm:list

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "user": [
      {
        "id": 1,
        "name": "user:list",
        "label": "用户列表",
        "module": "user",
        "status": 1,
        "createdAt": "2026-04-02T10:00:00Z"
      }
    ],
    "role": [
      {
        "id": 7,
        "name": "role:list",
        "label": "角色列表",
        "module": "role",
        "status": 1,
        "createdAt": "2026-04-02T10:00:00Z"
      }
    ]
  },
  "timestamp": 1712119200000
}
```

### 3.4 获取权限详情

**请求方式**: GET
**请求路径**: /api/permissions/{id}
**功能描述**: 获取单个权限详情
**权限要求**: perm:view

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 权限ID |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "user:list",
    "label": "用户列表",
    "module": "user",
    "status": 1,
    "createdAt": "2026-04-02T10:00:00Z"
  },
  "timestamp": 1712119200000
}
```

### 3.5 创建权限

**请求方式**: POST
**请求路径**: /api/permissions
**功能描述**: 创建新权限
**权限要求**: perm:create

**请求体**:
```json
{
  "name": "article:list",
  "label": "文章列表",
  "module": "article",
  "status": 1
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| name | String | 是 | 权限标识，格式 module:action，如 user:delete |
| label | String | 是 | 权限展示名称 |
| module | String | 是 | 所属模块，如 user / article / order |
| status | Integer | 是 | 状态：1=启用 0=禁用 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 18,
    "name": "article:list",
    "label": "文章列表",
    "module": "article",
    "status": 1,
    "createdAt": "2026-04-03T10:00:00Z"
  },
  "timestamp": 1712119200000
}
```

### 3.6 更新权限

**请求方式**: PUT
**请求路径**: /api/permissions/{id}
**功能描述**: 更新权限信息
**权限要求**: perm:update

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 权限ID |

**请求体**:
```json
{
  "name": "article:list",
  "label": "文章管理列表",
  "module": "article",
  "status": 1
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| name | String | 是 | 权限标识 |
| label | String | 是 | 权限展示名称 |
| module | String | 是 | 所属模块 |
| status | Integer | 是 | 状态：1=启用 0=禁用 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 18,
    "name": "article:list",
    "label": "文章管理列表",
    "module": "article",
    "status": 1,
    "createdAt": "2026-04-03T10:00:00Z"
  },
  "timestamp": 1712119200000
}
```

### 3.7 删除权限

**请求方式**: DELETE
**请求路径**: /api/permissions/{id}
**功能描述**: 删除权限
**权限要求**: perm:delete

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 权限ID |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1712119200000
}
```

### 3.8 更新权限状态

**请求方式**: PUT
**请求路径**: /api/permissions/{id}/status
**功能描述**: 更新权限状态
**权限要求**: perm:update

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 权限ID |

**请求参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| status | Integer | 是 | 状态：1=启用 0=禁用 |

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

### 6.1 使用curl调用获取权限列表

```bash
curl -X GET "http://localhost:8080/api/permissions?current=1&size=10&module=user" \
  -H "Authorization: Bearer {token}"
```

### 6.2 使用curl调用创建权限

```bash
curl -X POST "http://localhost:8080/api/permissions" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"name": "article:list", "label": "文章列表", "module": "article", "status": 1}'
```

## 7. 注意事项

1. 所有接口都需要在请求头中携带有效的token
2. 操作权限时需要对应的权限
3. 权限名称（name）采用 module:action 格式，如 user:delete
4. 删除权限时，会级联删除角色与权限的关联关系
5. 权限的模块（module）用于对权限进行分类管理
