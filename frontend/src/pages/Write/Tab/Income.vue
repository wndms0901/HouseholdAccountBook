<template>
  <div>
    <div class="pb-2 excel_btn_box">
      <button class="basicBtn">엑셀 업로드</button>
      <button class="basicBtn">엑셀 다운로드</button>
    </div>
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
      @selection-changed="onSelectionChanged"
    >
    </grid>
    <div class="pt-3">
      <div class="left_btn">
        <button
          class="basicBtn"
          :disabled="disabledSelectBtn"
          @click="onRowDelete"
        >
          선택삭제
        </button>
        <button
          class="basicBtn"
          :disabled="disabledSelectBtn"
          @click="onRowCopy"
        >
          선택복사
        </button>
      </div>
      <div class="right_btn">
        <span class="mr-2">수입합계</span>
        <span class="mr-2 income_color">{{ this.totalIncome }}</span>
        <button class="saveBtn" @click="onSave">저장</button>
      </div>
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
import InputCellEditor from "src/components/CellEditor/InputCellEditor";
export default {
  components: { InputCellEditor },
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
      accountCategory: [],
      largeCategory: [],
      categoryType: "INC",
      accountCategoryType: "DPST",
      deletedRows: [],
      disabledSelectBtn: true,
      totalIncomeNumber: 0,
    };
  },
  computed: {
    // 기본 row
    defaultRow() {
      const today = new Date();
      const incomeDate =
        String(today.getFullYear()) +
        "." +
        ("0" + (today.getMonth() + 1)).slice(-2) +
        "." +
        ("0" + today.getDate()).slice(-2);
      const row = [
        {
          incomeId: "",
          incomeDate: incomeDate,
          incomeDescription: "",
          incomeAmount: "0",
          accountCategory: {
            accountCategoryId: 6,
            accountCategoryName: "선택없음",
          },
          largeCategory: {
            largeCategoryId: 20,
            largeCategoryName: "미분류",
          },
          memo: "",
        },
      ];
      return row;
    },
    totalIncome() {
      return String(this.totalIncomeNumber).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
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
    // 카테고리 목록 조회
    this.getCategoryList();
    // // 입금통장 카테고리 목록 조회
    // this.getAccountCategoryList();
    // // 대분류 카테고리 목록 조회
    // this.getLargeCategoryList();
  },
  beforeMount() {
    this.gridOptions = {
      onCellValueChanged: this.onCellValueChanged,
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
    this.defaultColDef = {
      editable: (params) => {
        return params.node.selectable;
        //editable: true,
      },
      cellStyle: (params) => {
        return {
          cursor: "pointer",
        };
      },
    };
    // 그리드 header명과, 매핑되는 data attribute, column type과 width, column id 등을 지정 가능합니다.
    // computed에 선언하지 않고 data에서도 선언 가능합니다.
    this.columnDefs = [
      { field: "incomeId", hide: true },
      {
        headerName: "날짜",
        field: "incomeDate",
        cellEditor: "datePicker",
        width: 95,
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
        cellEditor: "InputCellEditor",
        valueFormatter: (params) => {
          return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
      },
      {
        headerName: "입금통장",
        field: "accountCategory",
        cellEditor: "agSelectCellEditor",
        //cellEditorParams: this.getAccountCategoryList.bind(this),
        cellEditorParams: (params) => {
          return {
            values: this.accountCategory,
          };
        },
        // convert code to value
        valueFormatter: (params) => {
          return params.value.accountCategoryName;
        },
      },
      {
        headerName: "분류",
        field: "largeCategory",
        cellEditor: "agSelectCellEditor",
        cellEditorParams: (params) => {
          return {
            values: this.largeCategory,
          };
        },
        // convert code to value
        valueFormatter: (params) => {
          return params.value.largeCategoryName;
        },
      },
      { headerName: "메모", field: "memo" },
    ];

    this.components = { datePicker: getDatePicker() };

    this.frameworkComponents = {
      InputCellEditor: InputCellEditor,
    };

    this.isRowSelectable = (rowNode) => {
      console.log("rowNode", rowNode);
      return rowNode.data.incomeDate === "" ? false : true;
    };
  },
  mounted() {
    // this.getIncomeList();
  },
  methods: {
    onGridReady(params) {
      this.gridApi = params.api;
      this.columnApi = params.columnApi;
      // 그리드 사이즈 자동 조정
      params.api.sizeColumnsToFit();
    },
    // grid cell 값 변경
    onCellValueChanged(params) {
      const colId = params.column.getId();
      if (colId === "incomeAmount") {
        this.getTotal();
      }
    },
    // 카테고리 목록 조회
    getCategoryList() {
      this.$store
        .dispatch("writeStore/selectCategoryList", this.categoryType)
        .then((res) => {
          console.log("getCategoryList", res.data);
          // 입금통장 카테고리 목록
          this.accountCategory = res.data.accountCategoryDtoList;
          // 대분류 카테고리 목록
          this.largeCategory = res.data.largeCategoryDtoList;
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // // 입금통장 카테고리 목록 조회
    // getAccountCategoryList() {
    //   this.$store
    //     .dispatch(
    //       "commonStore/selectAccountCtgryList",
    //       this.accountCategoryType
    //     )
    //     .then((res) => {
    //       // {id:name} 형식으로 만들기
    //       let tmp = {};
    //       _.forEach(res.data, function (obj) {
    //         let accountCategoryId = String(obj.accountCategoryId);
    //         tmp[accountCategoryId] = obj.accountCategoryName;
    //       });
    //       this.accountCategory = tmp;
    //     })
    //     .catch((Error) => {
    //       console.log(Error);
    //     });
    // },
    // // 대분류 카테고리 목록 조회
    // getLargeCategoryList() {
    //   this.$store
    //     .dispatch("commonStore/selectLargeCtgryList", this.categoryType)
    //     .then((res) => {
    //       console.log("getLargeCategoryList", res.data);
    //       // {id:name} 형식으로 만들기
    //       let tmp = {};
    //       _.forEach(res.data, function (obj) {
    //         let largeCategoryId = String(obj.largeCategoryId);
    //         tmp[largeCategoryId] = obj.largeCategoryName;
    //       });
    //       this.largeCategory = tmp;
    //     })
    //     .catch((Error) => {
    //       console.log(Error);
    //     });
    // },
    // 수입 목록 조회
    getIncomeList() {
      this.gridApi.clearFocusedCell();
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
            // accountCategory setting
            res.data[index].accountCategory = {
              accountCategoryId: row.accountCategoryId,
              accountCategoryName: row.accountCategoryName,
            };
            // largeCategory setting
            res.data[index].largeCategory = {
              largeCategoryId: row.largeCategoryId,
              largeCategoryName: row.largeCategoryName,
            };
          });
          this.gridApi.setRowData(res.data);
          this.setDefaultRow();
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    setDefaultRow() {
      const rowData = this.$refs.incomeGrid.getRowData();
      const defaultRow = _.cloneDeep(this.defaultRow);
      const emptyRow = {
        incomeId: "",
        incomeDate: "",
        incomeDescription: "",
        incomeAmount: "",
        accountCategory: " ",
        largeCategory: " ",
        memo: "",
      };
      defaultRow.push(emptyRow);

      this.gridApi.applyTransaction({
        add: defaultRow,
        addIndex: rowData.length,
      });
      this.gridApi.startEditingCell({
        rowIndex: rowData.length,
        colKey: "incomeDescription",
      });
      this.gridApi.sizeColumnsToFit();
      this.getTotal();
    },
    // 수입 합계 계산
    getTotal() {
      const rowData = _.cloneDeep(this.$refs.incomeGrid.getRowData());
      rowData.pop();
      // 수입 합계
      let totalIncome = _.reduce(
        rowData,
        function (sum, obj) {
          return sum + parseInt(String(obj.incomeAmount).replace(/,/g, ""));
        },
        0
      );
      this.totalIncomeNumber = totalIncome;
    },
    // Row 추가
    onRowClick(event) {
      if (event.data.incomeDate === "") {
        const columnData = this.gridApi.getFocusedCell();
        const rowData = this.$refs.incomeGrid.getRowData();
        const defaultRow = _.cloneDeep(this.defaultRow);
        // const today = new Date();
        // const incomeDate =
        //   String(today.getFullYear()) +
        //   "." +
        //   ("0" + (today.getMonth() + 1)).slice(-2) +
        //   "." +
        //   ("0" + today.getDate()).slice(-2);
        // const row = [
        //   {
        //     incomeDate: incomeDate,
        //     incomeDescription: "",
        //     incomeAmount: "0",
        //     accountCategoryId: "",
        //     largeCategoryId: "",
        //     memo: "",
        //   },
        // ];
        this.gridApi.applyTransaction({
          add: defaultRow,
          addIndex: rowData.length - 1,
        });
        this.gridApi.startEditingCell({
          rowIndex: columnData.rowIndex,
          colKey: columnData.column.colId,
        });
      }
    },

    // row 선택 또는 선택취소 시 호출
    onSelectionChanged(event) {
      var rowCount = event.api.getSelectedNodes().length;
      this.disabledSelectBtn = rowCount === 0;
    },
    // 수입 목록 저장
    onSave() {
      this.gridApi.clearFocusedCell();
      const rowData = _.cloneDeep(this.$refs.incomeGrid.getRowData());
      rowData.pop();
      const userDto = this.user.userInfo;
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
          incomeDescription: row.incomeDescription || "",
          incomeAmount: parseInt(String(row.incomeAmount).replace(/,/g, "")),
          accountCategoryId: row.accountCategory.accountCategoryId,
          largeCategoryId: row.largeCategory.largeCategoryId,
          memo: row.memo || "",
          userDto: userDto,
        };
      });
      // 등록 rows
      incomeSaveDto.insertIncomeDtoList = _.filter(rowData, function (row) {
        return (
          !row.incomeId &&
          (row.incomeDescription ||
            row.incomeAmount > 0 ||
            row.accountCategoryId !== 6 ||
            row.largeCategoryId !== 20 ||
            row.memo)
        );
      });
      // 수정 rows
      incomeSaveDto.updateIncomeDtoList = _.filter(rowData, "incomeId");
      // 삭제 rows
      incomeSaveDto.deleteIncomeDtoList = this.deletedRows;
      console.log("incomeSaveDto", incomeSaveDto);
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
      this.getTotal();
    },
    // Row 선택 복사
    onRowCopy() {
      const selectedRows = _.cloneDeep(this.gridApi.getSelectedRows());
      _.forEach(selectedRows, function (row, index) {
        selectedRows[index].incomeId = "";
      });
      this.gridApi.applyTransaction({
        add: selectedRows,
        addIndex: this.gridApi.getDisplayedRowCount() - 2,
      });
      this.gridApi.deselectAll();
      this.gridApi.clearFocusedCell();
      this.getTotal();
    },
  },
};
</script>
      
    