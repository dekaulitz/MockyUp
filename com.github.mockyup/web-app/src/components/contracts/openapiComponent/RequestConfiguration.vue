<template>
  <div v-bind="$attrs">
    <template v-for="(path,index) in paths" :key="index">
      <div class="holder">
        <h3 class="position-relative mb-0">Request {{ path.operation.summary }}</h3>
      </div>
    <div class="mt-3">
      <h5>Request Configuration</h5>
      {{ path.path }}
      {{ path.httpMethod }}
    </div>
      {{ path }}
    </template>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { Path } from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'

export default defineComponent({
  name: 'RequestConfiguration',
  components: {},
  data () {
    return {
      component: null
    }
  },
  props: {
    paths: { type: Array as PropType<Path[]> }
  }
})
</script>

<style scoped>

</style>
