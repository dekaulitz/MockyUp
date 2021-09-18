<template>
  <div class="page-container">
    <template v-if="showContractPlaceHolder">
      <place-holder-container/>
    </template>
    {{data}}
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import PlaceHolderContainer from '@/shared/placeholder/PlaceHolderContainer.vue'
import ContractService from '@/plugins/webclient/serice/ContractService'

export default defineComponent({
  name: 'ContractDetail',
  components: {
    PlaceHolderContainer
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
