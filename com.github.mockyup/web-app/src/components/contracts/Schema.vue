<template>
  <div v-bind="$attrs">
    <div class="schema">
      <label class="label-bold">Schema</label>
      <card-container>
        <card-body>
         <pre>{{ getSchemaRef(schema)}}</pre>
        </card-body>
      </card-container>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { Schema } from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'
import CardContainer from '@/shared/card/CardContainer.vue'
import CardBody from '@/shared/card/CardBody.vue'

import JsonRefs from 'json-refs'
import { OpenApiHelper } from '@/service/helper/OpenApiHelper'

export default defineComponent({
  name: 'Schema',
  components: {
    CardBody,
    CardContainer
  },
  data () {
    return {
    }
  },
  props: {
    schema: { type: Object as PropType<Schema> },
    components: { type: Object }
  },
  methods: {
    getSchemaRef (schema:any): unknown {
      if (schema.$ref === null || schema.$ref === undefined) {
        return JSON.stringify(OpenApiHelper.cleansingNullAttributes(schema, this.components), null, '\t')
      }
      const schemaComponent = OpenApiHelper.getSchemaFromName(schema.$ref, this.components)
      if (schemaComponent == null) {
        return null
      }
      return JSON.stringify(OpenApiHelper.cleansingNullAttributes(schemaComponent, this.components), null, '\t')
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
