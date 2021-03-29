import DashboardLayout from '../layout/DashboardLayout.vue'
import LoginLayout from '../layout/LoginLayout.vue'
// GeneralViews
import NotFound from '../pages/NotFoundPage.vue'

// Admin pages
import Write from 'src/pages/Write/Write.vue'
import Overview from 'src/pages/Overview.vue'
import UserProfile from 'src/pages/UserProfile.vue'
import TableList from 'src/pages/TableList.vue'
import Typography from 'src/pages/Typography.vue'
import Icons from 'src/pages/Icons.vue'
import Maps from 'src/pages/Maps.vue'
import Notifications from 'src/pages/Notifications.vue'
import Upgrade from 'src/pages/Upgrade.vue'

import Login from 'src/pages/Auth/Login.vue'
import Register from 'src/pages/Auth/Register.vue'

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
        path: 'write',
        name: 'Write',
        component: Write
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
        path: 'maps',
        name: 'Maps',
        component: Maps
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
  { path: '*', component: NotFound },
]

/**
 * Asynchronously load view (Webpack Lazy loading compatible)
 * The specified component must be inside the Views folder
 * @param  {string} name  the filename (basename) of the view to load.
function view(name) {
   var res= require('../components/Dashboard/Views/' + name + '.vue');
   return res;
};**/

export default routes
