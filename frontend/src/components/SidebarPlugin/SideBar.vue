<template>
  <div class="sidebar" :style="sidebarStyle" :data-color="backgroundColor">
    <div class="sidebar-wrapper">
      <div class="logo pt-3 pb-3">
        <a
          href="javascript:void(0);"
          class="simple-text logo__container"
          readonly
        >
          <div class="logo-img">
            <img src="../../assets/image/logo.png" alt="logo" />
          </div>
          <span>{{ title }}</span>
        </a>
      </div>

      <slot name="content"></slot>
      <ul class="nav nav-main__links">
        <!--By default vue-router adds an active class to each route link. This way the links are colored when clicked-->
        <slot>
          <sidebar-link
            v-for="(link, index) in sidebarLinks"
            :key="link.name + index"
            :to="link.path"
            @click="closeNavbar"
            :link="link"
          >
            <i :class="link.icon"></i>
            <p>{{ link.name }}</p>
          </sidebar-link>
        </slot>
      </ul>
      <ul class="nav nav-bottom" v-if="$slots['bottom-links']">
        <slot name="bottom-links"></slot>
      </ul>
    </div>
  </div>
</template>
<script>
import SidebarLink from "./SidebarLink.vue";

export default {
  components: {
    SidebarLink,
  },
  props: {
    title: {
      type: String,
      default: "가계부",
    },
    backgroundColor: {
      type: String,
      default: "black",
      validator: (value) => {
        let acceptedValues = [
          "",
          "blue",
          "azure",
          "green",
          "orange",
          "red",
          "purple",
          "black",
        ];
        return acceptedValues.indexOf(value) !== -1;
      },
    },
    activeColor: {
      type: String,
      default: "success",
      validator: (value) => {
        let acceptedValues = [
          "primary",
          "info",
          "success",
          "warning",
          "danger",
        ];
        return acceptedValues.indexOf(value) !== -1;
      },
    },
    sidebarLinks: {
      type: Array,
      default: () => [],
    },
    autoClose: {
      type: Boolean,
      default: true,
    },
  },
  provide() {
    return {
      autoClose: this.autoClose,
    };
  },
  computed: {
    sidebarStyle() {
      return {
        opacity: 0.95,
      };
    },
  },
};
</script>
<style>
.sidebar .sidebar-wrapper {
  display: flex;
  flex-direction: column;
}
.sidebar .nav-main__links {
  flex: 1;
}
.sidebar .sidebar-wrapper .logo .logo__container {
  padding-left: 10px;
}
.logo a:hover {
  text-decoration: none;
  cursor: default;
}
.logo a span {
  font-size: 2.2em;
}
</style>
