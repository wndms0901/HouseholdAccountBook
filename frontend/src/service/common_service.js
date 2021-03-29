import axios from 'axios';

const API_URL = 'http://localhost:8080/common/';

class CommonService {
    /**
     * 대분류 카테고리 목록 조회
     * @param {*} categoryType 
     * @returns 
     */
    selectLargeCtgryList(categoryType) {
        console.log('categoryType', categoryType);
        return axios
            .get(API_URL + 'large-category/list', { params: { categoryType: categoryType } })
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
        console.log('categoryType', categoryType);
        return axios
            .get(API_URL + 'category/list', { params: { categoryType: categoryType } })
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }
    /**
     * 통장 카테고리 목록 조회
     * @param {*} accountCategoryType
     * @returns
     */
    selectAccountCtgryList(accountCategoryType) {
        console.log('accountCategoryType', accountCategoryType);
        return axios
            .get(API_URL + 'account-category/list', { params: { accountCategoryType: accountCategoryType } })
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }

}


export default new CommonService();