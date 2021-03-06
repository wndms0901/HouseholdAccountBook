<template>
  <div>
    <div class="grid_top">
      <div class="grid_title">
        <span>예산 대비 지출</span>
      </div>
      <div class="pb-2 excel_btn_box">
        <span
          ><b-icon
            icon="circle-fill"
            font-scale="0.5"
            style="color: #608cef"
          ></b-icon
        ></span>
        <span>남은돈</span>&nbsp;
        <span
          ><b-icon
            icon="circle-fill"
            font-scale="0.5"
            style="color: #ff5658"
          ></b-icon
        ></span>
        <span>예산초과</span>
        <button class="ml-3 basicBtn" @click="excelDownload">
          엑셀 다운로드
        </button>
      </div>
    </div>
    <grid
      ref="budgetExpndGrid"
      style="height: 660px"
      class="ag-theme-alpine"
      :gridOptions="gridOptions"
      :columnDefs="columnDefs"
      :defaultColDef="defaultColDef"
      :rowData="rowData"
      :getRowStyle="getRowStyle"
      @grid-ready="onGridReady"
    >
    </grid>
  </div>
</template>
<script>
export default {
  name: "BudgetExpenditure",
  props: {
    user: Object,
    period: Object,
    monthStartDate: String,
  },
  data() {
    return {
      gridOptions: null,
      columnDefs: null,
      defaultColDef: null,
      getRowStyle: null,
      rowData: [],
      monthOfYearList: [],
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
        if (diff > 2) {
          this.getMonthOfYear();
        }
      },
    },
  },
  beforeMount() {
    this.gridOptions = {};
    this.defaultColDef = {
      valueFormatter: (params) => {
        return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      },
      tooltipValueGetter: (params) => {
        return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
      },
      cellStyle: (params) => {
        if (params.data.largeCategoryId === 0) {
          // 합계 row
          if (params.value > 0) {
            return { color: "#608cef" };
          } else if (params.value < 0) {
            return { color: "#ff5658" };
          }
        }
        return null;
      },
    };
    this.columnDefs = [
      { field: "largeCategoryId", hide: true },
      {
        headerName: "기간",
        field: "largeCategoryName",
      },
      {
        headerName: "",
        field: "firstMonth",
        type: "numericColumn",
      },
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
    this.getRowStyle = function (params) {
      if (params.data.largeCategoryId === 0) {
        return {
          backgroundColor: "rgba(33, 150, 243, 0.1)",
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
      // 예산 대비 지출 목록 조회
      this.selectBudgetExpenditureList();
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
    // 예산 대비 지출 목록 조회
    selectBudgetExpenditureList() {
      const budgetRequestDto = {
        periodDtoList: this.monthOfYearList,
      };
      this.$store
        .dispatch("budgetStore/selectBudgetExpenditureList", budgetRequestDto)
        .then((res) => {
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
        budgetRequestDto: {
          periodDtoList: this.monthOfYearList,
        },
        pageName: "BudgetExpenditure",
        period: period,
      };
      this.$store
        .dispatch("excelStore/excelDownload", excelRequestDto)
        .then((res) => {
          const fileName =
            "가계부_예산대비지출_" + startDate + "_" + endDate + ".xlsx";
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