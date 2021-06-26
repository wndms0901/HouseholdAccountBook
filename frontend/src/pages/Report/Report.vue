<template>
  <!-- <div class="content"> -->
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
    </div>
    <div class="tabs" style="width: 100%">
      <b-tabs
        active-nav-item-class="font-weight-bold text-primary"
        content-class="mt-3"
        v-model="tabIndex"
      >
        <b-tab title-item-class="defaultTab" title="월보고서">
          <monthReport :user="user" :period="period"></monthReport>
        </b-tab>
        <b-tab title-item-class="defaultTab" title="연간보고서">
          <yearReport
            :user="user"
            :period="period"
            :monthStartDate="monthStartDate"
          ></yearReport>
        </b-tab>
      </b-tabs>
    </div>
  </div>
  <!-- </div> -->
</template>

<script>
import datepicker from "vuejs-datepicker";
import monthReport from "./Tab/MonthReport.vue";
import yearReport from "./Tab/YearReport.vue";
import { ko } from "vuejs-datepicker/dist/locale";
export default {
  name: "Report",
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
      monthStartDate:
        this.$store.state.userStore.initialState.user.userInfo.monthStartDate,
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
    this.setPeriod();
  },
  mounted() {},
  methods: {
    // 조회 기간 setting
    setPeriod() {
      let today = new Date();

      if (this.tabIndex === 0) {
        // 월보고서(월 단위)
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
        // 연간보고서(연 단위)
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

      ////
      // if (this.monthStartDate === "last") {
      //   // 월시작일이 말일인 경우
      //   if (this.tabIndex === 0) {
      //     // 월보고서(월 단위)
      //     const month = today.getMonth() - 1;
      //     const startLastDate = this.$moment(
      //       new Date(today.getFullYear(), month, 1)
      //     ).endOf("month")._d;
      //     const endLastDate = this.$moment(
      //       new Date(today.getFullYear(), month + 1, 1)
      //     ).endOf("month")._d;

      //     this.period.from = new Date(
      //       today.getFullYear(),
      //       month,
      //       startLastDate.getDate()
      //     );
      //     this.period.to = new Date(
      //       today.getFullYear(),
      //       month + 1,
      //       endLastDate.getDate() - 1
      //     );
      //   } else if (this.tabIndex === 1) {
      //     // 연간보고서(연 단위)
      //     const year = today.getFullYear() - 1;
      //     const month = 11;
      //     this.period.from = new Date(year, month, 31);
      //     this.period.to = this.$moment(this.period.from)
      //       .add(1, "years")
      //       .subtract(1, "days")._d;
      //   }
      // } else {
      //   // 월시작일이 말일이 아닌 경우
      //   if (this.tabIndex === 0) {
      //     // 월보고서(월 단위)
      //     const month =
      //       parseInt(this.monthStartDate) > 15
      //         ? today.getMonth() - 1
      //         : today.getMonth();
      //     this.period.from = new Date(
      //       today.getFullYear(),
      //       month,
      //       parseInt(this.monthStartDate)
      //     );
      //     this.period.to = new Date(
      //       today.getFullYear(),
      //       month + 1,
      //       parseInt(this.monthStartDate) - 1
      //     );
      //   } else if (this.tabIndex === 1) {
      //     // 연간보고서(연 단위)
      //     const year =
      //       parseInt(this.monthStartDate) > 15
      //         ? today.getFullYear() - 1
      //         : today.getFullYear();
      //     const month = parseInt(this.monthStartDate) > 15 ? 11 : 0;
      //     this.period.from = new Date(
      //       year,
      //       month,
      //       parseInt(this.monthStartDate)
      //     );
      //     this.period.to = this.$moment(this.period.from)
      //       .add(1, "years")
      //       .subtract(1, "days")._d;
      //   }
      // }
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
          // 월보고서(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .endOf("month")
            .subtract(1, "days")._d;
        }
        //  else {
        //   // 연간보고서(연 단위)
        //   this.period.to = this.$moment(this.period.from)
        //     .add(1, "years")
        //     .subtract(1, "days")._d;
        // }
      } else {
        // 월시작일이 말일이 아닌 경우
        const periodFrom = _.cloneDeep(this.period.from);
        this.period.from = new Date(
          periodFrom.getFullYear(),
          periodFrom.getMonth(),
          parseInt(this.monthStartDate)
        );
        if (this.tabIndex === 0) {
          // 월보고서(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .subtract(1, "days")._d;
        }
        // else {
        //   // 연간보고서(연 단위)
        //   this.period.to = this.$moment(this.period.from)
        //     .add(1, "years")
        //     .subtract(1, "days")._d;
        // }
      }
      if (this.tabIndex === 1) {
        // 연간보고서(연 단위)
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
          // 월보고서(월 단위)
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
          // 월보고서(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .subtract(1, "days")._d;
        }
      }
      if (this.tabIndex === 1) {
        // 연간보고서(연 단위)
        this.period.to = this.$moment(this.period.from)
          .add(1, "years")
          .subtract(1, "days")._d;
      }
      // const periodFrom = _.cloneDeep(this.period.from);
      // const lastDay = this.$moment(periodFrom).endOf("month")._d;
      // // 말일 체크
      // if (periodFrom.getDate() === lastDay.getDate()) {
      //   this.period.from = this.$moment(this.period.from)
      //     .subtract(1, "months")
      //     .endOf("month")._d;
      // } else {
      //   this.period.from = this.$moment(periodFrom).subtract(1, "months")._d;
      // }
      // period.to setting
      // if (this.tabIndex === 0) {
      //   // 월보고서(월 단위)
      //   this.period.to = this.$moment(this.period.from)
      //     .add(1, "months")
      //     .subtract(1, "days")._d;
      // } else {
      //   // 연간보고서(연 단위)
      //   this.period.to = this.$moment(this.period.from)
      //     .add(1, "years")
      //     .subtract(1, "days")._d;
      // }
    },
    onNextMonth() {
      if (this.monthStartDate === "last") {
        // 월시작일이 말일인 경우
        this.period.from = this.$moment(this.period.from)
          .add(1, "months")
          .endOf("month")._d;
        if (this.tabIndex === 0) {
          // 월보고서(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .endOf("month")
            .subtract(1, "days")._d;
        }
      } else {
        // 월시작일이 말일이 아닌 경우
        this.period.from = this.$moment(this.period.from).add(1, "months")._d;
        if (this.tabIndex === 0) {
          // 월보고서(월 단위)
          this.period.to = this.$moment(this.period.from)
            .add(1, "months")
            .subtract(1, "days")._d;
        }
      }
      if (this.tabIndex === 1) {
        // 연간보고서(연 단위)
        this.period.to = this.$moment(this.period.from)
          .add(1, "years")
          .subtract(1, "days")._d;
      }

      //       const periodFrom = _.cloneDeep(this.period.from);
      //       const lastDay = this.$moment(periodFrom).endOf("month")._d;
      //       // 말일 체크
      //       if (periodFrom.getDate() === lastDay.getDate()) {
      //         this.period.from = this.$moment(this.period.from)
      //           .add(1, "months")
      //           .endOf("month")._d;
      //       } else {
      //         this.period.from = this.$moment(periodFrom).add(1, "months")._d;
      //       }
      //       // period.to setting
      //       if (this.tabIndex === 0) {
      //         // 월보고서(월 단위)
      //         this.period.to = this.$moment(this.period.from)
      //           .add(1, "months")
      //           .subtract(1, "days")._d;
      //       } else {
      //         // 연간보고서(연 단위)
      //         this.period.to = this.$moment(this.period.from)
      //           .add(1, "years")
      //           .subtract(1, "days")._d;
      //       }
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
.report_top {
  border: 1px solid lightgray;
  border-radius: 5px;
  background-color: white;
}
.report_top > table {
  width: 700px;
}
.report_top > table tr th {
  width: 50%;
  text-align: center;
  font-size: 50px;
  font-weight: 500;
  color: #424242;
}
.report_top > table tr td {
  padding-left: 20px;
  font-weight: bold;
  color: #424242;
}
.report_top > table tr th h1 {
  margin: 10px 20px;
}
</style>