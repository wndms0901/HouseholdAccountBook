<template>
  <div class="wrapper login_layout">
    <router-view></router-view>
    <div class="loader" v-show="$store.state.loadingStore.LoadingStatus">
      <fade-loader
        :loading="$store.state.loadingStore.LoadingStatus"
        :color="'#3472F7'"
      ></fade-loader>
    </div>
    <LoginFooter />
  </div>
</template>

<script>
import LoginFooter from "./LoginFooter.vue";

export default {
  name: "App",
  components: {
    LoginFooter,
  },
  date() {
    return {};
  },
  computed: {},
  created() {},
  mounted() {},
};
</script>

<style>
.login_layout {
  flex-direction: column;
}
.login_footer {
  padding: 20px;
  height: 60px;
  bottom: 0;
  text-align: center;
  vertical-align: bottom;
  opacity: 0.75;
  background: #000;
  color: #fff;
}

.login_footer img {
  position: relative;
  bottom: 8px;
  float: right;
  background: #fff;
  border-radius: 20px;
}
</style>
