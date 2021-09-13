import {ActionInputModel} from "@/shared/form/InputModel";

export enum InputValidationType {
  EMAIL = "email",
  REQUIRED = "required",
  Length = "length"
}

export interface InputValidationInterface {
  errMessage: string
  validationType: InputValidationType
  isShowErrMessage?: boolean
  maxLength?: number
  minLength?: number
}

export interface FormInputActionModel {
  validations: InputValidationInterface[]
  input: ActionInputModel
}

export interface FormGroupAction {
  needValidation: boolean
  isValidated: boolean
}
