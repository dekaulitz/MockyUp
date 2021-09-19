<template>
  <button v-bind="$attrs" class="dropdown-toggle" type="button"
          data-bs-toggle="dropdown" aria-expanded="false">
    <slot></slot>
  </button>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({})
</script>

<style scoped>

</style>
