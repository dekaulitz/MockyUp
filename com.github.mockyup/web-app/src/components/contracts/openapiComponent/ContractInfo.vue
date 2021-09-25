<template>
  <div v-bind="$attrs">
    <div class="holder">
      <h3 class="position-relative mb-0">{{ contractProjectInfo.title }}
        <span
          class="position-absolute top-0 ms-5 translate-middle badge rounded-pill bg-danger small">{{
            contractProjectInfo.version
          }}</span>
      </h3>
      <template v-if="contractProjectInfo.license">
        <a class="text-secondary"
           :href="contractProjectInfo.license.url">#{{ contractProjectInfo.license.name }}</a>
      </template>
    </div>
    <card-container  class="mt-3">
      <card-body>
        <div class="mt-3">
          <p class="text-break">{{ contractProjectInfo.description }}</p>
          <template v-if="contractProjectInfo.termsOfService">

            <h5>Term and condition</h5>
            <div v-html="contractProjectInfo.termsOfService"></div>
          </template>
          <template v-if="serverInfo">
            <div class="holder">
              <h5>Environment configuration</h5>
            </div>
            <div v-for="(server,index) in serverInfo" :key="index">
              <div class="mt-3">
                <h6 class="fw-bold">Host : {{ server.url }}</h6>
                <p class="text-break">{{ server.description }}</p>
                <template v-if="server.variables">
                  <h6 class="fw-bold">Server properties</h6>
                  <table class="table table-bordered">
                    <thead>
                    <tr>
                      <th scope="col">Name</th>
                      <th scope="col">Value</th>
                      <th scope="col">Environment</th>
                      <th scope="col">Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(value,key) in server.variables" :key="value">
                      <td>{{ key }}</td>
                      <td>{{ value.default }}</td>
                      <td>{{ value.enum }}</td>
                      <td>{{ value.description }}</td>
                    </tr>
                    </tbody>
                  </table>
                </template>
              </div>
            </div>
          </template>
        </div>
      </card-body>
    </card-container>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import {
  ContractProjectInfo,
  OpenApiServer
} from '@/plugins/webclient/model/openapi/embeddable/OpenApiEmbedded'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'

export default defineComponent({
  name: 'ContractInfo',
  components: {
    CardBody,
    CardContainer
  },
  data () {
    return {
      component: null
    }
  },
  props: {
    contractProjectInfo: { type: Object as PropType<ContractProjectInfo> },
    serverInfo: { type: Array as PropType<OpenApiServer[]> }
  }
})
</script>

<style scoped>

</style>
