<template>
  <div v-bind="$attrs">
    <h4>Server information</h4>
    <ul>
      <li v-for="(serverInfo,index) in serverInfos" :key="index">
        <h6>{{ serverInfo.url }}</h6>
        <p v-html="serverInfo.description"></p>
<!--        {{ serverInfo }}-->
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { OpenApiServer } from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'

export default defineComponent({
  name: 'ServerInformation',
  components: {},
  data () {
    return {
      component: null
    }
  },
  props: {
    serverInfos: { type: Array as PropType<OpenApiServer[]> }
  }
})
</script>

<style scoped>

</style>
