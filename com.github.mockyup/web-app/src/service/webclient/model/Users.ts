import { PageableParam } from '@/service/webclient/model/RequestModel'

export interface GetUsersParam extends PageableParam {
  userNameOrEmail?: string
}

export interface UserCreateRequest {
  username?: string
  email?: string
  password?: string
  access?: string[]
  isAccountNonLocked?: boolean
  isEnabled?: boolean
}

export interface UserUpdateRequest {
  username?: string
  email?: string
  access?: string[]
  isAccountNonLocked?: boolean
  isEnabled?: boolean
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

export interface UserDetailResponse {
  id?: string
  updatedByUserId?: string
  createdByUserId?: string
  username?: string
  email?: string
  enabled: boolean
  accountNonLocked: boolean
  updatedDate?: string
  access?: string[]
}
