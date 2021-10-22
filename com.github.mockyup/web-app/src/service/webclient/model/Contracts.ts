import {
  ContractProjectInfo,
  Component,
  OpenApiServer,
  OpenApiTag,
  Path
} from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'
import { PageableParam } from '@/service/webclient/model/RequestModel'

export interface GetContractParam extends PageableParam {
  projectId?: string
}

export interface ContractCreateRequest {
  projectId: string
  private: boolean
  spec: any
}
export interface ContractUpdateRequest {
  projectId: string
  private: boolean
  spec: any
}

export interface ContractCard {
  id?: string
  projectId?: string
  private: boolean
  openApiVersion?: string,
  info?: ContractProjectInfo
}

export interface ContractDetail {
  id?: string
  createdDate?:string
  projectId?: string
  private: boolean
  openApiVersion?: string,
  info?: ContractProjectInfo
  servers?: OpenApiServer[]
  security?: Map<string, string[]>
  paths?: Path[]
  tags?: OpenApiTag[]
  components?: Component
  rawSpecs?: string
}
