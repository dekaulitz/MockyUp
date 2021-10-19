<template>
<page-container>
  <div class="d-flex align-items-center holder">
    <div class="avatar avatar-md flex-shrink-0 me-3">
      <h1>{{$filters.subString(data.projectName,0,1)}}</h1>
    </div>
    <div>
      <h1 class="page-title mb-1">{{data.projectName}}</h1>
      <div class="text-secondary additional-info">Project ID : {{data.id}}</div>
      <span class="text-secondary additional-info fw-bold" v-if="data.projectTags">{{data.projectTags.join(',')}}</span>
    </div>
    <div class="ms-auto button-container">
      <button type="button" class="btn btn-primary btn-sm"><span class="fas fa-plus-circle"></span> New contract
      </button>
      <router-link :to="{name:'ProjectsEdit', params:{id:$route.params.id}}" class="btn btn-primary btn-sm"><span class="fas fa-edit"></span> Edit
      </router-link>
    </div>
  </div>
  <card-container class="mt-3">
    <card-body>
      <h5 class="mb-1">Description:</h5>
      <p v-html="data.projectDescription"></p>
    </card-body>
  </card-container>
</page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { ProjectService } from '@/plugins/webclient/service/CmsService'
import PageContainer from '@/pages/PageContainer.vue'
import { ProjectResponse } from '@/plugins/webclient/model/Projects'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import BaseViewComponent from '@/shared/base/BaseViewComponent'

export default defineComponent({
  name: 'ProjectsDetail',
  components: { CardBody, CardContainer, PageContainer },
  mixins: [BaseViewComponent],
  data () {
    return {
      data: {} as ProjectResponse,
      service: ProjectService
    }
  },
  mounted () {
    this.getByDetail(this.$route.params.id)
  }
})
</script>

<style scoped>

</style>
