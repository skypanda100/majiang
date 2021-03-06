import {queryService} from '@/libs/request';

function fetchJson (path) {
    return queryService({
        url: path
    });
}

export function fetchWaterFall () {
    return new Promise(function (resolve, reject) {
        fetchJson('/api/waterfall')
            .then(response => {
                resolve(response.data);
            });
    });
}

export function fetchScatter () {
    return new Promise(function (resolve, reject) {
        fetchJson('/api/scatter')
            .then(response => {
                resolve(response.data);
            });
    });
}

export function fetchLine () {
    return new Promise(function (resolve, reject) {
        fetchJson('/api/line')
            .then(response => {
                resolve(response.data);
            });
    });
}

export function fetchHeat () {
    return new Promise(function (resolve, reject) {
        fetchJson('/api/heat')
            .then(response => {
                resolve(response.data);
            });
    });
}

export function fetchDynamicBar () {
    return new Promise(function (resolve, reject) {
        fetchJson('/api/dynamicbar')
            .then(response => {
                resolve(response.data);
            });
    });
}
