export interface ContractContact{
  name?:string
  url?:string
  email?:string
  extensions?:Map<string,object>
}
export interface ContractLicense{
  name?:string
  url?:string
  extensions?:Map<string,string>
}

export interface ContractProjectInfo{
  title?:string
  description?:string
  termsOfService?:string
  contact?:ContractContact
  license?:ContractLicense
  version?:string
  extensions?:Map<string,object>
}

