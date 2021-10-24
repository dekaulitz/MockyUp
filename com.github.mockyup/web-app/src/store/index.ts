import { createStore } from 'vuex'
import { AuthStore } from '@/store/AuthStore'
import { AlertStore } from '@/shared/alert/AlertStore'

export default createStore({
  modules: {
    ...AuthStore,
    ...AlertStore
  }
})
