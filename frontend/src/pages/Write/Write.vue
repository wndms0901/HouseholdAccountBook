<template>
  <div class="container-fluid">
    <div class="row">
      <div class="date_wrap">
        <div class="date_picker_box">
          <button class="prevMonthBtn" @click="onPrevMonth">
            <b-icon
              icon="chevron-left"
              variant="dark"
              style="vertical-align: middle"
            ></b-icon></button
          >&nbsp;
          <datepicker
            ref="periodFrom"
            :format="'yyyy.MM.dd'"
            v-model="period.from"
            input-class="datepickerInput"
            :language="ko"
            @closed="onCloseStartDate"
          ></datepicker>
          <span
            class="calendarIconBox"
            @click="onClickPeriodFromCalendar"
            @blur="onBlurCalendar"
            tabindex="0"
            ><b-icon class="calendarIcon" icon="calendar3"></b-icon></span
          >&ensp; <span style="font-size: 1.5em">-</span>&ensp;
          <datepicker
            ref="periodTo"
            :format="'yyyy.MM.dd'"
            v-model="period.to"
            input-class="datepickerInput"
            :disabledDates="disabledDates"
            :language="ko"
          ></datepicker>
          <span
            class="calendarIconBox"
            @click="onClickPeriodToCalendar"
            @blur="onBlurCalendar"
            tabindex="0"
            ><b-icon class="calendarIcon" icon="calendar3"></b-icon
          ></span>
          &nbsp;
          <button class="nextMonthBtn" @click="onNextMonth">
            <b-icon
              icon="chevron-right"
              variant="dark"
              style="vertical-align: middle"
            ></b-icon>
          </button>
          <button class="startDateBtn" @click="openModal">월시작일</button>
        </div>
      </div>
    </div>
    <!-- 월시작일 설정 Modal -->
    <setStartDateModal v-click-outside="onClickOutside" v-if="showModal">
      <!-- default 슬롯 콘텐츠 -->
      <div>
        <strong>월 시작일 설정</strong>
        <b-form-select
          class="selectBox"
          v-model="monthStartDate"
          :options="options"
          size="sm"
        ></b-form-select>
        <span>(이달 1일 ~ 이달 말일)</span>
      </div>
      <!-- /default -->
      <!-- footer 슬롯 콘텐츠 -->
      <template slot="footer">
        <div>
          <span
            >월 시작일로 지정한 날부터 한달이 시작되어 가계부가
            계산됩니다.</span
          >
        </div>
        <div class="modalFooterBtn-box">
          <button class="basicBtn" @click="onSaveStartDate">확인</button>
        </div>
      </template>
      <!-- /footer -->
    </setStartDateModal>
    <div class="tabs" style="width: 100%">
      <b-tabs
        active-nav-item-class="font-weight-bold text-primary"
        content-class="mt-3"
        v-model="tabIndex"
        v-on:activate-tab="tabActivated"
      >
        <b-tab
          title-item-class="defaultTab"
          title="지출"
          @click="onClickExpenditure"
          ><expenditure
            ref="expenditureTab"
            :user="user"
            :period="period"
            :monthStartDate="monthStartDate"
            @getIncomeExpenditureDetail="getIncomeExpenditureDetail"
          ></expenditure
        ></b-tab>
        <b-tab title-item-class="defaultTab" title="수입" @click="onClickIncome"
          ><income
            ref="incomeTab"
            :user="user"
            :period="period"
            @getIncomeExpenditureDetail="getIncomeExpenditureDetail"
          ></income
        ></b-tab>
      </b-tabs>
    </div>
  </div>
</template>

<script>
import datepicker from "vuejs-datepicker";
import expenditure from "./Tab/Expenditure.vue";
import income from "./Tab/Income.vue";
import { ko } from "vuejs-datepicker/dist/locale";
import setStartDateModal from "src/components/Modal/SetStartDate";
export default {
  name: "Write",
  components: { datepicker, expenditure, income, setStartDateModal },
  data() {
    return {
      user: this.$store.state.userStore.initialState.user,
      ko: ko,
      period: {
        from: "",
        to: "",
      },
      tabIndex: 0,
      showModal: false,
      monthStartDate:
        this.$store.state.userStore.initialState.user.userInfo.monthStartDate,
    };
  },
  computed: {
    options() {
      let options = [];
      for (let i = 1; i < 29; i++) {
        options.push({ value: String(i), text: i + "일" });
      }
      options.push({ value: "last", text: "말일" });
      return options;
    },
    disabledDates() {
      return {
        to: this.period.from,
        from: this.$moment(this.period.from)
          .add(6, "months")
          .subtract(1, "days")._d,
      };
    },
  },
  // 다른 route로 이동할 경우 호출됨
  beforeRouteLeave(to, from, next) {
    const userInfo = this.$cookies.get("user");
    const movePage = this.$store.state.userStore.initialState.status.movePage;
    let currentRowData = null;
    let isGridUpdate = false;

    if (this.tabIndex === 0) {
      // 지출 grid 변경사항 체크
      this.$refs.expenditureTab.gridApi.clearFocusedCell();
      currentRowData =
        this.$refs.expenditureTab.$refs.expenditureGrid.getRowData();
      _.forEach(currentRowData, function (row, index) {
        row.expenditureDescription = row.expenditureDescription || "";
        row.memo = row.memo || "";
      });
      if (
        currentRowData.length !== this.$refs.expenditureTab.originRowData.length
      ) {
        isGridUpdate = true;
      } else if (
        JSON.stringify(currentRowData) !==
        JSON.stringify(this.$refs.expenditureTab.originRowData)
      ) {
        isGridUpdate = true;
      }
    } else {
      // 수입 grid 변경사항 체크
      this.$refs.incomeTab.gridApi.clearFocusedCell();
      currentRowData = this.$refs.incomeTab.$refs.incomeGrid.getRowData();
      _.forEach(currentRowData, function (row, index) {
        row.incomeDescription = row.incomeDescription || "";
        row.memo = row.memo || "";
      });
      if (currentRowData.length !== this.$refs.incomeTab.originRowData.length) {
        isGridUpdate = true;
      } else if (
        JSON.stringify(currentRowData) !==
        JSON.stringify(this.$refs.incomeTab.originRowData)
      ) {
        isGridUpdate = true;
      }
    }

    if (to.name === "Login") {
      if (movePage) {
        // 계정 권한 만료 + 로그인 페이지 이동
        this.$store.dispatch("userStore/logout");
        next();
      } else {
        // 로그아웃 click
        if (isGridUpdate) {
          // 그리드 변경
          if (
            confirm("저장하지 않은 내용이 있습니다. 로그아웃 하시겠습니까?")
          ) {
            this.$store.commit("userStore/clickLogout", true);
            next();
          } else {
            this.$store.commit("userStore/clickLogout", false);
          }
        } else {
          // 그리드 변경x
          this.$store.commit("userStore/clickLogout", true);
          next();
        }
      }
    } else if (to.name !== "Error") {
      if (!userInfo) {
        this.$store.commit("userStore/movePage", true);
      }
      // 페이지 이동
      if (isGridUpdate) {
        // 그리드 변경
        if (confirm("저장하지 않은 내용이 있습니다. 이동하겠습니까?")) {
          next();
        }
      } else {
        next();
      }
    } else {
      next();
    }
  },
  created() {
    this.setPeriod();
  },
  mounted() {
    // beforeunload 이벤트 등록
    window.addEventListener("beforeunload", this.unLoadEvent);
  },
  beforeDestroy() {
    // 등록된 이벤트 리스너를 제거
    window.removeEventListener("beforeunload", this.unLoadEvent);
  },
  methods: {
    unLoadEvent(event) {
      let currentRowData = null;
      if (this.tabIndex === 0) {
        // 지출 grid 변경사항 체크
        this.$refs.expenditureTab.gridApi.clearFocusedCell();
        currentRowData =
          this.$refs.expenditureTab.$refs.expenditureGrid.getRowData();
        _.forEach(currentRowData, function (row, index) {
          row.expenditureDescription = row.expenditureDescription || "";
          row.memo = row.memo || "";
        });
        if (
          JSON.stringify(currentRowData) ===
          JSON.stringify(this.$refs.expenditureTab.originRowData)
        )
          return;
      } else {
        // 수입 grid 변경사항 체크
        this.$refs.incomeTab.gridApi.clearFocusedCell();
        currentRowData = this.$refs.incomeTab.$refs.incomeGrid.getRowData();
        _.forEach(currentRowData, function (row, index) {
          row.incomeDescription = row.incomeDescription || "";
          row.memo = row.memo || "";
        });
        if (
          JSON.stringify(currentRowData) ===
          JSON.stringify(this.$refs.incomeTab.originRowData)
        )
          return;
      }

      event.preventDefault();
      event.returnValue = "";
    },
    // tab click
    tabActivated(newTabIndex, oldTabIndex, event) {
      let currentRowData = null;
      if (oldTabIndex === 0) {
        // 수입 tab click
        this.$refs.expenditureTab.gridApi.clearFocusedCell();
        currentRowData =
          this.$refs.expenditureTab.$refs.expenditureGrid.getRowData();
        _.forEach(currentRowData, function (row, index) {
          row.expenditureDescription = row.expenditureDescription || "";
          row.memo = row.memo || "";
        });

        // 지출 grid 변경사항 체크
        if (
          JSON.stringify(currentRowData) ===
          JSON.stringify(this.$refs.expenditureTab.originRowData)
        )
          return;
      } else {
        // 지출 tab click
        this.$refs.incomeTab.gridApi.clearFocusedCell();
        currentRowData = this.$refs.incomeTab.$refs.incomeGrid.getRowData();
        _.forEach(currentRowData, function (row, index) {
          row.incomeDescription = row.incomeDescription || "";
          row.memo = row.memo || "";
        });
        // 수입 grid 변경사항 체크
        if (
          JSON.stringify(currentRowData) ===
          JSON.stringify(this.$refs.incomeTab.originRowData)
        )
          return;
      }
      if (!confirm("저장하지 않은 내용이 있습니다. 이동하겠습니까?")) {
        event.preventDefault();
      }
    },
    // 조회 기간 setting
    setPeriod() {
      let today = new Date();
      const monthStartDate = this.monthStartDate;

      if (monthStartDate === "last") {
        // 월시작일이 말일인 경우
        const month = today.getMonth() - 1;
        const startLastDate = this.$moment(
          new Date(today.getFullYear(), month, 1)
        ).endOf("month")._d;
        const endLastDate = this.$moment(
          new Date(today.getFullYear(), month + 1, 1)
        ).endOf("month")._d;

        this.period.from = new Date(
          today.getFullYear(),
          month,
          startLastDate.getDate()
        );
        this.period.to = new Date(
          today.getFullYear(),
          month + 1,
          endLastDate.getDate() - 1
        );
      } else {
        // 월시작일이 말일이 아닌 경우
        const month =
          parseInt(monthStartDate) > 15
            ? today.getMonth() - 1
            : today.getMonth();
        this.period.from = new Date(today.getFullYear(), month, monthStartDate);
        this.period.to = new Date(
          today.getFullYear(),
          month + 1,
          monthStartDate - 1
        );
      }
      // 오늘 날짜
      const currentDate = new Date(
        today.getFullYear(),
        today.getMonth(),
        today.getDate()
      );
      // 오늘 날짜가 시작일~종료일 사이로 조회되도록 설정
      if (!(currentDate >= this.period.from && currentDate <= this.period.to)) {
        if (parseInt(monthStartDate) < 16) {
          this.onPrevMonth();
        } else {
          this.onNextMonth();
        }
      }
    },
    onClickPeriodFromCalendar() {
      this.$refs.periodTo.close();
      this.$refs.periodFrom.showCalendar();
    },
    onClickPeriodToCalendar() {
      this.$refs.periodFrom.close();
      this.$refs.periodTo.showCalendar();
    },
    onBlurCalendar() {
      this.$refs.periodFrom.close();
      this.$refs.periodTo.close();
    },
    onCloseStartDate() {
      const isLastDay = this.checkMonthLastDay(this.period.from);

      if (isLastDay) {
        // 말일인 경우
        this.period.to = this.$moment(this.period.from)
          .add(1, "months")
          .endOf("month")
          .subtract(1, "days")._d;
      } else {
        this.period.to = this.$moment(this.period.from)
          .add(1, "months")
          .subtract(1, "days")._d;
      }
    },
    onPrevMonth() {
      const isPeriodFromLastDay = this.checkMonthLastDay(this.period.from);
      const isPeriodtoLastDay = this.checkMonthLastDay(this.period.to);

      if (isPeriodFromLastDay) {
        // 말일인 경우
        this.period.from = this.$moment(this.period.from)
          .subtract(1, "months")
          .endOf("month")._d;
      } else {
        // 말일이 아닌 경우
        this.period.from = this.$moment(this.period.from).subtract(
          1,
          "months"
        )._d;
      }
      if (isPeriodtoLastDay) {
        this.period.to = this.$moment(this.period.to)
          .subtract(1, "months")
          .endOf("month")._d;
      } else {
        this.period.to = this.$moment(this.period.to).subtract(1, "months")._d;
      }
    },
    onNextMonth() {
      const isPeriodFromLastDay = this.checkMonthLastDay(this.period.from);
      const isPeriodtoLastDay = this.checkMonthLastDay(this.period.to);

      if (isPeriodFromLastDay) {
        // 말일인 경우
        this.period.from = this.$moment(this.period.from)
          .add(1, "months")
          .endOf("month")._d;
      } else {
        // 말일이 아닌 경우
        this.period.from = this.$moment(this.period.from).add(1, "months")._d;
      }
      if (isPeriodtoLastDay) {
        this.period.to = this.$moment(this.period.to)
          .add(1, "months")
          .endOf("month")._d;
      } else {
        this.period.to = this.$moment(this.period.to).add(1, "months")._d;
      }
    },
    // 말일 체크
    checkMonthLastDay(value) {
      const dt = _.cloneDeep(value);
      const lastDay = this.$moment(dt).endOf("month")._d;
      return (
        parseInt(this.monthStartDate) !== 28 &&
        dt.getDate() === lastDay.getDate()
      );
    },
    // 지출 tab click
    onClickExpenditure() {
      this.$refs.expenditureTab.getExpenditureList();
    },
    // 수입 tab click
    onClickIncome() {
      this.$refs.incomeTab.getIncomeList();
    },
    openModal() {
      this.showModal = !this.showModal;
    },
    closeModal() {
      this.showModal = false;
    },
    onClickOutside(event) {
      if (event.target.className !== "startDateBtn") {
        this.closeModal();
      }
    },
    // 월시작일 저장
    onSaveStartDate() {
      this.showModal = false;
      const userDto = {
        email: this.user.userInfo.email,
        monthStartDate: this.monthStartDate,
      };
      this.$store
        .dispatch("userStore/updateMonthStartDate", userDto)
        .then((res) => {
          this.$emit("updateStartDate");
          this.setPeriod();
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    // 수입/지출 상세 조회
    getIncomeExpenditureDetail() {
      this.$emit("getIncomeExpenditureDetail");
    },
  },
};
</script>

<style>
.startDateBtn {
  margin-left: 5px;
  padding: 7px 15px;
  border: 1px solid lightgray;
  background-color: white;
  /* display: inline-block; */
  text-align: center;
  text-decoration: none;
  font-size: 16px;
}
.startDateBtn:hover {
  border: 1px solid #1953d7;
  background-color: white;
  color: #1953d7;
}
.selectBox {
  margin: 0 6px 0 12px;
  width: 70px;
}
.excel_select_box {
  margin-bottom: 20px;
  padding-left: 20px;
  width: 90%;
  /* text-align: center; */
}
.excel_select_box input {
  cursor: default;
}
.excel_form_box {
  padding-left: 20px;
  width: 100%;
}
.excel_form_box span {
  vertical-align: middle;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}
.excel_form_box button {
  margin-left: 20px;
}
.modalFooterBtn-box {
  width: 100%;
  text-align: center;
}
.tabs {
  font-weight: 600;
}
.left_btn {
  float: left;
}
.right_btn {
  float: right;
}
.right_btn span {
  vertical-align: middle;
  font-size: 20px;
}
.right_btn span:first-child {
  font-weight: normal;
}
.total_box {
  margin-left: 50px;
  display: inline-block;
}
.total_box table {
  width: 300px;
  border-collapse: collapse;
  background-color: white;
}
.total_box table tr td {
  padding: 3px 10px;
  border: 1px solid #babfc7;
}
.total_box table tr:last-child td {
  font-size: 18px;
  font-weight: bold;
}
</style>
