<template>
  <input v-bind="$attrs" class="form-control input-short" type="text" placeholder="Search something?" aria-label="input search"
         @input="$emit('update:modelValue', $event.target.value)">
</template>
<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({

})
</script>

<style scoped>

</style>
