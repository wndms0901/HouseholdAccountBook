<template>
  <input ref="input" class="inputCell" v-model="value" maxlength="255" />
</template>
<script>
import Vue from "vue";
export default Vue.extend({
  data() {
    return {
      value: "",
    };
  },
  created() {},
  beforeMount() {},
  mounted() {
    this.value = this.params.value;
    Vue.nextTick(() => this.$refs.input.focus());
  },
  methods: {
    /* Component Editor Lifecycle methods */
    // the final value to send to the grid, on completion of editing
    getValue() {
      return this.value;
    },
    // Gets called once before editing starts, to give editor a chance to
    // cancel the editing before it even starts.
    isCancelBeforeStart() {
      return false;
    },
    // Gets called once when editing is finished (eg if enter is pressed).
    // If you return true, then the result of the edit will be ignored.
    isCancelAfterEnd() {
      return false;
    },
  },
});
</script>
<style>
.inputCell {
  width: inherit;
  border-style: none;
}
</style>