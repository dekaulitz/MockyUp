import { BaseResponse } from '@/plugins/webclient/model/ResponseModel'
import { BaseCrudService } from '@/plugins/webclient/base/BaseService'
import { WebClient } from '@/plugins/webclient/tmp/serice/CommonService'
import Qs from 'querystring'
import { GetUsersParam } from '@/plugins/webclient/model/Users'
import {
  GetProjectParam
} from '@/plugins/webclient/model/Projects'

export const UserService:BaseCrudService = {
  getById<T = never> (id: string): Promise<T> {
    return Promise.resolve(undefined)
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

export const ProjectService:BaseCrudService = {
  getAll: async function <ProjectCardResponse> (parameter?: GetProjectParam): Promise<ProjectCardResponse> {
    return WebClient.get<BaseResponse<ProjectCardResponse>>('/v1/projects?' + Qs.stringify(parameter))
      .then(value => {
        return value.data.data
      })
  },
  getById<ProjectResponse> (id: string): Promise<ProjectResponse> {
    return WebClient.get<BaseResponse<ProjectResponse>>('/v1/projects/' + id)
      .then(value => {
        return value.data.data
      })
  },
  getCount: async function (param?: GetUsersParam): Promise<number> {
    return WebClient.get<BaseResponse<number>>('/v1/projects/count?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  }

}
