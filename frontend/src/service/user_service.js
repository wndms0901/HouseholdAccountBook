import axios from 'axios';

const API_URL = 'http://localhost:8080/user/';

class UserService {
    /**
     * 로그인
     * @param {*} user 
     * @returns 
     */
    login(user) {
        return axios
            .post(API_URL + 'login', {
                email: user.email,
                password: user.password
            })
            .then(response => {
                console.log('response', response);
                if (response.data.token) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    }
    /**
     * 로그아웃
     * @param {*}
     * @returns
     */
    logout() {
        localStorage.removeItem('user');
    }
    /**
     * 테스트 계정 로그인
     * @returns
     */
    testIdLogin() {
        return axios
            .get(API_URL + 'login/test-id')
            .then(response => {
                console.log('response', response);
                if (response.data.token) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    }
    /**
     * 회원등록
     * @param {*} userDto
     * @returns
     */
    register(userDto) {
        return axios.post(API_URL + 'register', userDto);
    }
    /**
     * 회원등록 확인
     * @param {*} email
     * @returns
     */
    selectRegisterCheck(email) {
        return axios
            .get(API_URL + 'register/check', { params: { email: email } })
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }
    /**
     * 임시 비밀번호 이메일 전송
     * @param {*} email
     * @returns
     */
    sendPasswordResetEmail(email) {
        return axios
            .get(API_URL + 'send/password-reset', { params: { email: email } })
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }
    /**
     * 월시작일 업데이트
     * @param {*} userDto
     * @returns
     */
    updateMonthStartDate(userDto) {
        return axios
            .post(API_URL + 'update/month-start-date', userDto)
            .then(response => {
                console.log('response', response);
                return response;
            });
    }
    /**
     * 비밀번호 변경
     * @param {*} userDto
     * @returns 
     */
    updatePassword(userDto) {
        return axios
            .post(API_URL + 'update/password', userDto)
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }


}

export default new UserService();