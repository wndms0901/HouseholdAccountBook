import axios from 'axios'
import store from '../store/index'
import router from '../routes/index'

/* axios 인스턴스를 생성 */
const axiosInstance = axios.create({
})

/*  요청 인터셉터 */
axiosInstance.interceptors.request.use(
    config => {
        if (store.state.userStore.initialState.user) {
            config.headers.Authorization = store.state.userStore.initialState.user.token;
        }
        if (!store.state.loadingStore.LoadingDisable) {
            store.commit('loadingStore/startSpinner');
        }
        return config;
    },
    error => {
        store.commit('loadingStore/endSpinner');
        return Promise.reject(error);
    }
);

/*  응답 인터셉터 */
axiosInstance.interceptors.response.use(
    response => {
        store.commit('loadingStore/endSpinner');
        return response;
    },
    error => {
        console.log(error.response);
        if (error.response.status === 403) {
            store.dispatch("userStore/logout");
        } else {
            router.push({ path: '/error', query: { status: error.response.status, statusText: error.response.statusText } });
        }
        store.commit('loadingStore/endSpinner');
        return Promise.reject(error);
    }
)

export default axiosInstance
