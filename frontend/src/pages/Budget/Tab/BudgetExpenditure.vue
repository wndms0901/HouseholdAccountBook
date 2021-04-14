<template>
  <div>
    <h4>예산 대비 지출</h4>
    <grid
      ref="budgetExpndGrid"
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
</template>
<script>
export default {
  props: {
    user: Object,
    period: Object,
    tabIndex: Number,
  },
  data() {
    return {
      gridOptions: null,
      columnDefs: null,
      defaultColDef: null,
      rowData: [],
    };
  },
  watch: {
    period: {
      deep: true,
      handler(newData) {
        const periodFrom = this.$moment(newData.from);
        const periodTo = this.$moment(newData.to);
        const diff = periodTo.diff(periodFrom, "months");
        // 예산 대비 지출 기간이 변경되었을때만 재조회
        if (diff > 0) {
          this.getMonthOfYear();
        }
      },
    },
  },
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
    ];
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
      const monthOfYearList = [];
      const period_from = _.cloneDeep(this.period.from);
      //const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      const monthStartDate = 16;
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
        console.log("obj", obj);
        monthOfYearList.push(obj);
      }
      // column headers name setting
      this.setHeaderNames(monthOfYearList);
      // 예산 대비 지출 목록 조회
      this.selectBudgetExpenditureList(monthOfYearList);
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
    // 예산 대비 지출 목록 조회
    selectBudgetExpenditureList(monthOfYearList) {},
  },
};
</script>