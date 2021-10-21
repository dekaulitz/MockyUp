<template>
  <card-container class="project-card">
    <card-body class="d-flex align-items-center">
      <div class="avatar avatar-sm flex-shrink-0">
        <span>{{ projectCard.projectName.substr(0, 1).toUpperCase() }}</span>
      </div>
      <div class="flex-row ms-2 project-card-description">
        <div>
          <router-link :to="'/project-detail/'+projectCard.id" class="card-title">
            {{ projectCard.projectName }}
          </router-link>
        </div>
        <small class="card-subtitle">Project ID {{ projectCard.id }}</small>
      </div>
      <div class="justify-content-start">
        <span class="fw-normal">{{ projectCard.projectTags.join(', ') }}</span>
      </div>
      <div class="ms-auto">
        Last update {{ $filters.localDate(projectCard.updatedDate) }}
      </div>
    </card-body>
  </card-container>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import { ProjectCardInterface } from '@/service/webclient/model/ResponseModel'

export default defineComponent({
  name: 'ProjectCard',
  components: {
    CardBody,
    CardContainer
  },
  props: {
    projectCard: { type: Object as PropType<ProjectCardInterface> }
  }
})
</script>

<style scoped lang="scss">
</style>
