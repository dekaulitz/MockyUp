import { WebClient } from '@/service/webclient/service/CommonService';
import Qs from 'querystring';
export const UserService = {
    deleteById(id) {
        return WebClient.delete('/v1/users/' + id)
            .then(value => {
            return value.data;
        });
    },
    doPost(request) {
        return WebClient.post('/v1/users', request)
            .then(value => {
            return value.data;
        });
    },
    doUpdate(request, id) {
        return WebClient.put('/v1/users/' + id, request)
            .then(value => {
            return value.data;
        });
    },
    getAll(param) {
        return WebClient.get('/v1/users?' + Qs.stringify(param))
            .then(value => {
            return value.data.data;
        });
    },
    getById(id) {
        return WebClient.get('/v1/users/' + id)
            .then(value => {
            return value.data.data;
        });
    },
    getCount(param) {
        return WebClient.get('/v1/users/count?' + Qs.stringify(param))
            .then(value => {
            return value.data.data;
        });
    }
};
//# sourceMappingURL=UserService.js.map