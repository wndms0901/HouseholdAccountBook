<template>
  <div>
    <div class="budgetWrite_top">
      <table>
        <tr>
          <td style="width: 175px">
            <span style="font-size: 1.5em">04월 수입 예산</span>
          </td>
          <td style="width: 25%"></td>
          <td>3개월 간 평균 지출</td>
          <td style="text-align: right">
            {{ this.threeMonthAverageExpenditure }}원
          </td>
        </tr>
        <tr>
          <td style="text-align: right">
            <span style="font-size: 1.5em" v-show="showSpan"
              >{{ incomeBudgetAmount }}원</span
            >
            <input
              ref="incomeBudgetAmount"
              type="text"
              v-model="incomeBudgetAmount"
              v-show="showInput"
            />
          </td>
          <td>
            <b-button
              @mousedown="onClickUpdateIncomeBudget"
              @mouseup="onFocusIncomeBudget"
              >수정</b-button
            >
            <!-- <button style="vertical-align: top" @click="onUpdateIncomeBudget">
              수정
            </button> -->
          </td>
          <td>지난달 지출</td>
          <td style="text-align: right">{{ this.lastMonthExpenditure }}원</td>
        </tr>
      </table>
    </div>
    <div>
      <div class="grid_top_title">
        <span v-show="showBudget">{{ this.remainingBudget }}원 남음</span>
      </div>
      <div>
        <div class="grid_left_title">
          <span>카테고리별 예산</span>
        </div>
        <div class="grid_right_title">
          <span v-show="showBudget"
            >전체 예산 {{ this.incomeBudgetAmount }}원</span
          >
        </div>
      </div>
    </div>
    <div
      style="height: 100%; display: flex; flex-direction: column"
      class="ag-theme-alpine"
    >
      <grid
        ref="budgetGrid"
        style="height: 420px; flex: 1 1 auto"
        class="ag-theme-alpine"
        :gridOptions="topGridOptions"
        :columnDefs="columnDefs"
        :rowData="rowData"
        rowSelection="multiple"
        :modules="modules"
        @grid-ready="onGridReady"
      ></grid>
      <grid
        style="height: 50px; flex: none"
        :gridOptions="bottomGridOptions"
        :headerHeight="0"
        :columnDefs="columnDefs"
        :rowData="bottomData"
        :modules="modules"
        :rowStyle="rowStyle"
      ></grid>
    </div>
    <div class="right_btn mt-3"><button @click="onSave">저장</button></div>
  </div>
</template>
<script>
import { AllCommunityModules } from "@ag-grid-community/all-modules";
export default {
  components: { AllCommunityModules },
  props: {
    user: Object,
    period: Object,
    tabIndex: Number,
  },
  data() {
    return {
      columnDefs: null,
      rowData: [],
      topGridOptions: null,
      bottomData: null,
      bottomGridOptions: null,
      modules: AllCommunityModules,
      rowStyle: { fontWeight: "bold" },
      incomeBudgetAmount: "-",
      threeMonthAverageExpenditure: 0,
      lastMonthExpenditure: 0,
      showSpan: true,
      showInput: false,
    };
  },
  computed: {
    // readonly: {
    //   get() {
    //     if (this.incomeBudgetAmount === "-") {
    //       return true;
    //     } else {
    //       return false;
    //     }
    //   },
    //   set() {},
    // },

    showBudget() {
      if (this.incomeBudgetAmount === "-") {
        return false;
      } else {
        return true;
      }
    },
    remainingBudget() {
      if (this.incomeBudgetAmount === "-") {
        return "-";
      } else {
        return (
          this.incomeBudgetAmount - this.bottomData[0].expenditureBudgetAmount
        );
      }
    },
  },
  watch: {
    incomeBudgetAmount() {
      // 숫자만 입력
      this.incomeBudgetAmount = this.incomeBudgetAmount.replace(/[^0-9]/g, "");
      // 한자릿수 0만 입력
      if (parseInt(this.incomeBudgetAmount) === 0) {
        this.incomeBudgetAmount = "0";
      }
      // 문자열의 맨 앞 0 제거
      if (this.incomeBudgetAmount.length > 1) {
        this.incomeBudgetAmount = this.incomeBudgetAmount.replace(/(^0+)/, "");
      }
      return this.incomeBudgetAmount;
    },
    // incomeBudgetAmount: function () {
    //   // 숫자만 입력
    //   return (this.incomeBudgetAmount = this.incomeBudgetAmount.replace(
    //     /[^0-9]/g,
    //     ""
    //   ));
    // },
  },
  created() {},
  beforeMount() {
    this.topGridOptions = {
      alignedGrids: [],
      defaultColDef: {
        // editable: true,
        sortable: true,
        resizable: true,
        filter: true,
        flex: 1,
        minWidth: 100,
      },
      suppressHorizontalScroll: true,
    };
    this.bottomGridOptions = {
      alignedGrids: [],
      defaultColDef: {
        editable: true,
        sortable: true,
        resizable: true,
        filter: true,
        flex: 1,
        minWidth: 100,
      },
    };
    this.topGridOptions.alignedGrids.push(this.bottomGridOptions);
    this.bottomGridOptions.alignedGrids.push(this.topGridOptions);
    // grid columns
    this.columnDefs = [
      { field: "largeCategoryId", hide: true },
      {
        headerName: "분류",
        field: "largeCategoryName",
        //cellEditor: "datePicker",
      },
      {
        headerName: "예산",
        field: "expenditureBudgetAmount",
        type: "numericColumn",
        editable: true,
      },
      {
        headerName: "지출",
        field: "expenditureAmount",
        type: "numericColumn",
      },
      {
        headerName: "남은 돈",
        field: "total",
        type: "numericColumn",
      },
    ];
  },
  mounted() {
    if (this.tabIndex === 0) {
      this.getBudgetList();
    }
  },
  methods: {
    onGridReady(params) {
      console.log("params", params);
      this.gridApi = params.api;
      this.columnApi = params.columnApi;

      params.api.sizeColumnsToFit();
    },
    onClickUpdateIncomeBudget() {
      if (this.incomeBudgetAmount === "-") {
        this.incomeBudgetAmount = "";
      }
      this.showSpan = false;
      this.showInput = true;
    },
    onFocusIncomeBudget() {
      this.$refs.incomeBudgetAmount.focus();
    },
    // 예산 목록 조회
    getBudgetList() {
      const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      const budgetDate =
        monthStartDate > 15
          ? this.$moment(this.period.to).format("YYYYMM")
          : this.$moment(this.period.from).format("YYYYMM");
      const budgetRequestDto = {
        categoryType: "EXP",
        incomeBudgetDate: budgetDate,
        expenditureBudgetDate: budgetDate,
        email: this.user.userInfo.email,
      };
      this.$store
        .dispatch("budgetStore/selectBudgetList", budgetRequestDto)
        .then((res) => {
          // 한달 예산 readonly
          console.log("getBudgetList", res.data);
          // const selectBudgetList = _.cloneDeep(res.data.budgetListDtoList);
          // 미분류 마지막 순서로 변경
          // res.data.budgetListDtoList.shift();
          // res.data.budgetListDtoList.push(selectBudgetList[0]);

          // 한달 예산
          this.incomeBudgetAmount = res.data.incomeBudgetAmount;
          // 3개월 간 평균 지출
          this.threeMonthAverageExpenditure =
            res.data.threeMonthAverageExpenditure === null
              ? "0"
              : res.data.threeMonthAverageExpenditure;
          // 지난달 지출
          this.lastMonthExpenditure =
            res.data.lastMonthExpenditure === null
              ? "0"
              : res.data.lastMonthExpenditure;
          // 남은 돈 column setting
          _.forEach(res.data.budgetListDtoList, function (row, index) {
            res.data.budgetListDtoList[index].total =
              row.expenditureBudgetAmount - row.expenditureAmount;
          });
          this.gridApi.setRowData(res.data.budgetListDtoList);
          this.bottomData = [
            {
              largeCategoryName: "합계",
              expenditureBudgetAmount: _.sumBy(
                res.data.budgetListDtoList,
                function (o) {
                  return o.expenditureBudgetAmount;
                }
              ),
              expenditureAmount: _.sumBy(
                res.data.budgetListDtoList,
                function (o) {
                  return o.expenditureAmount;
                }
              ),
              total: _.sumBy(res.data.budgetListDtoList, function (o) {
                return o.total;
              }),
            },
          ];
          console.log("this.bottomData", this.bottomData);
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    onSave() {
      this.gridApi.clearFocusedCell();
      const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      const budgetDate =
        monthStartDate > 15
          ? this.$moment(this.period.to).format("YYYYMM")
          : this.$moment(this.period.from).format("YYYYMM");

      const budgetDto = {
        incomeBudgetAmount: this.incomeBudgetAmount,
        budgetListDtoList: this.$refs.budgetGrid.getRowData(),
        expenditureBudgetDate: budgetDate,
        incomeBudgetDate: budgetDate,
        userDto: this.user.userInfo,
      };
      console.log("budgetDto>>", budgetDto);
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
  margin-bottom: 20px;
  border: 1px solid lightgray;
  border-radius: 5px;
  background-color: white;
}
.budgetWrite_top > table {
  width: 600px;
  border-collapse: collapse;
}
.budgetWrite_top > table tr td {
  /* border: 1px solid lightgrey; */
  padding: 6px 0px 5px 12px;
}
.budgetWrite_top > table tr td input[type="text"] {
  width: 160px;
}
.budgetWrite_top button {
  width: 55px;
  height: 30px;
  padding: 0;
}
.grid_top_title {
  text-align: right;
}
.grid_left_title {
  display: inline-block;
  width: 20%;
  margin-bottom: 5px;
  text-align: left;
  font-size: 22px;
}
.grid_right_title {
  display: inline-block;
  width: 80%;
  text-align: right;
  vertical-align: top;
  color: gray;
}
</style>