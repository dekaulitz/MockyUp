<template>
  <div v-bind="$attrs">
    <div id="toolbar-container"></div>
    <textarea id="text-editor">
    </textarea>
  </div>
</template>

<script lang="ts">
import ClassicEditor from '@ckeditor/ckeditor5-build-classic/build/ckeditor'
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'TextEditor',
  emits: ['update:modelValue'],
  data () {
    return {
      contentData: ClassicEditor,
      $_instance: null
    }
  },
  model: {
    prop: 'modelValue',
    event: 'update:modelValue'
  },
  mounted () {
    this.content = false
    // eslint-disable-next-line no-undef
    this.contentData.create(document.querySelector('#text-editor'))
      .then(editor => {
        const toolbarContainer = document.querySelector('#toolbar-container')
        toolbarContainer.appendChild(editor.ui.view.toolbar.element)
        // this.contentData = editor
        // this.contentData = markRaw(editor)
        this.$_instance = editor
        this.setUpEditor()
      })
      .catch(error => {
        console.error(error)
      })
  },
  watch: {
    modelValue (newValue, oldValue) {
      if (newValue !== oldValue && newValue !== this.$_lastEditorData) {
        this.$_instance.setData(newValue)
      }
    }
  },
  methods: {
    setUpEditor () {
      const editor = this.$_instance
      const publishMessage = (message: any) => {
        this.$emit('update:modelValue', message)
      }
      editor.model.document.on('change:data', function () {
        const message = editor.getData()
        publishMessage(message)
      })
    }
  }
})
</script>

<style>
.ck-content {
  min-height: 450px !important;
}
</style>
