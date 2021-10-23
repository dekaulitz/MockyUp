<template>
  <div v-bind="$attrs">
    <div class="parameters mt-2">
      <label class="label-bold">Parameters</label>
      <div class="parameters">
        <ul>
          <li class="list-unstyled" v-for="(parameter,index) in parameters" :key="index">
            <pre>{{ getSchemaRef(parameter)}}</pre>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { Parameter } from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'
import { OpenApiHelper } from '@/service/helper/OpenApiHelper'

export default defineComponent({
  name: 'Parameters',
  components: {},
  data () {
    return {
      component: null
    }
  },
  props: {
    parameters: { type: Array as PropType<Parameter[]> },
    components: { type: Object }
  },
  methods: {
    getSchemaRef (schema:unknown): unknown {
      return JSON.stringify(OpenApiHelper.cleansingNullAttributes(schema, this.components), null, '\t')
    }
  }
})
</script>

<style lang="scss" scoped>
.parameters {
  table {
    td.title {
      width: 200px;
    }
  }

  table.schema {
    margin-bottom: 0px;
    tr {
      &:first-child {
        border-top: transparent;
      }

      &:last-child {
        border-bottom: transparent;
      }
    }
    td.title {
      width: 150px;
    }
  }
}
</style>
