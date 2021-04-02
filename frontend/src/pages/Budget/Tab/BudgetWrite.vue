<template>
  <div class="table-responsive">
    <table class="table" style="width: 600px">
      <!-- table-borderless -->
      <tr>
        <td rowspan="2">
          <p class="m-0">한달 예산</p>
          <input
            ref="incomeBudgetAmount"
            type="text"
            v-model="incomeBudgetAmount"
            :readonly="readonly"
          />
          <button @click="onUpdateIncomeBudget">수정</button>
        </td>
        <td>3개월 간 평균 지출</td>
        <td style="text-align: right">
          {{ this.threeMonthAverageExpenditure }}원
        </td>
      </tr>
      <tr>
        <td>지난달 지출</td>
        <td style="text-align: right">{{ this.lastMonthExpenditure }}원</td>
      </tr>
    </table>
    <div id="title">
      <div id="top_title">
        <span v-show="showBudget">{{ this.remainingBudget }}원 남음</span>
      </div>
      <div id="bottom_title">
        <div id="left_title"><span>카테고리별 예산</span></div>
        <div id="right_title">
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
        style="height: 400px; flex: 1 1 auto"
        class="ag-theme-alpine"
        :gridOptions="topGridOptions"
        :columnDefs="columnDefs"
        :rowData="rowData"
        rowSelection="multiple"
        :modules="modules"
        @first-data-rendered="onFirstDataRendered"
        @grid-ready="onGridReady"
      ></grid>
      <grid
        style="height: 60px; flex: none"
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
      lastMonthExpenditure: "",
      readonly: true,
    };
  },
  computed: {
    // readonly: {
    //   get() {
    //     if (this.incomeBudgetAmount == "-") {
    //       return true;
    //     } else {
    //       return false;
    //     }
    //   },
    //   set() {},
    // },

    showBudget() {
      if (this.incomeBudgetAmount == "-") {
        return false;
      } else {
        return true;
      }
    },
    remainingBudget() {
      if (this.incomeBudgetAmount == "-") {
        return "-";
      } else {
        return (
          this.incomeBudgetAmount - this.bottomData[0].expenditureBudgetAmount
        );
      }
    },
  },
  watch: {
    incomeBudgetAmount: function () {
      // 숫자만 입력
      return (this.incomeBudgetAmount = this.incomeBudgetAmount.replace(
        /[^0-9]/g,
        ""
      ));
    },
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
        field: "balance",
        type: "numericColumn",
      },
    ];
  },
  mounted() {
    this.getBudgetList();
  },
  methods: {
    onGridReady(params) {
      console.log("params", params);
      this.gridApi = params.api;
      this.columnApi = params.columnApi;

      params.api.sizeColumnsToFit();
    },
    onFirstDataRendered() {
      // this.columnApi.autoSizeAllColumns();
    },
    onUpdateIncomeBudget() {
      this.readonly = false;
      if (this.incomeBudgetAmount == "-") {
        this.incomeBudgetAmount = "";
      }
      this.$refs.incomeBudgetAmount.focus();
    },
    // 예산 목록 조회
    getBudgetList() {
      const params = {
        categoryType: "EXP",
        incomeBudgetDate: this.$moment(this.period.from).format("YYYYMM"),
        expenditureBudgetDate: this.$moment(this.period.from).format("YYYYMM"),
        email: this.user.userInfo.email,
      };
      console.log("params", params);
      this.$store
        .dispatch("budgetStore/selectBudgetList", params)
        .then((res) => {
          // 한달 예산 readonly
          this.readonly = true;
          console.log("getBudgetList", res.data);
          // const selectBudgetList = _.cloneDeep(res.data.budgetListDtoList);
          // 미분류 마지막 순서로 변경
          // res.data.budgetListDtoList.shift();
          // res.data.budgetListDtoList.push(selectBudgetList[0]);

          // 한달 예산
          this.incomeBudgetAmount = res.data.incomeBudgetAmount;
          // 3개월 간 평균 지출
          this.threeMonthAverageExpenditure =
            res.data.threeMonthAverageExpenditure == null
              ? "0"
              : res.data.threeMonthAverageExpenditure;
          // 지난달 지출
          this.lastMonthExpenditure =
            res.data.lastMonthExpenditure == null
              ? "0"
              : res.data.lastMonthExpenditure;
          // 남은 돈 column setting
          _.forEach(res.data.budgetListDtoList, function (row, index) {
            res.data.budgetListDtoList[index].balance =
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
              balance: _.sumBy(res.data.budgetListDtoList, function (o) {
                return o.balance;
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
      const budgetDto = {
        incomeBudgetAmount: this.incomeBudgetAmount,
        budgetListDtoList: this.$refs.budgetGrid.getRowData(),
        expenditureBudgetDate: this.$moment(this.period.from).format("YYYYMM"),
        incomeBudgetDate: this.$moment(this.period.from).format("YYYYMM"),
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
#top_title {
  text-align: right;
}

#left_title {
  display: inline-block;
  width: 20%;
  text-align: left;
}
#right_title {
  display: inline-block;
  width: 80%;
  text-align: right;
}
</style>