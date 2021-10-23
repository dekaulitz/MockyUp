<template>
  <div class="page-container">
    <div id="swagger"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import 'swagger-ui/dist/swagger-ui.css'
import SwaggerUI from 'swagger-ui/dist/swagger-ui-es-bundle-core'
import BaseViewComponent from '@/shared/base/BaseViewComponent'
import { ContractService } from '@/service/webclient/service/ContractService'
import { ContractDetailResponse } from '@/service/webclient/model/Contracts'

export default defineComponent({
  name: 'SwaggerUI',
  mixins: [BaseViewComponent],
  components: {
    // PlaceHolderContainer
  },
  data () {
    return {
      spec: {} as ContractDetailResponse,
      contractService: ContractService
    }
  },
  mounted () {
    this.contractService.getById(this.$route.params.contractId)
      .then(value => {
        this.spec = value
        this.renderingSwaggerUI()
      })
      .catch(reason => {
        this.validateResponse(reason)
      })
  },
  methods: {
    renderingSwaggerUI () {
      SwaggerUI({
        dom_id: '#swagger',
        spec: JSON.parse(this.spec.rawSpecs),
        presets: [
          SwaggerUI.presets.apis,
          SwaggerUI.SwaggerUIStandalonePreset
        ]
      })
    }
  }
})
</script>

<style lang="scss">
</style>
