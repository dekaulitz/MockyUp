<template>
  <div class="page-container">
    <div id="swagger"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ContractService from '@/plugins/webclient/tmp/serice/ContractService'
import 'swagger-ui/dist/swagger-ui.css'
import SwaggerUI from 'swagger-ui/dist/swagger-ui-es-bundle-core'

export default defineComponent({
  name: 'SwaggerComponent',
  components: {
    // PlaceHolderContainer
  },
  data () {
    return {
      data: {},
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
    ContractService.getContractById(this.$route.params.contractId)
      .then(value => {
        this.showContractPlaceHolder = false
        this.data = value.data
        SwaggerUI({
          dom_id: '#swagger',
          spec: JSON.parse(this.data.rawSpecs),
          presets: [
            SwaggerUI.presets.apis,
            SwaggerUI.SwaggerUIStandalonePreset
          ]
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

<style lang="scss">
</style>
