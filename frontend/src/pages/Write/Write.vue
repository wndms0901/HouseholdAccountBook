<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div id="title" style="width: 100%">
          <p>쓰기</p>
        </div>
        <div
          style="width: 100%; position: relative; display: flex; height: 88px"
        >
          <b-button
            variant="outline-info"
            class="p-0"
            style="height: 30px"
            @click="onPrevMonth"
          >
            <b-icon-chevron-left></b-icon-chevron-left>
          </b-button>
          <div id="date_picker_box" style="display: flex">
            <datepicker
              :format="'yyyy.MM.dd'"
              v-model="period.from"
              :language="ko"
              @closed="onCloseStartDate"
            ></datepicker>
            <span>-</span>
            <datepicker
              :format="'yyyy.MM.dd'"
              v-model="period.to"
              :disabledDates="{ to: this.period.from }"
              :language="ko"
            ></datepicker>
          </div>
          <b-button
            variant="outline-info"
            class="p-0"
            style="height: 30px"
            @click="onNextMonth"
          >
            <b-icon-chevron-right></b-icon-chevron-right>
          </b-button>
        </div>
        <div class="tabs" style="width: 100%">
          <b-tabs content-class="mt-3">
            <b-tab title="지출" active
              ><expenditure :user="user" :period="period"></expenditure
            ></b-tab>
            <b-tab title="수입"
              ><income :user="user" :period="period"></income
            ></b-tab>
            <b-tab title="달력"></b-tab>
          </b-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import datepicker from "vuejs-datepicker";
import expenditure from "./Tab/Expenditure.vue";
import income from "./Tab/Income.vue";
import { ko } from "vuejs-datepicker/dist/locale";
export default {
  components: { datepicker, expenditure, income },
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