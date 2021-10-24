import { BaseCrudService } from '@/service/webclient/base/BaseService'
import { BaseResponse } from '@/service/webclient/model/ResponseModel'

import { AxiosResponse } from 'axios'
import { WebClient } from '@/service/webclient/service/CommonService'
import Qs from 'querystring'
import { GetContractParam } from '@/service/webclient/model/Contracts'

export const ContractService: BaseCrudService = {
  doPost: async function <ContractCreateRequest, BaseResponse> (createRequest: ContractCreateRequest): Promise<BaseResponse> {
    return WebClient.post<ContractCreateRequest, AxiosResponse<BaseResponse>>('/v1/project-contracts', createRequest)
      .then(value => {
        return value.data
      })
  },
  doUpdate<T = never, R = never> (t: T, id: string): Promise<R> {
    return Promise.resolve(undefined)
  },
  getAll<ContractCard> (param: GetContractParam): Promise<ContractCard> {
    return WebClient.get<BaseResponse<ContractCard>>('/v1/project-contracts?' + Qs.stringify(param))
      .then(value => {
        return value.data.data
      })
  },
  getById<ContractDetail> (id: string): Promise<ContractDetail> {
    return WebClient.get<BaseResponse<ContractDetail>>('/v1/project-contracts/' + id)
      .then(value => {
        return value.data.data
      })
  },
  getCount (parameter?: never): Promise<number> {
    return Promise.resolve(0)
  }
}
