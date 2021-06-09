/*!

 =========================================================
 * Vue Light Bootstrap Dashboard - v2.0.0 (Bootstrap 4)
 =========================================================

 * Product Page: http://www.creative-tim.com/product/light-bootstrap-dashboard
 * Copyright 2019 Creative Tim (http://www.creative-tim.com)
 * Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard/blob/master/LICENSE.md)

 =========================================================

 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 */
import Vue from 'vue'
import App from './App.vue'
import vClickOutside from 'v-click-outside'
import VueCookies from 'vue-cookies'

// LightBootstrap plugin
import LightBootstrap from './light-bootstrap-main'
// Bootstrap-vue
import { BootstrapVue, IconsPlugin, BFormFile } from 'bootstrap-vue'
// Import Bootstrap an BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

// router setup
import router from './routes/index'
// Axios
import axios from 'axios'
Vue.prototype.$Axios = axios;
// Store
import store from './store'

// ag-Grid
import { AgGridVue } from 'ag-grid-vue'
import '../node_modules/ag-grid-community/dist/styles/ag-grid.css'
import '../node_modules/ag-grid-community/dist/styles/ag-theme-alpine.css'
Vue.component('grid', AgGridVue)

// vue-google-charts
import VueGoogleCharts from 'vue-google-charts'

// lodash
import 'lodash'
// vue-moment
import VueMoment from 'vue-moment'
// vue-spinner
import FadeLoader from 'vue-spinner/src/FadeLoader.vue'
Vue.component('fade-loader', FadeLoader)

import './registerServiceWorker'
// plugin setup
Vue.use(vClickOutside)
Vue.use(VueCookies)
Vue.use(LightBootstrap)
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.component('b-form-file', BFormFile)
Vue.use(VueGoogleCharts)
Vue.use(VueMoment)

//set default config
Vue.$cookies.config(60 * 60 * 2, '', '', '')

/* eslint-disable no-new */
new Vue({
  el: '#app',
  render: h => h(App),
  router,
  store
})
