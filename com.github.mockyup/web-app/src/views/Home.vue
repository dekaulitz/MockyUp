<template>
  <LoginForm/>
  <HelloWorld></HelloWorld>
</template>

<script lang="ts">
import { Options, Vue } from 'vue-class-component'
import LoginForm from '@/components/login/LoginForm.vue'
import HelloWorld from '@/components/HelloWorld.vue' // @ is an alias to /src

@Options({
  components: { LoginForm, HelloWorld }
})
export default class Home extends Vue {}
</script>
