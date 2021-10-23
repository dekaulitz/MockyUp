<template>
  <page-container>
    <breadcrumb-container class="border-bottom mb-2"
                          :bread-crumb-attributes="breadCrumbAttributes"/>
    <div class="d-flex align-items-center holder">
      <template v-if="data.info">
        <div class="avatar avatar-md flex-shrink-0 me-3">
          <h1>{{ $filters.subString(data.info.title, 0, 1) }}</h1>
        </div>
        <div>
          <h1 class="page-title mb-1 position-relative">{{ data.info.title }}
            <span class="fas fa-lock" v-if="data.private"/>
            <span
              class="position-absolute top-0 ms-4 translate-middle badge rounded-pill bg-danger small">{{
                data.info.version
              }}</span>
          </h1>
          <small class="text-secondary additional-info">Contract ID : {{ data.id }}</small>
        </div>
      </template>
      <div class="ms-auto button-container">
        <router-link
          :to="{name:'SwaggerUI',params:{id:$route.params.id,contractId:$route.params.contractId}}"
          class="btn btn-primary btn-sm"><span
          class="fas fa-fire"></span>Swagger UI
        </router-link>
        <router-link
          :to="{name:'ContractEdit', params:{id:$route.params.id,contractId:$route.params.contractId}}"
          class="btn btn-primary btn-sm"><span class="fas fa-edit"></span> Edit
        </router-link>
      </div>
    </div>
    <div class="mt-3">
      <template v-if="data.info">
        <div class="contract-info">
          <h4>{{ data.info.title }}</h4>
          <small>{{ data.createdDate }}</small>
          <p>{{ data.info.description }}</p>
        </div>
      </template>
      <template v-if="data.servers">
        <server-information :server-infos="data.servers"></server-information>
      </template>
      <template v-if="data.security">
        <h4>Security Configuration</h4>
        {{ data.security }}
      </template>
      <template v-if="data.paths">
        <request-configuration :paths="data.paths" :components="data.components"/>
      </template>
    </div>
  </page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import BaseViewComponent from '@/shared/base/BaseViewComponent'
import { ContractService } from '@/service/webclient/service/ContractService'
import PageContainer from '@/pages/PageContainer.vue'
import { ContractDetail } from '@/service/webclient/model/Contracts'
import ServerInformation from '@/components/contracts/ServerInformation.vue'
import RequestConfiguration from '@/components/contracts/RequestConfigration.vue'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import BreadhCrumbMixins from '@/shared/breadcrumb/BreadhCrumbMixins'

export default defineComponent({
  name: 'ContractsCreate',
  mixins: [BaseViewComponent, BreadhCrumbMixins],
  data () {
    return {
      service: ContractService,
      data: {} as ContractDetail
    }
  },
  mounted () {
    this.getByDetail(this.$route.params.contractId)
    this.breadCrumbAttributes = [
      {
        label: 'Project',
        routerLink: {
          name: 'ProjectsDetail',
          id: this.data.projectId
        },
        isActive: false
      },
      {
        label: 'Contract Detail',
        routerLink: {
          name: 'ContractDetail',
          id: this.data.projectId,
          contractId: this.data.id
        },
        isActive: true
      }
    ]
  },
  components: {
    BreadcrumbContainer,
    RequestConfiguration,
    ServerInformation,
    PageContainer
  }
})
</script>

<style scoped lang="scss">
#code-editor {
  position: relative;
  width: 100%;
  min-height: 720px;
}
</style>
