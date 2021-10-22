import {
  ContractProjectInfo, Component, OpenApiServer,
  OpenApiTag, Path
} from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'

export interface ContractDetail {
  id?: string
  projectId?: string
  private: boolean
  openApiVersion?: string,
  info?: ContractProjectInfo
  servers?: OpenApiServer[]
  security?: Map<string, string[]>
  paths?:Path[]
  tags?: OpenApiTag[]
  components?: Component
  rawSpecs?: string
}
