import Vue from 'vue';
import App from './app.vue';

import * as G2Plot from '@antv/g2plot';

Vue.prototype.$G2Plot = G2Plot;

new Vue({
    el: '#app',
    created () {
    },
    render: h => h(App)
});
