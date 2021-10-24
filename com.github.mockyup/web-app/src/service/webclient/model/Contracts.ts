import {
  ContractProjectInfo,
  OpenApiComponents,
  OpenApiServer,
  OpenApiTag,
  Path
} from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'
import { PageableParam } from '@/service/webclient/model/RequestModel'

export interface GetContractParam extends PageableParam {
  userNameOrEmail?: string
}

export interface ContractCreateRequest {
  projectId: string
  isPrivate: boolean
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
  projectId?: string
  private: boolean
  openApiVersion?: string,
  info?: ContractProjectInfo
  servers?: OpenApiServer[]
  security?: Map<string, string[]>
  paths?: Path[]
  tags?: OpenApiTag[]
  components?: OpenApiComponents
  rawSpecs?: string
}
