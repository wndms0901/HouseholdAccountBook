<template>
  <div>
    <div class="monthReport_top">
      <table style="width: 700px">
        <tr>
          <th rowspan="2">
            <h1>{{ startDate }} - {{ endDate }}</h1>
          </th>
          <td>
            연간 수입 <span class="text-danger">{{ totalExpenditure }}</span>
          </td>
        </tr>
        <tr>
          <td>
            지출 합계 <span class="text-primary">{{ totalIncome }}</span>
          </td>
        </tr>
      </table>
    </div>
    <!-- <div class="monthReport_centent"> -->
    <div>
      <h4>연간 보고서 현황</h4>
      <grid
        ref="yearReportGrid"
        style="height: 580px"
        class="ag-theme-alpine"
        :gridOptions="gridOptions"
        :columnDefs="columnDefs"
        :defaultColDef="defaultColDef"
        :rowData="rowData"
        rowSelection="multiple"
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
      rowData: [],
    };
  },
  computed: {
    startDate() {
      const periodFrom = this.period.from;
      const startDate = periodFrom.getMonth() + 1 + "." + periodFrom.getDate();
      return startDate;
    },
    endDate() {
      const periodTo = this.period.to;
      const endDate = periodTo.getMonth() + 1 + "." + periodTo.getDate();
      return endDate;
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
      { headerName: "1", field: "firstMonth", type: "numericColumn" },
      { headerName: "2", field: "secondMonth", type: "numericColumn" },
      { headerName: "3", field: "thirdMonth", type: "numericColumn" },
      { headerName: "4", field: "fourthMonth", type: "numericColumn" },
      { headerName: "5", field: "fifthMonth", type: "numericColumn" },
      { headerName: "6", field: "sixthMonth", type: "numericColumn" },
      { headerName: "7", field: "seventhMonth", type: "numericColumn" },
      { headerName: "8", field: "eighthMonth", type: "numericColumn" },
      { headerName: "9", field: "ninthMonth", type: "numericColumn" },
      { headerName: "10", field: "tenthMonth", type: "numericColumn" },
      { headerName: "11", field: "eleventhMonth", type: "numericColumn" },
      { headerName: "12", field: "twelfthMonth", type: "numericColumn" },
      { headerName: "합계", field: "total", type: "numericColumn" },
    ];
  },
  mounted() {
    this.getMonthOfYear();
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
      //const period_to = _.cloneDeep(this.period.to);
      for (let i = 0; i < 12; i++) {
        const startDate = this.$moment(period_from).add(i, "months");
        const endDate = this.$moment(period_from)
          .add(i + 1, "months")
          .subtract(1, "days");
        const obj = {
          year: startDate.format("YYYY"),
          month: startDate.format("MM"),
          startDate: startDate.format("YYYYMMDD"),
          endDate: endDate.format("YYYYMMDD"),
          email: this.user.userInfo.email,
        };
        monthOfYearList.push(obj);
      }
      this.selectYearReport(monthOfYearList);
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
          this.gridApi.setRowData(res.data);
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
  },
};
</script>
<style scoped>
.monthReport_centent > h4 {
  margin-top: 0;
}
</style>