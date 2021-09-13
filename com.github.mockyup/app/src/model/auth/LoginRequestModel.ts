import {BaseModel} from "@/shared/base/BaseModel";

export interface ILoginModel {
  usernameOrPassword: string
  password: string
  rememberMe?: boolean
}

export class LoginRequestModel extends BaseModel implements ILoginModel {
  usernameOrPassword = ""
  password = ""
  rememberMe = false
}
