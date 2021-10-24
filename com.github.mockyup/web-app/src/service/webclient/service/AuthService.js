import { StorageService, WebClient } from '@/service/webclient/service/CommonService';
import { StorageKeyType } from '@/service/webclient/model/EnumModel';
export const AuthLoginService = {
    getAuthDetail() {
        return Promise.resolve(undefined);
    },
    doLogin: async function (authLogin) {
        return WebClient.post('/v1/login', authLogin)
            .then(value => {
            return value.data.data;
        });
    },
    logout: async function () {
        const auth = StorageService.getData(StorageKeyType.AUTH_PROFILE);
        return WebClient.put('/v1/logout', {}, {
            headers: {
                Authorization: 'Bearer ' + auth.accessToken
            }
        }).then(value => {
            StorageService.clearData(StorageKeyType.AUTH_PROFILE);
            return value.data;
        });
    },
    refreshLogin() {
        return undefined;
    }
};
export default AuthLoginService;
//# sourceMappingURL=AuthService.js.map