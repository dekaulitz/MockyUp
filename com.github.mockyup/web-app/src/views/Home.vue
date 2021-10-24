<template>
  <nav class="navbar navbar-expand-lg bg-indigo-800">
    <div class="container-fluid">
      <router-link class="navbar-brand text-white" to="/"><span
        class="fas fa-align-left me-3"></span>DevStock | MOCK Service
      </router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
              data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
              aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item" v-if="hasAccessPermissions('PROJECTS_READ_WRITE','PROJECTS_READ')">
            <router-link class="nav-link text-white" aria-current="page" to="/projects">Management
              Projects
            </router-link>
          </li>
          <li class="nav-item" v-if="hasAccessPermissions('USERS_READ','USERS_READ_WRITE')">
            <router-link class="nav-link text-white" aria-current="page" to="/users">Management
              Users
            </router-link>
          </li>
        </ul>
        <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
          <!--          <li class="nav-item dropdown">-->
          <!--            <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdown" role="button"-->
          <!--               data-bs-toggle="dropdown" aria-expanded="false">-->
          <!--             <span class="fas fa-cogs ms-2"></span> Configuration-->
          <!--            </a>-->
          <!--            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">-->
          <!--              <li><a class="dropdown-item" href="#">Action</a></li>-->
          <!--              <li><a class="dropdown-item" href="#">Another action</a></li>-->
          <!--              <li>-->
          <!--                <hr class="dropdown-divider">-->
          <!--              </li>-->
          <!--              <li><a class="dropdown-item" href="#">Something else here</a></li>-->
          <!--            </ul>-->
          <!--          </li>-->
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle text-white" href="#" id="navbarDropdown1"
               role="button"
               data-bs-toggle="dropdown" aria-expanded="false">
              <span class="fas fa-user"></span> {{ authProfile.username }}
            </a>
            <ul class="dropdown-menu dropdown-menu-lg-end" aria-labelledby="navbarDropdown1">
              <li class="nav-item">
                <router-link class="dropdown-item " aria-current="page" :to="{name:'AuthProfile'}">
                  <div class="label-bold">@{{ authProfile.username }}</div>
                </router-link>
              </li>
              <!--              <li><a class="dropdown-item" href="#">Another action</a></li>-->
              <li>
                <hr class="dropdown-divider">
              </li>
              <!--              @TODO  need to enhance in this poisition-->
              <li>
                <button class="dropdown-item" @click="doLogout">Logout</button>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <div class="container mt-3">
    <alert-container/>
    <router-view v-slot="{ Component }">
      <transition appear mode="out-in" name="fade">
        <component :is="Component"/>
      </transition>
    </router-view>
  </div>
  <footer-navigation/>
</template>

<script lang="ts">

import { defineComponent } from 'vue'
import { AuthResponse } from '@/service/webclient/model/ResponseModel'
import { StorageKeyType } from '@/service/webclient/model/EnumModel'
import { StorageService } from '@/service/webclient/service/CommonService'
import AuthService from '@/service/webclient/service/AuthService'
import FooterNavigation from '@/components/FooterNavigation.vue'
import BaseAccessMixins from '@/shared/base/BaseAccessMixins'
import AlertContainer from '@/shared/alert/AlertContainer.vue'

export default defineComponent({
  components: { AlertContainer, FooterNavigation },
  mixins: [BaseAccessMixins],
  data () {
    return {
    }
  },
  computed: {
    authProfile (): AuthResponse {
      let authProfile = this.$store.state.authStore
      if (authProfile.username === '') {
        authProfile = StorageService.getData(StorageKeyType.AUTH_PROFILE)
      }
      return authProfile
    }
  },
  methods: {
    doLogout (): void {
      AuthService.logout()
        .then(value => {
          this.$router.push({ name: 'Login' })
        })
    }
  }

})
</script>
