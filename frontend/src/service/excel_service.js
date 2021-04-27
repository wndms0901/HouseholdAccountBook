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

}


export default new ExcelService();