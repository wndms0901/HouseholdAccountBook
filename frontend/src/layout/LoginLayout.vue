<template>
  <div>
    <router-view></router-view>
    <LoginFooter id="login_footer" />
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
#login_footer {
  width: 100%;
  height: 10%;
  position: absolute;
  bottom: 0;
  text-align: center;
  background: lightgrey;
  /* border-top: 1px solid #262b35;
  background: #31323a; */
}
</style>
