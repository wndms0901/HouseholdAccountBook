<template>
  <div class="wrapper">
    <side-bar>
      <mobile-menu slot="content"></mobile-menu>
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
      <sidebar-link to="/mybook/overview">
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
      <!-- <sidebar-link to="/mybook/maps">
        <i class="nc-icon nc-pin-3"></i>
        <p>Maps</p>
      </sidebar-link> -->
      <sidebar-link to="/mybook/notifications">
        <i class="nc-icon nc-bell-55"></i>
        <p>Notifications</p>
      </sidebar-link>

      <!-- <template slot="bottom-links">
        <sidebar-link class="active"
                      to="/mybook/upgrade">
          <i class="nc-icon nc-alien-33"></i>
          <p>Upgrade to PRO</p>
        </sidebar-link>
      </template> -->
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
  mounted() {
    if (this.loggedIn) {
      this.$router.push("/");
    }
  },
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
.ag-cell {
  cursor: pointer;
}
</style>