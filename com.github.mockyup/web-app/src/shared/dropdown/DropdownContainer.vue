<template>
  <div v-bind="$attrs" class="dropdown">
    <slot></slot>
<!--    <button class="btn btn-outline-secondary dropdown-toggle" type="button"-->
<!--            id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">-->
<!--      Last Update-->
<!--    </button>-->
<!--    <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">-->
<!--      <li><a class="dropdown-item disabled">Sorty By</a></li>-->
<!--      <li><a class="dropdown-item" href="#">Action</a></li>-->
<!--      <li><a class="dropdown-item" href="#">Another action</a></li>-->
<!--      <li><a class="dropdown-item" href="#">Something else here</a></li>-->
<!--    </ul>-->
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
export default defineComponent({
})
</script>

<style scoped>

</style>
