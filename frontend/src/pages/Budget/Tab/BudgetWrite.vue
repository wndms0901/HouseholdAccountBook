<template>
  <div class="table-responsive">
    <table class="table" style="width: 600px">
      <!-- table-borderless -->
      <tr>
        <td rowspan="2">
          <p class="m-0">한달 예산</p>
          <input type="text" />
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
    ></grid>
    <div class="right_btn mt-3"><button @click="onSave">저장</button></div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      columnDefs: null,
      rowData: [],
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
      { headerName: "내역", field: "incomeDescription" },
      {
        headerName: "예산",
        field: "expenditureBudgetAmount",
        type: "numericColumn",
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
  mounted() {},
  methods: {},
};
</script>