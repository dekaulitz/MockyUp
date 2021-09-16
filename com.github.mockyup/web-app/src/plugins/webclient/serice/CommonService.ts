import { StorageKeyType } from '@/plugins/webclient/model/EnumModel'
import axios from 'axios'

const host = process.env.NODE_ENV === 'production' ? 'http://localhost:7070' : 'http://localhost:7070'
export const webClient = axios.create({
  baseURL: host,
  headers: {
    'client-id': 'devstock',
    Accept: 'application/json'
  }
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
