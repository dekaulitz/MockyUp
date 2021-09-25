import { PageableParam } from '@/plugins/webclient/model/RequestModel'

export interface GetUsersParam extends PageableParam {
  userNameOrEmail?: string
}

export interface UserCardsResponse {
  id?: string
  updatedByUserId?: string
  createdByUserId?: string
  username?: string
  email?: string
  enabled: boolean
  accountNonLocked: boolean
  updatedDate?: string
}
