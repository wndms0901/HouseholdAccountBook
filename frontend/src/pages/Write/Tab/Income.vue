<template>
  <div>
    <!-- <button @click="getSelectedRows()">Get Selected Rows</button> -->
    <grid
      ref="incomeGrid"
      style="height: 580px"
      class="ag-theme-alpine"
      :gridOptions="gridOptions"
      :columnDefs="columnDefs"
      :defaultColDef="defaultColDef"
      :rowData="rowData"
      :singleClickEdit="true"
      :suppressRowClickSelection="true"
      :isRowSelectable="isRowSelectable"
      rowSelection="multiple"
      :components="components"
      :frameworkComponents="frameworkComponents"
      @grid-ready="onGridReady"
      @rowClicked="onRowClick"
    >
    </grid>
    <div class="pt-3">
      <div class="left_btn">
        <button @click="onRowDelete">선택삭제</button>
        <button @click="onRowCopy">선택복사</button>
      </div>
      <div class="right_btn"><button @click="onSave">저장</button></div>
    </div>
  </div>
</template>
<script>
const getDatePicker = () => {
  function Datepicker() {}
  Datepicker.prototype.init = function (params) {
    this.eInput = document.createElement("input");
    this.eInput.value = params.value;
    this.eInput.classList.add("ag-input");
    this.eInput.style.height = "100%";
    this.eInput.style.width = "100%";
    this.eInput.readOnly = true;
    $(this.eInput).datepicker({ dateFormat: "yy.mm.dd" });
  };
  Datepicker.prototype.getGui = function () {
    return this.eInput;
  };
  Datepicker.prototype.afterGuiAttached = function () {
    this.eInput.focus();
    this.eInput.select();
  };
  Datepicker.prototype.getValue = function () {
    return this.eInput.value;
  };
  Datepicker.prototype.destroy = function () {};
  Datepicker.prototype.isPopup = function () {
    return false;
  };
  return Datepicker;
};

window.extractValues = function extractValues(mappings) {
  //var value = Object.keys(mappings);
  return Object.keys(mappings).sort();
  //return ["English", "Spanish", "French", "Portuguese", "(other)"];
};

window.lookupValue = function lookupValue(mappings, key) {
  return mappings[key];
};
// window.lookupKey = function lookupKey(mappings, name) {
//   var keys = Object.keys(mappings);
//   for (var i = 0; i < keys.length; i++) {
//     var key = keys[i];
//     if (mappings[key] === name) {
//       return key;
//     }
//   }
// };

// function onCellValueChanged(params) {
//   const colId = params.column.getId();
//   if (colId === "largeCategory") {
//     let value = params.value == "" ? "미분류" : "선택";
//     console.log("node", params.node);
//     params.node.setDataValue("smallCategory", { key: "", value: value });
//   }
// }

export default {
  props: {
    user: Object,
    period: Object,
  },
  data() {
    return {
      gridOptions: null,
      columnDefs: null,
      defaultColDef: null,
      rowData: [],
      components: null,
      frameworkComponents: null,
      accountCategoryList: [],
      accountCategory: {},
      largeCategory: {},
      categoryType: "INC",
      accountCategoryType: "DPST",
      deletedRows: [],
    };
  },
  computed: {
    // searchPeriod: function () {
    //   console.log("this.period11", this.period);
    //   const period = {
    //     from: this.$moment(this.period.from).format("YYYYMMDD"),
    //     to: this.$moment(this.period.to).format("YYYYMMDD"),
    //   };
    //   // const period = _.cloneDeep(this.period);
    //   // let from = period.from;
    //   // let to = period.to;
    //   // period.from =
    //   //   String(from.getFullYear()) +
    //   //   ("0" + (from.getMonth() + 1)).slice(-2) +
    //   //   ("0" + from.getDate()).slice(-2);
    //   // period.to =
    //   //   String(to.getFullYear()) +
    //   //   ("0" + (to.getMonth() + 1)).slice(-2) +
    //   //   ("0" + to.getDate()).slice(-2);
    //   return period;
    // },
  },
  watch: {
    period: {
      deep: true,
      handler(newData) {
        this.getIncomeList();
      },
    },
  },
  beforeCreate() {},
  created() {
    // 입금통장 카테고리 목록 조회
    this.getAccountCategoryList();
    // 대분류 카테고리 목록 조회
    this.getLargeCategoryList();
  },
  beforeMount() {
    this.gridOptions = {
      //onCellValueChanged: onCellValueChanged,
      //enableColResize: true,
      // enableSorting: true,
      // enableFilter: true,
      // animateRows: false,
      // onGridReady: function (event) {
      //   event.api.sizeColumnsToFit();
      //   console.log("event", event);
      //   this.gridApi = event.api;
      //   this.columnApi = event.columnApi;
      // },
    };
    // 그리드 header명과, 매핑되는 data attribute, column type과 width, column id 등을 지정 가능합니다.
    // computed에 선언하지 않고 data에서도 선언 가능합니다.
    this.columnDefs = [
      { field: "incomeId", hide: true },
      {
        headerName: "날짜",
        field: "incomeDate",
        cellEditor: "datePicker",
        //cellRenderer: "datePicker",
        checkboxSelection: true,
        headerCheckboxSelection: true,
        //headerCheckboxSelectionFilteredOnly: true,
      },
      { headerName: "내역", field: "incomeDescription" },
      {
        headerName: "금액",
        field: "incomeAmount",
        type: "numericColumn",
      },
      {
        headerName: "입금통장",
        field: "accountCategoryId",
        cellEditor: "agSelectCellEditor",
        //cellEditorParams: this.getAccountCategoryList.bind(this),
        cellEditorParams: (params) => {
          console.log("입금통장>", Object.keys(this.accountCategory).sort());
          return {
            values: Object.keys(this.accountCategory).sort(function (a, b) {
              return a - b;
            }),
          };
          // values: extractValues(this.accountCategory),
          // values: this.accountCategory,
          //       cellRenderer: cellRenderer2(this.accountCategoryList),
        },
        // convert code to value
        valueFormatter: (params) => {
          console.log("this.accountCategory,", params.value);
          return this.accountCategory[params.value];
          //  return lookupValue(this.accountCategory, params.value);
        },
        // // convert value to code
        //valueParser: (params) => {
        //return lookupKey(this.accountCategoryList, params.newValue);
        // },
      },
      {
        headerName: "분류",
        field: "largeCategoryId",
        cellEditor: "agSelectCellEditor",
        cellEditorParams: (params) => {
          return {
            values: Object.keys(this.largeCategory).sort(function (a, b) {
              return a - b;
            }),
          };
          // values: extractValues(this.accountCategory),
          // values: this.accountCategory,
        },
        // convert code to value
        valueFormatter: (params) => {
          console.log("this.largeCategory,", params.value);
          return this.largeCategory[params.value];
          //  return lookupValue(this.accountCategory, params.value);
        },
        // // convert value to code
        // valueParser: (params) => {
        //return lookupKey(this.accountCategoryList, params.newValue);
        // },
      },
      { headerName: "메모", field: "memo" },
    ];
    (this.defaultColDef = {
      editable: (params) => {
        return params.node.selectable;
        //editable: true,
      },
    }),
      (this.components = { datePicker: getDatePicker() });

    this.isRowSelectable = (rowNode) => {
      console.log("rowNode", rowNode);
      return rowNode.data.incomeDate == "" ? false : true;
    };

    // this.frameworkComponents = {
    //   Datepicker: Datepicker,
    // };
    // this.$nextTick(function () {

    // });
  },
  mounted() {
    this.getIncomeList();
  },
  methods: {
    onGridReady(params) {
      console.log("params", params);
      this.gridApi = params.api;
      this.columnApi = params.columnApi;

      params.api.sizeColumnsToFit();
    },
    // 입금통장 카테고리 목록 조회
    getAccountCategoryList() {
      this.$store
        .dispatch(
          "commonStore/selectAccountCtgryList",
          this.accountCategoryType
        )
        .then((res) => {
          // {id:name} 형식으로 만들기
          let tmp = {};
          _.forEach(res.data, function (obj) {
            let accountCategoryId = String(obj.accountCategoryId);
            tmp[accountCategoryId] = obj.accountCategoryName;
          });
          this.accountCategory = tmp;
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // 대분류 카테고리 목록 조회
    getLargeCategoryList() {
      this.$store
        .dispatch("commonStore/selectLargeCtgryList", this.categoryType)
        .then((res) => {
          console.log("getLargeCategoryList", res.data);
          // {id:name} 형식으로 만들기
          let tmp = {};
          _.forEach(res.data, function (obj) {
            let largeCategoryId = String(obj.largeCategoryId);
            tmp[largeCategoryId] = obj.largeCategoryName;
          });
          this.largeCategory = tmp;
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // 수입 목록 조회
    getIncomeList() {
      // reset
      this.deletedRows = [];

      const params = {
        startDate: this.$moment(this.period.from).format("YYYYMMDD"),
        endDate: this.$moment(this.period.to).format("YYYYMMDD"),
        email: this.user.userInfo.email,
      };
      this.$store
        .dispatch("writeStore/selectIncomeList", params)
        .then((res) => {
          console.log("getIncomeList", res.data);
          // 날짜 형식 setting
          _.forEach(res.data, function (row, index) {
            const year = row.incomeDate.substr(0, 4);
            const month = row.incomeDate.substr(4, 2);
            const date = row.incomeDate.substr(6, 2);
            res.data[index].incomeDate = year.concat(".", month, ".", date);
          });
          this.gridApi.setRowData(res.data);
          this.defaultRow();
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    defaultRow() {
      const rowData = this.$refs.incomeGrid.getRowData();
      const today = new Date();
      const incomeDate =
        String(today.getFullYear()) +
        "." +
        ("0" + (today.getMonth() + 1)).slice(-2) +
        "." +
        ("0" + today.getDate()).slice(-2);
      const rows = [
        {
          incomeId: "",
          incomeDate: incomeDate,
          incomeDescription: "",
          incomeAmount: "0",
          accountCategoryId: 6,
          largeCategoryId: 20,
          memo: "",
        },
        {
          incomeId: "",
          incomeDate: "",
          incomeDescription: "",
          incomeAmount: "",
          accountCategoryId: " ",
          largeCategoryId: " ",
          memo: "",
        },
      ];
      this.gridApi.applyTransaction({
        add: rows,
        addIndex: rowData.length,
      });
      this.gridApi.startEditingCell({
        rowIndex: rowData.length,
        colKey: "incomeDescription",
      });
    },
    onRowClick(event) {
      if (event.data.incomeDate == "") {
        const columnData = this.gridApi.getFocusedCell();
        const rowData = this.$refs.incomeGrid.getRowData();
        const today = new Date();
        const incomeDate =
          String(today.getFullYear()) +
          "." +
          ("0" + (today.getMonth() + 1)).slice(-2) +
          "." +
          ("0" + today.getDate()).slice(-2);
        const row = [
          {
            incomeDate: incomeDate,
            incomeDescription: "",
            incomeAmount: "0",
            accountCategoryId: "",
            largeCategoryId: "",
            memo: "",
          },
        ];
        this.gridApi.applyTransaction({
          add: row,
          addIndex: rowData.length - 1,
        });
        this.gridApi.startEditingCell({
          rowIndex: columnData.rowIndex,
          colKey: columnData.column.colId,
        });
      }
    },
    // 수입 목록 저장
    onSave() {
      this.gridApi.clearFocusedCell();
      const rowData = _.cloneDeep(this.$refs.incomeGrid.getRowData());
      rowData.pop();
      const incomeSaveDto = {
        insertIncomeDtoList: [],
        updateIncomeDtoList: [],
        deleteIncomeDtoList: [],
      };

      _.forEach(rowData, function (row, index) {
        const incomeDate = row.incomeDate.replace(/\./gi, "");
        rowData[index] = {
          incomeId: row.incomeId,
          incomeDate: incomeDate,
          incomeDescription: row.incomeDescription,
          incomeAmount: row.incomeAmount,
          accountCategoryId: row.accountCategoryId,
          largeCategoryId: row.largeCategoryId,
          memo: row.memo,
          userDto: this.user.userInfo,
        };
      });
      incomeSaveDto.insertIncomeDtoList = _.filter(rowData, function (row) {
        return !row.incomeId && (row.incomeDescription || row.memo);
      });
      incomeSaveDto.updateIncomeDtoList = _.filter(rowData, "incomeId");
      incomeSaveDto.deleteIncomeDtoList = this.deletedRows;
      console.log(
        "incomeSaveDto.insertIncomeDtoList",
        incomeSaveDto.insertIncomeDtoList
      );
      this.$store
        .dispatch("writeStore/saveIncomeList", incomeSaveDto)
        .then((res) => {
          this.getIncomeList();
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // Row 선택 삭제
    onRowDelete() {
      const selectedRows = this.gridApi.getSelectedRows();
      this.deletedRows = _.filter(selectedRows, "incomeId");
      this.gridApi.applyTransaction({ remove: selectedRows });
    },
    // Row 선택 복사
    onRowCopy() {
      console.log(this.searchPeriod);
      const selectedRows = _.cloneDeep(this.gridApi.getSelectedRows());
      _.forEach(selectedRows, function (row, index) {
        selectedRows[index].incomeId = "";
      });
      this.gridApi.applyTransaction({
        add: selectedRows,
        addIndex: this.gridApi.getDisplayedRowCount() - 1,
      });
    },
  },
  // getSelectedRows() {
  //   const selectedNodes = this.gridApi.getSelectedNodes();
  //   const selectedData = selectedNodes.map((node) => node.data);
  //   const selectedDataStringPresentation = selectedData
  //     .map((node) => `${node.make} ${node.model}`)
  //     .join(", ");
  //   alert(`Selected nodes: ${selectedDataStringPresentation}`);
  // },
};
</script>
      
    