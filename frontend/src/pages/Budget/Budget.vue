<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div id="title" style="width: 100%">
          <p>예산쓰기</p>
        </div>
        <div class="date_wrap">
          <div class="date_picker_box" style="display: flex">
            <b-button
              class="prevMonthBtn"
              v-show="showMonthBtn"
              @click="onPrevMonth"
            >
              <b-icon-chevron-left
                variant="dark"
              ></b-icon-chevron-left></b-button
            >&nbsp;
            <datepicker
              ref="periodFrom"
              :format="'yyyy.MM.dd'"
              v-model="period.from"
              :input-class="datepickerInputClass"
              minimum-view="month"
              :language="ko"
              @closed="onCloseStartDate"
              :disabled="periodFromDisabled"
            ></datepicker>
            <span
              :class="datepickerSpanClass"
              @click="onClickPeriodFromCalendar"
              @blur="onBlurCalendar"
              tabindex="0"
              ><b-icon class="calendarIcon" icon="calendar3"></b-icon></span
            >&ensp; <span style="font-size: 1.5em">-</span>&ensp;
            <datepicker
              :format="'yyyy.MM.dd'"
              v-model="period.to"
              input-class="datepickerInput_disabled"
              minimum-view="month"
              :language="ko"
              disabled
            ></datepicker
            ><span class="calendarIconBox_disabled"
              ><b-icon class="calendarIcon" icon="calendar3"></b-icon
            ></span>
            &nbsp;<b-button
              class="nextMonthBtn"
              v-show="showMonthBtn"
              @click="onNextMonth"
            >
              <b-icon-chevron-right variant="dark"></b-icon-chevron-right>
            </b-button>
          </div>
        </div>
        <div class="tabs" style="width: 100%">
          <b-tabs v-model="tabIndex" content-class="mt-3">
            <b-tab title="예산쓰기"
              ><budgetWrite
                :user="user"
                :period="period"
                :tabIndex="tabIndex"
              ></budgetWrite
            ></b-tab>
            <b-tab title="예산 대비 지출"
              ><budgetExpenditure
                :user="user"
                :period="period"
                :tabIndex="tabIndex"
              ></budgetExpenditure
            ></b-tab>
          </b-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import datepicker from "vuejs-datepicker";
import budgetWrite from "./Tab/BudgetWrite.vue";
import budgetExpenditure from "./Tab/BudgetExpenditure.vue";
import { ko } from "vuejs-datepicker/dist/locale";
export default {
  components: { datepicker, budgetWrite, budgetExpenditure },
  data() {
    return {
      user: this.$store.state.userStore.initialState.user,
      ko: ko,
      period: {
        from: "",
        to: "",
      },
      tabIndex: 0,
    };
  },
  computed: {
    // 캘린더 이전달, 다음달 버튼 보이기 여부
    showMonthBtn() {
      return this.tabIndex === 1;
    },
    // 시작일 datepicker disabled
    periodFromDisabled() {
      return this.tabIndex === 0;
    },
    // datepicker input class
    datepickerInputClass() {
      if (this.tabIndex === 0) {
        return "datepickerInput_disabled";
      } else if (this.tabIndex === 1) {
        return "datepickerInput";
      }
    },
    datepickerSpanClass() {
      if (this.tabIndex === 0) {
        return "calendarIconBox_disabled";
      } else if (this.tabIndex === 1) {
        return "calendarIconBox";
      }
    },
  },
  watch: {
    tabIndex: {
      handler(newData) {
        this.setPeriod();
      },
    },
  },
  beforeCreate() {},
  created() {
    this.setPeriod();
  },
  mounted() {},
  methods: {
    // 조회 기간 setting
    setPeriod() {
      let startDate = null;
      let endDate = null;
      // let today = new Date();
      // const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      let today = new Date(2021, 0, 1);
      const monthStartDate = 16;
      if (this.tabIndex === 0) {
        // 예산쓰기(월 단위)
        const month =
          monthStartDate > 15 ? today.getMonth() - 1 : today.getMonth();
        startDate = new Date(today.getFullYear(), month, monthStartDate);
        endDate = new Date(today.getFullYear(), month + 1, monthStartDate - 1);
      } else if (this.tabIndex === 1) {
        // 예산 대비 지출(연 단위)
        const year =
          monthStartDate > 15 ? today.getFullYear() - 1 : today.getFullYear();
        const month = monthStartDate > 15 ? 11 : 0;
        startDate = new Date(year, month, monthStartDate);
        endDate = this.$moment(startDate).add(1, "years").subtract(1, "days")
          ._d;
      }
      this.period.from = startDate;
      this.period.to = endDate;
    },
    onClickPeriodFromCalendar() {
      this.$refs.periodFrom.showCalendar();
    },
    onBlurCalendar() {
      this.$refs.periodFrom.close();
    },
    onCloseStartDate() {
      this.period.to = this.$moment(this.period.from)
        .add(1, "years")
        .subtract(1, "days")._d;
    },
    onPrevMonth() {
      const periodFrom = _.cloneDeep(this.period.from);
      const lastDay = this.$moment(periodFrom).endOf("month")._d;
      // 말일 체크
      if (periodFrom.getDate() === lastDay.getDate()) {
        this.period.from = this.$moment(this.period.from)
          .subtract(1, "months")
          .endOf("month")._d;
      } else {
        this.period.from = this.$moment(periodFrom).subtract(1, "months")._d;
      }
      // period.to setting
      this.period.to = this.$moment(this.period.from)
        .add(1, "years")
        .subtract(1, "days")._d;
    },
    onNextMonth() {
      const periodFrom = _.cloneDeep(this.period.from);
      const lastDay = this.$moment(periodFrom).endOf("month")._d;
      // 말일 체크
      if (periodFrom.getDate() === lastDay.getDate()) {
        this.period.from = this.$moment(this.period.from)
          .add(1, "months")
          .endOf("month")._d;
      } else {
        this.period.from = this.$moment(periodFrom).add(1, "months")._d;
      }
      // period.to setting
      this.period.to = this.$moment(this.period.from)
        .add(1, "years")
        .subtract(1, "days")._d;
    },
  },
};
</script>
<style>
.budgetDateInput {
  cursor: default;
}
.budgetCalendarIconBox {
  cursor: default;
}
</style>