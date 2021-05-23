<template>
  <!-- <div class="wrapper"> -->
  <div class="wrapper login_layout">
    <router-view></router-view>
    <LoginFooter class="login_footer" />
  </div>
  <!-- <img alt="Vue logo" src="./assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/> -->
</template>

<script>
import LoginFooter from "./LoginFooter.vue";

//import HelloWorld from './components/HelloWorld.vue'

export default {
  name: "App",
  components: {
    LoginFooter,
    //HelloWorld
  },
  date() {
    return {};
  },
  computed: {},
  mounted() {},
  created() {},
};
</script>

<style>
.login_layout {
  display: flex;
  flex-direction: column;
}
.login_footer {
  bottom: 0;
  /* position: relative; */
  /* width: 100%;
  height: 10%; */
  /* position: absolute;
  bottom: 0; */
  text-align: center;
  background: lightgrey;
  /* border-top: 1px solid #262b35;
  background: #31323a; */
}
</style>
