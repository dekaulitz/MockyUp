<template>
  <ul v-bind="$attrs" class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
  <slot></slot>
  </ul>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({})
</script>

<style scoped>

</style>
