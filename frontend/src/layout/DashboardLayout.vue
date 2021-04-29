<template>
  <div class="wrapper">
    <side-bar>
      <!-- <mobile-menu slot="content"></mobile-menu> -->
      <sidebar-link to="/mybook/write">
        <!-- <i class="nc-icon nc-chart-pie-35"></i> -->
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
      <!-- <sidebar-link to="/mybook/overview">
        <i class="nc-icon nc-chart-pie-35"></i>
        <p>Dashboard</p>
      </sidebar-link>
      <sidebar-link to="/mybook/user">
        <i class="nc-icon nc-circle-09"></i>
        <p>User Profile</p>
      </sidebar-link>
      <sidebar-link to="/mybook/table-list">
        <i class="nc-icon nc-notes"></i>
        <p>Table list</p>
      </sidebar-link>
      <sidebar-link to="/mybook/typography">
        <i class="nc-icon nc-paper-2"></i>
        <p>Typography</p>
      </sidebar-link>
      <sidebar-link to="/mybook/icons">
        <i class="nc-icon nc-atom"></i>
        <p>Icons</p>
      </sidebar-link>
      <sidebar-link to="/mybook/maps">
        <i class="nc-icon nc-pin-3"></i>
        <p>Maps</p>
      </sidebar-link>
      <sidebar-link to="/mybook/notifications">
        <i class="nc-icon nc-bell-55"></i>
        <p>Notifications</p>
      </sidebar-link> -->

      <!-- <template slot="bottom-links">
        <sidebar-link class="active" to="/mybook/upgrade">
          <i class="nc-icon nc-alien-33"></i>
          <p>Upgrade to PRO</p>
        </sidebar-link>
      </template> -->
      <div class="nav_month">
        <p>이달의 가계</p>
        <p>2021.03.31 ~ 2021.04.29</p>
        <div class="nav_month_box">
          <ul>
            <div class="nav_month_box_total">
              <li><span>= 수입 - 지출</span><span>10,000</span></li>
            </div>
            <div class="nav_month_box_income">
              <li><span>+ 수입</span><span>10,000</span></li>
              <li><span>ㄴ 이달의 수입</span><span>10,000</span></li>
              <li><span>ㄴ 전월이월</span><span>0</span></li>
            </div>
            <div class="nav_month_box_expenditure">
              <li><span>- 지출</span><span>10,000</span></li>
              <li><span>ㄴ 현금지출</span><span>10,000</span></li>
              <li><span>ㄴ 카드지출</span><span>0</span></li>
            </div>
          </ul>
        </div>
      </div>
    </side-bar>
    <div class="main-panel">
      <top-navbar></top-navbar>

      <dashboard-content @click="toggleSidebar"> </dashboard-content>

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
import MobileMenu from "./MobileMenu.vue";

export default {
  components: {
    TopNavbar,
    ContentFooter,
    DashboardContent,
    MobileMenu,
  },
  computed: {
    loggedIn() {
      return this.$store.state.userStore.initialState.status.loggedIn;
    },
  },
  mounted() {},
  methods: {
    toggleSidebar() {
      if (this.$sidebar.showSidebar) {
        this.$sidebar.displaySidebar(false);
      }
    },
  },
};
</script>
<style>
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
.nav_month_box_total {
  padding: 10px 0px 10px 10px;
  border-bottom: 1px solid black;
}
.nav_month_box_income {
  padding: 10px 0px 10px 10px;
  border-bottom: 1px solid black;
}
.nav_month_box_expenditure {
  padding: 10px 0px 10px 10px;
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
