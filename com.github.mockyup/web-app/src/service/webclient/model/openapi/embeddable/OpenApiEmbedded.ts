export interface ContractContact {
  name?: string
  url?: string
  email?: string
  extensions?: Map<string, unknown>
}

export interface ContractLicense {
  name?: string
  url?: string
  extensions?: Map<string, string>
}

export interface ExternalDocumentation {
  description?: string
  url?: string
  extensions?: Map<string, string>
}

export interface ContractProjectInfo {
  title?: string
  description?: string
  termsOfService?: string
  contact?: ContractContact
  license?: ContractLicense
  version?: string
  extensions?: Map<string, unknown>
}

export interface OpenApiTag {
  name?: string
  description?: string
  externalDocs?: ExternalDocumentation
  extensions?: Map<string, unknown>
}

export interface OpenApiServer {
  url?: string
  description?: string
  variables?:Map<string, unknown>
  extensions?: Map<string, unknown>
}

export interface Path{
  path?:string
  httpMethod?:string
  extensions?: Map<string, unknown>
  servers?:OpenApiServer[]
  parameters?:unknown[]
  operation:unknown
  $ref?:string
}

export interface OpenApiComponents{
  schemas?:Map<string, unknown>
  responses?:unknown[]
  parameters?:Map<string, unknown>
  examples?:Map<string, unknown>
  requestBodies?:Map<string, unknown>
  headers?:Map<string, unknown>
  securitySchemes?:Map<string, unknown>
  extensions?: Map<string, unknown>
}
