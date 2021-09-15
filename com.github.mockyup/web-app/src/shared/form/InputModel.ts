export type InputType = 'text' | 'password' | 'email' | 'checkbox'

export enum InputValidationType {
  EMAIL = 'email',
  REQUIRED = 'required',
  Length = 'length'
}

export interface InputValidationInterface {
  isValid: boolean,
  errMessage: string
  successMessage?: string
  validationType: InputValidationType
  isShowErrMessage?: boolean
  maxLength?: number
  minLength?: number
}

export interface FormGroupAction {
  needValidation: boolean
  isValidated: boolean
}

export interface InputAttributeInterface {
  validMessage?: string
  placeHolder?: string
  isValid: boolean
  hint?: string
  type: InputType
  validations?: InputValidationInterface[]
  errorMessage?: string
  value?: any
  formSubmitted: boolean
}

export interface ButtonAttributeInterface {
  isLoading: boolean
  usingLoader: boolean
}

export interface FormContainerAttributes {
  successMessage?: string
  errorMessage?: string
  isError: boolean
}
