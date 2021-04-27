import ExcelService from '../../service/excel_service'


const excelStore = {
    namespaced: true,
    actions: {
        /**
         * 엑셀 다운로드
         * @param {*} ExcelRequestDto
         * @returns 
         */
        excelDownload({ }, ExcelRequestDto) {
            return ExcelService.excelDownload(ExcelRequestDto).then(
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

export default excelStore