import BudgetService from '../../service/budget_service'

const budgetStore = {
    namespaced: true,
    actions: {
        /**
         * 예산 목록 조회
         * @param {*} BudgetRequestDto
         * @returns 
         */
        selectBudgetList({ }, BudgetRequestDto) {
            return BudgetService.selectBudgetList(BudgetRequestDto).then(
                response => {
                    console.log('response', response);
                    return Promise.resolve(response);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        },
        /**
         * 예산 목록 저장
         * @param {*} ExpenditureSaveDto
         * @returns 
         */
        saveBudgetList({ }, BudgetDto) {
            return BudgetService.saveBudgetList(BudgetDto).then(
                response => {
                    console.log('response', response);
                    return Promise.resolve(response);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        },
        /**
         * 예산 대비 지출 목록 조회
         * @param {*} BudgetRequestDto
         * @returns 
         */
        selectBudgetExpenditureList({ }, BudgetRequestDto) {
            return BudgetService.selectBudgetExpenditureList(BudgetRequestDto).then(
                response => {
                    console.log('response', response);
                    return Promise.resolve(response);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        },

    }
}

export default budgetStore