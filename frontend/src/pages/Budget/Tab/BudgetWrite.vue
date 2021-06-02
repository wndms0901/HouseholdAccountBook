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
    <div class="grid_top">
      <div class="grid_title">
        <span>카테고리별 예산</span>
      </div>
      <div class="pb-2 excel_btn_box">
        <button class="basicBtn" @click="excelDownload">엑셀 다운로드</button>
      </div>
    </div>
    <grid
      ref="budgetGrid"
      style="height: 490px"
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
import InputCellEditor from "src/components/CellEditor/InputCellEditor";
export default {
  name: "BudgetWrite",
  components: { AllCommunityModules, InputCellEditor },
  props: {
    user: Object,
    period: Object,
    monthStartDate: String,
  },
  data() {
    return {
      gridApi: null,
      defaultColDef: null,
      columnDefs: null,
      rowData: [],
      gridOptions: null,
      pinnedBottomRowData: null,
      modules: AllCommunityModules,
      frameworkComponents: null,
      getRowStyle: null,
      totalIncomeNumber: 0,
      totalBudgetNumber: 0,
      threeMonthAverageExpenditureNumber: 0,
      lastMonthExpenditureNumber: 0,
      originRowData: [],
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
    budgetDate() {
      return parseInt(this.monthStartDate) < 16
        ? this.$moment(this.period.from).format("YYYYMM")
        : this.$moment(this.period.to).format("YYYYMM");
    },
    // 3개월 전 시작일
    threeMonthStartDate() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        return this.$moment(this.period.from)
          .subtract(3, "months")
          .endOf("month");
      } else {
        // 월시작일이 말일이 아닌 경우
        return this.$moment(this.period.from).subtract(3, "months");
      }
    },
    // 3개월 전 종료일
    threeMonthEndDate() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        return this.$moment(this.threeMonthStartDate._d)
          .add(3, "months")
          .endOf("month")
          .subtract(1, "days");
      } else {
        // 월시작일이 말일이 아닌 경우
        return this.$moment(this.threeMonthStartDate._d)
          .add(3, "months")
          .subtract(1, "days");
      }
    },
    // 지난달 시작일
    lastMonthStartDate() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        return this.$moment(this.period.from)
          .subtract(1, "months")
          .endOf("month");
      } else {
        // 월시작일이 말일이 아닌 경우
        return this.$moment(this.period.from).subtract(1, "months");
      }
    },
    // 지난달 종료일
    lastMonthEndDate() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        return this.$moment(this.lastMonthStartDate._d)
          .add(1, "months")
          .endOf("month")
          .subtract(1, "days");
      } else {
        // 월시작일이 말일이 아닌 경우
        return this.$moment(this.lastMonthStartDate._d)
          .add(1, "months")
          .subtract(1, "days");
      }
    },
    totalIncome() {
      return String(this.totalIncomeNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    totalBudget() {
      return String(this.totalBudgetNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    threeMonthAverageExpenditure() {
      return String(this.threeMonthAverageExpenditureNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    lastMonthExpenditure() {
      return String(this.lastMonthExpenditureNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    styleObject() {
      if (this.totalBudgetNumber < 0) {
        return {
          color: "#ff5658",
        };
      }
    },
  },
  watch: {
    period: {
      deep: true,
      handler(newData) {
        const periodFrom = this.$moment(newData.from);
        const periodTo = this.$moment(newData.to);
        const diff = periodTo.diff(periodFrom, "months");
        // 예산쓰기 기간이 변경되었을때만 재조회
        if (diff < 2) {
          this.getBudgetList();
        }
      },
    },
    rowData: {
      deep: true,
      handler(newData) {
        // 합계 row
        // 예산 금액 숫자로 변환
        const budgetAmount = _.reduce(
          this.rowData,
          function (sum, obj) {
            return sum + parseInt(String(obj.budgetAmount).replace(/,/g, ""));
          },
          0
        );
        const obj = [
          {
            largeCategoryName: "합계",
            budgetAmount: budgetAmount,
            expenditureAmount: _.sumBy(this.rowData, "expenditureAmount"),
            total: _.sumBy(this.rowData, "total"),
          },
        ];
        this.pinnedBottomRowData = obj;
        if (this.gridApi) {
          this.gridApi.setPinnedBottomRowData(obj);
        }
      },
    },
    pinnedBottomRowData: {
      deep: true,
      handler(newData) {
        // 예산 설정 가능 금액
        this.totalBudgetNumber =
          this.totalIncomeNumber - newData[0].budgetAmount;
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
        field: "budgetAmount",
        cellEditor: "InputCellEditor",
        type: "numericColumn",
        valueFormatter: (params) => {
          if (params.data.largeCategoryId === 1) {
            return "-";
          }
          return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
        editable: (params) => {
          if (params.node.rowPinned || params.data.largeCategoryId === 1) {
            return false;
          } else {
            return true;
          }
        },
        cellStyle: (params) => {
          if (params.node.rowPinned || params.data.largeCategoryId === 1) {
            return {
              backgroundColor: "rgba(33, 150, 243, 0.1)",
            };
          } else {
            return {
              backgroundColor: "rgba(33, 150, 243, 0.1)",
              cursor: "pointer",
            };
          }
        },
      },
      {
        headerName: "지출",
        field: "expenditureAmount",
        type: "numericColumn",
        valueFormatter: (params) => {
          return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
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
        valueFormatter: (params) => {
          return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
        cellStyle: (params) => {
          if (params.value < 0) {
            return { color: "#ff5658" };
          }
        },
      },
    ];
    this.frameworkComponents = {
      InputCellEditor: InputCellEditor,
    };
    this.getRowStyle = (params) => {
      if (params.node.rowPinned) {
        return { "font-weight": "bold" };
      }
    };
  },
  mounted() {
    this.getBudgetList();
  },
  methods: {
    onGridReady(params) {
      this.gridApi = params.api;
      this.columnApi = params.columnApi;

      params.api.sizeColumnsToFit();
    },
    // 예산 목록 조회
    getBudgetList() {
      this.rowData = [];
      const budgetRequestDto = {
        budgetDate: this.budgetDate,
        thisMonthStartDate: this.$moment(this.period.from).format("YYYYMMDD"),
        thisMonthEndDate: this.$moment(this.period.to).format("YYYYMMDD"),
        threeMonthStartDate: this.$moment(this.threeMonthStartDate).format(
          "YYYYMMDD"
        ),
        threeMonthEndDate: this.$moment(this.threeMonthEndDate).format(
          "YYYYMMDD"
        ),
        lastMonthStartDate: this.$moment(this.lastMonthStartDate).format(
          "YYYYMMDD"
        ),
        lastMonthEndDate: this.$moment(this.lastMonthEndDate).format(
          "YYYYMMDD"
        ),
        email: this.user.userInfo.email,
      };
      console.log("budgetRequestDto", budgetRequestDto);
      this.$store
        .dispatch("budgetStore/selectBudgetList", budgetRequestDto)
        .then((res) => {
          console.log("결과>", res.data);
          // 이번달 수입 합계
          this.totalIncomeNumber = res.data.totalIncome;
          // 3개월 간 평균 지출
          this.threeMonthAverageExpenditureNumber =
            res.data.threeMonthAverageExpenditure;
          // // 지난달 지출
          this.lastMonthExpenditureNumber = res.data.lastMonthExpenditure;
          // 카테고리별 예산 grid
          this.rowData = res.data.budgetListDtoList;
          this.gridApi.setRowData(res.data.budgetListDtoList);
          this.originRowData = _.cloneDeep(res.data.budgetListDtoList);
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // 저장
    onSave() {
      this.gridApi.clearFocusedCell();
      const budgetListDtoList = this.$refs.budgetGrid.getRowData();
      budgetListDtoList.pop();
      // 예산 금액 숫자로 변환
      _.map(budgetListDtoList, this.convertStringToNumber);
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
    // 예산 금액 숫자로 변환
    convertStringToNumber(obj) {
      obj.budgetAmount = parseInt(String(obj.budgetAmount).replace(/,/g, ""));
      return obj;
    },
    // 엑셀 다운로드
    excelDownload() {
      const startDate = this.$moment(this.period.from).format("YYYYMMDD");
      const endDate = this.$moment(this.period.to).format("YYYYMMDD");
      const period = startDate
        .substr(0, 4)
        .concat(
          "년",
          startDate.substr(4, 2),
          "월",
          startDate.substr(6, 2),
          "일 ~ ",
          endDate.substr(0, 4),
          "년",
          endDate.substr(4, 2),
          "월",
          endDate.substr(6, 2),
          "일"
        );
      const excelRequestDto = {
        budgetRequestDto: {
          budgetDate: this.budgetDate,
          thisMonthStartDate: this.$moment(this.period.from).format("YYYYMMDD"),
          thisMonthEndDate: this.$moment(this.period.to).format("YYYYMMDD"),
          threeMonthStartDate: this.$moment(this.threeMonthStartDate).format(
            "YYYYMMDD"
          ),
          threeMonthEndDate: this.$moment(this.threeMonthEndDate).format(
            "YYYYMMDD"
          ),
          lastMonthStartDate: this.$moment(this.lastMonthStartDate).format(
            "YYYYMMDD"
          ),
          lastMonthEndDate: this.$moment(this.lastMonthEndDate).format(
            "YYYYMMDD"
          ),
          email: this.user.userInfo.email,
        },
        pageName: "BudgetWrite",
        period: period,
      };
      this.$store
        .dispatch("excelStore/excelDownload", excelRequestDto)
        .then((res) => {
          const fileName =
            "가계부_예산내역_" + startDate + "_" + endDate + ".xlsx";
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
<style scoped>
.budgetWrite_top {
  height: 100px;
  margin-bottom: 25px;
  border: 1px solid lightgray;
  border-radius: 5px;
  background-color: white;
}
.budgetWrite_top > table {
  width: inherit;
  margin-top: 10px;
  border-collapse: collapse;
}
.budgetWrite_top > table tr:first-child span {
  color: #608cef;
}
.budgetWrite_top > table tr th {
  /* border: 1px solid lightgrey; */
  padding-left: 20px;
  padding-right: 20px;
  font-size: 38px;
  color: #424242;
}
.budgetWrite_top > table tr td {
  /* border: 1px solid lightgrey; */
  padding: 8px 0px 8px 5px;
  color: #424242;
  font-weight: bold;
}

.budgetWrite_top > table tr td:nth-of-type(3) {
  padding-left: 40px;
}
.td_text_rigth {
  width: 135px;
  text-align: right;
}
</style>