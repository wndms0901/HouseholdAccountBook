<template>
  <div>
    <div class="budgetWrite_top">
      <table>
        <tr>
          <td style="width: 175px">
            <span style="font-size: 1.5em">{{ this.month }}월 수입 예산</span>
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
    <div class="grid_left_title">
      <span>카테고리별 예산</span>
    </div>
    <!-- <div>
      <div class="grid_top_title">
        <span>{{ this.totalBudget }}</span>
      </div>
      <div>
        <div class="grid_left_title">
          <span>카테고리별 예산</span>
        </div>
        <div class="grid_right_title">
          <span>전체 예산 {{ this.incomeBudget }}원</span>
        </div>
      </div>
    </div> -->
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
      incomeBudgetAmount: "0",
      incomeBudget: 0,
      threeMonthAverageExpenditure: 0,
      lastMonthExpenditure: 0,
      showSpan: true,
      showInput: false,
    };
  },
  computed: {
    month() {
      const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      return monthStartDate > 15
        ? this.$moment(this.period.to).format("MM")
        : this.$moment(this.period.from).format("MM");
    },
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

    // showBudget() {
    //   if (this.incomeBudgetAmount === "0") {
    //     return false;
    //   } else {
    //     return true;
    //   }
    // },
    totalBudget() {
      if (this.bottomData) {
        const value =
          this.incomeBudgetAmount - this.bottomData[0].expenditureBudgetAmount;
        console.log("typeof value", typeof value);
        if (value < 0) {
          return Math.abs(value) + "원 초과";
        } else {
          return value + "원 남음";
        }
      }

      // const value =
      //   this.incomeBudgetAmount - this.bottomData[0].expenditureBudgetAmount;
      // if (value < 0) {
      //   value *= -1;
      //   return value + "원 초과";
      // } else {
      //   return value + "원 남음";
      // }
      //return value;
      // return (
      //   this.incomeBudgetAmount - this.bottomData[0].expenditureBudgetAmount
      // );
    },
  },
  watch: {
    incomeBudgetAmount() {
      // 숫자만 입력
      this.incomeBudgetAmount = this.incomeBudgetAmount.replace(/[^0-9]/g, "");
      // 한자릿수 0만 입력
      if (
        parseInt(this.incomeBudgetAmount) === 0 ||
        this.incomeBudgetAmount === ""
      ) {
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
        budgetDate: budgetDate,
        thisMonthStartDate: this.$moment(this.period.from).format("YYYYMM"),
        thisMonthEndDate: this.$moment(this.period.to).format("YYYYMM"),
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
      this.$store
        .dispatch("budgetStore/selectBudgetList", budgetRequestDto)
        .then((res) => {
          console.log("결과>", res.data);
          // 한달 수입 예산
          this.incomeBudgetAmount = res.data.incomeBudgetAmount;
          this.incomeBudget = res.data.incomeBudgetAmount;
          // 3개월 간 평균 지출
          this.threeMonthAverageExpenditure =
            res.data.threeMonthAverageExpenditure;
          // // 지난달 지출
          this.lastMonthExpenditure = res.data.lastMonthExpenditure;

          const bottomData = [];
          const bottomRow = res.data.budgetListDtoList.pop();
          // 카테고리별 예산 grid
          this.gridApi.setRowData(res.data.budgetListDtoList);
          // 합계 grid
          bottomData.push(bottomRow);
          this.bottomData = bottomData;
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
  color: gray;
}
</style>