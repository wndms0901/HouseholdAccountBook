import ReportService from '../../service/report_service'

const reportStore = {
    namespaced: true,
    actions: {
        /**
         * 월 보고서 조회
         * @param {*} ReportRequestDto
         * @returns 
         */
        selectMonthReport({ }, ReportRequestDto) {
            return ReportService.selectMonthReport(ReportRequestDto).then(
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
         * 연간보고서 조회
         * @param {*} ReportRequestDto
         * @returns 
         */
        selectYearReport({ }, ReportRequestDto) {
            return ReportService.selectYearReport(ReportRequestDto).then(
                response => {
                    console.log('response', response);
                    return Promise.resolve(response);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        }
    }
}

export default reportStore