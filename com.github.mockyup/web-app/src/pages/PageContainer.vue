<template>
  <div class="page-container">
<!--    <breadcrumb-container class="border-bottom mb-2"/>-->
    <slot></slot>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
// import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'

export default defineComponent({
  name: 'PageContainer'
  // components: { BreadcrumbContainer }
})
</script>

<style scoped>

</style>
