
const state = {
    LoadingStatus: false,
    LoadingDisable: false
};

const loadingStore = {
    namespaced: true,
    state: state,
    mutations: {
        startSpinner(state) {
            state.LoadingStatus = true;
        },
        endSpinner(state) {
            state.LoadingStatus = false;
        },
        disableLoading(state) {
            state.LoadingDisable = true;
        },
        enableLoading(state) {
            state.LoadingDisable = false;
        },
    },
}

export default loadingStore