<template>
  <div class="page-container">
    <template v-if="showProjectPlaceHolder">
      <place-holder-container/>
    </template>
    <div class="holder border-bottom-0" v-if="!showProjectPlaceHolder">
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
          #{{tag}}
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
import ProjectService from '@/plugins/webclient/serice/ProjectService'
import {
  ContractCardInterface,
  ProjectInterface
} from '@/plugins/webclient/model/ResponseModel'
import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import ContractService, { GetContractParam } from '@/plugins/webclient/serice/ContractService'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'
import ContractCard from '@/components/contracts/cards/ContractCard.vue'

export default defineComponent({
  name: 'ProjectDetail',
  components: {
    ContractCard,
    CardBody,
    CardContainer,
    PlaceHolderContainer
  },
  data () {
    return {
      data: {} as ProjectInterface,
      // contractCards: [] as ContractCardInterface,
      contractCards: [] as ContractCardInterface[],
      showProjectPlaceHolder: true,
      showContractPlaceHolder: true
    }
  },
  // components: {
  //   CardBody,
  //   CardContainer
  // },
  // props: {
  //   projectCard: { type: Object as PropType<ProjectCardInterface> }
  // },
  mounted () {
    ProjectService.getProjectById(this.$route.params.id)
      .then(baseResponseProject => {
        const projectDetail = baseResponseProject.data
        this.data = projectDetail
        this.showProjectPlaceHolder = false
        const getContractParam = { projectId: projectDetail.id } as GetContractParam
        ContractService.getContracts(getContractParam)
          .then(baseResponseContract => {
            this.contractCards = baseResponseContract.data
            this.showContractPlaceHolder = false
          })
      })
  },
  methods: {
    popUpCard () {
      alert('berak')
    }
  }
})
</script>

<style scoped lang="scss">
</style>
