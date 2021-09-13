type InputType = "text" | "password" | "email" | "checkbox"

export interface ActionInputModel {
  placeHolder?: string,
  hint?: string
  inputType: InputType
  value?: any
  formSubmitted?: boolean,
  disabledOnSubmitted: boolean
  hasError?: boolean
}

