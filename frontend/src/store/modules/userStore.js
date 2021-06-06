import UserService from '../../service/user_service'
import cookies from 'vue-cookies'

const user = cookies.get('user');
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
         * 테스트 계정 로그인
         * @returns
         */
        testIdLogin({ commit }) {
            return UserService.testIdLogin().then(
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
         * 회원등록 확인
         * @param {*} email
         * @returns
         */
        selectRegisterCheck({ }, email) {
            return UserService.selectRegisterCheck(email).then(
                response => {
                    return Promise.resolve(response);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        },
        /**
         * 임시 비밀번호 이메일 전송
         * @param {*} email
         * @returns
         */
        sendPasswordResetEmail({ }, email) {
            return UserService.sendPasswordResetEmail(email).then(
                response => {
                    return Promise.resolve(response);
                },
                error => {
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
        },
        /**
         * 비밀번호 변경
         * @param {*} userDto
         * @returns
         */
        updatePassword({ }, userDto) {
            return UserService.updatePassword(userDto).then(
                response => {
                    return Promise.resolve(response);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        }
    }
}

export default userStore