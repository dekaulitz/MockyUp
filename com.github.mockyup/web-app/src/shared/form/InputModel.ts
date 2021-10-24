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

export interface InputAttribute {
  validMessage?: string
  placeHolder?: string
  isValid: boolean
  hint?: string
  type: InputType
  validations?: InputValidationInterface[]
  errorMessage?: string
  value?: any
  values?: string[]
  formSubmitted: boolean
}

export interface ButtonAttribute {
  isLoading: boolean
  usingLoader: boolean
}
