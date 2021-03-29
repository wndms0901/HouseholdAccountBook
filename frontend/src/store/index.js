import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import commonStore from './modules/commonStore.js'
import userStore from './modules/userStore.js'
import writeStore from './modules/writeStore.js'

const store = new Vuex.Store({
    modules: {
        userStore: userStore,
        writeStore: writeStore,
        commonStore: commonStore,
    }
})

export default store