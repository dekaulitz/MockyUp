<template>
  <project-card-list></project-card-list>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ProjectCardList from '@/components/projects/ProjectCardsList.vue'

export default defineComponent({
  components: { ProjectCardList }
})
</script>
<style scoped lang="scss">
</style>
