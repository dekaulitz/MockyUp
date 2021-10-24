export interface PageableParam {
  page: number
  size: number
  sort?: string
}

export interface AuthLogin {
  usernameOrEmail: string
  password: string
  rememberMe: boolean
}
