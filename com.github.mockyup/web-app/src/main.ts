import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import 'bootstrap'
import './assets/styles/_app.scss'
import WebClient from '@/plugins/WebClient'

const app = createApp(App)
app.use(WebClient)
app.use(store).use(router).mount('#app')

app.directive('validator', {
  mounted (el, binding) {
    console.log(el)
    console.log(binding)
  }
})
