
import {
  BaseResponse,
  ProjectCardInterface,
  ProjectInterface
} from '@/plugins/webclient/model/ResponseModel'
import { WebClient } from '@/plugins/webclient/tmp/serice/CommonService'

export interface GetProjectParam {
  page: number
  size: number,
  sort?: any
}

interface ProjectService {
  getProjects (parameter?: GetProjectParam): Promise<BaseResponse<ProjectCardInterface>>

  getProjectById (id: string): Promise<BaseResponse<ProjectCardInterface>>

}

const ProjectService: ProjectService = {
  getProjects: async function (parameter: GetProjectParam): Promise<BaseResponse<ProjectCardInterface>> {
    return WebClient.get<BaseResponse<ProjectCardInterface>>('/v1/projects')
      .then(value => {
        return value.data
      }).catch((reason: BaseResponse) => {
        return reason
      })
  },
  getProjectById: async function (id: string): Promise<BaseResponse<ProjectInterface>> {
    return WebClient.get<BaseResponse<ProjectInterface>>('/v1/projects/' + id)
      .then(value => {
        return value.data
      }).catch((reason: BaseResponse) => {
        return reason
      })
  }
}

export default ProjectService
