<template>
  <div>
    <div id="code-editor">
    </div>
  </div>
</template>

<script>
import AceEditor from 'ace-builds/src-noconflict/ace'
import crhome from 'ace-builds/src-noconflict/theme-github'
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'CodeEditor',
  data () {
    return {}
  },
  mounted: async function () {
    const editor = AceEditor.edit('code-editor')
    editor.setTheme(crhome)
  }
})
</script>

<style scoped>
#editor {
  position: relative;
  width: 500px;
  height: 400px;
}
</style>
