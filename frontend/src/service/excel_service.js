import axios from '../util/axios';
const API_URL = window.location.protocol + '//' + window.location.host + '/api/excel/';

class ExcelService {
    /**
     * 엑셀 다운로드
     * @param {*} ExcelRequestDto
     * @returns 
     */
    excelDownload(ExcelRequestDto) {
        return axios
            .post(API_URL + 'download', ExcelRequestDto, {
                responseType: "blob",
            })
            .then(response => {
                return response;
            });
    }
    /**
     * 엑셀 양식 다운로드
     * @param {*} pageName
     * @returns
     */
    excelFormDownload(pageName) {
        return axios
            .get(API_URL + 'form/download', { params: { pageName: pageName }, responseType: 'blob' }
            )
            .then(response => {
                return response;
            });
    }
    /**
     * 엑셀 업로드
     * @param {*} data
     * @returns 
     */
    excelUpload(data) {
        return axios
            .post(API_URL + 'upload', data,
                {
                    headers: {
                        "Content-Type": `multipart/form-data`,
                    }
                }
            )
            .then(response => {
                return response.data;
            });
    }

}

export default new ExcelService();