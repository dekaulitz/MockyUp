<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">DevStock</a>
    </div>
  </nav>
  <div class="d-inline-flex  login-page w-100 ">
    <div class="d-inline-flex align-items-center flex-fill">
      <div class="container w-100">
        <div class=" row ">
          <div class="col-md-8">
            asdasdadasd
          </div>
          <div class="col-md-4">
            <login-form/>
          </div>
        </div>
      </div>
    </div>
  </div>
  <nav class="navbar navbar-light bg-light">
    <div class="container">
      something
    </div>
  </nav>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import LoginForm from '@/components/login/LoginForm.vue'

export default defineComponent({
  components: { LoginForm }
})
</script>
<style scoped lang="scss">
.login-page {
  width: auto;
  height: 720px;
}
</style>
