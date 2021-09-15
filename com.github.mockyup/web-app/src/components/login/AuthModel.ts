export interface LoginRequest {
  usernameOrEmail: string
  password?: string
  rememberMe?: boolean
}

export interface AuthProfileResponse {
  username: string
  accessToken: string
  access: string[]
}
