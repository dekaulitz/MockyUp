<template>
  <div class="d-flex editor-on-hover">
    <div v-show="!isEditActive">
      <slot name="content"></slot>
    </div>
    <div class="editor-flag" :class="{'d-block':isEditActive}">
      <button @click="editorActive" class="border-0 btn-outline-default"><span class="fas fa-times small"></span></button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'
import BaseComponent from '@/shared/base/BaseComponent'
// isEditActive
export default defineComponent({
  name: 'DeleteOnHover',
  mixins: [BaseComponent],
  emits: ['update:deleteOnHover'],
  data () {
    return {
      isEditActive: false
    }
  },
  props: {
    id: { type: String },
    value: { type: String as PropType<string> }
  },
  methods: {
    editorActive () {
      this.isEditActive = false
      this.$emit('update:deleteOnHover', this.value)
    }
  }
})
</script>

<style scoped lang="scss">
.editor-on-hover {
  position: relative;
  > .editor-flag {
    display: none;
    position: absolute;
    right: -1rem;
  }

  &:hover {
    background-color: #c4c4c4;
    > .editor-flag {
      display: block;
      z-index: 999;
    }
  }
}
</style>
