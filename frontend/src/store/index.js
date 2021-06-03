import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from "vuex-persistedstate"

Vue.use(Vuex)

import excelStore from './modules/excelStore.js'
import userStore from './modules/userStore.js'
import writeStore from './modules/writeStore.js'
import budgetStore from './modules/budgetStore.js'
import reportStore from './modules/reportStore.js'
import loadingStore from './modules/loadingStore.js'

const store = new Vuex.Store({
    modules: {
        excelStore: excelStore,
        userStore: userStore,
        writeStore: writeStore,
        budgetStore: budgetStore,
        reportStore: reportStore,
        loadingStore: loadingStore
    },
    plugins: [createPersistedState({
        // 필요한 모듈만 선택해서 저장
        paths: ['userStore']
    })]
})

export default store