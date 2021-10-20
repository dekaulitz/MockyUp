<template>
  <div class="page-container container-limited limit-container-width">
    <place-holder-container v-if="showContractPlaceHolder"/>
    <template v-if="!showContractPlaceHolder">
      <div class="holder">
        <div class="d-flex align-items-center">
          <div class="avatar avatar-md flex-shrink-0">
            <span>{{ $filters.subString(data.info.title, 0, 1).toUpperCase() }}</span>
          </div>
          <div class="flex-row ms-2 project-card-description w-50">
            <div class="page-title">{{ data.info.title }} <span class="fas fa-lock ms-1"
                                                                v-if="data.private"/></div>
            <div class="text-secondary"><span
              v-if="data.info.contact.name">{{ data.info.contact.name }}/</span>{{
                data.info.contact.email
              }}
            </div>
            <div class="text-secondary">Last update {{ data.updatedDate }}</div>
          </div>
        </div>
      </div>
      <div class="page-body">
        <div class="row mt-3">
          <div class="col-4">
            <div class="list-group">
              <button class="list-group-item text-start" aria-current="true"
                      :class="{'active':showingContractInfo}" @click="displayContractInfo">Getting
                started
              </button>
              <button class="list-group-item  text-start"
                      :class="{'active':showingRequestReference}" @click="displayRequestInfo">
                Request Reference
              </button>
            </div>
          </div>
          <div class="col-8">
            <contract-info :contract-project-info="data.info" :server-info="data.servers"
                           v-if="showingContractInfo"/>
            <request-configuration :paths="data.paths" v-if="showingRequestReference"/>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { ContractDetail } from '@/service/webclient/model/openapi/ContractModel'
import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import ContractInfo from '@/components/contracts/openapiComponent/ContractInfo.vue'
import RequestConfiguration from '@/components/contracts/openapiComponent/RequestConfiguration.vue'
import { ContractService } from '@/service/webclient/service/ContractService'

export default defineComponent({
  name: 'ContractDetail',
  components: {
    RequestConfiguration,
    ContractInfo,
    PlaceHolderContainer
  },
  data () {
    return {
      data: {} as ContractDetail,
      showingContractInfo: true,
      showingRequestReference: false,
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
    ContractService.getById(this.$route.params.contractId)
      .then(value => {
        this.showContractPlaceHolder = false
        this.data = value.data
      })
  },
  methods: {
    displayRequestInfo () {
      this.showingContractInfo = false
      this.showingRequestReference = true
    },
    displayContractInfo () {
      this.showingContractInfo = true
      this.showingRequestReference = false
    }
  }
})
</script>

<style lang="scss">
.scrollspy-example {
  position: relative;
}
</style>
