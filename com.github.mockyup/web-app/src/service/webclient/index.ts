import axios from 'axios'

export default {
  install (app: any, options: any) {
    app.config.globalProperties.$http = axios
  }
}
