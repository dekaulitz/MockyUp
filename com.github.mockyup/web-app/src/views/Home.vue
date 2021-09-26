<template>
  <nav class="navbar navbar-expand-lg bg-indigo-800">
    <div class="container-fluid">
      <router-link class="navbar-brand text-white" to="/"><span class="fas fa-align-left me-3"></span>DevStock | MOCK Service</router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
              data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
              aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link class="nav-link text-white" aria-current="page" to="/projects">Management Projects</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link text-white" aria-current="page" to="/users">Management Users</router-link>
          </li>
        </ul>
        <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdown" role="button"
               data-bs-toggle="dropdown" aria-expanded="false">
             <span class="fas fa-cogs ms-2"></span> Configuration
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li><a class="dropdown-item" href="#">Action</a></li>
              <li><a class="dropdown-item" href="#">Another action</a></li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <li><a class="dropdown-item" href="#">Something else here</a></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdown1" role="button"
               data-bs-toggle="dropdown" aria-expanded="false">
              <span class="fas fa-user"></span> {{ accountUser.username }}
            </a>
            <ul class="dropdown-menu dropdown-menu-lg-end" aria-labelledby="navbarDropdown1">
              <li><a class="dropdown-item " href="#">
                @{{ accountUser.username }}
              </a></li>
              <li><a class="dropdown-item" href="#">Another action</a></li>
              <li>
                <hr class="dropdown-divider">
              </li>
<!--              @TODO  need to enhance in this poisition-->
              <li><button class="dropdown-item" @click="doLogout">Logout</button></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <div class="container mt-3">
    <breadcrumb-container class="border-bottom mb-2"/>
    <router-view v-slot="{ Component }">
      <transition appear mode="out-in" name="fade">
        <component :is="Component" />
      </transition>
    </router-view>
  </div>
 <footer-navigation/>
</template>

<script lang="ts">

import { defineComponent } from 'vue'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import { AuthResponse } from '@/plugins/webclient/model/ResponseModel'
import { StorageKeyType } from '@/plugins/webclient/model/EnumModel'
import { StorageService } from '@/plugins/webclient/tmp/serice/CommonService'
import AuthService from '@/plugins/webclient/tmp/serice/AuthService'
import FooterNavigation from '@/components/FooterNavigation.vue'

export default defineComponent({
  components: { FooterNavigation, BreadcrumbContainer },
  data () {
    return {
      accountUser: {} as AuthResponse
    }
  },
  mounted () {
    this.accountUser = StorageService.getData(StorageKeyType.AUTH_PROFILE)
  },
  methods: {
    doLogout (): void {
      AuthService.logout()
        .then(value => {
          this.$router.push({ name: 'Login' })
          console.log(value)
        })
    }
  }

})
</script>
