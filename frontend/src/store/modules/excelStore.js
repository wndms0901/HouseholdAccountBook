import ExcelService from '../../service/excel_service'


const excelStore = {
    namespaced: true,
    actions: {
        /**
         * 엑셀 다운로드
         * @param {*} data
         * @returns 
         */
        excelDownload({ }, data) {
            return ExcelService.excelDownload(data).then(
                response => {
                    return Promise.resolve(response);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        },
        /**
         * 엑셀 양식 다운로드
         * @param {*} pageName
         * @returns
         */
        excelFormDownload({ }, pageName) {
            return ExcelService.excelFormDownload(pageName).then(
                response => {
                    return Promise.resolve(response);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        },
        /**
         * 엑셀 업로드
         * @param {*} ExcelRequestDto
         * @returns 
         */
        excelUpload({ }, ExcelRequestDto) {
            return ExcelService.excelUpload(ExcelRequestDto).then(
                response => {
                    return Promise.resolve(response);
                },
                error => {
                    return Promise.reject(error);
                }
            );
        }
    }
}

export default excelStore