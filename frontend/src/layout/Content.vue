<template>
  <!-- <transition name="fade" mode="out-in"> -->
  <div class="content">
    <router-view @updateStartDate="updateStartDate"></router-view>
    <div class="loader" v-show="$store.state.loadingStore.LoadingStatus">
      <fade-loader
        :loading="$store.state.loadingStore.LoadingStatus"
        :color="'#3472F7'"
      ></fade-loader>
    </div>
  </div>
  <!-- </transition> -->
</template>
<script>
export default {
  methods: {
    updateStartDate() {
      this.$emit("updateStartDate");
    },
  },
};
</script>
<style>
.loader {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 30;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.1s;
}

.fade-enter,
  .fade-leave-to
    /* .fade-leave-active in <2.1.8 */ {
  opacity: 0;
}
</style>
