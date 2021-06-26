<template>
  <nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
      <h4 class="m-0 font-weight-bold">{{ title }}</h4>
      <!--{{ page.title }} -->
      <!-- <a class="navbar-brand" href="#">Dashboard</a> -->
      <!-- <button
        type="button"
        class="navbar-toggler navbar-toggler-right"
        :class="{ toggled: $sidebar.showSidebar }"
        aria-controls="navigation-index"
        aria-expanded="false"
        aria-label="Toggle navigation"
        @click="toggleSidebar"
      >
        <span class="navbar-toggler-bar burger-lines"></span>
        <span class="navbar-toggler-bar burger-lines"></span>
        <span class="navbar-toggler-bar burger-lines"></span>
      </button> -->
      <div class="collapse navbar-collapse justify-content-end">
        <!-- <ul class="nav navbar-nav mr-auto">
          <li class="nav-item">
            <a class="nav-link" href="#" data-toggle="dropdown">
              <i class="nc-icon nc-palette"></i>
            </a>
          </li>
          <base-dropdown tag="li">
            <template slot="title">
              <i class="nc-icon nc-planet"></i>
              <b class="caret"></b>
              <span class="notification">5</span>
            </template>
            <a class="dropdown-item" href="#">Notification 1</a>
            <a class="dropdown-item" href="#">Notification 2</a>
            <a class="dropdown-item" href="#">Notification 3</a>
            <a class="dropdown-item" href="#">Notification 4</a>
            <a class="dropdown-item" href="#">Another notification</a>
          </base-dropdown>
          <li class="nav-item">
            <a href="#" class="nav-link">
              <i class="nc-icon nc-zoom-split"></i>
              <span class="d-lg-block">&nbsp;Search</span>
            </a>
          </li>
        </ul> -->
        <div class="topNavBarBox">
          <ul class="navbar-nav ml-auto">
            <!-- <base-dropdown title="Dropdown">
            <a class="dropdown-item" href="/mybook/overview">Action</a>
            <a class="dropdown-item" href="#">Another action</a>
            <a class="dropdown-item" href="#">Something</a>
            <a class="dropdown-item" href="#">Another action</a>
            <a class="dropdown-item" href="#">Something</a>
            <div class="divider"></div>
            <a class="dropdown-item" href="#">Separated link</a>
          </base-dropdown> -->
            <li class="nav-item">
              <!-- <b-button id="top_btn" variant="outline-primary" class="m-0"> -->
            </li>
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
              <!-- <a class="nav-link" href="" @click="logout"
              ><i class="nc-icon nc-button-power pr-1"></i>로그아웃</a
            > -->
              <!-- <b-button
              id="top_btn"
              variant="outline-secondary"
              class="m-0"
              @click="logout"
            >
            </b-button>-->
              <!-- <a href="/user/login" class="nav-link"> Log out </a> -->
            </li>
          </ul>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
export default {
  computed: {
    // routeName() {
    //   const { name } = this.$route;
    //   return this.capitalizeFirstLetter(name);
    // },
  },
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
    logout() {
      // 쓰기, 예산쓰기 페이지가 아닌 경우
      const pages = ["Write", "Budget"];
      if (!pages.includes(this.path)) {
        this.$emit("logout", true);
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

