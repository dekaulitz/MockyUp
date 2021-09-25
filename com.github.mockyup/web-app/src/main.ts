import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import 'bootstrap'
import './assets/styles/app.scss'
import WebClient from '@/plugins/webclient'
import moment from 'moment'

const app = createApp(App)
app.use(WebClient)
app.use(store).use(router).mount('#app')

app.directive('validator', {
  mounted (el, binding) {
    console.log(el)
    console.log(binding)
  }
})
app.config.globalProperties.$filters = {
  localDate (value: string) {
    return moment(value).local().format('MMM Do YY HH:mm:ss')
  },
  subString (value: string, start: number, end: number) {
    if (!value) {
      return ''
    } else {
      return value.substr(start, end)
    }
  },
  filterUndefined (value: unknown):unknown {
    if (!value) {
      return undefined
    } else {
      return value
    }
  }
}
