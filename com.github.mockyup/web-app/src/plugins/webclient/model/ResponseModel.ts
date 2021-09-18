import { ContractProjectInfo } from '@/plugins/webclient/model/Embedded'

export interface BaseResponse<T = any> {
  statusCode: number
  message: string
  data?: T
  error?: any
  requestId: string,
  requestTime: number
}

export interface AuthResponse {
  username: string
  accessToken: string
  access: string[]
}

export interface ProjectCardInterface {
  id?: string
  projectDescription?: string
  projectName?: string
  projectTags?: string[]
  updatedDate?: string
}

export interface ProjectInterface{
  id?: string
  projectName?:string
  projectDescription?:string
  projectTags?:string[]
  updatedByUserId?:string
  createdByUserId?:string
  updatedDate?:string
}
export interface ContractCardInterface{
  id?:string
  projectId?:string
  private:boolean
  openApiVersion?:string,
  info?:ContractProjectInfo
}
