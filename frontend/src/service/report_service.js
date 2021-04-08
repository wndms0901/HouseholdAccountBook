import axios from 'axios';

const API_URL = 'http://localhost:8080/report/';

class ReportService {
    /**
     * 월 보고서 조회
     * @param {*} ReportRequestDto
     * @returns 
     */
    selectMonthReport(ReportRequestDto) {
        return axios
            .post(API_URL + 'month', ReportRequestDto)
            .then(response => {
                console.log('response', response);
                return response.data;
            });
    }

}



export default new ReportService();