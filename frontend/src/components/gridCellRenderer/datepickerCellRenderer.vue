<template>
  <b-form-select v-model="selected" :options="options"></b-form-select>
</template>
<script>
export default {
  data() {
    return {
      selected: null,
      options: [
        { value: null, text: "Please select an option" },
        { value: "01", text: "This is First option" },
        { value: "02", text: "Selected Option" },
      ],
    };
  },
  beforeMount() {
    this.selected = this.params.value;
  },
};
</script>