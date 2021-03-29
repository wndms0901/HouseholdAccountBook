import axios from 'axios';

const API_URL = 'http://localhost:8080/write/';

class WriteService {
    /**
     * 지출 목록 조회
     * @param {*} commonRequestDto 
     * @returns 
     */
    selectExpenditureList(commonRequestDto) {
        return axios
            .get(API_URL + 'expenditure/list', {
                params: {
                    startDate: commonRequestDto.startDate,
                    endDate: commonRequestDto.endDate,
                    email: commonRequestDto.email,
                }
            })
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }
    /**
     * 지출 목록 저장
     * @param {*} ExpenditureSaveDto
     * @returns 
     */
    saveExpenditureList(ExpenditureSaveDto) {
        return axios
            .post(API_URL + 'expenditure/save', ExpenditureSaveDto)
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }

    /**
     * 수입 목록 조회
     * @param {*} commonRequestDto 
     * @returns 
     */
    selectIncomeList(commonRequestDto) {
        return axios
            .get(API_URL + 'income/list', {
                params: {
                    startDate: commonRequestDto.startDate,
                    endDate: commonRequestDto.endDate,
                    email: commonRequestDto.email,
                }
            })
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }
    /**
     * 수입 목록 저장
     * @param {*} IncomeSaveDto 
     * @returns 
     */
    saveIncomeList(IncomeSaveDto) {
        return axios
            .post(API_URL + 'income/save', IncomeSaveDto)
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }

}



export default new WriteService();