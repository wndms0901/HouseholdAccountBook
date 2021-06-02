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
import VueRouter from 'vue-router'
import App from './App.vue'
import vClickOutside from 'v-click-outside'

// LightBootstrap plugin
import LightBootstrap from './light-bootstrap-main'
// Bootstrap-vue
import { BootstrapVue, IconsPlugin, BFormFile } from 'bootstrap-vue'
// Import Bootstrap an BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'


// router setup
import routes from './routes/routes'
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

import './registerServiceWorker'
// plugin setup
Vue.use(vClickOutside)
Vue.use(VueRouter)
Vue.use(LightBootstrap)
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.component('b-form-file', BFormFile)
Vue.use(VueGoogleCharts)
Vue.use(VueMoment)

// configure router
const router = new VueRouter({
  mode: 'history',
  routes, // short for routes: routes
  linkActiveClass: 'nav-item active'
  scrollBehavior: (to) => {
    if (to.hash) {
      return { selector: to.hash }
    } else {
      return { x: 0, y: 0 }
    }
  }
})

// 네비게이션 가드 : 뷰 라우터로 특정 URL에 접근할 때 해당 URL의 접근을 막는 방법
// 애플리케이션 전역에서 동작하는 전역 가드
// to : 이동할 URL 정보가 담긴 라우터 객체
// from : 현재 URL 정보가 담긴 라우터 객체
// next : to에서 지정한 URL로 이동하기 위해 반드시 호출해야하는 함수
// next 호출전까지는 대기 상태에서 머물기 때문에 화면전환이 이루어지지 않음.
router.beforeEach((to, from, next) => {
  const publicPages = ['Login', 'Register'];
  const authRequired = !publicPages.includes(to.name);
  const loginCheck = localStorage.getItem('user');
  console.log('loginCheck', loginCheck);
  if (authRequired && !loginCheck) {
    router.push({ path: '/user/login' });
  } else {
    next();
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  render: h => h(App),
  router,
  store
})
