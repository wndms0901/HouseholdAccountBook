<template>
  <div class="content">
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
              minimum-view="month"
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
            &nbsp;<button class="nextMonthBtn" @click="onNextMonth">
              <b-icon
                icon="chevron-right"
                variant="dark"
                style="vertical-align: middle"
              ></b-icon>
            </button>
          </div>
        </div>
        <div class="tabs" style="width: 100%">
          <b-tabs v-model="tabIndex" content-class="mt-3">
            <b-tab title="예산쓰기"
              ><budgetWrite
                :user="user"
                :period="period"
                :monthStartDate="monthStartDate"
              ></budgetWrite
            ></b-tab>
            <b-tab title="예산 대비 지출"
              ><budgetExpenditure
                :user="user"
                :period="period"
                :monthStartDate="monthStartDate"
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
  name: "Budget",
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
  computed: {},
  watch: {
    tabIndex: {
      handler(newData) {
        this.setPeriod();
      },
    },
  },
  beforeCreate() {},
  created() {
    alert(this.name);
    this.setPeriod();
  },
  mounted() {},
  methods: {
    // 조회 기간 setting
    setPeriod() {
      let today = new Date();

      if (this.tabIndex === 0) {
        // 예산쓰기(월 단위)
        if (this.monthStartDate === "last") {
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
        }
        // 오늘 날짜
        const currentDate = new Date(
          today.getFullYear(),
          today.getMonth(),
          today.getDate()
        );
        // 오늘 날짜가 시작일~종료일 사이로 조회되도록 설정
        if (
          !(currentDate >= this.period.from && currentDate <= this.period.to)
        ) {
          if (parseInt(this.monthStartDate) < 16) {
            this.onPrevMonth();
          } else {
            this.onNextMonth();
          }
        }
      } else if (this.tabIndex === 1) {
        // 예산 대비 지출(연 단위)
        if (this.monthStartDate === "last") {
          // 월시작일이 말일인 경우
          const year = today.getFullYear() - 1;
          const month = 11;
          this.period.from = new Date(year, month, 31);
          this.period.to = this.$moment(this.period.from)
            .add(1, "years")
            .subtract(1, "days")._d;
        } else {
          // 월시작일이 말일이 아닌 경우
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
        if (this.tabIndex === 0) {
          // 예산쓰기(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .endOf("month")
            .subtract(1, "days")._d;
        }
      } else {
        // 월시작일이 말일이 아닌 경우
        const periodFrom = _.cloneDeep(this.period.from);
        this.period.from = new Date(
          periodFrom.getFullYear(),
          periodFrom.getMonth(),
          parseInt(this.monthStartDate)
        );
        if (this.tabIndex === 0) {
          // 예산쓰기(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .subtract(1, "days")._d;
        }
      }
      if (this.tabIndex === 1) {
        // 예산 대비 지출(연 단위)
        this.period.to = this.$moment(this.period.from)
          .add(1, "years")
          .subtract(1, "days")._d;
      }
    },
    onPrevMonth() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        this.period.from = this.$moment(this.period.from)
          .subtract(1, "months")
          .endOf("month")._d;
        if (this.tabIndex === 0) {
          // 예산쓰기(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .endOf("month")
            .subtract(1, "days")._d;
        }
      } else {
        // 월시작일이 말일이 아닌 경우
        this.period.from = this.$moment(this.period.from).subtract(
          1,
          "months"
        )._d;
        if (this.tabIndex === 0) {
          // 예산쓰기(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .subtract(1, "days")._d;
        }
      }
      if (this.tabIndex === 1) {
        // 예산 대비 지출(연 단위)
        this.period.to = this.$moment(this.period.from)
          .add(1, "years")
          .subtract(1, "days")._d;
      }
    },
    onNextMonth() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        this.period.from = this.$moment(this.period.from)
          .add(1, "months")
          .endOf("month")._d;
        if (this.tabIndex === 0) {
          // 예산쓰기(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .endOf("month")
            .subtract(1, "days")._d;
        }
      } else {
        // 월시작일이 말일이 아닌 경우
        this.period.from = this.$moment(this.period.from).add(1, "months")._d;
        if (this.tabIndex === 0) {
          // 예산쓰기(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .subtract(1, "days")._d;
        }
      }
      if (this.tabIndex === 1) {
        // 예산 대비 지출(연 단위)
        this.period.to = this.$moment(this.period.from)
          .add(1, "years")
          .subtract(1, "days")._d;
      }
    },
  },
};
</script>