import { WebClient } from '@/service/webclient/service/CommonService';
import Qs from 'querystring';
export const ContractService = {
    deleteById(id) {
        return WebClient.delete('/v1/project-contracts/' + id)
            .then(value => {
            return value.data;
        });
    },
    doPost(request) {
        return WebClient.post('/v1/project-contracts', request)
            .then(value => {
            return value.data;
        });
    },
    doUpdate(request, id) {
        return WebClient.put('/v1/project-contracts/' + id, request)
            .then(value => {
            return value.data;
        });
    },
    getAll(param) {
        return WebClient.get('/v1/project-contracts?' + Qs.stringify(param))
            .then(value => {
            return value.data.data;
        });
    },
    getById(id) {
        return WebClient.get('/v1/project-contracts/' + id)
            .then(value => {
            return value.data.data;
        });
    },
    getCount(param) {
        return WebClient.get('/v1/project-contracts?' + Qs.stringify(param))
            .then(value => {
            return value.data.data;
        });
    }
};
//# sourceMappingURL=ContractService.js.map