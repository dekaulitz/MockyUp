<template>
  <div class="page-container">
    <bradcrumb-container class="border-bottom mb-2"/>
    <slot></slot>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BradcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'

export default defineComponent({
  name: 'PageContainer',
  components: { BradcrumbContainer }
})
</script>

<style scoped>

</style>
