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
                    <schema :schema="content.schema" :components="components"/>
                  </div>
                </li>
              </ul>
            </template>
          </div>
          <div class="responses" v-if="path.operation.responses">
            <label class="label-bold">Responses</label>
            <div class="response-status">
              <ul>
                <li v-for="(response,index) in path.operation.responses" :key="index">
                  <label>Status Code : {{ response.statusCode }}</label>
                  <small class="ms-2">{{ response.description }}</small>
                  <div class="response-headers" v-if="response.headers">
                    <label class="label-bold">Headers</label>
                    <ul>
                      <li v-for="(value,key) in response.headers" :key="key">
                        {{key}}
                        <headers :header="value" :components="components"/>
                      </li>
                    </ul>
                  </div>
                  <div class="response-content">
                    <ul>
                      <li v-for="(content,index) in response.content" :key="index">
                        <label>Content Type : {{ content.contentType }}</label>
                        <div>
                          <schema :schema="content.schema" :components="components"/>
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
import Schema from '@/components/contracts/Schema.vue'
import Headers from '@/components/contracts/Header.vue'

export default defineComponent({
  name: 'RequestConfiguration',
  components: {
    Headers,
    Schema,
    Parameters
  },
  data () {
    return {}
  },
  props: {
    paths: { type: Array as PropType<Path[]> },
    components: { type: Object }
  }
})
</script>

<style lang="scss" scoped>
.summary:first-letter {
  text-transform: capitalize
}
</style>
