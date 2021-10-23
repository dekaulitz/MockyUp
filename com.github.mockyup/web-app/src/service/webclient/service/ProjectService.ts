import {
  GetProjectParam,
  GetProjectTags,
  ProjectCardResponse,
  ProjectCreateRequest,
  ProjectResponse,
  ProjectTagsResponse,
  ProjectUpdateRequest
} from '@/service/webclient/model/Projects'
import { WebClient } from '@/service/webclient/service/CommonService'
import { BaseResponse } from '@/service/webclient/model/ResponseModel'
import Qs from 'querystring'
import { AxiosResponse } from 'axios'
import { BaseCrudService } from '@/service/webclient/base/BaseService'

export interface ProjectServiceInterface extends BaseCrudService{
  getTags (getProjectTags: GetProjectTags): Promise<ProjectTagsResponse[]>

  doPost (request: ProjectCreateRequest): Promise<BaseResponse>

  doUpdate (request: ProjectUpdateRequest, id: string): Promise<BaseResponse>

  getById (id: string): Promise<ProjectResponse>

  getCount (param?: GetProjectParam): Promise<number>

  getAll (param?: GetProjectParam): Promise<ProjectCardResponse[]>

  deleteById (id: string): Promise<BaseResponse>
}

export const ProjectService: ProjectServiceInterface = {
  deleteById (id: string): Promise<BaseResponse> {
    return WebClient.delete<BaseResponse, AxiosResponse<BaseResponse>>('/v1/projects/' + id)
      .then(value => {
        return value.data
      })
  },
  doPost (request: ProjectUpdateRequest): Promise<BaseResponse> {
    return WebClient.post<ProjectUpdateRequest, AxiosResponse<BaseResponse>>('/v1/projects', request)
      .then(value => {
        return value.data
      })
  },
  doUpdate (request: ProjectUpdateRequest, id: string): Promise<BaseResponse> {
    return WebClient.put<ProjectUpdateRequest, AxiosResponse<BaseResponse>>('/v1/projects/' + id, request)
      .then(value => {
        return value.data
      })
  },
  getAll (param?: GetProjectParam): Promise<ProjectCardResponse[]> {
    return WebClient.get<BaseResponse<ProjectCardResponse[]>>('/v1/projects?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  },
  getById (id: string): Promise<ProjectResponse> {
    return WebClient.get<BaseResponse<ProjectResponse>>('/v1/projects/' + id)
      .then(value => {
        return value.data.data
      })
  },
  getCount (param?: GetProjectParam): Promise<number> {
    return WebClient.get<BaseResponse<number>>('/v1/projects/count?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  },
  getTags (param?: GetProjectTags): Promise<ProjectTagsResponse[]> {
    return WebClient.get<BaseResponse<ProjectTagsResponse[]>>('/v1/projects/tags?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  }
}
