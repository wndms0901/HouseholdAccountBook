<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div id="title" style="width: 100%">
          <p>쓰기</p>
        </div>
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
              :disabledDates="{ to: this.period.from }"
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
          </div>
          <div>
            <button class="startDateBtn">월시작일</button>
          </div>
        </div>
      </div>
      <div class="tabs" style="width: 100%">
        <b-tabs v-model="tabIndex" content-class="mt-3">
          <b-tab title="지출" @click="onClickExpenditure"
            ><expenditure
              ref="expenditureTab"
              :user="user"
              :period="period"
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
  beforeCreate() {},
  created() {
    this.setPeriod();
  },
  mounted() {
    //  this.user = this.$store.state.initialState.user;
  },
  methods: {
    setPeriod() {
      let today = new Date();
      const monthStartDate = parseInt(this.user.userInfo.monthStartDate);
      const month =
        monthStartDate > 15 ? today.getMonth() - 1 : today.getMonth();
      const startDate = new Date(today.getFullYear(), month, monthStartDate);
      const endDate = new Date(
        today.getFullYear(),
        month + 1,
        monthStartDate - 1
      );
      // let startDate = new Date(
      //   today.getFullYear(),
      //   today.getMonth(),
      //   monthStartDate
      // );
      // let endDate = new Date(
      //   today.getFullYear(),
      //   today.getMonth() + 1,
      //   monthStartDate - 1
      // );
      this.period.from = startDate;
      this.period.to = endDate;
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
      this.period.to = this.$moment(this.period.from)
        .add(1, "months")
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
        .add(1, "months")
        .subtract(1, "days")._d;
    },
    // 지출 tab click
    onClickExpenditure() {
      this.$refs.expenditureTab.getExpenditureList();
    },
    // 수입 tab click
    onClickIncome() {
      this.$refs.incomeTab.getIncomeList();
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
</style>