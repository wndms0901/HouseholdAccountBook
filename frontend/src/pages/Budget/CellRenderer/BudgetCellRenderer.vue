<template>
  <span>{{ value }}</span>
</template>
<script>
import Vue from "vue";
export default Vue.extend({
  //   computed: {
  //     value() {
  //       return this.params.value + "$";
  //     },
  //   },
  data() {
    return {
      value: "",
    };
  },
  //   computed: {
  //       value(){

  //       }
  //   },
  watch: {
    value: {
      handler(newData) {},
    },
  },
  beforeMount() {
    this.value = this.params.value;
  },
});
</script>