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
    <div class="yearReport_centent">
      <div class="grid_title">
        <span>연간 보고서 현황</span>
      </div>
      <grid
        ref="yearReportGrid"
        style="height: 530px"
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
  </div>
</template>
<script>
export default {
  props: {
    user: Object,
    period: Object,
  },
  data() {
    return {
      totalExpenditure: 0,
      totalIncome: 0,
      gridOptions: null,
      columnDefs: null,
      defaultColDef: null,
      getRowStyle: null,
      rowData: [],
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
  },
  watch: {
    period: {
      deep: true,
      handler(newData) {
        const periodFrom = this.$moment(newData.from);
        const periodTo = this.$moment(newData.to);
        const diff = periodTo.diff(periodFrom, "months");
        // 연간보고서 기간이 변경되었을때만 재조회
        if (diff > 0) {
          this.getMonthOfYear();
        }
      },
    },
  },
  created() {},
  beforeMount() {
    this.gridOptions = {};
    this.defaultColDef = {};
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
  mounted() {
    // if (this.tabIndex === 1) {
    //   alert(this.tabIndex);
    //   this.getMonthOfYear();
    // }
  },
  methods: {
    onGridReady(params) {
      this.gridApi = params.api;
      this.columnApi = params.columnApi;
      params.api.sizeColumnsToFit();
    },
    // 월 목록 조회
    getMonthOfYear() {
      const monthOfYearList = [];
      const period_from = _.cloneDeep(this.period.from);
      const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      for (let i = 0; i < 12; i++) {
        const startDate = this.$moment(period_from).add(i, "months");
        const endDate = this.$moment(period_from)
          .add(i + 1, "months")
          .subtract(1, "days");

        const year =
          monthStartDate > 15
            ? endDate.format("YYYY")
            : startDate.format("YYYY");
        const month =
          monthStartDate > 15 ? endDate.format("MM") : startDate.format("MM");

        const obj = {
          year: year,
          month: month,
          startDate: startDate.format("YYYYMMDD"),
          endDate: endDate.format("YYYYMMDD"),
          email: this.user.userInfo.email,
        };
        monthOfYearList.push(obj);
      }
      // column headers name setting
      this.setHeaderNames(monthOfYearList);
      // 연간보고서 조회
      this.selectYearReport(monthOfYearList);
    },
    // column headers name setting
    setHeaderNames(monthOfYearList) {
      let columnDefs = this.columnDefs;

      _.forEach(monthOfYearList, function (obj, index) {
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
    selectYearReport(monthOfYearList) {
      const reportRequestDto = {
        periodDtoList: monthOfYearList,
      };
      this.$store
        .dispatch("reportStore/selectYearReport", reportRequestDto)
        .then((res) => {
          console.log("결과>", res.data);
          const totalIncome = _.filter(res.data, {
            largeCategoryId: "",
          });
          const totalExpenditure = _.filter(res.data, {
            largeCategoryId: "0",
          });
          // 연간 수입
          this.totalIncome = totalIncome[0].total;
          // 지출 합계
          this.totalExpenditure = totalExpenditure[0].total;
          // 연간 수입/지출 목록
          this.gridApi.setRowData(res.data);
          this.gridApi.sizeColumnsToFit();
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
  },
};
</script>
<style scoped>
.yearReport_centent {
  margin-top: 20px;
}
</style>