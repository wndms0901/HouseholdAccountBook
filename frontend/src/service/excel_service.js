import axios from 'axios';

const API_URL = 'http://localhost:8080/excel/';

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
                console.log('response', response);
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
                console.log('response', response);
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
                        // "X-AUTH-TOKEN": token,
                        "Content-Type": `multipart/form-data`,
                    }
                }
            )
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }

}


export default new ExcelService();