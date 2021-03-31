<template>
  <div class="table-responsive">
    <table class="table" style="width: 600px">
      <!-- table-borderless -->
      <tr>
        <td rowspan="2">
          <p class="m-0">한달 예산</p>
          <input type="text" v-model="incomeBudgetAmount" />
          <!-- <p>한달 예산</p> -->
        </td>
        <td>3개월 간 평균 지출</td>
        <td>1000000원</td>
      </tr>
      <tr>
        <td>지난달 지출</td>
        <td>1000000원</td>
      </tr>
    </table>
    <div style="text-align: right"><span>10000000원 남음</span></div>
    <div>
      <div style="float: left"><span>카테고리별 예산</span></div>
      <div style="text-align: right"><span>전체 예산 10000000원</span></div>
    </div>
    <grid
      ref="budgetGrid"
      style="height: 420px"
      class="ag-theme-alpine"
      :columnDefs="columnDefs"
      :rowData="rowData"
      rowSelection="multiple"
      @grid-ready="onGridReady"
    ></grid>
    <div class="right_btn mt-3"><button @click="onSave">저장</button></div>
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
      columnDefs: null,
      rowData: [],
      incomeBudgetAmount: "",
    };
  },
  computed: {},
  watch: {},
  created() {},
  beforeMount() {
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
          console.log("getBudgetList", res.data);
          // 한달 예산
          this.incomeBudgetAmount = res.data.incomeBudgetAmount;
          // 남은 돈 column setting
          _.forEach(res.data.budgetListDtoList, function (row, index) {
            res.data.budgetListDtoList[index].balance =
              row.expenditureBudgetAmount - row.expenditureAmount;
          });
          this.gridApi.setRowData(res.data.budgetListDtoList);
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    onSave() {},
  },
};
</script>