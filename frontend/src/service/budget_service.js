import axios from '../util/axios';

const API_URL = window.location.protocol + '//' + window.location.host + '/api/budget/';

class BudgetService {
    /**
     * 예산 목록 조회
     * @param {*} BudgetRequestDto
     * @returns
     */
    selectBudgetList(BudgetRequestDto) {
        return axios
            .post(API_URL + 'list', BudgetRequestDto)
            .then(response => {
                return response.data;
            });
    }
    /**
     * 예산 목록 저장
     * @param {*} BudgetDto
     * @returns
     */
    saveBudgetList(BudgetDto) {
        return axios
            .post(API_URL + 'save', BudgetDto)
            .then(response => {
                return response.data;
            });
    }
    /**
     * 예산 대비 지출 목록 조회
     * @param {*} BudgetRequestDto
     * @returns
     */
    selectBudgetExpenditureList(BudgetRequestDto) {
        return axios
            .post(API_URL + 'expenditure/list', BudgetRequestDto)
            .then(response => {
                return response.data;
            });
    }


}

export default new BudgetService();