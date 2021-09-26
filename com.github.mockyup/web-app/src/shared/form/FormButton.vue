<template>
  <button v-bind="$attrs" class="btn btn-primary" type="submit"
          :disabled="formButtonAttribute.isLoading">
    <template v-if="formButtonAttribute.isLoading"><i class="fas fa-spinner fa-spin"/></template>
    <template v-else>
      <slot></slot>
    </template>
  </button>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import { ButtonAttribute } from '@/shared/form/InputModel'

export default defineComponent({
  props: {
    formButtonAttribute: { type: Object as PropType<ButtonAttribute> }
  }
})
</script>

<style scoped>

</style>
