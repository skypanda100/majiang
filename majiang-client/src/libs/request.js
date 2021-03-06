import axios from 'axios';
import env from '../../build/env';

const queryService = axios.create({
    baseURL: env.QUERY_BASE_API,
    timeout: 60000,
    // withCredentials: true,
    responseType: 'json',
    method: 'get'
});

queryService.interceptors.request.use(config => {
    // config.headers['majiang-token'] = auth.getToken();
    return config;
}, error => {
    Promise.reject(error);
});

queryService.interceptors.response.use(
    response => response,
    error => {
        return Promise.reject(error);
    }
);

export {queryService};
