<template>
  <div class="wrapper">
    <side-bar>
      <sidebar-link to="/mybook/write">
        <b-icon
          icon="pencil-square"
          style="width: 28px; height: 28px; margin-right: 15px"
        ></b-icon>
        <p>쓰기</p>
      </sidebar-link>
      <sidebar-link to="/mybook/report">
        <i class="nc-icon nc-notes"></i>
        <p>보고서</p>
      </sidebar-link>
      <sidebar-link to="/mybook/budget">
        <i class="nc-icon nc-bullet-list-67"></i>
        <p>예산쓰기</p>
      </sidebar-link>
      <div class="nav_month">
        <p>이달의 가계</p>
        <p>{{ this.period.from }} ~ {{ this.period.to }}</p>
        <div class="nav_month_box">
          <ul>
            <div class="nav_month_box_total">
              <li>
                <span>=&ensp;수입 - 지출</span><span>{{ this.total }}</span>
              </li>
            </div>
            <div class="nav_month_box_income">
              <li>
                <span>+&ensp;&nbsp;수입</span><span>{{ this.income }}</span>
              </li>
              <li>
                <span>ㄴ 이달의 수입</span
                ><span>{{ this.incomeAmountString }}</span>
              </li>
              <li>
                <span>ㄴ 전월이월</span
                ><span>{{ this.balanceCarriedForwardString }}</span>
              </li>
            </div>
            <div class="nav_month_box_expenditure">
              <li>
                <span>-&ensp;&ensp;지출</span
                ><span>{{ this.expenditure }}</span>
              </li>
              <li>
                <span>ㄴ 현금지출</span><span>{{ this.cashString }}</span>
              </li>
              <li>
                <span>ㄴ 카드지출</span><span>{{ this.cardString }}</span>
              </li>
            </div>
          </ul>
        </div>
      </div>
    </side-bar>
    <div class="main-panel">
      <top-navbar></top-navbar>
      <dashboard-content
        @click="toggleSidebar"
        @updateStartDate="setPeriod"
        @getIncomeExpenditureDetail="getIncomeExpenditureDetail"
      >
      </dashboard-content>
      <content-footer></content-footer>
    </div>
  </div>
</template>
<style lang="scss">
</style>
<script>
import TopNavbar from "./TopNavbar.vue";
import ContentFooter from "./ContentFooter.vue";
import DashboardContent from "./Content.vue";

export default {
  components: {
    TopNavbar,
    ContentFooter,
    DashboardContent,
  },
  data() {
    return {
      user: this.$store.state.userStore.initialState.user,
      period: {
        from: "",
        to: "",
      },
      incomeAmount: 0,
      balanceCarriedForward: 0,
      cash: 0,
      card: 0,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.userStore.initialState.status.loggedIn;
    },
    clickLogout() {
      return this.$store.state.userStore.initialState.status.clickLogout;
    },
    monthStartDate() {
      return this.$store.state.userStore.initialState.user.userInfo
        .monthStartDate;
    },
    total() {
      const total =
        this.incomeAmount +
        this.balanceCarriedForward -
        (this.cash + this.card);
      return String(total).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    expenditure() {
      const expenditure = this.cash + this.card;
      return String(expenditure).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    income() {
      const income = this.incomeAmount + this.balanceCarriedForward;
      return String(income).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    incomeAmountString() {
      return String(this.incomeAmount).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    balanceCarriedForwardString() {
      return String(this.balanceCarriedForward).replace(
        /\B(?=(\d{3})+(?!\d))/g,
        ","
      );
    },
    cashString() {
      return String(this.cash).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
    cardString() {
      return String(this.card).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
  },
  watch: {
    loggedIn: {
      handler(newData) {
        if (!newData && !this.clickLogout) {
          alert("계정 권한이 유효하지 않습니다.\n다시 로그인 해주세요.");
        }
      },
    },
  },
  mounted() {
    this.setPeriod();
  },
  methods: {
    // 조회 기간 setting
    setPeriod() {
      let today = new Date();
      const monthStartDate = this.monthStartDate;
      let periodFrom = null;
      let periodTo = null;

      if (monthStartDate === "last") {
        // 월시작일이 말일인 경우
        const month = today.getMonth() - 1;
        const startLastDate = this.$moment(
          new Date(today.getFullYear(), month, 1)
        ).endOf("month")._d;
        const endLastDate = this.$moment(
          new Date(today.getFullYear(), month + 1, 1)
        ).endOf("month")._d;

        periodFrom = new Date(
          today.getFullYear(),
          month,
          startLastDate.getDate()
        );
        periodTo = new Date(
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
        periodFrom = new Date(today.getFullYear(), month, monthStartDate);
        periodTo = new Date(today.getFullYear(), month + 1, monthStartDate - 1);
      }
      this.period.from = this.$moment(periodFrom).format("YYYY.MM.DD");
      this.period.to = this.$moment(periodTo).format("YYYY.MM.DD");
      // 오늘 날짜
      const currentDate = new Date(
        today.getFullYear(),
        today.getMonth(),
        today.getDate()
      );
      // 오늘 날짜가 시작일~종료일 사이로 조회되도록 설정
      if (!(currentDate >= periodFrom && currentDate <= periodTo)) {
        if (parseInt(monthStartDate) < 16) {
          this.onPrevMonth(periodFrom, periodTo);
        } else {
          this.onNextMonth(periodFrom, periodTo);
        }
      }
      this.getIncomeExpenditureDetail();
    },
    onPrevMonth(periodFrom, periodTo) {
      const isPeriodFromLastDay = this.checkMonthLastDay(periodFrom);
      const isPeriodtoLastDay = this.checkMonthLastDay(periodTo);

      if (isPeriodFromLastDay) {
        // 말일인 경우
        this.period.from = this.$moment(periodFrom)
          .subtract(1, "months")
          .endOf("month")
          .format("YYYY.MM.DD");
      } else {
        // 말일이 아닌 경우
        this.period.from = this.$moment(periodFrom)
          .subtract(1, "months")
          .format("YYYY.MM.DD");
      }
      if (isPeriodtoLastDay) {
        this.period.to = this.$moment(periodTo)
          .subtract(1, "months")
          .endOf("month")
          .format("YYYY.MM.DD");
      } else {
        this.period.to = this.$moment(periodTo)
          .subtract(1, "months")
          .format("YYYY.MM.DD");
      }
    },
    onNextMonth(periodFrom, periodTo) {
      const isPeriodFromLastDay = this.checkMonthLastDay(periodFrom);
      const isPeriodtoLastDay = this.checkMonthLastDay(periodTo);

      if (isPeriodFromLastDay) {
        // 말일인 경우
        this.period.from = this.$moment(periodFrom)
          .add(1, "months")
          .endOf("month")
          .format("YYYY.MM.DD");
      } else {
        // 말일이 아닌 경우
        this.period.from = this.$moment(periodFrom)
          .add(1, "months")
          .format("YYYY.MM.DD");
      }
      if (isPeriodtoLastDay) {
        this.period.to = this.$moment(periodTo)
          .add(1, "months")
          .endOf("month")
          .format("YYYY.MM.DD");
      } else {
        this.period.to = this.$moment(periodTo)
          .add(1, "months")
          .format("YYYY.MM.DD");
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
    // 수입/지출 상세 조회
    getIncomeExpenditureDetail() {
      const startDate = this.period.from;
      const endDate = this.period.to;
      const writeRequestDto = {
        startDate: startDate.replace(/\./g, ""),
        endDate: endDate.replace(/\./g, ""),
        userDto: this.user.userInfo,
      };
      this.$store
        .dispatch("writeStore/selectIncomeExpenditureDetail", writeRequestDto)
        .then((res) => {
          this.incomeAmount = res.data.incomeAmount;
          this.balanceCarriedForward = res.data.balanceCarriedForward;
          this.cash = res.data.cash;
          this.card = res.data.card;
        })
        .catch((Error) => {
          console.log(Error);
        });
    },
    toggleSidebar() {
      if (this.$sidebar.showSidebar) {
        this.$sidebar.displaySidebar(false);
      }
    },
  },
};
</script>
<style>
.defaultTab a {
  color: black;
}
.nav_month {
  margin-top: 30px;
  padding: 10px 15px 30px 15px;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
  /*border-bottom: 1px solid rgba(255, 255, 255, 0.2); */
}
.nav_month p:first-child {
  margin-top: 15px;
  text-align: center;
  margin-bottom: 0;
  font-size: 22px;
}
.nav_month p:nth-child(2) {
  text-align: center;
  margin-bottom: 10px;
}
.nav_month_box {
  background: rgba(255, 255, 255, 0.2);
}
.nav_month_box ul {
  padding: 0;
  list-style-type: none;
}
.nav_month_box ul li:first-child span:first-child {
  font-weight: bold;
}
.nav_month_box ul li span:last-child {
  padding-right: 7px;
  float: right;
}
.nav_month_box_total {
  padding: 10px 0px 10px 10px;
  border-bottom: 1px solid black;
}
.nav_month_box_income {
  padding: 10px 0px 10px 10px;
  border-bottom: 1px solid black;
}
.nav_month_box_income li:first-child span:first-child {
  color: #89d7ff;
}
.nav_month_box_expenditure {
  padding: 10px 0px 10px 10px;
}
.nav_month_box_expenditure li:first-child span:first-child {
  color: #09e76c;
}
.date_wrap {
  padding-top: 20px;
  padding-bottom: 10px;
  width: 100%;
  display: flex;
  justify-content: center;
}
.date_picker_box {
  position: relative;
  display: flex;
  width: 50%;
  justify-content: center;
}
.datepickerInput {
  width: 160px;
  border: 1px solid lightgray;
  text-align: center;
  font-size: 1.5em;
  background-color: white;
  cursor: pointer;
  display: inline-block;
}
.datepickerInput_disabled {
  width: 160px;
  border: 1px solid lightgray;
  text-align: center;
  font-size: 1.5em;
  color: black;
  background-color: white;
  display: inline-block;
}
.calendarIconBox {
  width: 40px;
  height: 40px;
  padding: 6px;
  border: 1px solid lightgray;
  background-color: white;
  outline: none;
  cursor: pointer;
}
.calendarIconBox_disabled {
  width: 40px;
  height: 40px;
  padding: 6px;
  border: 1px solid lightgray;
  background-color: white;
  outline: none;
}
.calendarIcon {
  width: 24px;
  height: 24px;
}
.prevMonthBtn {
  width: 20px;
  height: 40px;
  padding: 0;
  border: 1px solid lightgray;
  border-radius: 0;
  background-color: white;
  display: inline-block;
}
.nextMonthBtn {
  width: 20px;
  height: 40px;
  padding: 0px;
  border: 1px solid lightgray;
  border-radius: 0;
  background-color: white;
  display: inline-block;
}
.income_color {
  color: #608cef;
}
.expenditure_color {
  color: #1fab89;
}
.excess_color {
  color: #ff5658;
}
.grid_top {
  display: flex;
  margin-top: 20px;
}
.grid_title {
  margin-bottom: 10px;
  text-align: left;
  font-size: 18px;
  font-weight: 600;
  color: #424242;
}
.excel_btn_box {
  margin-left: auto;
  text-align: right;
}
.excel_btn_box > span {
  margin-left: 5px;
}
.outlineSecondaryBtn {
  margin-right: 5px;
  padding: 2px 8px;
  border: 1px solid #babfc7;
  border-radius: 3px;
  background-color: white;
}
.primaryBtn {
  margin-right: 5px;
  padding: 2px 8px;
  border: 1px solid #1953d7;
  border-radius: 3px;
  background-color: #1953d7;
  color: white;
}
.basicBtn {
  margin-right: 5px;
  padding: 2px 8px;
  border: 1px solid #babfc7;
  border-radius: 3px;
  background-color: white;
}
.basicBtn:hover:enabled {
  border: 1px solid #1953d7;
  background-color: white;
  color: #1953d7;
}
.saveBtn {
  margin-left: 5px;
  padding: 8px 25px;
  border: none;
  border-radius: 3px;
  background-color: #1953d7;
  color: white;
  display: inline-block;
  text-align: center;
  text-decoration: none;
  font-size: 16px;
  font-weight: bold;
}
.calculateBtn {
  margin-left: 5px;
  padding: 8px 25px;
  border: 1px solid #babfc7;
  border-radius: 3px;
  background-color: white;
  display: inline-block;
  text-align: center;
  text-decoration: none;
  font-size: 16px;
  font-weight: bold;
}
.calculateBtn:hover {
  border: 1px solid #1953d7;
  background-color: white;
  color: #1953d7;
}
</style>