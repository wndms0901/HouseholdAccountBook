<template>
  <div>
    <div class="pb-2 excel_btn_box">
      <button class="basicBtn" @click="openExcelUploadModal">
        엑셀 업로드
      </button>
      <button class="basicBtn" @click="excelDownload">엑셀 다운로드</button>
    </div>
    <grid
      ref="expenditureGrid"
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
        <button
          id="calculate-button"
          class="calculateBtn"
          @click="openCalculateModal"
        >
          정산
        </button>
      </div>
      <!-- 월시작일 설정 Modal -->
      <calculateModal v-if="showCalculateModal">
        <!-- top 슬롯 콘텐츠 -->
        <template slot="top">
          <span>가계내역 정산하기</span>
        </template>
        <!-- /top -->
        <!-- default 슬롯 콘텐츠 -->
        <div>
          <span
            >전월이월을 원하는 시점에 생성할 수 있습니다.<br />
            정산은 한달치만 가능하며, 전월의 잔액을 이달의 시작일로
            생성합니다.</span
          >
        </div>
        <!-- /default -->
        <!-- footer 슬롯 콘텐츠 -->
        <template slot="footer">
          <div class="modalFooterBtn-box">
            <button class="primaryBtn" @click="onSaveCalculation">정산</button>
            <button class="basicBtn" @click="closeCalculateModal">닫기</button>
          </div>
        </template>
        <!-- /footer -->
      </calculateModal>
      <!-- 엑셀 업로드 Modal -->
      <excelUploadModal v-if="showExcelUploadModal">
        <!-- top 슬롯 콘텐츠 -->
        <template slot="top">
          <span>엑셀 업로드</span>
        </template>
        <!-- /top -->
        <!-- cotent 슬롯 콘텐츠 -->
        <div class="excel_select_box">
          <b-form-file
            ref="excelForm"
            v-model="file"
            accept=".xlsx, .xls"
            browse-text="엑셀선택"
            placeholder="엑셀 파일을 선택해 주세요."
          ></b-form-file>
        </div>
        <div class="excel_form_box">
          <img
            id="excel_logo"
            src="../../../assets/image/excel.png"
          />&nbsp;<span>엑셀 양식 다운로드</span>&ensp;
          <button class="outlineSecondaryBtn" @click="excelFormDownload">
            지출내역
          </button>
        </div>
        <!-- /cotent -->
        <!-- footer 슬롯 콘텐츠 -->
        <template slot="footer">
          <div class="modalFooterBtn-box">
            <button class="primaryBtn" @click="excelUpload">엑셀 업로드</button>
            <button class="basicBtn" @click="closeExcelUploadModal">
              닫기
            </button>
          </div>
        </template>
        <!-- /footer -->
      </excelUploadModal>
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

import TextInputCellEditor from "src/components/CellEditor/TextInputCellEditor";
import NumberInputCellEditor from "src/components/CellEditor/NumberInputCellEditor";
import calculateModal from "src/components/Modal/Calculate";
import excelUploadModal from "src/components/Modal/ExcelUpload";
export default {
  name: "Expenditure",
  components: {
    TextInputCellEditor,
    NumberInputCellEditor,
    calculateModal,
    excelUploadModal,
  },
  props: {
    user: Object,
    period: Object,
    monthStartDate: String,
  },
  data() {
    return {
      gridApi: null,
      gridOptions: null,
      columnDefs: null,
      defaultColDef: null,
      rowData: [],
      components: null,
      frameworkComponents: null,
      accountCategory: [],
      largeCategory: [],
      smallCategory: {},
      categoryType: "EXP",
      accountCategoryType: "WDRL",
      deletedRows: [],
      totalCashNumber: 0,
      totalCardNumber: 0,
      totalCashString: 0,
      totalCardString: 0,
      disabledSelectBtn: true,
      showCalculateModal: false,
      showExcelUploadModal: false,
      file: null,
      originRowData: [],
    };
  },
  computed: {
    // 지출합계
    totalExpenditure() {
      let value = this.totalCashNumber + this.totalCardNumber;
      return String(value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    // 기본 row
    defaultRow() {
      const today = new Date();
      const expenditureDate =
        String(today.getFullYear()) +
        "." +
        ("0" + (today.getMonth() + 1)).slice(-2) +
        "." +
        ("0" + today.getDate()).slice(-2);
      const row = [
        {
          expenditureId: "",
          expenditureDate: expenditureDate,
          expenditureDescription: "",
          cash: "0",
          card: "0",
          accountCategory: {
            accountCategoryId: 1,
            accountCategoryName: "선택없음",
          },
          largeCategory: {
            largeCategoryId: 1,
            largeCategoryName: "미분류",
          },
          smallCategory: { smallCategoryId: 1, smallCategoryName: "미분류" },
          memo: "",
        },
      ];
      return row;
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
  created() {
    // 카테고리 목록 조회
    this.getCategoryList();
  },
  beforeMount() {
    this.gridOptions = {
      onCellValueChanged: this.onCellValueChanged,
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
      { field: "expenditureId", hide: true },
      {
        headerName: "날짜",
        field: "expenditureDate",
        cellEditor: "datePicker",
        width: 135,
        checkboxSelection: true,
        headerCheckboxSelection: true,
      },
      {
        headerName: "사용내역",
        field: "expenditureDescription",
        cellEditor: "TextInputCellEditor",
        tooltipField: "expenditureDescription",
      },
      {
        headerName: "현금",
        field: "cash",
        type: "numericColumn",
        cellEditor: "NumberInputCellEditor",
        valueFormatter: (params) => {
          return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
      },
      {
        headerName: "카드",
        field: "card",
        type: "numericColumn",
        cellEditor: "NumberInputCellEditor",
        valueFormatter: (params) => {
          return String(params.value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
      },
      {
        headerName: "출금통장",
        field: "accountCategory",
        cellEditor: "agSelectCellEditor",
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
        headerName: "대분류",
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
      {
        headerName: "소분류",
        field: "smallCategory",
        cellEditor: "agSelectCellEditor",
        cellEditorParams: (params) => {
          return {
            values:
              this.smallCategory[params.data.largeCategory.largeCategoryId],
          };
        },
        // convert code to value
        valueFormatter: (params) => {
          return params.value.smallCategoryName;
        },
      },
      {
        headerName: "메모",
        field: "memo",
        cellEditor: "TextInputCellEditor",
        tooltipField: "memo",
      },
    ];

    this.components = { datePicker: getDatePicker() };

    this.frameworkComponents = {
      TextInputCellEditor: TextInputCellEditor,
      NumberInputCellEditor: NumberInputCellEditor,
    };

    this.isRowSelectable = (rowNode) => {
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
      if (colId === "largeCategory") {
        params.api.startEditingCell({
          rowIndex: params.rowIndex,
          colKey: "smallCategory",
        });
      } else if (colId === "cash" || colId === "card") {
        this.getTotal();
      }
    },
    // 카테고리 목록 조회
    getCategoryList() {
      this.$store
        .dispatch("writeStore/selectCategoryList", this.categoryType)
        .then((res) => {
          // 출금통장 카테고리 목록
          this.accountCategory = res.data.accountCategoryDtoList;

          // 대분류 카테고리 목록
          this.largeCategory = res.data.largeCategoryDtoList;

          // 소분류 카테고리 목록
          let smallCategoryObj = {};
          _.forEach(res.data.largeCategoryDtoList, function (obj) {
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
            });
            smallCategoryObj[obj.largeCategoryId] = lst;
          });
          this.smallCategory = smallCategoryObj;
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // 지출 목록 조회
    getExpenditureList() {
      // reset
      this.deletedRows = [];

      const params = {
        startDate: this.$moment(this.period.from).format("YYYYMMDD"),
        endDate: this.$moment(this.period.to).format("YYYYMMDD"),
        email: this.user.userInfo.email,
      };
      this.$store
        .dispatch("writeStore/selectExpenditureList", params)
        .then((res) => {
          this.gridApi.clearFocusedCell();
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
            // smallCategory setting
            res.data[index].smallCategory = {
              smallCategoryId: row.smallCategoryId,
              smallCategoryName: row.smallCategoryName,
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
      const rowData = this.$refs.expenditureGrid.getRowData();
      const defaultRow = _.cloneDeep(this.defaultRow);
      const emptyRow = {
        expenditureId: "",
        expenditureDate: "",
        expenditureDescription: "",
        cash: "",
        card: "",
        accountCategory: " ",
        largeCategory: " ",
        smallCategory: " ",
        memo: "",
      };
      defaultRow.push(emptyRow);
      this.gridApi.applyTransaction({
        add: defaultRow,
        addIndex: rowData.length,
      });
      this.gridApi.startEditingCell({
        rowIndex: rowData.length,
        colKey: "expenditureDescription",
      });
      let originRowData = _.cloneDeep(this.$refs.expenditureGrid.getRowData());
      _.forEach(originRowData, function (row, index) {
        row.expenditureDescription = row.expenditureDescription || "";
        row.memo = row.memo || "";
      });
      this.originRowData = originRowData;
      this.gridApi.sizeColumnsToFit();
      this.getTotal();
    },
    // 현금, 카드 합계
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
        this.gridApi.clearFocusedCell();
        const rowData = this.$refs.expenditureGrid.getRowData();
        const defaultRow = _.cloneDeep(this.defaultRow);

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
    toast(toaster, msg) {
      this.$bvToast.toast(msg, {
        toaster: toaster,
        bodyClass: "text-center font-weight-bold",
        solid: true,
        variant: "primary",
        autoHideDelay: 1000,
        noCloseButton: true,
      });
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
          expenditureDescription: row.expenditureDescription || "",
          cash: parseInt(String(row.cash).replace(/,/g, "")),
          card: parseInt(String(row.card).replace(/,/g, "")),
          accountCategoryId: row.accountCategory.accountCategoryId,
          largeCategoryId: row.largeCategory.largeCategoryId,
          smallCategoryId: row.smallCategory.smallCategoryId,
          memo: row.memo || "",
          userDto: userDto,
        };
      });
      // 등록 rows
      expenditureSaveDto.insertExpenditureDtoList = _.filter(
        rowData,
        function (row) {
          return (
            !row.expenditureId &&
            (row.expenditureDescription ||
              row.cash > 0 ||
              row.card > 0 ||
              row.accountCategoryId !== 1 ||
              row.largeCategoryId !== 1 ||
              row.memo)
          );
        }
      );
      // 수정 rows
      expenditureSaveDto.updateExpenditureDtoList = _.filter(
        rowData,
        "expenditureId"
      );
      // 삭제 rows
      expenditureSaveDto.deleteExpenditureDtoList = this.deletedRows;
      this.$store
        .dispatch("writeStore/saveExpenditureList", expenditureSaveDto)
        .then((res) => {
          this.toast("b-toaster-bottom-center", "저장되었습니다.");
          this.getExpenditureList();
          this.$emit("getIncomeExpenditureDetail");
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // 정산
    onSaveCalculation() {
      const periodTo = _.cloneDeep(this.period.to);
      let subtractMonth = null;
      let startDate = null;
      let endDate = null;
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        // 종료일이 말일인 경우 지난달, 아닌경우 지지난달 정산
        subtractMonth =
          this.$moment(periodTo).format("YYYYMMDD") ===
          this.$moment(periodTo).endOf("month").format("YYYYMMDD")
            ? 1
            : 2;
        // 정산 시작일
        startDate = this.$moment(periodTo)
          .subtract(subtractMonth, "months")
          .endOf("month")._d;
        // 정산 종료일
        endDate = this.$moment(startDate)
          .add(1, "months")
          .endOf("month")
          .subtract(1, "days");
      } else {
        // 월시작일이 말일이 아닌 경우
        // 종료일이 월시작일과 같거나 큰 경우 지난달, 아닌경우 지지난달 정산
        subtractMonth =
          periodTo.getDate() < parseInt(this.monthStartDate) ? 2 : 1;
        // 정산 시작일
        startDate = this.$moment(periodTo).subtract(subtractMonth, "months")._d;
        startDate = new Date(
          startDate.getFullYear(),
          startDate.getMonth(),
          parseInt(this.monthStartDate)
        );
        // 정산 종료일
        endDate = this.$moment(startDate).add(1, "months").subtract(1, "days");
      }
      const writeRequestDto = {
        startDate: this.$moment(startDate).format("YYYYMMDD"),
        endDate: endDate.format("YYYYMMDD"),
        incomeDate: this.$moment(endDate).add(1, "days").format("YYYYMMDD"),
        lastMonth:
          parseInt(this.monthStartDate) < 16
            ? this.$moment(startDate).format("MM")
            : endDate.format("MM"),
        userDto: this.user.userInfo,
      };

      this.$store
        .dispatch("writeStore/saveCalculation", writeRequestDto)
        .then((res) => {
          this.toast(
            "b-toaster-bottom-center",
            "수입으로 전월 잔액이 생성되었습니다."
          );
          this.showCalculateModal = false;
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // 엑셀 양식 다운로드
    excelFormDownload() {
      const pageName = "Expenditure";
      this.$store
        .dispatch("excelStore/excelFormDownload", pageName)
        .then((res) => {
          const fileName = "가계부_지출양식.xlsx";
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
    // 엑셀 업로드
    excelUpload() {
      if (!this.file) {
        alert("엑셀파일을 선택해 주세요.");
      }
      const fileExtension = this.file.name
        .split(".")
        .reverse()[0]
        .toLowerCase();
      if (!(fileExtension === "xlsx" || fileExtension === "xls")) {
        alert(
          "엑셀 파일은 xlsx, xls인 경우만 업로드가 가능합니다. \n파일을 다시 한번 확인한 후 업로드해 주세요"
        );
      } else {
        const formData = new FormData();
        const excelRequestDto = {
          userDto: this.user.userInfo,
          pageName: "Expenditure",
          fileNameExtension: fileExtension,
        };
        const json = JSON.stringify(excelRequestDto);
        const blob = new Blob([json], {
          type: "application/json",
        });

        formData.append("file", this.file);
        formData.append("excelRequestDto", blob);
        this.$store
          .dispatch("excelStore/excelUpload", formData)
          .then((res) => {
            if (res.result === "failure") {
              alert(
                "엑셀 업로드에 실패하였습니다. 작성한 내용을 다시 확인해 주세요."
              );
            } else {
              this.showExcelUploadModal = false;
              this.getExpenditureList();
            }
          })
          .catch((Error) => {
            console.log(Error);
          });
      }
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
        writeRequestDto: {
          startDate: startDate,
          endDate: endDate,
          userDto: this.user.userInfo,
        },
        pageName: "Expenditure",
        period: period,
      };
      this.$store
        .dispatch("excelStore/excelDownload", excelRequestDto)
        .then((res) => {
          const fileName =
            "가계부_지출현황_" + startDate + "_" + endDate + ".xlsx";
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
        addIndex: this.gridApi.getDisplayedRowCount() - 2,
      });
      this.gridApi.deselectAll();
      this.gridApi.clearFocusedCell();
      this.getTotal();
    },

    // 월시작일 모달 open
    openCalculateModal() {
      this.showCalculateModal = true;
    },
    // 월시작일 모달 close
    closeCalculateModal() {
      this.showCalculateModal = false;
    },
    // 엑셀 업로드 모달 open
    openExcelUploadModal() {
      this.showExcelUploadModal = true;
    },
    // 엑셀 업로드 모달 close
    closeExcelUploadModal() {
      this.showExcelUploadModal = false;
    },
  },
};
</script>

