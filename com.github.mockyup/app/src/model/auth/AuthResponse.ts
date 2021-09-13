import {BaseModel} from "@/shared/base/BaseModel";

export interface IAuthProfileModel {
  username: String
  accessToken: String
  access: String[]
}

export class AuthResponseModel extends BaseModel implements IAuthProfileModel {
  access = [];
  accessToken = "";
  username = "";
}
