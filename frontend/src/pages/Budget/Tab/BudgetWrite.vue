<template>
  <div>
    <div class="budgetWrite_top">
      <table>
        <tr>
          <th rowspan="2">{{ startDate }} - {{ endDate }}</th>
          <td>이달의 수입</td>
          <td class="td_text_rigth">
            <span>{{ this.totalIncome }}</span
            >원
          </td>
          <td>3개월 간 평균 지출</td>
          <td class="td_text_rigth">
            {{ this.threeMonthAverageExpenditure }}원
          </td>
        </tr>
        <tr>
          <td>예산 설정 가능 금액</td>
          <td class="td_text_rigth">
            <span :style="styleObject">{{ this.totalBudget }}</span
            >원
          </td>
          <td>지난달 지출</td>
          <td class="td_text_rigth">{{ this.lastMonthExpenditure }}원</td>
        </tr>
      </table>
    </div>
    <div class="grid_title">
      <span>카테고리별 예산</span>
    </div>
    <!-- <div
      style="height: 100%; display: flex; flex-direction: column"
      class="ag-theme-alpine"
    > -->
    <grid
      ref="budgetGrid"
      style="height: 420px"
      class="ag-theme-alpine"
      :defaultColDef="defaultColDef"
      :gridOptions="gridOptions"
      :columnDefs="columnDefs"
      :rowData="rowData"
      :pinnedBottomRowData="pinnedBottomRowData"
      :singleClickEdit="true"
      :getRowStyle="getRowStyle"
      :modules="modules"
      :frameworkComponents="frameworkComponents"
      @grid-ready="onGridReady"
    ></grid>
    <!-- </div> -->
    <div class="right_btn mt-3">
      <button class="saveBtn" @click="onSave">저장</button>
    </div>
  </div>
</template>
<script>
import { AllCommunityModules } from "@ag-grid-community/all-modules";
import BudgetCellRenderer from "../CellRenderer/BudgetCellRenderer";
export default {
  components: { AllCommunityModules, BudgetCellRenderer },
  props: {
    user: Object,
    period: Object,
    tabIndex: Number,
  },
  data() {
    return {
      defaultColDef: null,
      columnDefs: null,
      rowData: [],
      gridOptions: null,
      pinnedBottomRowData: null,
      modules: AllCommunityModules,
      frameworkComponents: null,
      getRowStyle: null,
      totalIncome: 0,
      totalBudget: 0,
      threeMonthAverageExpenditure: 0,
      lastMonthExpenditure: 0,
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
      const endDate = periodTo.getMonth() + 1 + "." + periodTo.getDate();
      return endDate;
    },
    budgetDate() {
      const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      return monthStartDate > 15
        ? this.$moment(this.period.to).format("YYYYMM")
        : this.$moment(this.period.from).format("YYYYMM");
    },
    styleObject() {
      if (this.totalBudget < 0) {
        return {
          color: "#ff5658",
        };
      }
    },
  },
  watch: {
    rowData: {
      deep: true,
      handler(newData) {
        // 합계 row
        // 예산 금액 숫자로 변환
        const expenditureBudgetAmount = _.reduce(
          this.rowData,
          function (sum, obj) {
            return sum + parseInt(obj.expenditureBudgetAmount);
          },
          0
        );
        const obj = [
          {
            largeCategoryName: "합계",
            expenditureBudgetAmount: expenditureBudgetAmount,
            expenditureAmount: _.sumBy(this.rowData, "expenditureAmount"),
            total: _.sumBy(this.rowData, "total"),
          },
        ];
        this.pinnedBottomRowData = obj;
        this.gridApi.setPinnedBottomRowData(obj);
      },
    },
    pinnedBottomRowData: {
      deep: true,
      handler(newData) {
        this.totalBudget =
          this.totalIncome - newData[0].expenditureBudgetAmount;
      },
    },
    // incomeBudgetAmount() {
    //   // 숫자만 입력
    //   this.incomeBudgetAmount = this.incomeBudgetAmount.replace(/[^0-9]/g, "");
    //   // 한자릿수 0만 입력
    //   if (
    //     parseInt(this.incomeBudgetAmount) === 0 ||
    //     this.incomeBudgetAmount === ""
    //   ) {
    //     this.incomeBudgetAmount = "0";
    //   }
    //   // 문자열의 맨 앞 0 제거
    //   if (this.incomeBudgetAmount.length > 1) {
    //     this.incomeBudgetAmount = this.incomeBudgetAmount.replace(/(^0+)/, "");
    //   }
    //   return this.incomeBudgetAmount;
    // },
  },
  created() {},
  beforeMount() {
    this.gridOptions = {};
    this.defaultColDef = {
      // sortable: true,
      // resizable: true,
      // filter: true,
    };
    // grid columns
    this.columnDefs = [
      { field: "largeCategoryId", hide: true },
      {
        headerName: "분류",
        field: "largeCategoryName",
      },
      {
        headerName: "예산",
        field: "expenditureBudgetAmount",
        cellRenderer: "BudgetCellRenderer",
        type: "numericColumn",
        // valueFormatter: (params) => {
        //   if (params.data.largeCategoryId === 1) {
        //     return "-";
        //   }
        // },
        editable: (params) => {
          if (params.node.rowPinned || params.data.largeCategoryId === 1) {
            return false;
          } else {
            return true;
          }
        },
        // cellStyle: (params) => {
        //   return { backgroundColor: "#F8FFFA" };
        //   // if (params.node.rowPinned) {
        //   //   return { color: "#1fab89" };
        //   // }
        // },
      },
      {
        headerName: "지출",
        field: "expenditureAmount",
        type: "numericColumn",
        cellStyle: (params) => {
          if (params.node.rowPinned) {
            return { color: "#1fab89" };
          }
        },
      },
      {
        headerName: "남은 돈",
        field: "total",
        type: "numericColumn",
        cellStyle: (params) => {
          if (params.value < 0) {
            return { color: "#ff5658" };
          }
        },
      },
    ];
    this.frameworkComponents = {
      BudgetCellRenderer: BudgetCellRenderer,
    };
    this.getRowStyle = (params) => {
      if (params.node.rowPinned) {
        return { "font-weight": "bold" };
      }
    };
  },
  mounted() {
    if (this.tabIndex === 0) {
      this.getBudgetList();
    }
  },
  methods: {
    onGridReady(params) {
      this.gridApi = params.api;
      this.columnApi = params.columnApi;

      params.api.sizeColumnsToFit();
    },
    // 예산 목록 조회
    getBudgetList() {
      // 3개월 전 시작일
      const threeMonthStartDate = this.$moment(this.period.from).subtract(
        3,
        "months"
      );
      // 지난달 시작일
      const lastMonthStartDate = this.$moment(this.period.from).subtract(
        1,
        "months"
      );

      const budgetRequestDto = {
        budgetDate: this.budgetDate,
        thisMonthStartDate: this.$moment(this.period.from).format("YYYYMMDD"),
        thisMonthEndDate: this.$moment(this.period.to).format("YYYYMMDD"),
        threeMonthStartDate: threeMonthStartDate.format("YYYYMMDD"),
        threeMonthEndDate: this.$moment(threeMonthStartDate._d)
          .add(3, "months")
          .subtract(1, "days")
          .format("YYYYMMDD"),
        lastMonthStartDate: lastMonthStartDate.format("YYYYMMDD"),
        lastMonthEndDate: this.$moment(lastMonthStartDate._d)
          .add(1, "months")
          .subtract(1, "days")
          .format("YYYYMMDD"),
        email: this.user.userInfo.email,
      };
      console.log("budgetRequestDto", budgetRequestDto);
      this.$store
        .dispatch("budgetStore/selectBudgetList", budgetRequestDto)
        .then((res) => {
          console.log("결과>", res.data);
          // 이번달 수입 합계
          this.totalIncome = res.data.totalIncome;
          // 3개월 간 평균 지출
          this.threeMonthAverageExpenditure =
            res.data.threeMonthAverageExpenditure;
          // // 지난달 지출
          this.lastMonthExpenditure = res.data.lastMonthExpenditure;
          // 카테고리별 예산 grid
          this.rowData = res.data.budgetListDtoList;
          this.gridApi.setRowData(res.data.budgetListDtoList);
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    onSave() {
      this.gridApi.clearFocusedCell();
      const budgetListDtoList = this.$refs.budgetGrid.getRowData();
      budgetListDtoList.pop();
      const budgetDto = {
        budgetListDtoList: budgetListDtoList,
        budgetDate: this.budgetDate,
        userDto: this.user.userInfo,
      };
      this.$store
        .dispatch("budgetStore/saveBudgetList", budgetDto)
        .then((res) => {
          this.getBudgetList();
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
  },
};
</script>
<style scoped>
.budgetWrite_top {
  height: 100px;
  margin-bottom: 30px;

  border: 1px solid lightgray;
  border-radius: 5px;
  background-color: white;
}
.budgetWrite_top > table {
  width: 900px;
  margin-top: 10px;
  border-collapse: collapse;
}
.budgetWrite_top > table tr:first-child span {
  color: #608cef;
}
.budgetWrite_top > table tr th {
  /* border: 1px solid lightgrey; */
  width: 28%;
  padding-left: 20px;
  /* text-align: center; */
  font-size: 36px;
  /* font-weight: bold;
  font-weight: 550; */
  color: #424242;
  /* padding: 6px 0px 5px 12px; */
}
.budgetWrite_top > table tr td {
  /* border: 1px solid lightgrey; */
  padding: 8px 0px 8px 12px;
}
.budgetWrite_top > table tr td:first-of-type {
  width: 18%;
}
.budgetWrite_top > table tr td:nth-of-type(3) {
  width: 23%;
  padding-left: 65px;
}
.td_text_rigth {
  width: 14%;
  text-align: right;
}
.grid_title {
  /* display: inline-block; */
  /* width: 20%; */
  margin-bottom: 5px;
  text-align: left;
  font-size: 18px;
  font-weight: 600;
}
</style>