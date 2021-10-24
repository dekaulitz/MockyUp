import { AuthResponse } from '@/service/webclient/model/ResponseModel'
import { AlertInterface, AlertType } from '@/shared/alert'

export const AlertStore = {
  alertStore: {
    namespaced: true,
    state: (): AlertInterface => ({
      show: false,
      icon: '',
      alertType: AlertType.ERROR,
      closeable: true,
      content: ''
    }),
    mutations: {
      setAlert (state: AlertInterface, payload: AlertInterface): void {
        state.icon = payload.icon
        state.show = payload.show
        state.alertType = payload.alertType
        state.closeable = payload.closeable
        state.content = payload.content
      }
    },
    actions: {
      setAlert (context: any, payload: AuthResponse): void {
        context.commit('setAlert', payload)
      }
    }
  }
}

// import { InjectionKey } from 'vue'
// import { AlertActionModel, AlertType } from '../IBaseAlert'
// import { Store } from 'vuex'
//
// // define injection key
// export const AlertStore = {
//   alertStore: {
//     namespaced: true,
//     state: (): AlertActionModel => ({
//       isShown: false,
//       alertType: AlertType.DEFAULT,
//       message: ''
//     }),
//     mutations: {
//       pushAlert (state: AlertActionModel, payload: AlertActionModel): void {
//         state.isShown = payload.isShown
//         state.message = payload.message
//         state.alertType = payload.alertType
//       },
//       closeAlert (state: AlertActionModel): void {
//         state.isShown = false
//         state.message = ''
//       }
//     },
//     actions: {
//       pushAlert (context: any, payload: AlertActionModel): void {
//         context.commit('pushAlert', payload)
//       },
//       closeAlert (context: any): void {
//         context.commit('closeAlert')
//       }
//     }
//   }
// }
