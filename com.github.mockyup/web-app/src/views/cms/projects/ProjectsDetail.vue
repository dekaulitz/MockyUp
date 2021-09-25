<template>
<page-container>
  <div class="d-flex align-items-center holder">
    <div class="avatar avatar-md flex-shrink-0 me-3">
      <h1>{{$filters.subString(value.projectName,0,1)}}</h1>
    </div>
    <div>
      <h1 class="page-title mb-1">{{value.projectName}}</h1>
      <div class="text-secondary additional-info">Project ID : {{value.id}}</div>
      <span class="text-secondary additional-info fw-bold" v-if="value.projectTags">{{value.projectTags.join(',')}}</span>
    </div>
  </div>
  <card-container class="mt-3">
    <card-body>
      <h5 class="mb-1">Description:</h5>
      <p v-html="value.projectDescription"></p>
    </card-body>
  </card-container>
</page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BaseComponent from '@/components/base/BaseComponent'
import { ProjectService } from '@/plugins/webclient/service/CmsService'
import PageContainer from '@/pages/PageContainer.vue'
import { ProjectResponse } from '@/plugins/webclient/model/Projects'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'

export default defineComponent({
  name: 'ProjectsDetail',
  components: { CardBody, CardContainer, PageContainer },
  mixins: [BaseComponent],
  data () {
    return {
      value: {} as ProjectResponse
    }
  },
  beforeMount () {
    this.service = ProjectService
  },
  mounted () {
    this.getByDetail(this.$route.params.id)
  }
})
</script>

<style scoped>

</style>
