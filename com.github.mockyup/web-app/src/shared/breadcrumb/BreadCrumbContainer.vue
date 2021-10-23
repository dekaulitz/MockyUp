<template>
  <nav v-bind="$attrs" aria-label="breadcrumb">
    <ol class="breadcrumb">
      <li class="breadcrumb-item">
        <router-link to="/">Home</router-link>
      </li>
      <li class="breadcrumb-item" v-for="(breadcrumb,index) in breadCrumbAttributes" :class="{ 'active': breadcrumb.isActive }"
          :aria-current="breadcrumb.label" :key="index">
        <router-link :to="breadcrumb.routerLink">{{ breadcrumb.label }}</router-link>
      </li>
    </ol>
  </nav>
</template>
<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { BreadCrumbAttribute } from '@/shared/breadcrumb/BreadhCrumbMixins'

export default defineComponent({
  name: 'BreadcrumbContainer',
  props: {
    breadCrumbAttributes: { type: Array as PropType<BreadCrumbAttribute[]> }
  }
})
</script>

<style scoped>

</style>
