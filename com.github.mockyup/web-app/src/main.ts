import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import 'bootstrap'
import './assets/styles/app.scss'
import WebClient from '@/service/webclient'
import moment from 'moment'
import { AccessData, AccessInterface } from '@/service/helper/AccessHelper'

const app = createApp(App)
const accessData = AccessData

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
  access (access:string):AccessInterface {
    const translations = accessData
    if (translations.has(access)) {
      return translations.get(access)
    }
    return undefined
  },
  subString (value: string, start: number, end: number) {
    if (!value) {
      return ''
    } else {
      return value.substr(start, end)
    }
  },
  filterUndefined (value: unknown): unknown {
    if (!value) {
      return undefined
    } else {
      return value
    }
  }
}
