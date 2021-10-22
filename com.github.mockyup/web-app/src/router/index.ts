import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { StorageKeyType } from '@/service/webclient/model/EnumModel'
import { StorageService } from '@/service/webclient/service/CommonService'
import { AuthResponse } from '@/service/webclient/model/ResponseModel'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    beforeEnter: (to, from, next) => {
      const authProfile = StorageService.getData<AuthResponse>(StorageKeyType.AUTH_PROFILE)
      if (authProfile) {
        return next({ path: '/' })
      } else {
        next()
      }
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/Login.vue')
  },
  {
    path: '/',
    name: 'Home',
    beforeEnter: (to, from, next) => {
      const authProfile = StorageService.getData<AuthResponse>(StorageKeyType.AUTH_PROFILE)
      if (authProfile) {
        return next()
      } else {
        next({ name: 'Login' })
      }
    },
    component: () => import(/* webpackChunkName: "about" */ '../views/Home.vue'),
    children: [
      {
        path: '/',
        name: 'Home',
        component: () => import(/* webpackChunkName: "about" */ '../views/dashboard/ProjectsDashboard.vue')
      },
      {
        path: '/project-detail/:id',
        name: 'something',
        component: () => import(/* webpackChunkName: "about" */ '../views/projects/ProjectDetail.vue')
      },
      {
        path: '/project-detail/:id/contracts/create',
        name: 'ContractsCreate',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/contracts/ContractsCreate.vue')
      },
      {
        path: '/project-detail/:id/contracts/:contractId',
        name: 'ContractDetail',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/contracts/ContractDetail.vue')
      },
      {
        path: '/project-detail/:id/contracts/:contractId/edit',
        name: 'ContractEdit',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/contracts/ContractsEdit.vue')
      },
      {
        path: '/project-detail/:id/contracts/:contractId/swagger-ui',
        name: 'SwaggerUI',
        component: () => import(/* webpackChunkName: "about" */ '../components/contracts/SwaggerUI.vue')
      },
      {
        path: '/users',
        name: 'Users',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/users/Users.vue')
      },
      {
        path: '/users/create',
        name: 'UsersCreate',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/users/UsersCreate.vue')
      },
      {
        path: '/users/:id',
        name: 'UsersDetail',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/users/UsersDetail.vue')
      },
      {
        path: '/users/:id/edit',
        name: 'UsersEdit',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/users/UsersEdit.vue')
      },
      {
        path: '/projects',
        name: 'Projects',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/projects/Projects.vue')
      },
      {
        path: '/projects/create',
        name: 'ProjectsCreate',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/projects/ProjectsCreate.vue')
      },
      {
        path: '/projects/:id',
        name: 'ProjectsDetail',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/projects/ProjectsDetail.vue')
      },
      {
        path: '/projects/:id/edit',
        name: 'ProjectsEdit',
        component: () => import(/* webpackChunkName: "about" */ '../views/cms/projects/ProjectsEdit.vue')
      }
    ]
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "about" */ '../views/NotFound404.vue')
  },
  {
    path: '/:catchAll(.*)', // Unrecognized path automatically matches 404
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: routes
})

export default router
