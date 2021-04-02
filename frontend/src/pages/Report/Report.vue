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
              :format="'yyyy.MM.dd'"
              v-model="period.from"
              input-class="datepickerInput"
              minimum-view="month"
              :language="ko"
              @closed="onCloseStartDate"
            ></datepicker>
            <span class="calendarIconBox"
              ><b-icon class="calendarIcon" icon="calendar3"></b-icon></span
            >&ensp; <span style="font-size: 1.5em">-</span>&ensp;
            <datepicker
              :format="'yyyy.MM.dd'"
              v-model="period.to"
              input-class="datepickerInput"
              minimum-view="month"
              :disabledDates="{ to: this.period.from }"
              :language="ko"
            ></datepicker
            ><span class="calendarIconBox"
              ><b-icon class="calendarIcon" icon="calendar3"></b-icon
            ></span>
            &nbsp;<b-button class="nextMonthBtn" @click="onNextMonth">
              <b-icon-chevron-right variant="dark"></b-icon-chevron-right>
            </b-button>
          </div>
        </div>
      </div>
      <div class="tabs" style="width: 100%">
        <b-tabs content-class="mt-3">
          <b-tab title="월보고서" active>
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
  beforeCreate() {},
  created() {
    this.setPeriod();
  },
  mounted() {
    //  this.user = this.$store.state.initialState.user;
  },
  methods: {
    setPeriod() {
      const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      let today = new Date();
      let startDate = new Date(
        today.getFullYear(),
        today.getMonth(),
        monthStartDate
      );
      let endDate = new Date(
        today.getFullYear(),
        today.getMonth() + 1,
        monthStartDate - 1
      );
      this.period.from = startDate;
      this.period.to = endDate;
    },
    onCloseStartDate() {
      this.period.to = this.$moment(this.period.from)
        .add(1, "months")
        .subtract(1, "days")
        .format("YYYY-MM-DD");
      console.log(typeof new Date());
      console.log("this.period.from", typeof this.period.from);
      console.log("this.period.to", typeof this.period.to);
    },
    onPrevMonth() {
      this.period.from = new Date(
        this.$moment(this.period.from).subtract(1, "months").format()
      );
      this.period.to = new Date(
        this.$moment(this.period.to).subtract(1, "months").format()
      );
    },
    onNextMonth() {
      this.period.from = new Date(
        this.$moment(this.period.from).add(1, "months").format()
      );
      this.period.to = new Date(
        this.$moment(this.period.to).add(1, "months").format()
      );
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