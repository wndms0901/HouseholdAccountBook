<template>
  <nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
      <h4 class="m-0 font-weight-bold">{{ title }}</h4>
      <div class="collapse navbar-collapse justify-content-end">
        <div class="topNavBarBox">
          <ul class="navbar-nav ml-auto">
            <li class="nav-item"></li>
            <li class="nav-item">
              <router-link class="text-decoration-none" to="/mybook/myInfo"
                ><i class="nc-icon nc-single-02 pr-1"></i
                ><span>내정보</span></router-link
              >
            </li>
            <li class="nav-item">
              <router-link
                class="text-decoration-none"
                to="/user/login"
                @click.native="logout"
                ><i class="nc-icon nc-button-power pr-1"></i
                ><span>로그아웃</span></router-link
              >
            </li>
          </ul>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
export default {
  computed: {},
  watch: {
    $route(to, from) {
      const pageInfo = to.matched;
      this.title = pageInfo[pageInfo.length - 1].alias[0];
      this.path = pageInfo[pageInfo.length - 1].name;
    },
  },
  data() {
    return {
      activeNotifications: false,
      title:
        this.$router.currentRoute.matched[
          this.$router.currentRoute.matched.length - 1
        ].alias[0],
      path: this.$router.currentRoute.name,
    };
  },
  mounted() {},
  methods: {
    // logout button click
    logout() {
      const clickLogout =
        this.$store.state.userStore.initialState.status.clickLogout;
      if (clickLogout) {
        this.$store.dispatch("userStore/logout");
      }
    },
    capitalizeFirstLetter(string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
    },
    toggleNotificationDropDown() {
      this.activeNotifications = !this.activeNotifications;
    },
    closeDropDown() {
      this.activeNotifications = false;
    },
    toggleSidebar() {
      this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
    },
    hideSidebar() {
      this.$sidebar.displaySidebar(false);
    },
  },
};
</script>
<style scoped>
.topNavBarBox ul li {
  margin-right: 15px;
}
.topNavBarBox ul li i {
  vertical-align: middle;
  color: #888;
}
.topNavBarBox ul li span {
  color: #888;
}
</style>>

