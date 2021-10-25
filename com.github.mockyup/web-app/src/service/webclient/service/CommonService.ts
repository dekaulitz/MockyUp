import { StorageKeyType } from '@/service/webclient/model/EnumModel'
import axios from 'axios'
const host = process.env.VUE_APP_NODE_ENV === 'production' ? process.env.VUE_APP_API_HOST : 'http://localhost:7070'
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
