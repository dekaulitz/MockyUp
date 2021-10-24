import { StorageKeyType } from '@/service/webclient/model/EnumModel';
import axios from 'axios';
const host = process.env.NODE_ENV === 'production' ? process.env.HOST : 'http://localhost:7070';
export const WebClient = axios.create({
    baseURL: host,
    headers: {
        'client-id': 'devstock',
        Accept: 'application/json'
    }
});
WebClient.interceptors.request.use(value => {
    const authProfile = StorageService.getData(StorageKeyType.AUTH_PROFILE);
    if (authProfile) {
        value.headers.Authorization = `Bearer ${authProfile.accessToken}`;
    }
    return value;
});
export const StorageService = {
    setData(key, t) {
        localStorage.setItem(key, JSON.stringify(t));
        return t;
    },
    clearData(key) {
        localStorage.removeItem(key);
    },
    getData(key) {
        return JSON.parse(localStorage.getItem(key));
    }
};
//# sourceMappingURL=CommonService.js.map