import axios from '../util/axios';

const API_URL = 'http://localhost:8080/write/';

class WriteService {
    /**
     * 수입/지출 상세 조회
     * @param {*} writeRequestDto
     * @returns
     */
    selectIncomeExpenditureDetail(writeRequestDto) {
        return axios
            .post(API_URL + 'income-expenditure/detail', writeRequestDto)
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }
    /**
     * 카테고리 목록 조회
     * @param {*} categoryType
     * @returns
     */
    selectCategoryList(categoryType) {
        return axios
            .get(API_URL + 'category/list', { params: { categoryType: categoryType } })
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }
    /**
     * 지출 목록 조회
     * @param {*} writeRequestDto 
     * @returns 
     */
    selectExpenditureList(writeRequestDto) {
        return axios
            .get(API_URL + 'expenditure/list', {
                params: {
                    startDate: writeRequestDto.startDate,
                    endDate: writeRequestDto.endDate,
                    email: writeRequestDto.email,
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
     * 정산 저장
     * @param {*} WriteRequestDto
     * @returns 
     */
    saveCalculation(WriteRequestDto) {
        return axios
            .post(API_URL + 'calculation/save', WriteRequestDto)
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }

    /**
     * 수입 목록 조회
     * @param {*} writeRequestDto 
     * @returns 
     */
    selectIncomeList(writeRequestDto) {
        return axios
            .get(API_URL + 'income/list', {
                params: {
                    startDate: writeRequestDto.startDate,
                    endDate: writeRequestDto.endDate,
                    email: writeRequestDto.email,
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