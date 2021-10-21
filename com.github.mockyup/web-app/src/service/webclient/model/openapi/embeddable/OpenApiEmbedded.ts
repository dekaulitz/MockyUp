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
  variables?: Map<string, unknown>
  extensions?: Map<string, unknown>
}

export interface Schema {
  _default?: unknown
  example?: unknown
  _enum?: string[]
  name?: string
  title?: string
  multipleOf?: number
  maximum?: number
  exclusiveMaximum?: number
  minimum?: number
  exclusiveMinimum?: number
  maxLength?: number
  minLength?: number
  pattern?: string
  maxItems?: number
  minItems?: number
  uniqueItems?: boolean
  maxProperties?: number
  minProperties?: number
  required?: string[]
  type?: string
  not?: Schema
  properties?: Map<string, Schema>
  additionalProperties?: unknown
  description?: string
  format?: string
  $ref?: string
  nullable?: boolean
  readOnly?: boolean
  writeOnly?: boolean
  externalDocs?: unknown
  deprecated?: boolean
  xml?: unknown
  extensions?: Map<string, unknown>
  discriminator?: unknown
  reference$ref?: Schema
  value?: unknown
}

export interface Content {
  contentType?: string
  schema?: Map<string, Schema>
  examples?: Map<string, unknown>
  example?: unknown
  encoding?: Map<string, unknown>
  extensions?: Map<string, unknown>
}

export interface RequestBody {
  description?: string
  content: Content[]
  required?: boolean
  extensions?: Map<string, unknown>
  $ref?: string
}

export interface Parameter {
  name?: string
  in: string
  description?: string
  required?: boolean
  deprecated?: boolean
  allowEmptyValue?: boolean
  $ref?: string
  style?: string
  explode?: boolean
  allowReserved?: boolean
  schema?: Schema
  examples?: Map<string, unknown>
  example?: unknown
  content?: unknown[]
  extensions?: Map<string, unknown>
}

export interface Operation {
  summary?: string
  tags?: string[]
  description?: string
  externalDocs?: unknown
  operationId?: string
  parameters?: Parameter[]
  requestBody?: RequestBody
  responses?: unknown
  callbacks?: unknown
  deprecated?: boolean
  security?: unknown
  servers?: OpenApiServer[]
  extensions?: unknown
  mockup?: Map<string, unknown>
}

export interface Path {
  path?: string
  httpMethod?: string
  extensions?: Map<string, unknown>
  servers?: OpenApiServer[]
  parameters?: Parameter[]
  operation?: Operation
  $ref?: string
}

export interface OpenApiComponents {
  schemas?: Map<string, Schema>
  responses?: unknown[]
  parameters?: Map<string, unknown>
  examples?: Map<string, unknown>
  requestBodies?: Map<string, unknown>
  headers?: Map<string, unknown>
  securitySchemes?: Map<string, unknown>
  extensions?: Map<string, unknown>
}
