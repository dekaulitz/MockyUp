<template>
  <div v-bind="$attrs">
    <div class="parameters mt-2">
      <label class="label-bold">Parameters</label>
      <div class="parameters">
        <ul>
          <li class="list-unstyled" v-for="(parameter,index) in parameters" :key="index">
            <table class="table table-bordered mt-2">
              <tr class="label-bold">
                <td class="title">Parameter name</td>
                <td>{{ parameter.name }}</td>
              </tr>
              <tr>
                <td class="title">Position In</td>
                <td>{{ parameter.in }}</td>
              </tr>
              <tr>
                <td class="title">Required</td>
                <td>{{ parameter.required }}</td>
              </tr>
              <tr>
                <td class="title">Schema</td>
                <td>
                  <label>Schema</label>
                  <table class="table table-bordered schema">
                    <tr>
                      <td class="title">Type</td>
                      <td>{{ parameter.schema.type }}</td>
                    </tr>
                    <tr>
                      <td class="title">Enum</td>
                      <td>{{ parameter.schema._enum }}</td>
                    </tr>
                    <tr>
                      <td class="title">Format</td>
                      <td>{{ parameter.schema.format }}</td>
                    </tr>
                  </table>

                </td>
              </tr>
              <tr>
                <td class="title">Description</td>
                <td>{{ parameter.description }}</td>
              </tr>
            </table>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { Parameter } from '@/service/webclient/model/openapi/embeddable/OpenApiEmbedded'

export default defineComponent({
  name: 'Parameters',
  components: {},
  data () {
    return {
      component: null
    }
  },
  props: {
    parameters: { type: Array as PropType<Parameter[]> }
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
