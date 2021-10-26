import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import 'bootstrap'
import './assets/styles/app.scss'
import WebClient from '@/service/webclient'
import { AccessData } from '@/service/helper/AccessHelper'
import TimeAgo from 'javascript-time-ago'
import en from 'javascript-time-ago/locale/en.json'

const app = createApp(App)
const accessData = AccessData

TimeAgo.addDefaultLocale(en)
const timeAgo = new TimeAgo('en-US')
app.use(WebClient)
app.use(store).use(router).mount('#app')

app.config.globalProperties.$filters = {
  localDate (value: string):any {
    if (value === undefined) {
      return value
    }
    return timeAgo.format(new Date(value))
  },
  capitalizeFirstLetter (value: string) {
    return value.charAt(0).toUpperCase() + value.slice(1)
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
