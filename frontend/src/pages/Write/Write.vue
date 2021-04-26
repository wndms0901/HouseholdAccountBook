<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="date_wrap">
          <div class="date_picker_box">
            <b-button class="prevMonthBtn" @click="onPrevMonth">
              <b-icon-chevron-left
                variant="dark"
              ></b-icon-chevron-left> </b-button
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
            <b-button class="nextMonthBtn" @click="onNextMonth">
              <b-icon-chevron-right variant="dark"></b-icon-chevron-right>
            </b-button>
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
        <b-tabs v-model="tabIndex" content-class="mt-3">
          <b-tab title="지출" @click="onClickExpenditure"
            ><expenditure
              ref="expenditureTab"
              :user="user"
              :period="period"
              :monthStartDate="monthStartDate"
            ></expenditure
          ></b-tab>
          <b-tab title="수입" @click="onClickIncome"
            ><income ref="incomeTab" :user="user" :period="period"></income
          ></b-tab>
          <b-tab title="달력"></b-tab>
        </b-tabs>
      </div>
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
      monthStartDate: this.$store.state.userStore.initialState.user.userInfo
        .monthStartDate,
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
  beforeCreate() {},
  created() {
    this.setPeriod();
  },
  mounted() {
    //  this.user = this.$store.state.initialState.user;
  },
  methods: {
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

      // const periodFrom = _.cloneDeep(this.period.from);
      // const periodTo = _.cloneDeep(this.period.to);
      // const startLastDate = this.$moment(periodFrom).endOf("month")._d;
      // const endLastDate = this.$moment(periodTo).endOf("month")._d;
      // //const lastDay = this.$moment(periodFrom).endOf("month")._d;
      // // 말일 체크
      // if (periodFrom.getDate() === startLastDate.getDate()) {
      //   this.period.from = this.$moment(this.period.from)
      //     .subtract(1, "months")
      //     .endOf("month")._d;
      //   this.period.to = this.$moment(this.period.from)
      //     .add(1, "months")
      //     .endOf("month")._d;
      // } else {
      //   this.period.from = this.$moment(periodFrom).subtract(1, "months")._d;
      // }
      // // period.to setting
      // this.period.to = this.$moment(this.period.from)
      //   .add(1, "months")
      //   .subtract(1, "days")._d;
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

      // const periodFrom = _.cloneDeep(this.period.from);
      // const lastDay = this.$moment(periodFrom).endOf("month")._d;
      // // 말일 체크
      // if (periodFrom.getDate() === lastDay.getDate()) {
      //   this.period.from = this.$moment(this.period.from)
      //     .add(1, "months")
      //     .endOf("month")._d;
      // } else {
      //   this.period.from = this.$moment(periodFrom).add(1, "months")._d;
      // }
      // // period.to setting
      // this.period.to = this.$moment(this.period.from)
      //   .add(1, "months")
      //   .subtract(1, "days")._d;
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
      console.log("userDto", userDto);
      this.$store
        .dispatch("userStore/updateMonthStartDate", userDto)
        .then((res) => {
          this.setPeriod();
        })
        .catch((Error) => {
          console.log(Error);
        });
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
.modalFooterBtn-box {
  width: 100%;
  text-align: center;
}
.tabs {
  font-weight: 600;
}
.excel_btn_box {
  text-align: right;
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
/* tooltip css */
/* .tooltip .tooltip-inner {
  max-width: 330px !important;
  text-align: left;
  background: white;
  color: #000;
  border: 1px solid #1953d7;
}
.tooltip .arrow {
  width: 11px;
  height: 11px;
  border: 1px solid #1953d7;
  bottom: 2px;
}
.tooltip .arrow:before {
  width: 11px;
  height: 11px;
  background: white;
  border: 0;
}
.tooltip.bs-tooltip-top .arrow {
  transform: rotate(-135deg);
}
.tooltip.bs-tooltip-bottom .arrow {
  transform: rotate(135deg);
  top: 2px;
} */
</style>
