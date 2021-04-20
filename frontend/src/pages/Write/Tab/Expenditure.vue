<template>
  <div>
    <div class="pb-2 excel_btn_box">
      <button class="selectBtn">엑셀 업로드</button>
      <button class="selectBtn">엑셀 다운로드</button>
    </div>
    <grid
      ref="expenditureGrid"
      style="height: 550px"
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
        <button
          class="selectBtn"
          :disabled="disabledSelectBtn"
          @click="onRowDelete"
        >
          선택삭제
        </button>
        <button
          class="selectBtn"
          :disabled="disabledSelectBtn"
          @click="onRowCopy"
        >
          선택복사
        </button>
      </div>
      <div class="total_box">
        <table>
          <tr>
            <td>현금합계</td>
            <td>카드합계</td>
          </tr>
          <tr>
            <td>{{ this.totalCashString }}</td>
            <td>{{ this.totalCardString }}</td>
          </tr>
        </table>
      </div>
      <div class="right_btn">
        <span class="mr-2">지출합계</span>
        <span class="mr-2 expenditure_color">{{ this.totalExpenditure }}</span>
        <button class="saveBtn" @click="onSave">저장</button>
        <button class="calculateBtn">정산</button>
      </div>
    </div>
  </div>
</template>
<script>
import InputCellEditor from "src/components/CellEditor/InputCellEditor";
const getDatePicker = () => {
  function Datepicker() {}
  Datepicker.prototype.init = function (params) {
    this.eInput = document.createElement("input");
    this.eInput.value = params.value;
    this.eInput.classList.add("ag-input");
    this.eInput.style.height = "100%";
    this.eInput.style.width = "100%";
    this.eInput.readOnly = true;
    $(this.eInput).datepicker({
      dateFormat: "yy.mm.dd",
      prevText: "이전 달",
      nextText: "다음 달",
      monthNames: [
        "1월",
        "2월",
        "3월",
        "4월",
        "5월",
        "6월",
        "7월",
        "8월",
        "9월",
        "10월",
        "11월",
        "12월",
      ],
      monthNamesShort: [
        "1월",
        "2월",
        "3월",
        "4월",
        "5월",
        "6월",
        "7월",
        "8월",
        "9월",
        "10월",
        "11월",
        "12월",
      ],
      dayNames: ["일", "월", "화", "수", "목", "금", "토"],
      dayNamesShort: ["일", "월", "화", "수", "목", "금", "토"],
      dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
      showMonthAfterYear: true,
      yearSuffix: "년",
    });
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
//   console.log("onCellValueChanged", params);
//   if (colId === "largeCategoryId") {
//     params.api.startEditingCell({
//       rowIndex: params.rowIndex,
//       colKey: "smallCategory",
//     });
//   }
// }

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
      accountCategory: {},
      largeCategory: {},
      smallCategory: {},
      categoryType: "EXP",
      accountCategoryType: "WDRL",
      deletedRows: [],
      totalCashNumber: 0,
      totalCardNumber: 0,
      totalCashString: 0,
      totalCardString: 0,
      disabledSelectBtn: false,
    };
  },
  computed: {
    // 지출합계
    totalExpenditure() {
      let value = this.totalCashNumber + this.totalCardNumber;
      return String(value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
  },
  watch: {
    period: {
      deep: true,
      handler(newData) {
        this.getExpenditureList();
      },
    },
  },
  beforeCreate() {},
  created() {
    // 출금통장 카테고리 목록 조회
    //this.getAccountCategoryList();
    // 카테고리 목록 조회
    this.getCategoryList();
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
    // 그리드 header명과, 매핑되는 data attribute, column type과 width, column id 등을 지정 가능합니다.
    // computed에 선언하지 않고 data에서도 선언 가능합니다.
    this.columnDefs = [
      { field: "expenditureId", hide: true },
      {
        headerName: "날짜",
        field: "expenditureDate",
        cellEditor: "datePicker",
        checkboxSelection: true,
        headerCheckboxSelection: true,
        //headerCheckboxSelectionFilteredOnly: true,
      },
      { headerName: "사용내역", field: "expenditureDescription" },
      {
        headerName: "현금",
        field: "cash",
        type: "numericColumn",
        cellEditor: "InputCellEditor",
        valueFormatter: (params) => {
          return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
      },
      {
        headerName: "카드",
        field: "card",
        type: "numericColumn",
        cellEditor: "InputCellEditor",
        valueFormatter: (params) => {
          return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
      },
      {
        headerName: "출금통장",
        field: "accountCategoryId",
        cellEditor: "agSelectCellEditor",
        cellEditorParams: (params) => {
          return {
            values: Object.keys(this.accountCategory).sort(function (a, b) {
              return a - b;
            }),
          };
        },
        // convert code to value
        valueFormatter: (params) => {
          console.log("출금통장>", this.accountCategory);
          console.log("출금통장>>", params.value);
          return this.accountCategory[params.value];
        },
        // // convert value to code
        //valueParser: (params) => {
        //return lookupKey(this.accountCategoryList, params.newValue);
        // },
      },
      {
        headerName: "대분류",
        field: "largeCategoryId",
        cellEditor: "agSelectCellEditor",
        cellEditorParams: (params) => {
          return {
            values: Object.keys(this.largeCategory).sort(function (a, b) {
              return a - b;
            }),
          };
        },
        // convert code to value
        valueFormatter: (params) => {
          console.log("대분류>", this.largeCategory);
          console.log("대분류>>", params.value);
          return this.largeCategory[params.value];
        },
        // // convert value to code
        // valueParser: (params) => {
        //return lookupKey(this.largeCategory, params.newValue);
        // },
      },
      {
        headerName: "소분류",
        field: "smallCategory",
        cellEditor: "agSelectCellEditor",
        cellEditorParams: (params) => {
          console.log(
            "cellEditorParams",
            this.smallCategory[params.data.largeCategoryId]
          );
          return {
            values: this.smallCategory[params.data.largeCategoryId],
          };
        },
        // convert code to value
        valueFormatter: (params) => {
          return params.value.smallCategoryName;
        },
      },
      { headerName: "메모", field: "memo" },
    ];
    (this.defaultColDef = {
      editable: (params) => {
        return params.node.selectable;
        //editable: true,
      },
      cellStyle: (params) => {
        return {
          cursor: "pointer",
        };
      },
    }),
      (this.components = { datePicker: getDatePicker() });
    this.frameworkComponents = {
      InputCellEditor: InputCellEditor,
    };
    this.isRowSelectable = (rowNode) => {
      console.log("rowNode", rowNode);
      return rowNode.data.expenditureDate === "" ? false : true;
    };
  },
  mounted() {
    this.getExpenditureList();
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
      console.log("onCellValueChanged", params);
      if (colId === "largeCategoryId") {
        params.api.startEditingCell({
          rowIndex: params.rowIndex,
          colKey: "smallCategory",
        });
      } else if (colId === "cash" || colId === "card") {
        this.getTotal();
      }
    },
    // 출금통장 카테고리 목록 조회
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
    // 카테고리 목록 조회
    getCategoryList() {
      this.$store
        .dispatch("writeStore/selectCategoryList", this.categoryType)
        .then((res) => {
          console.log("getCategoryList", res.data);
          // 출금통장 카테고리 목록
          // {id:name} 형식으로 만들기
          let tmp = {};
          _.forEach(res.data.accountCategoryDtoList, function (obj) {
            let accountCategoryId = String(obj.accountCategoryId);
            tmp[accountCategoryId] = obj.accountCategoryName;
          });
          this.accountCategory = tmp;

          // {largeCategoryId:largeCategoryName} 형식으로 만들기
          let largeCategoryObj = {};
          let smallCategoryObj = {};
          _.forEach(res.data.largeCategoryDtoList, function (obj) {
            let largeCategoryId = String(obj.largeCategoryId);
            largeCategoryObj[largeCategoryId] = obj.largeCategoryName;
            const filterList = _.filter(res.data.smallCategoryDtoList, {
              largeCategoryId: obj.largeCategoryId,
            });
            // {largeCategoryId:[{smallCategoryId:"",smallCategoryName:""}]} 형식으로 만들기
            let lst = [];
            _.forEach(filterList, function (obj2) {
              const tmp = {
                smallCategoryId: obj2.smallCategoryId,
                smallCategoryName: obj2.smallCategoryName,
              };
              lst.push(tmp);
              // let smallCategoryId = String(obj2.smallCategoryId);
              // tmp[smallCategoryId] = obj2.smallCategoryName;
            });
            smallCategoryObj[largeCategoryId] = lst;
          });
          this.largeCategory = largeCategoryObj;
          this.smallCategory = smallCategoryObj;
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // getCategoryList() {
    //   this.$store
    //     .dispatch("commonStore/selectCategoryList", this.categoryType)
    //     .then((res) => {
    //       console.log("getCategoryList", res.data);
    //       // {largeCategoryId:largeCategoryName} 형식으로 만들기
    //       let largeCategoryObj = {};
    //       let smallCategoryObj = {};
    //       _.forEach(res.data.largeCategoryDtoList, function (obj) {
    //         let largeCategoryId = String(obj.largeCategoryId);
    //         largeCategoryObj[largeCategoryId] = obj.largeCategoryName;
    //         const filterList = _.filter(res.data.smallCategoryDtoList, {
    //           largeCategoryId: obj.largeCategoryId,
    //         });
    //         // {largeCategoryId:[{smallCategoryId:"",smallCategoryName:""}]} 형식으로 만들기
    //         let lst = [];
    //         _.forEach(filterList, function (obj2) {
    //           const tmp = {
    //             smallCategoryId: obj2.smallCategoryId,
    //             smallCategoryName: obj2.smallCategoryName,
    //           };
    //           lst.push(tmp);
    //           // let smallCategoryId = String(obj2.smallCategoryId);
    //           // tmp[smallCategoryId] = obj2.smallCategoryName;
    //         });
    //         smallCategoryObj[largeCategoryId] = lst;
    //       });
    //       this.largeCategory = largeCategoryObj;
    //       this.smallCategory = smallCategoryObj;
    //     })
    //     .catch((Error) => {
    //       console.log(Error);
    //     });
    // },
    // 지출 목록 조회
    getExpenditureList() {
      // reset
      this.deletedRows = [];

      const params = {
        startDate: this.$moment(this.period.from).format("YYYYMMDD"),
        endDate: this.$moment(this.period.to).format("YYYYMMDD"),
        email: this.user.userInfo.email,
      };
      console.log("params>", params);
      this.$store
        .dispatch("writeStore/selectExpenditureList", params)
        .then((res) => {
          console.log("getExpenditureList", res.data);
          // 날짜 형식 setting
          _.forEach(res.data, function (row, index) {
            const year = row.expenditureDate.substr(0, 4);
            const month = row.expenditureDate.substr(4, 2);
            const date = row.expenditureDate.substr(6, 2);
            res.data[index].expenditureDate = year.concat(
              ".",
              month,
              ".",
              date
            );
            // accountCategoryId
            res.data[index].accountCategoryId =
              row.accountCategoryId === null ? "" : row.accountCategoryId;
            // largeCategoryId
            res.data[index].largeCategoryId =
              row.largeCategoryId === null ? "" : row.largeCategoryId;
            // smallCategory setting
            res.data[index].smallCategory = {
              smallCategoryId: row.smallCategoryId,
              smallCategoryName: row.smallCategoryName,
            };
          });
          this.gridApi.setRowData(res.data);
          this.defaultRow();
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    defaultRow() {
      const rowData = this.$refs.expenditureGrid.getRowData();
      const today = new Date();
      const expenditureDate =
        String(today.getFullYear()) +
        "." +
        ("0" + (today.getMonth() + 1)).slice(-2) +
        "." +
        ("0" + today.getDate()).slice(-2);
      const rows = [
        {
          expenditureId: "",
          expenditureDate: expenditureDate,
          expenditureDescription: "",
          cash: "0",
          card: "0",
          accountCategoryId: 1,
          largeCategoryId: 1,
          smallCategory: { smallCategoryId: 1, smallCategoryName: "미분류" },
          memo: "",
        },
        {
          expenditureId: "",
          expenditureDate: "",
          expenditureDescription: "",
          cash: "",
          card: "",
          accountCategoryId: " ",
          largeCategoryId: " ",
          smallCategory: " ",
          memo: "",
        },
      ];
      this.gridApi.applyTransaction({
        add: rows,
        addIndex: rowData.length,
      });
      this.gridApi.startEditingCell({
        rowIndex: rowData.length,
        colKey: "expenditureDescription",
      });
      this.getTotal();
    },
    getTotal() {
      const rowData = _.cloneDeep(this.$refs.expenditureGrid.getRowData());
      rowData.pop();
      // 현금합계
      let totalCash = _.reduce(
        rowData,
        function (sum, obj) {
          return sum + parseInt(String(obj.cash).replace(/,/g, ""));
        },
        0
      );
      this.totalCashNumber = totalCash;
      this.totalCashString = String(totalCash).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
      // 카드합계
      let totalCard = _.reduce(
        rowData,
        function (sum, obj) {
          return sum + parseInt(String(obj.card).replace(/,/g, ""));
        },
        0
      );
      this.totalCardNumber = totalCard;
      this.totalCardString = String(totalCard).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    // Row 추가
    onRowClick(event) {
      if (event.data.expenditureDate === "") {
        const columnData = this.gridApi.getFocusedCell();
        const rowData = this.$refs.expenditureGrid.getRowData();
        const today = new Date();
        const expenditureDate =
          String(today.getFullYear()) +
          "." +
          ("0" + (today.getMonth() + 1)).slice(-2) +
          "." +
          ("0" + today.getDate()).slice(-2);
        const row = [
          {
            expenditureDate: expenditureDate,
            expenditureDescription: "",
            cash: "0",
            card: "0",
            accountCategoryId: "",
            largeCategoryId: 1,
            smallCategory: { smallCategoryId: "", smallCategoryName: "미분류" },
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
    // 지출 목록 저장
    onSave() {
      this.gridApi.clearFocusedCell();
      const rowData = _.cloneDeep(this.$refs.expenditureGrid.getRowData());
      rowData.pop();
      const userDto = this.user.userInfo;
      const expenditureSaveDto = {
        insertExpenditureDtoList: [],
        updateExpenditureDtoList: [],
        deleteExpenditureDtoList: [],
      };

      _.forEach(rowData, function (row, index) {
        const expenditureDate = row.expenditureDate.replace(/\./gi, "");
        rowData[index] = {
          expenditureId: row.expenditureId,
          expenditureDate: expenditureDate,
          expenditureDescription: row.expenditureDescription,
          cash: row.cash,
          card: row.card,
          accountCategoryId: row.accountCategoryId,
          largeCategoryId: row.largeCategoryId,
          smallCategoryId: row.smallCategory.smallCategoryId,
          memo: row.memo,
          userDto: userDto,
        };
      });
      expenditureSaveDto.insertExpenditureDtoList = _.filter(
        rowData,
        function (row) {
          return !row.expenditureId && (row.expenditureDescription || row.memo);
        }
      );
      expenditureSaveDto.updateExpenditureDtoList = _.filter(
        rowData,
        "expenditureId"
      );
      expenditureSaveDto.deleteExpenditureDtoList = this.deletedRows;
      console.log("expenditureSaveDto", expenditureSaveDto);
      // this.$store
      //   .dispatch("writeStore/saveExpenditureList", expenditureSaveDto)
      //   .then((res) => {
      //     this.getExpenditureList();
      //   })
      //   .catch((Error) => {
      //     console.log(Error);
      //   });
    },
    // Row 선택 삭제
    onRowDelete() {
      const selectedRows = this.gridApi.getSelectedRows();
      this.deletedRows = _.filter(selectedRows, "expenditureId");
      this.gridApi.applyTransaction({ remove: selectedRows });
      this.getTotal();
    },
    // Row 선택 복사
    onRowCopy() {
      const selectedRows = _.cloneDeep(this.gridApi.getSelectedRows());
      _.forEach(selectedRows, function (row, index) {
        selectedRows[index].expenditureId = "";
      });
      this.gridApi.applyTransaction({
        add: selectedRows,
        addIndex: this.gridApi.getDisplayedRowCount() - 1,
      });
      this.getTotal();
    },
  },
};
</script>

