<template>
<page-container>
  <breadcrumb-container class="border-bottom mb-2"
                        :bread-crumb-attributes="breadCrumbAttributes"/>
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
      <router-link :to="{name:'ContractsCreate'}" class="btn btn-primary btn-sm"><span class="fas fa-plus-circle"></span> New contract
      </router-link>
      <router-link :to="{name:'ProjectsEdit', params:{id:$route.params.id}}" class="btn btn-primary btn-sm"><span class="fas fa-edit"></span> Edit
      </router-link>
    </div>
  </div>
  <h5 class="mb-1">Description:</h5>
  <p v-html="data.projectDescription"></p>
  <div v-if="!showContractPlaceHolder">
    <h5 class="page-title holder mb-2">Contract list</h5>
    <div v-for="(contract, index) in contractCards" :key="index">
      <contract-card :contract-card="contract" class="mt-2 radius-none"/>
    </div>
  </div>
</page-container>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { ProjectService } from '@/service/webclient/service/ProjectService'
import PageContainer from '@/pages/PageContainer.vue'
import { ProjectResponse } from '@/service/webclient/model/Projects'
import BaseViewComponent from '@/shared/base/BaseViewComponent'
import { ContractCardInterface } from '@/service/webclient/model/ResponseModel'
import { ContractService } from '@/service/webclient/service/ContractService'
import { GetContractParam } from '@/service/webclient/model/Contracts'
import ContractCard from '@/components/cards/ContractCard.vue'
import BreadcrumbContainer from '@/shared/breadcrumb/BreadCrumbContainer.vue'
import BreadhCrumbMixins from '@/shared/breadcrumb/BreadhCrumbMixins'

export default defineComponent({
  name: 'ProjectsDetail',
  components: { BreadcrumbContainer, ContractCard, PageContainer },
  mixins: [BaseViewComponent, BreadhCrumbMixins],
  data () {
    return {
      data: {} as ProjectResponse,
      service: ProjectService,
      showContractPlaceHolder: true,
      contractCards: [] as ContractCardInterface[],
      contractService: ContractService,
      getContractParam: {
        page: 1,
        size: 10,
        sort: 'id:desc',
        projectId: this.$route.params.id
      } as GetContractParam
    }
  },
  mounted () {
    this.service.getById(this.$route.params.id)
      .then(value => {
        this.data = value
        this.showPlaceHolder = false
        this.breadCrumbAttributes = [
          {
            label: 'Projects',
            routerLink: {
              name: 'Projects'
            },
            isActive: false
          },
          {
            label: 'Project Detail',
            routerLink: {
              name: 'ProjectsDetail',
              id: this.data.id
            },
            isActive: false
          }
        ]
        this.contractService.getAll(this.getContractParam)
          .then(contractRes => {
            this.contractCards = contractRes
            this.showContractPlaceHolder = false
          })
      }).catch(reason => {
        this.validateResponse(reason)
      })
  }
})
</script>

<style scoped>

</style>
