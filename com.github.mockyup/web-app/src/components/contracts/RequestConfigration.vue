<template>
  <div v-bind="$attrs">
    <h4>Request Configuration</h4>
    <div class="list-group">
      <div class="list-group-item" v-for="(path,index) in paths" :key="index">
        <div class="path-info holder">
          <h6 class="page-title mb-0">{{ path.path }} <small>{{ path.operation.tags }}</small>
            <small class="text-secondary ms-2">{{ path.httpMethod }}</small></h6>
          <label class="text-secondary">Operation ID : {{path.operation.operationId}}</label>

        </div>
        <label class="label-bold">Description</label>
        <p class="mt-2" v-html="path.operation.description"></p>
        <div>
          <parameters :parameters="path.operation.parameters" v-if="path.operation.parameters"/>
          <div class="request-body mt-2">
            <template v-if="path.operation.requestBody">
              <label class="label-bold">Request Body</label>
              <p v-html="path.operation.requestBody.description"></p>
              <ul>
                <li v-for="(content,index) in path.operation.requestBody.content" :key="index">
                  <label>Content Type: {{ content.contentType }}</label>
                  <div class="requestBody">
                    {{ content.schema }}
                  </div>
                </li>
              </ul>
            </template>
          </div>
          <div class="responses">
            <label class="label-bold">Responses</label>
            <div class="response-status">
              <ul>
                <li v-for="(response,index) in path.operation.responses" :key="index">
                  <label>Status Code : {{ response.statusCode }}</label>
                  <small class="ms-2">{{ response.description }}</small>
                  <div class="response-headers">
                    {{ response.headers }}
                  </div>
                  <div class="response-content">
                    <ul>
                      <li v-for="(content,index) in response.content" :key="index">
                        <label>Content Type : {{ content.contentType }}</label>
                        <div>
                          {{ content.schema }}
                        </div>
                      </li>
                    </ul>
                  </div>
                  <div>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { Path } from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'
import Parameters from '@/components/contracts/Parameters.vue'

export default defineComponent({
  name: 'RequestConfiguration',
  components: { Parameters },
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

<style lang="scss" scoped>
.summary:first-letter {
  text-transform: capitalize
}
</style>
