import { StorageKeyType } from '@/plugins/webclient/model/EnumModel'
import axios from 'axios'
import { BaseResponse } from '@/plugins/webclient/model/ResponseModel'

const host = process.env.NODE_ENV === 'production' ? 'http://localhost:7070' : 'http://localhost:7070'
export const WebClient = axios.create({
  baseURL: host,
  headers: {
    'client-id': 'devstock',
    Accept: 'application/json'
  }
})
WebClient.interceptors.request.use(value => {
  const authProfile = StorageService.getData(StorageKeyType.AUTH_PROFILE)
  if (authProfile) {
    value.headers.Authorization = `Bearer ${authProfile.accessToken}`
  }
  return value
}
)
WebClient.interceptors.response.use(value => value, error => {
  const responseData: BaseResponse = error.response.data
  if (responseData.statusCode === 4032) {
    StorageService.clearData(StorageKeyType.AUTH_PROFILE)
  }
  return responseData
})

interface LocalStorage {
  getData<T = any> (key: StorageKeyType): T

  setData<T = any> (key: StorageKeyType, t: T): T

  clearData (key: StorageKeyType): void
}

export const StorageService: LocalStorage = {
  setData (key: StorageKeyType, t: any): any {
    localStorage.setItem(key, JSON.stringify(t))
    return t
  },
  clearData (key: StorageKeyType) {
    localStorage.removeItem(key)
  },
  getData (key: StorageKeyType): any {
    return JSON.parse(localStorage.getItem(key))
  }
}
