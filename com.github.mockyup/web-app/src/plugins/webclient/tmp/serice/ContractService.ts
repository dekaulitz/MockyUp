import {
  BaseResponse,
  ContractCardInterface
} from '@/plugins/webclient/model/ResponseModel'
import { WebClient } from '@/plugins/webclient/tmp/serice/CommonService'
import Qs from 'querystring'
import { ContractDetail } from '@/plugins/webclient/model/openapi/ContractModel'
export interface GetContractParam {
  page: number
  size: number,
  sort?: any
  projectId?:string
}

interface ContractService {
  getContracts (getContractParam:GetContractParam): Promise<BaseResponse<ContractCardInterface>>
  getContractById (id:string): Promise<BaseResponse<ContractDetail>>
}

const ContractService: ContractService = {
  getContractById (id: string): Promise<BaseResponse<ContractDetail>> {
    return WebClient.get<BaseResponse<ContractDetail>>('/v1/project-contracts/' + id)
      .then(value => {
        return value.data
      }).catch((reason: BaseResponse) => {
        return reason
      })
  },
  getContracts (getContractParam:GetContractParam): Promise<BaseResponse<ContractCardInterface>> {
    return WebClient.get<BaseResponse<ContractCardInterface>>('/v1/project-contracts?' + Qs.stringify(getContractParam))
      .then(value => {
        return value.data
      }).catch((reason: BaseResponse) => {
        return reason
      })
  }

}

export default ContractService
