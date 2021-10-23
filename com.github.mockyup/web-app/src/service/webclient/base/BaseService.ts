export interface PageableParam {
  page: number
  size: number
  sort?: string
}

export interface BaseResponse<T = never> {
  statusCode: number
  message: string
  data?: T
  error?: any
  requestId: string,
  requestTime: number
}

export interface BaseCrudService {
  getAll (parameter?: any): Promise<any[]>

  getCount (parameter?: never): Promise<any>

  getById (id: string): Promise<any>

  doPost (t: never): Promise<any>

  doUpdate (t: any, id: string): Promise<any>

  deleteById (id: string): Promise<any>
}
