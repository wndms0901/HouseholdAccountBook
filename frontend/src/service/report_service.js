import axios from '../util/axios';
const API_URL = window.location.protocol + '//' + window.location.host + '/api/report/';

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
                return response.data;
            });
    }
    /**
     * 연간보고서 조회
     * @param {*} ReportRequestDto
     * @returns 
     */
    selectYearReport(ReportRequestDto) {
        return axios
            .post(API_URL + 'year', ReportRequestDto)
            .then(response => {
                return response.data;
            });
    }

}

export default new ReportService();