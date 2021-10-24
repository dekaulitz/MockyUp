import { AuthResponse } from '@/service/webclient/model/ResponseModel'

export const AuthStore = {
  authStore: {
    namespaced: true,
    state: (): AuthResponse => ({
      username: '',
      accessToken: '',
      access: []
    }),
    mutations: {
      setAuthProfile (state:AuthResponse, payload:AuthResponse):void{
        state.accessToken = payload.accessToken
        state.access = payload.access
        state.username = payload.username
      }
    },
    actions: {
      setAuthProfile (context: any, payload: AuthResponse): void {
        context.commit('setAuthProfile', payload)
      }
    }
  }
}
