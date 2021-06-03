
const state = {
    LoadingStatus: false
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
        }
    },
}

export default loadingStore