import { PageableParam } from '@/service/webclient/model/RequestModel'

export interface GetProjectParam extends PageableParam {
  projectName?: string
}

export interface GetProjectTags {
  tag?: string
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

export interface ProjectCreateRequest {
  projectName: string
  projectDescription: string
  projectTags?: string[]
}

export interface ProjectTagsResponse {
  tag: string
  totalProjects: number
}
