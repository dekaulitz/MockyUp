<template>
  <div class="page-container">
    <template v-if="showPlaceHolder">
      <place-holder-container/>
    </template>
    <div class="holder border-bottom-0" v-if="!showPlaceHolder">
      <div class="d-flex align-items-center">
        <div class="avatar avatar-md flex-shrink-0">
          <span>{{ $filters.subString(data.projectName, 0, 1).toUpperCase() }}</span>
        </div>
        <div class="flex-row ms-2 project-card-description w-50">
          <div class="page-title">{{ data.projectName }}</div>
          <div class="text-secondary">Last update {{ data.updatedDate }}</div>
        </div>
      </div>
      <div class="text-secondary mt-1">
        <span v-for="(tag,index) in data.projectTags" :key="index" class="fw-bold">
          #{{ tag }}
        </span>
      </div>
    </div>
    <div class="page-body mt-3 mb-3">
      <card-container class="mb-3">
        <card-body>
          <div class="text-secondary mb-3">{{ data.projectDescription }}</div>
        </card-body>
      </card-container>
      <template v-if="showContractPlaceHolder">
        <place-holder-container/>
      </template>
      <div v-if="!showContractPlaceHolder">
        <div class="page-title border-bottom mb-2">Contract list</div>
        <div v-for="(contract, index) in contractCards" :key="index">
          <contract-card :contract-card="contract"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { ContractCardInterface } from '@/service/webclient/model/ResponseModel'
import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import ContractCard from '@/components/cards/ContractCard.vue'
import BaseViewComponent from '@/shared/base/BaseViewComponent'
import { ProjectService } from '@/service/webclient/service/ProjectService'
import { ContractService } from '@/service/webclient/service/ContractService'
import { ProjectResponse } from '@/service/webclient/model/Projects'

export default defineComponent({
  name: 'ProjectDetail',
  mixins: [BaseViewComponent],
  components: {
    ContractCard,
    CardBody,
    CardContainer,
    PlaceHolderContainer
  },
  data () {
    return {
      contractCards: [] as ContractCardInterface[],
      showProjectPlaceHolder: true,
      showContractPlaceHolder: true,
      service: ProjectService,
      data: {} as ProjectResponse,
      contractService: ContractService
    }
  },
  mounted () {
    this.service.getById(this.$route.params.id)
      .then(value => {
        this.data = value
        this.showPlaceHolder = false
        this.contractService.getAll()
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

<style scoped lang="scss">
</style>
