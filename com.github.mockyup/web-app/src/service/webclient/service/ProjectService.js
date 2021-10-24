import { WebClient } from '@/service/webclient/service/CommonService';
import Qs from 'querystring';
export const ProjectService = {
    deleteById(id) {
        return WebClient.delete('/v1/projects/' + id)
            .then(value => {
            return value.data;
        });
    },
    doPost(request) {
        return WebClient.post('/v1/projects', request)
            .then(value => {
            return value.data;
        });
    },
    doUpdate(request, id) {
        return WebClient.put('/v1/projects/' + id, request)
            .then(value => {
            return value.data;
        });
    },
    getAll(param) {
        return WebClient.get('/v1/projects?' + Qs.stringify(param))
            .then(value => {
            return value.data.data;
        });
    },
    getById(id) {
        return WebClient.get('/v1/projects/' + id)
            .then(value => {
            return value.data.data;
        });
    },
    getCount(param) {
        return WebClient.get('/v1/projects/count?' + Qs.stringify(param))
            .then(value => {
            return value.data.data;
        });
    },
    getTags(param) {
        return WebClient.get('/v1/projects/tags?' + Qs.stringify(param))
            .then(value => {
            return value.data.data;
        });
    }
};
//# sourceMappingURL=ProjectService.js.map