import { BaseCrudService } from '@/service/webclient/base/BaseService'
import { BaseResponse } from '@/service/webclient/model/ResponseModel'
import { WebClient } from '@/service/webclient/service/CommonService'
import { AxiosResponse } from 'axios'
import { GetUsersParam } from '@/service/webclient/model/Users'
import Qs from 'querystring'

export const UserService: BaseCrudService = {
  doPost: async function <UserCreateRequest, BaseResponse> (createRequest: UserCreateRequest): Promise<BaseResponse> {
    return WebClient.post<UserCreateRequest, AxiosResponse<BaseResponse>>('/v1/users', createRequest)
      .then(value => {
        return value.data
      })
  },
  doUpdate: async function <UserUpdateRequest, BaseResponse> (updateRequest: UserUpdateRequest, id: string): Promise<BaseResponse> {
    return WebClient.put<UserUpdateRequest, AxiosResponse<BaseResponse>>('/v1/users/' + id, updateRequest)
      .then(value => {
        return value.data
      })
  },
  getById<UserDetailResponse> (id: string): Promise<UserDetailResponse> {
    return WebClient.get<BaseResponse<UserDetailResponse>>('/v1/users/' + id)
      .then(value => {
        return value.data.data
      })
  },
  getCount: async function (param?: GetUsersParam): Promise<number> {
    return WebClient.get<BaseResponse<number>>('/v1/users/count?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  },
  getAll: async function <UserCards> (param?: GetUsersParam): Promise<UserCards> {
    return WebClient.get<BaseResponse<UserCards>>('/v1/users?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  }
}
