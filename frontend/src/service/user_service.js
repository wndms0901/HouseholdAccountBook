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
     * 회원등록
     * @param {*} userDto
     * @returns
     */
    register(userDto) {
        return axios.post(API_URL + 'register', userDto);
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


}

export default new UserService();