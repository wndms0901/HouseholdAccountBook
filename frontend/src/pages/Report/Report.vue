<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div id="title" style="width: 100%">
          <p>보고서</p>
        </div>
        <div class="date_wrap">
          <div class="date_picker_box" style="display: flex">
            <b-button class="prevMonthBtn" @click="onPrevMonth">
              <b-icon-chevron-left
                variant="dark"
              ></b-icon-chevron-left></b-button
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
            &nbsp;<b-button class="nextMonthBtn" @click="onNextMonth">
              <b-icon-chevron-right variant="dark"></b-icon-chevron-right>
            </b-button>
          </div>
        </div>
      </div>
      <div class="tabs" style="width: 100%">
        <b-tabs v-model="tabIndex" content-class="mt-3">
          <b-tab title="월보고서">
            <monthReport :user="user" :period="period"></monthReport>
          </b-tab>
          <b-tab title="연간보고서">
            <yearReport :user="user" :period="period"></yearReport>
          </b-tab>
        </b-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import datepicker from "vuejs-datepicker";
import monthReport from "./Tab/MonthReport.vue";
import yearReport from "./Tab/YearReport.vue";
import { ko } from "vuejs-datepicker/dist/locale";
export default {
  components: { datepicker, monthReport, yearReport },
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
    // period: function () {
    //   const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
    //   let today = new Date();
    //   let startDate = new Date(
    //     today.getFullYear(),
    //     today.getMonth(),
    //     monthStartDate
    //   );
    //   let endDate = new Date(
    //     today.getFullYear(),
    //     today.getMonth() + 1,
    //     monthStartDate - 1
    //   );
    //   const period = { from: startDate, to: endDate };
    //   return period;
    // },
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
  mounted() {
    //  this.user = this.$store.state.initialState.user;
  },
  methods: {
    // 조회 기간 setting
    setPeriod() {
      const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      // const monthStartDate = 13;
      let today = new Date();
      let startDate = new Date(
        today.getFullYear(),
        today.getMonth(),
        monthStartDate
      );
      let endDate = null;

      if (this.tabIndex === 0) {
        // 월보고서(월 단위)
        endDate = new Date(
          today.getFullYear(),
          today.getMonth() + 1,
          monthStartDate - 1
        );
      } else {
        // 연간보고서(연 단위)
        endDate = new Date(
          today.getFullYear() + 1,
          today.getMonth(),
          monthStartDate - 1
        );
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
      const periodTo = this.$moment(this.period.from)
        .add(1, "months")
        .subtract(1, "days")._d;
      this.period.to = periodTo;
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
      if (this.tabIndex === 0) {
        // 월보고서(월 단위)
        this.period.to = this.$moment(this.period.from)
          .add(1, "months")
          .subtract(1, "days")._d;
      } else {
        // 연간보고서(연 단위)
        this.period.to = this.$moment(this.period.from)
          .add(1, "years")
          .subtract(1, "days")._d;
      }
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
      if (this.tabIndex === 0) {
        // 월보고서(월 단위)
        this.period.to = this.$moment(this.period.from)
          .add(1, "months")
          .subtract(1, "days")._d;
      } else {
        // 연간보고서(연 단위)
        this.period.to = this.$moment(this.period.from)
          .add(1, "years")
          .subtract(1, "days")._d;
      }
    },
  },
};
</script>

<style>
.left_btn {
  float: left;
}
.right_btn {
  float: right;
}
</style>