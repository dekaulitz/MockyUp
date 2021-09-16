import { AuthResponse, BaseResponse } from '@/plugins/webclient/model/ResponseModel'
import { AuthLogin } from '@/plugins/webclient/model/RequestModel'
import { StorageService, webClient } from '@/plugins/webclient/serice/CommonService'
import { StorageKeyType } from '@/plugins/webclient/model/EnumModel'
import { AxiosResponse } from 'axios'

interface AuthService {
  doLogin (authLogin: AuthLogin): Promise<AuthResponse>

  refreshLogin (): Promise<BaseResponse>

  logout (): Promise<BaseResponse>
}

export const AuthLoginService: AuthService = {
  doLogin: async function (authLogin: AuthLogin): Promise<AuthResponse> {
    return webClient.post<AuthLogin, AxiosResponse<BaseResponse<AuthResponse>>>('/v1/login', authLogin)
      .then(value => {
        return StorageService.setData<AuthResponse>(StorageKeyType.AUTH_PROFILE, value.data.data)
      }).catch(error => {
        if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
          console.log(error.response.data)
          console.log(error.response.status)
          console.log(error.response.headers)
        } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
          console.log(error.request)
        } else {
        // Something happened in setting up the request that triggered an Error
          console.log('Error', error.message)
        }
        console.log(error.config)
        return error
      })
  },
  logout: async function (): Promise<BaseResponse> {
    const auth = StorageService.getData<AuthResponse>(StorageKeyType.AUTH_PROFILE)
    return webClient.put<null, AxiosResponse<BaseResponse>>('/v1/logout', {}, {
      headers: {
        Authorization: 'Bearer ' + auth.accessToken
      }
    }).then(value => {
      StorageService.clearData(StorageKeyType.AUTH_PROFILE)
      return value.data
    })
  },
  refreshLogin (): Promise<BaseResponse> {
    return undefined
  }
}
