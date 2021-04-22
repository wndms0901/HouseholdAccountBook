import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate";

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
    },
    plugins: [createPersistedState({
        // 필요한 모듈만 선택해서 저장
        paths: ["userStore"],
    }),],

})

export default store