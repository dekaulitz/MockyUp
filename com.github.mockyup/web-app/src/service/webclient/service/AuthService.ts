import { AuthLogin } from '@/service/webclient/model/RequestModel'
import { AuthResponse, BaseResponse } from '@/service/webclient/model/ResponseModel'
import { StorageService, WebClient } from '@/service/webclient/service/CommonService'
import { AxiosResponse } from 'axios'
import { StorageKeyType } from '@/service/webclient/model/EnumModel'

interface AuthService {
  doLogin (authLogin: AuthLogin): Promise<AuthResponse>

  refreshLogin (): Promise<BaseResponse>

  logout (): Promise<BaseResponse>
}

export const AuthLoginService: AuthService = {
  doLogin: async function (authLogin: AuthLogin): Promise<AuthResponse> {
    return WebClient.post<AuthLogin, AxiosResponse<BaseResponse<AuthResponse>>>('/v1/login', authLogin)
      .then(value => {
        return value.data.data
      })
  },
  logout: async function (): Promise<BaseResponse> {
    const auth = StorageService.getData<AuthResponse>(StorageKeyType.AUTH_PROFILE)
    return WebClient.put<null, AxiosResponse<BaseResponse>>('/v1/logout', {}, {
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
export default AuthLoginService
