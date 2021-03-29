import writeService from '../../service/write_service'

const writeStore = {
    namespaced: true,
    actions: {
        /**
         * 지출 목록 조회
         * @param {*} commonRequestDto 
         * @returns 
         */
        selectExpenditureList({ }, commonRequestDto) {
            return writeService.selectExpenditureList(commonRequestDto).then(
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
            return writeService.saveExpenditureList(ExpenditureSaveDto).then(
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
         * @param {*} commonRequestDto 
         * @returns 
         */
        selectIncomeList({ }, commonRequestDto) {
            return writeService.selectIncomeList(commonRequestDto).then(
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
            return writeService.saveIncomeList(IncomeSaveDto).then(
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