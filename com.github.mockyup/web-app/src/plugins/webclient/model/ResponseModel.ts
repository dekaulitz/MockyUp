export interface BaseResponse<T = any> {
  statusCode: number
  message: string
  data?: T
  error?: any
  requestId: string,
  requestTime: number
}

export interface AuthResponse {
  username: string
  accessToken: string
  access: string[]
}
