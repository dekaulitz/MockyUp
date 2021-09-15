<template>
  <label v-bind="$attrs" class="form-label">
    <slot></slot>
  </label>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({})
</script>

<style scoped>

</style>
