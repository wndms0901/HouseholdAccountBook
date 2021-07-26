<template>
  <div>
    <div class="report_top">
      <table>
        <tr>
          <th rowspan="2">{{ startDate }} - {{ endDate }}</th>
          <td>
            연간 수입 <span class="ml-2 income_color">{{ totalIncome }}</span>
          </td>
        </tr>
        <tr>
          <td>
            지출 합계
            <span class="ml-2 expenditure_color">{{ totalExpenditure }}</span>
          </td>
        </tr>
      </table>
    </div>
    <div class="grid_top">
      <div class="grid_title">
        <span>연간 보고서 현황</span>
      </div>
      <div class="pb-2 excel_btn_box">
        <button class="basicBtn" @click="excelDownload">엑셀 다운로드</button>
      </div>
    </div>
    <grid
      ref="yearReportGrid"
      style="height: 550px"
      class="ag-theme-alpine"
      :gridOptions="gridOptions"
      :columnDefs="columnDefs"
      :defaultColDef="defaultColDef"
      :getRowStyle="getRowStyle"
      :rowData="rowData"
      @grid-ready="onGridReady"
    >
    </grid>
  </div>
</template>
<script>
export default {
  name: "YearReport",
  props: {
    user: Object,
    period: Object,
    monthStartDate: String,
  },
  data() {
    return {
      totalIncomeNumber: 0,
      totalExpenditureNumber: 0,
      gridOptions: null,
      columnDefs: null,
      defaultColDef: null,
      getRowStyle: null,
      rowData: [],
      monthOfYearList: [],
    };
  },
  computed: {
    startDate() {
      const periodFrom = this.period.from;
      let month = periodFrom.getMonth() + 1;
      month = month / 10 >= 1 ? month : "0" + month;
      let day = periodFrom.getDate();
      day = day / 10 >= 1 ? day : "0" + day;
      return month + "." + day;
    },
    endDate() {
      const periodTo = this.period.to;
      let month = periodTo.getMonth() + 1;
      month = month / 10 >= 1 ? month : "0" + month;
      let day = periodTo.getDate();
      day = day / 10 >= 1 ? day : "0" + day;
      return month + "." + day;
    },
    totalIncome() {
      return String(this.totalIncomeNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    totalExpenditure() {
      return String(this.totalExpenditureNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
  },
  watch: {
    period: {
      deep: true,
      handler(newData) {
        const periodFrom = this.$moment(newData.from);
        const periodTo = this.$moment(newData.to);
        const diff = periodTo.diff(periodFrom, "months");
        // 연간보고서 기간이 변경되었을때만 재조회
        if (diff > 2) {
          this.getMonthOfYear();
        }
      },
    },
  },
  created() {},
  beforeMount() {
    this.gridOptions = {};
    this.defaultColDef = {
      valueFormatter: (params) => {
        return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      },
      tooltipValueGetter: (params) => {
        return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      },
    };
    this.columnDefs = [
      { field: "largeCategoryId", hide: true },
      {
        headerName: "기간",
        field: "largeCategoryName",
      },
      { headerName: "", field: "firstMonth", type: "numericColumn" },
      { headerName: "", field: "secondMonth", type: "numericColumn" },
      { headerName: "", field: "thirdMonth", type: "numericColumn" },
      { headerName: "", field: "fourthMonth", type: "numericColumn" },
      { headerName: "", field: "fifthMonth", type: "numericColumn" },
      { headerName: "", field: "sixthMonth", type: "numericColumn" },
      { headerName: "", field: "seventhMonth", type: "numericColumn" },
      { headerName: "", field: "eighthMonth", type: "numericColumn" },
      { headerName: "", field: "ninthMonth", type: "numericColumn" },
      { headerName: "", field: "tenthMonth", type: "numericColumn" },
      { headerName: "", field: "eleventhMonth", type: "numericColumn" },
      { headerName: "", field: "twelfthMonth", type: "numericColumn" },
      { headerName: "합계", field: "total", type: "numericColumn" },
    ];
    this.getRowStyle = function (params) {
      if (params.data.largeCategoryId === "") {
        return {
          color: "#608cef",
          backgroundColor: "rgba(96, 140, 239, 0.1)",
          fontWeight: "bold",
        };
      } else if (params.data.largeCategoryId === "0") {
        return {
          color: "#1fab89",
          backgroundColor: "rgba(31, 171, 137, 0.1)",
          fontWeight: "bold",
        };
      }
    };
  },
  mounted() {},
  methods: {
    onGridReady(params) {
      this.gridApi = params.api;
      this.columnApi = params.columnApi;
      params.api.sizeColumnsToFit();
    },
    // 월 목록 조회
    getMonthOfYear() {
      this.monthOfYearList = [];
      const period_from = _.cloneDeep(this.period.from);
      let startDate = null;
      let endDate = null;
      let year = null;
      let month = null;
      for (let i = 0; i < 12; i++) {
        if (this.monthStartDate === "last") {
          // 월시작일이 말일인 경우
          startDate = this.$moment(period_from).add(i, "months").endOf("month");
          endDate = this.$moment(period_from)
            .add(i + 1, "months")
            .endOf("month")
            .subtract(1, "days");
          year = endDate.format("YYYY");
          month = endDate.format("MM");
        } else {
          // 월시작일이 말일이 아닌 경우
          startDate = this.$moment(period_from).add(i, "months");
          endDate = this.$moment(period_from)
            .add(i + 1, "months")
            .subtract(1, "days");
          year =
            parseInt(this.monthStartDate) > 15
              ? endDate.format("YYYY")
              : startDate.format("YYYY");
          month =
            parseInt(this.monthStartDate) > 15
              ? endDate.format("MM")
              : startDate.format("MM");
        }
        const obj = {
          year: year,
          month: month,
          startDate: startDate.format("YYYYMMDD"),
          endDate: endDate.format("YYYYMMDD"),
          email: this.user.userInfo.email,
        };
        this.monthOfYearList.push(obj);
      }
      // column headers name setting
      this.setHeaderNames();
      // 연간보고서 조회
      this.selectYearReport();
    },
    // column headers name setting
    setHeaderNames() {
      let columnDefs = this.columnDefs;

      _.forEach(this.monthOfYearList, function (obj, index) {
        // 첫번째 월과 1월은 연도 표시
        if (index === 0 || obj.month === "01") {
          columnDefs[index + 2].headerName = obj.year + "." + obj.month + " 월";
        } else {
          columnDefs[index + 2].headerName = obj.month + " 월";
        }
      });

      this.gridApi.setColumnDefs(columnDefs);
    },
    // 연간보고서 조회
    selectYearReport() {
      const reportRequestDto = {
        periodDtoList: this.monthOfYearList,
      };
      this.$store
        .dispatch("reportStore/selectYearReport", reportRequestDto)
        .then((res) => {
          const totalIncome = _.filter(res.data, {
            largeCategoryId: "",
          });
          const totalExpenditure = _.filter(res.data, {
            largeCategoryId: "0",
          });
          // 연간 수입
          this.totalIncomeNumber = totalIncome[0].total;
          // 지출 합계
          this.totalExpenditureNumber = totalExpenditure[0].total;
          // 연간 수입/지출 목록
          this.gridApi.setRowData(res.data);
          this.gridApi.sizeColumnsToFit();
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // 엑셀 다운로드
    excelDownload() {
      const startDate = this.$moment(this.period.from).format("YYYYMMDD");
      const endDate = this.$moment(this.period.to).format("YYYYMMDD");
      const period =
        this.monthOfYearList[0].year +
        "년 " +
        this.monthOfYearList[0].month +
        "월 ~ " +
        this.monthOfYearList[11].year +
        "년 " +
        this.monthOfYearList[11].month +
        "월";
      const excelRequestDto = {
        reportRequestDto: {
          periodDtoList: this.monthOfYearList,
        },
        pageName: "YearReport",
        period: period,
      };
      this.$store
        .dispatch("excelStore/excelDownload", excelRequestDto)
        .then((res) => {
          const fileName =
            "가계부_연간보고서_" + startDate + "_" + endDate + ".xlsx";
          const url = window.URL.createObjectURL(
            new Blob([res.data], {
              type: res.headers["content-type"],
            })
          );
          const link = document.createElement("a");
          link.href = url;
          link.setAttribute("download", fileName);
          document.body.appendChild(link);
          link.click();
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
  },
};
</script>