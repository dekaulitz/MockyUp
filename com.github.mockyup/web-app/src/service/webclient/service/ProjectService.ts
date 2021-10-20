import { BaseCrudService } from '@/service/webclient/base/BaseService'
import {
  GetProjectParam,
  GetProjectTags,
  ProjectTagsResponse
} from '@/service/webclient/model/Projects'
import { WebClient } from '@/service/webclient/service/CommonService'
import { BaseResponse } from '@/service/webclient/model/ResponseModel'
import Qs from 'querystring'
import { AxiosResponse } from 'axios'
import { GetUsersParam } from '@/service/webclient/model/Users'

export interface ProjectServiceInterface extends BaseCrudService {
  getTags (getProjectTags: GetProjectTags): Promise<ProjectTagsResponse[]>
}

export const ProjectService: ProjectServiceInterface = {
  getTags: async function (param: GetProjectTags): Promise<ProjectTagsResponse[]> {
    return WebClient.get<BaseResponse<ProjectTagsResponse[]>>('/v1/projects/tags?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  },
  doPost: async function <ProjectCreateRequest, BaseResponse> (createRequest: ProjectCreateRequest): Promise<BaseResponse> {
    return WebClient.post<ProjectCreateRequest, AxiosResponse<BaseResponse>>('/v1/projects', createRequest)
      .then(value => {
        return value.data
      })
  },
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
  },
  doUpdate: async function <ProjectCreateRequest, BaseResponse> (updateRequest: ProjectCreateRequest, id: string): Promise<BaseResponse> {
    return WebClient.put<ProjectCreateRequest, AxiosResponse<BaseResponse>>('/v1/projects/' + id, updateRequest)
      .then(value => {
        return value.data
      })
  }
}
