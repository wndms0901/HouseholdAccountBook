import Vue from 'vue'
import VueRouter from 'vue-router';
import store from '../store'

import DashboardLayout from '../layout/DashboardLayout.vue'
import LoginLayout from '../layout/LoginLayout.vue'
// GeneralViews
import NotFound from '../pages/NotFoundPage.vue'

// Admin pages
import Write from 'src/pages/Write/Write.vue'
import Report from 'src/pages/Report/Report.vue'
import Budget from 'src/pages/Budget/Budget.vue'
import MyInfo from 'src/pages/User/MyInfo.vue'
import Overview from 'src/pages/Overview.vue'
import UserProfile from 'src/pages/UserProfile.vue'
import TableList from 'src/pages/TableList.vue'
import Typography from 'src/pages/Typography.vue'
import Icons from 'src/pages/Icons.vue'
import Notifications from 'src/pages/Notifications.vue'
import Upgrade from 'src/pages/Upgrade.vue'

import Login from 'src/pages/Auth/Login.vue'
import Register from 'src/pages/Auth/Register.vue'

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    component: DashboardLayout,
    redirect: '/mybook/write'
  },
  {
    path: '/mybook',
    component: DashboardLayout,
    redirect: '/mybook/write',
    children: [
      {
        path: 'budget',
        name: 'Budget',
        component: Budget
      },
      {
        path: 'write',
        name: 'Write',
        component: Write
      },
      {
        path: 'report',
        name: 'Report',
        component: Report
      },
      {
        path: 'myInfo',
        name: 'MyInfo',
        component: MyInfo
      },
      {
        path: 'Overview',
        name: 'Overview',
        component: Overview
      },
      {
        path: 'user',
        name: 'User',
        component: UserProfile
      },
      {
        path: 'table-list',
        name: 'Table List',
        component: TableList
      },
      {
        path: 'typography',
        name: 'Typography',
        component: Typography
      },
      {
        path: 'icons',
        name: 'Icons',
        component: Icons
      },
      {
        path: 'notifications',
        name: 'Notifications',
        component: Notifications
      },
      {
        path: 'upgrade',
        name: 'Upgrade to PRO',
        component: Upgrade
      }
    ]
  },

  {
    path: '/user',
    name: 'LoginLayout',
    component: LoginLayout,
    redirect: '/user/login',
    children: [{ path: 'login', name: 'Login', component: Login },
    { path: 'register', name: 'Register', component: Register }]
  },
  { path: '*', name: 'Error', component: NotFound }
]

// configure router
const router = new VueRouter({
  mode: 'history',
  routes, // short for routes: routes
  linkActiveClass: 'nav-item active',
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
  console.log('to', to);
  console.log('from', from);
  setTimeout(() => {
    if (authRequired && !loginCheck && to.name !== "Error") {
      router.push({ path: '/user/login' });
    } else {
      store.commit('loadingStore/startSpinner');
      next();
    }
  }, 500);
})

router.afterEach((to, from) => {
  store.commit('loadingStore/endSpinner');
})

/**
 * Asynchronously load view (Webpack Lazy loading compatible)
 * The specified component must be inside the Views folder
 * @param  {string} name  the filename (basename) of the view to load.
function view(name) {
   var res= require('../components/Dashboard/Views/' + name + '.vue');
   return res;
};**/

export default router
