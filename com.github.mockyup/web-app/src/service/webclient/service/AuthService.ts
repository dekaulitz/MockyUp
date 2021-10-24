import { AuthLogin, UpdateProfileAuthRequest } from '@/service/webclient/model/RequestModel'
import { AuthResponse, BaseResponse } from '@/service/webclient/model/ResponseModel'
import { StorageService, WebClient } from '@/service/webclient/service/CommonService'
import { AxiosResponse } from 'axios'
import { StorageKeyType } from '@/service/webclient/model/EnumModel'
import { UserDetailResponse } from '@/service/webclient/model/Users'

interface AuthService {
  doLogin (request: AuthLogin): Promise<AuthResponse>

  refreshLogin (): Promise<BaseResponse>

  logout (): Promise<BaseResponse>

  getAuthDetail (): Promise<UserDetailResponse>

  doUpdateProfile (request: UpdateProfileAuthRequest): Promise<AuthResponse>
}

export const AuthLoginService: AuthService = {
  doUpdateProfile (request: UpdateProfileAuthRequest): Promise<AuthResponse> {
    return WebClient.put<UpdateProfileAuthRequest, AxiosResponse<BaseResponse<AuthResponse>>>('/v1/auth-detail', request)
      .then(value => {
        return value.data.data
      })
  },
  getAuthDetail (): Promise<UserDetailResponse> {
    return WebClient.get<BaseResponse<UserDetailResponse>>('/v1/auth-detail')
      .then(value => {
        return value.data.data
      })
  },
  doLogin: async function (request: AuthLogin): Promise<AuthResponse> {
    return WebClient.post<AuthLogin, AxiosResponse<BaseResponse<AuthResponse>>>('/v1/login', request)
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
