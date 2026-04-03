# RoleController 接口文档

## 1. 接口概览

RoleController 提供角色管理相关的接口，包括角色列表查询、角色详情获取、角色创建、角色更新、角色删除、权限分配和角色状态更新等功能。

## 2. 接口列表

| 接口名称 | 请求方式 | 请求路径 | 功能描述 | 权限要求 |
| :--- | :--- | :--- | :--- | :--- |
| 获取角色列表 | GET | /api/roles | 分页获取角色列表 | role:list |
| 获取所有角色 | GET | /api/roles/all | 获取所有角色（不分页） | role:list |
| 获取角色详情 | GET | /api/roles/{id} | 获取单个角色详情 | role:view |
| 创建角色 | POST | /api/roles | 创建新角色 | role:create |
| 更新角色 | PUT | /api/roles/{id} | 更新角色信息 | role:update |
| 删除角色 | DELETE | /api/roles/{id} | 删除角色 | role:delete |
| 分配权限 | PUT | /api/roles/{id}/permissions | 为角色分配权限 | role:assign-perm |
| 更新角色状态 | PUT | /api/roles/{id}/status | 更新角色状态 | role:update |

## 3. 详细接口说明

### 3.1 获取角色列表

**请求方式**: GET
**请求路径**: /api/roles
**功能描述**: 分页获取角色列表
**权限要求**: role:list

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
        "name": "admin",
        "label": "后台管理员",
        "description": "可管理内容与普通用户",
        "scope": 1,
        "status": 1,
        "createdAt": "2026-04-02T10:00:00Z"
      }
    ],
    "total": 3,
    "size": 10,
    "current": 1,
    "pages": 1
  },
  "timestamp": 1712119200000
}
```

### 3.2 获取所有角色

**请求方式**: GET
**请求路径**: /api/roles/all
**功能描述**: 获取所有角色（不分页）
**权限要求**: role:list

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "super_admin",
      "label": "超级管理员",
      "description": "拥有所有权限，代码层直接放行",
      "scope": 1,
      "status": 1,
      "createdAt": "2026-04-02T10:00:00Z"
    },
    {
      "id": 2,
      "name": "admin",
      "label": "后台管理员",
      "description": "可管理内容与普通用户",
      "scope": 1,
      "status": 1,
      "createdAt": "2026-04-02T10:00:00Z"
    },
    {
      "id": 3,
      "name": "user",
      "label": "普通用户",
      "description": "前台注册用户，拥有基础操作权限",
      "scope": 2,
      "status": 1,
      "createdAt": "2026-04-02T10:00:00Z"
    }
  ],
  "timestamp": 1712119200000
}
```

### 3.3 获取角色详情

**请求方式**: GET
**请求路径**: /api/roles/{id}
**功能描述**: 获取单个角色详情
**权限要求**: role:view

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 角色ID |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "admin",
    "label": "后台管理员",
    "description": "可管理内容与普通用户",
    "scope": 1,
    "status": 1,
    "createdAt": "2026-04-02T10:00:00Z",
    "permissions": [
      {
        "id": 1,
        "name": "user:list",
        "label": "用户列表",
        "module": "user"
      }
    ]
  },
  "timestamp": 1712119200000
}
```

### 3.4 创建角色

**请求方式**: POST
**请求路径**: /api/roles
**功能描述**: 创建新角色
**权限要求**: role:create

**请求体**:
```json
{
  "name": "editor",
  "label": "编辑",
  "description": "内容编辑角色",
  "scope": 1,
  "status": 1
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| name | String | 是 | 角色标识，如 super_admin / admin / user |
| label | String | 是 | 角色展示名称 |
| description | String | 否 | 角色描述 |
| scope | Integer | 是 | 作用域：1=后台 2=前台 |
| status | Integer | 是 | 状态：1=启用 0=禁用 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 4,
    "name": "editor",
    "label": "编辑",
    "description": "内容编辑角色",
    "scope": 1,
    "status": 1,
    "createdAt": "2026-04-03T10:00:00Z"
  },
  "timestamp": 1712119200000
}
```

### 3.5 更新角色

**请求方式**: PUT
**请求路径**: /api/roles/{id}
**功能描述**: 更新角色信息
**权限要求**: role:update

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 角色ID |

**请求体**:
```json
{
  "name": "editor",
  "label": "内容编辑",
  "description": "负责内容编辑的角色",
  "scope": 1,
  "status": 1
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| name | String | 是 | 角色标识 |
| label | String | 是 | 角色展示名称 |
| description | String | 否 | 角色描述 |
| scope | Integer | 是 | 作用域：1=后台 2=前台 |
| status | Integer | 是 | 状态：1=启用 0=禁用 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 4,
    "name": "editor",
    "label": "内容编辑",
    "description": "负责内容编辑的角色",
    "scope": 1,
    "status": 1,
    "createdAt": "2026-04-03T10:00:00Z"
  },
  "timestamp": 1712119200000
}
```

### 3.6 删除角色

**请求方式**: DELETE
**请求路径**: /api/roles/{id}
**功能描述**: 删除角色
**权限要求**: role:delete

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 角色ID |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1712119200000
}
```

### 3.7 分配权限

**请求方式**: PUT
**请求路径**: /api/roles/{id}/permissions
**功能描述**: 为角色分配权限
**权限要求**: role:assign-perm

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 角色ID |

**请求体**:
```json
[1, 2, 3]
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| permissionIds | List<Long> | 是 | 权限ID列表 |

**响应体**:
```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1712119200000
}
```

### 3.8 更新角色状态

**请求方式**: PUT
**请求路径**: /api/roles/{id}/status
**功能描述**: 更新角色状态
**权限要求**: role:update

**路径参数**:

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | Long | 是 | 角色ID |

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

### 6.1 使用curl调用获取角色列表

```bash
curl -X GET "http://localhost:8080/api/roles?current=1&size=10&keyword=admin" \
  -H "Authorization: Bearer {token}"
```

### 6.2 使用curl调用创建角色

```bash
curl -X POST "http://localhost:8080/api/roles" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"name": "editor", "label": "编辑", "description": "内容编辑角色", "scope": 1, "status": 1}'
```

## 7. 注意事项

1. 所有接口都需要在请求头中携带有效的token
2. 操作角色时需要对应的权限
3. 分配权限时，会覆盖角色原有的权限
4. 删除角色时，会级联删除角色与用户的关联关系以及角色与权限的关联关系
5. 超级管理员角色（super_admin）通常不允许修改或删除
