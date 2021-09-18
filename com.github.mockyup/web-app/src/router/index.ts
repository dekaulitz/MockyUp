import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { StorageKeyType } from '@/plugins/webclient/model/EnumModel'
import { StorageService, WebClient } from '@/plugins/webclient/serice/CommonService'
import { AuthResponse } from '@/plugins/webclient/model/ResponseModel'

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
        name: 'Projects',
        component: () => import(/* webpackChunkName: "about" */ '../views/projects/Projects.vue')
      },
      {
        path: '/project-detail/:id',
        name: 'ProjectsDetail',
        component: () => import(/* webpackChunkName: "about" */ '../views/projects/ProjectDetail.vue')
      },
      {
        path: '/project-detail/:id/contracts/:contractId',
        name: 'ContractDetail',
        component: () => import(/* webpackChunkName: "about" */ '../views/contracts/ContractDetail.vue')
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
