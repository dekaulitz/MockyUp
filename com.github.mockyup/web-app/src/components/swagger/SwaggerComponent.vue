<template>
  <div class="page-container">
    <div id="swagger"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
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
  props: {
    spec: {
      type: String,
      default: ''
    }
  },
  mounted () {
    SwaggerUI({
      dom_id: '#swagger',
      spec: JSON.parse(this.spec),
      presets: [
        SwaggerUI.presets.apis,
        SwaggerUI.SwaggerUIStandalonePreset
      ]
    })
  },
  watch: {
    spec () {
      console.log(this.spec)
      SwaggerUI({
        dom_id: '#swagger',
        spec: JSON.parse(this.spec),
        presets: [
          SwaggerUI.presets.apis,
          SwaggerUI.SwaggerUIStandalonePreset
        ]
      })
    }
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
