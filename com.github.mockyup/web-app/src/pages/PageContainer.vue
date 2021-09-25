<template>
  <div class="page-container container-limited limit-container-width">
    <slot></slot>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'PageContainer'
})
</script>

<style scoped>

</style>
