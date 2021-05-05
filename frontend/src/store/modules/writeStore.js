import WriteService from '../../service/write_service'

const writeStore = {
    namespaced: true,
    actions: {
        /**
         * 수입/지출 상세 조회
         * @param {*} writeRequestDto
         * @returns
         */
        selectIncomeExpenditureDetail({ }, writeRequestDto) {
            return WriteService.selectIncomeExpenditureDetail(writeRequestDto).then(
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
         * 카테고리 목록 조회
         * @param {*} categoryType
         * @returns
         */
        selectCategoryList({ }, categoryType) {
            return WriteService.selectCategoryList(categoryType).then(
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
         * 지출 목록 조회
         * @param {*} writeRequestDto 
         * @returns 
         */
        selectExpenditureList({ }, writeRequestDto) {
            return WriteService.selectExpenditureList(writeRequestDto).then(
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
         * 지출 목록 저장
         * @param {*} ExpenditureSaveDto
         * @returns 
         */
        saveExpenditureList({ }, ExpenditureSaveDto) {
            return WriteService.saveExpenditureList(ExpenditureSaveDto).then(
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
         * 정산 저장
         * @param {*} WriteRequestDto
         * @returns 
         */
        saveCalculation({ }, WriteRequestDto) {
            return WriteService.saveCalculation(WriteRequestDto).then(
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
         * 수입 목록 조회
         * @param {*} writeRequestDto 
         * @returns 
         */
        selectIncomeList({ }, writeRequestDto) {
            return WriteService.selectIncomeList(writeRequestDto).then(
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
         * 수입 목록 저장
         * @param {*} IncomeSaveDto 
         * @returns 
         */
        saveIncomeList({ }, IncomeSaveDto) {
            return WriteService.saveIncomeList(IncomeSaveDto).then(
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

export default writeStore