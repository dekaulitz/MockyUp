import { PageableParam } from '@/plugins/webclient/model/RequestModel'

export interface GetProjectParam extends PageableParam {
  projectTitle?: string
}

export interface ProjectCardResponse {
  id?: string
  projectDescription?: string
  projectName?: string
  projectTags?: string[]
  updatedDate?: string
}

export interface ProjectResponse {
  id?: string
  projectName?: string
  projectDescription?: string
  projectTags?: string[]
  updatedByUserId?: string
  createdByUserId?: string
  updatedDate?: string
}
