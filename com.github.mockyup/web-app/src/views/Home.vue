<template>
  <nav class="container-fluid navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">Navbar</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
              data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
              aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="#">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Link</a>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
               data-bs-toggle="dropdown" aria-expanded="false">
              Dropdown
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
          <li class="nav-item">
            <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
          </li>
        </ul>
        <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
          <div class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown1" role="button"
               data-bs-toggle="dropdown" aria-expanded="false">
              <span class="fas fa-user"></span>
            </a>
            <ul class="dropdown-menu dropdown-menu-lg-end" aria-labelledby="navbarDropdown1">
              <li><a class="dropdown-item" href="#">@{{ accountUser.username }}</a></li>
              <li><a class="dropdown-item" href="#">Another action</a></li>
              <li>
                <hr class="dropdown-divider">
              </li>
<!--              @TODO  need to enhance in this poisition-->
              <li><button class="dropdown-item" @click="doLogout">Logout</button></li>
            </ul>
          </div>
        </ul>
      </div>
    </div>
  </nav>
  <div class="container mt-3">
    <breadcrumb-container/>
    <transition appear mode="out-in" name="fade">
      <router-view/>
    </transition>
  </div>
  <nav class="navbar navbar-light bg-light">
    <div class="container">
      something
    </div>

  </nav>
</template>

<script lang="ts">

import { defineComponent } from 'vue'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import { AuthResponse } from '@/plugins/webclient/model/ResponseModel'
import { StorageKeyType } from '@/plugins/webclient/model/EnumModel'
import { StorageService } from '@/plugins/webclient/serice/CommonService'
import AuthService from '@/plugins/webclient/serice/AuthService'

export default defineComponent({
  components: { BreadcrumbContainer },
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
