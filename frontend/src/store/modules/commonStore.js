import CommonService from '../../service/common_service'


const commonStore = {
    namespaced: true,
    actions: {
        /**
         * 대분류 카테고리 목록 조회
         * @param {*} categoryType
         * @returns
         */
        selectLargeCtgryList({ }, categoryType) {
            return CommonService.selectLargeCtgryList(categoryType).then(
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
            return CommonService.selectCategoryList(categoryType).then(
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
         * 통장 카테고리 목록 조회
         * @param {*} accountCategoryType
         * @returns
         */
        selectAccountCtgryList({ }, accountCategoryType) {
            return CommonService.selectAccountCtgryList(accountCategoryType).then(
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

export default commonStore