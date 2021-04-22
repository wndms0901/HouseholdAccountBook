<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div id="title" style="width: 100%">
          <p>예산쓰기</p>
        </div>
        <div class="date_wrap">
          <div class="date_picker_box">
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
      monthStartDate: this.$store.state.userStore.initialState.user.userInfo
        .monthStartDate,
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
      let today = new Date();

      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        if (this.tabIndex === 0) {
          // 예산쓰기(월 단위)
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
        } else if (this.tabIndex === 1) {
          // 예산 대비 지출(연 단위)
          const year = today.getFullYear() - 1;
          const month = 11;
          this.period.from = new Date(year, month, 31);
          this.period.to = this.$moment(this.period.from)
            .add(1, "years")
            .subtract(1, "days")._d;
        }
      } else {
        // 월시작일이 말일이 아닌 경우
        if (this.tabIndex === 0) {
          // 예산쓰기(월 단위)
          const month =
            parseInt(this.monthStartDate) > 15
              ? today.getMonth() - 1
              : today.getMonth();
          this.period.from = new Date(
            today.getFullYear(),
            month,
            parseInt(this.monthStartDate)
          );
          this.period.to = new Date(
            today.getFullYear(),
            month + 1,
            parseInt(this.monthStartDate) - 1
          );
        } else if (this.tabIndex === 1) {
          // 예산 대비 지출(연 단위)
          const year =
            parseInt(this.monthStartDate) > 15
              ? today.getFullYear() - 1
              : today.getFullYear();
          const month = parseInt(this.monthStartDate) > 15 ? 11 : 0;
          this.period.from = new Date(
            year,
            month,
            parseInt(this.monthStartDate)
          );
          this.period.to = this.$moment(this.period.from)
            .add(1, "years")
            .subtract(1, "days")._d;
        }
      }
    },
    onClickPeriodFromCalendar() {
      this.$refs.periodFrom.showCalendar();
    },
    onBlurCalendar() {
      this.$refs.periodFrom.close();
    },
    onCloseStartDate() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        this.period.from = this.$moment(this.period.from).endOf("month")._d;
      } else {
        // 월시작일이 말일이 아닌 경우
        const periodFrom = _.cloneDeep(this.period.from);
        this.period.from = new Date(
          periodFrom.getFullYear(),
          periodFrom.getMonth(),
          parseInt(this.monthStartDate)
        );
      }
      this.period.to = this.$moment(this.period.from)
        .add(1, "years")
        .subtract(1, "days")._d;
    },
    onPrevMonth() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        this.period.from = this.$moment(this.period.from)
          .subtract(1, "months")
          .endOf("month")._d;
      } else {
        // 월시작일이 말일이 아닌 경우
        this.period.from = this.$moment(this.period.from).subtract(
          1,
          "months"
        )._d;
      }
      this.period.to = this.$moment(this.period.from)
        .add(1, "years")
        .subtract(1, "days")._d;
    },
    onNextMonth() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        this.period.from = this.$moment(this.period.from)
          .add(1, "months")
          .endOf("month")._d;
      } else {
        // 월시작일이 말일이 아닌 경우
        this.period.from = this.$moment(this.period.from).add(1, "months")._d;
      }
      this.period.to = this.$moment(this.period.from)
        .add(1, "years")
        .subtract(1, "days")._d;
    },
  },
};
</script>