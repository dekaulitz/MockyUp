<template>
  <a v-bind="$attrs" class="dropdown-item ">
    <slot></slot>
  </a>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({})
</script>

<style scoped>

</style>
