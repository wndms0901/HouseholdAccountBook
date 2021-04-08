import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import commonStore from './modules/commonStore.js'
import userStore from './modules/userStore.js'
import writeStore from './modules/writeStore.js'
import budgetStore from './modules/budgetStore.js'
import reportStore from './modules/reportStore.js'

const store = new Vuex.Store({
    modules: {
        commonStore: commonStore,
        userStore: userStore,
        writeStore: writeStore,
        budgetStore: budgetStore,
        reportStore: reportStore,
    }
})

export default store