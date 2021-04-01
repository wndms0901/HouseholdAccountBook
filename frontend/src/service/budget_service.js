import axios from 'axios';

const API_URL = 'http://localhost:8080/budget/';

class BudgetService {
    /**
     * 예산 목록 조회
     * @param {*} BudgetRequestDto
     * @returns 
     */
    selectBudgetList(BudgetRequestDto) {
        return axios
            .get(API_URL + 'list', {
                params: {
                    categoryType: BudgetRequestDto.categoryType,
                    incomeBudgetDate: BudgetRequestDto.incomeBudgetDate,
                    expenditureBudgetDate: BudgetRequestDto.expenditureBudgetDate,
                    email: BudgetRequestDto.email,
                }
            })
            .then(response => {
                console.log('response', response);
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
                console.log('response', response);
                return response.data;
            });
    }



}



export default new BudgetService();