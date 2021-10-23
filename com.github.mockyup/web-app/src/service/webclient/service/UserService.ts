import { BaseResponse } from '@/service/webclient/model/ResponseModel'
import { WebClient } from '@/service/webclient/service/CommonService'
import {
  GetUsersParam,
  UserCardResponse,
  UserCreateRequest,
  UserDetailResponse,
  UserUpdateRequest
} from '@/service/webclient/model/Users'
import { AxiosResponse } from 'axios'
import Qs from 'querystring'
import { BaseCrudService } from '@/service/webclient/base/BaseService'

interface UserServiceInterface extends BaseCrudService {
  doPost (request: UserCreateRequest): Promise<BaseResponse>

  doUpdate (request: UserUpdateRequest, id: string): Promise<BaseResponse>

  getById (id: string): Promise<UserDetailResponse>

  getCount (param?: GetUsersParam): Promise<number>

  getAll (param?: GetUsersParam): Promise<UserCardResponse[]>

  deleteById (id: string): Promise<BaseResponse>
}

export const UserService: UserServiceInterface = {
  deleteById (id: string): Promise<BaseResponse> {
    return WebClient.delete<BaseResponse, AxiosResponse<BaseResponse>>('/v1/users/' + id)
      .then(value => {
        return value.data
      })
  },
  doPost (request: UserCreateRequest): Promise<BaseResponse> {
    return WebClient.post<UserCreateRequest, AxiosResponse<BaseResponse>>('/v1/users', request)
      .then(value => {
        return value.data
      })
  },
  doUpdate (request: UserUpdateRequest, id: string): Promise<BaseResponse> {
    return WebClient.put<UserUpdateRequest, AxiosResponse<BaseResponse>>('/v1/users/' + id, request)
      .then(value => {
        return value.data
      })
  },
  getAll (param?: GetUsersParam): Promise<UserCardResponse[]> {
    return WebClient.get<BaseResponse<UserCardResponse[]>>('/v1/users?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  },
  getById (id: string): Promise<UserDetailResponse> {
    return WebClient.get<BaseResponse<UserDetailResponse>>('/v1/users/' + id)
      .then(value => {
        return value.data.data
      })
  },
  getCount (param?: GetUsersParam): Promise<number> {
    return WebClient.get<BaseResponse<number>>('/v1/users/count?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  }
}
