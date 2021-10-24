import { BaseResponse } from '@/service/webclient/model/ResponseModel'

import { AxiosResponse } from 'axios'
import { WebClient } from '@/service/webclient/service/CommonService'
import {
  ContractCardResponse,
  ContractCreateRequest, ContractDetailResponse,
  ContractUpdateRequest,
  GetContractParam
} from '@/service/webclient/model/Contracts'
import Qs from 'querystring'
import { BaseCrudService } from '@/service/webclient/base/BaseService'

export interface ContractServiceInterface extends BaseCrudService{
  doPost (request: ContractCreateRequest): Promise<BaseResponse>

  doUpdate (request: ContractUpdateRequest, id: string): Promise<BaseResponse>

  getById (id: string): Promise<ContractDetailResponse>

  getCount (param?: GetContractParam): Promise<number>

  getAll (param?: GetContractParam): Promise<ContractCardResponse[]>

  deleteById (id: string): Promise<BaseResponse>
}

export const ContractService: ContractServiceInterface = {
  deleteById (id: string): Promise<BaseResponse> {
    return WebClient.delete<BaseResponse, AxiosResponse<BaseResponse>>('/v1/project-contracts/' + id)
      .then(value => {
        return value.data
      })
  },
  doPost (request: ContractCreateRequest): Promise<BaseResponse> {
    return WebClient.post<ContractCreateRequest, AxiosResponse<BaseResponse>>('/v1/project-contracts', request)
      .then(value => {
        return value.data
      })
  },
  doUpdate (request: ContractUpdateRequest, id: string): Promise<BaseResponse> {
    return WebClient.put<ContractUpdateRequest, AxiosResponse<BaseResponse>>('/v1/project-contracts/' + id, request)
      .then(value => {
        return value.data
      })
  },
  getAll (param?: GetContractParam): Promise<ContractCardResponse[]> {
    return WebClient.get<BaseResponse<ContractCardResponse[]>>('/v1/project-contracts?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  },
  getById (id: string): Promise<ContractDetailResponse> {
    return WebClient.get<BaseResponse<ContractDetailResponse>>('/v1/project-contracts/' + id)
      .then(value => {
        return value.data.data
      })
  },
  getCount (param?: GetContractParam): Promise<number> {
    return WebClient.get<BaseResponse<number>>('/v1/project-contracts?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  }
}
