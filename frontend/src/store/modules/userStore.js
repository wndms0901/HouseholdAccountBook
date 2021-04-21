import UserService from '../../service/user_service'

const user = JSON.parse(localStorage.getItem('user'));
const initialState = user
    ? { initialState: { status: { loggedIn: true }, user } }
    : {
        initialState: { status: { loggedIn: false }, user: null }
    };

const userStore = {
    namespaced: true,
    state: initialState,
    mutations: {
        loginSuccess(state, user) {
            state.initialState.status.loggedIn = true;
            state.initialState.user = user;
        },
        loginFailure(state) {
            state.initialState.status.loggedIn = false;
            state.initialState.user = null;
        },
        logout(state) {
            state.initialState.status.loggedIn = false;
            state.initialState.user = null;
        },
        registerSuccess(state) {
            state.initialState.status.loggedIn = false;
        },
        registerFailure(state) {
            state.initialState.status.loggedIn = false;
        },
        updateMonthStartDate(state, userDto) {
            state.initialState.user.userInfo.monthStartDate = userDto.monthStartDate;
            console.log('state.initialState.user.userInfo.monthStartDate', state.initialState.user.userInfo.monthStartDate);
        }
    },
    actions: {
        /**
         * 로그인
         * @param {*} user
         * @returns
         */
        login({ commit }, user) {
            return UserService.login(user).then(
                user => {
                    commit('loginSuccess', user);
                    return Promise.resolve(user);
                },
                error => {
                    commit('loginFailure');
                    return Promise.reject(error);
                }
            );
        },
        /**
         * 로그아웃
         * @param {*} 
         * @returns
         */
        logout({ commit }) {
            UserService.logout();
            commit('logout');
        },
        /**
         * 회원등록
         * @param {*} user
         * @returns
         */
        register({ commit }, user) {
            return UserService.register(user).then(
                response => {
                    commit('registerSuccess');
                    return Promise.resolve(response.data);
                },
                error => {
                    commit('registerFailure');
                    return Promise.reject(error);
                }
            );
        },
        /**
         * 월시작일 업데이트
         * @param {*} userDto
         * @returns
         */
        updateMonthStartDate({ commit }, userDto) {
            return UserService.updateMonthStartDate(userDto).then(
                response => {
                    commit('updateMonthStartDate', userDto);
                    return Promise.resolve(response.data);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        }
    }
}

export default userStore