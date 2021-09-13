import {createApp} from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import 'bootstrap'
import './assets/styles/_app.scss'
import axios from "axios";
import WebClient from "@/plugins/WebClient";


const app = createApp(App)
app.use(WebClient)
app.use(store).use(router).mount('#app')


// // Plugin for validating some data
// export default {
//   install(app: any, options: any) {
//     app.config.globalProperties.$validate = (data: any, rule: any) => {
//     }
//   }
// }
