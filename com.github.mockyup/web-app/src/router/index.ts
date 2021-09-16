import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { StorageKeyType } from '@/plugins/webclient/model/EnumModel'
import { StorageService } from '@/plugins/webclient/serice/CommonService'
import { AuthResponse } from '@/plugins/webclient/model/ResponseModel'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    beforeEnter: (to, from, next) => {
      const authProfile = StorageService.getData<AuthResponse>(StorageKeyType.AUTH_PROFILE)
      if (authProfile) {
        return next({ name: 'Home' })
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
    component: () => import(/* webpackChunkName: "about" */ '../views/Home.vue')
  }
  // {
  //   path: '*',
  //   component: PageNotFound
  // }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
