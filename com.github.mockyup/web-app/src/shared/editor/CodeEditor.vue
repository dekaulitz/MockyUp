<template>
  <div>
    <div :id="id" style="
    position: relative;
    width: 100%;
    min-height: 720px;">
    </div>
  </div>
</template>

<script lang="ts">
import ace, { Ace } from 'ace-builds'
import 'ace-builds/webpack-resolver'
import 'ace-builds/src-noconflict/mode-json'
import 'ace-builds/src-noconflict/theme-mono_industrial'
import { defineComponent, markRaw } from 'vue'

interface CodeEditorInterface {
  editorInstance: Ace.Editor
}

export default defineComponent({
  name: 'CodeEditor',
  emits: ['update:modelValue'],
  data () {
    return {
      instance: {} as CodeEditorInterface
    }
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    id: {
      type: String,
      default: 'code-editor'
    }
  },
  mounted: async function () {
    const editor = this.instance.editorInstance = markRaw(ace.edit(this.id, {
      // autoScrollEditorIntoView: true,
      // copyWithEmptySelection: true,
      showLineNumbers: true,
      fontSize: 14,
      mode: 'ace/mode/json',
      theme: 'ace/theme/chrome'
    }))
    editor.on('change', () => {
      // ref: https://github.com/CarterLi/vue3-ace-editor/issues/11
      const content = editor.getValue()
      this.$emit('update:modelValue', content)
    })
    // editor.setTheme('github')
    // const editor = AceEditor.edit('code-editor')
    // editor.config.set('basePath', 'ace-builds/src-noconflict')
  },
  watch: {
    value (value:string) {
      this.instance.editorInstance.setValue(value)
    }
  }
})
</script>

<style scoped>

</style>
