<template>
  <hello></hello>
</template>

<script lang="ts">

import { defineComponent } from 'vue'
import Hello from '@/components/HelloWorld.vue'

export default defineComponent({
  components: { Hello }
})
</script>
