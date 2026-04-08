import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

// 公开路由
const publicRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: {
      title: '登录'
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: {
      title: '注册'
    }
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('../views/404.vue'),
    meta: {
      title: '页面不存在'
    }
  }
]

// 受保护路由
const protectedRoutes = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('../layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: {
          title: '首页',
          requiresAuth: true
        }
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('../views/UserManagement.vue'),
        meta: {
          title: '用户管理',
          requiresAuth: true,
          permissions: ['user:list']
        }
      },
      {
        path: 'roles',
        name: 'RoleManagement',
        component: () => import('../views/RoleManagement.vue'),
        meta: {
          title: '角色管理',
          requiresAuth: true,
          permissions: ['role:list']
        }
      },
      {
        path: 'permissions',
        name: 'PermissionManagement',
        component: () => import('../views/PermissionManagement.vue'),
        meta: {
          title: '权限管理',
          requiresAuth: true,
          permissions: ['perm:list']
        }
      },
      {
        path: 'chat',
        name: 'Chat',
        component: () => import('../views/Chat.vue'),
        meta: {
          title: 'AI对话',
          requiresAuth: true,
          permissions: ['agent:chat']
        }
      }
    ]
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('../views/403.vue'),
    meta: {
      title: '无权限访问'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: [...publicRoutes, ...protectedRoutes, {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }]
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  const token = userStore.token
  
  // 设置页面标题
  document.title = to.meta.title || 'AGS Admin'
  
  // 已登录用户访问登录/注册页，重定向到首页
  if (token && (to.path === '/login' || to.path === '/register')) {
    next({ path: '/dashboard' })
    return
  }
  
  // 未登录用户访问受保护页面，重定向到登录页
  if (to.meta.requiresAuth && !token) {
    next({ path: '/login', query: { redirect: to.fullPath } })
    return
  }
  
  // 权限检查
  if (to.meta.requiresAuth && to.meta.permissions) {
    const hasPermission = to.meta.permissions.some(perm => 
      userStore.permissions.includes(perm)
    )
    if (!hasPermission) {
      next({ path: '/403' })
      return
    }
  }
  
  next()
})

export default router