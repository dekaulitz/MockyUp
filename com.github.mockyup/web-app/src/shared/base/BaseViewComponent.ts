import { defineComponent } from 'vue'
import { BaseCrudService } from '@/service/webclient/base/BaseService'
import BaseComponent from '@/shared/base/BaseComponent'

export default defineComponent({
  name: 'BaseComponent',
  mixins: [BaseComponent],
  data () {
    return {
      service: {} as BaseCrudService,
      data: {} as unknown,
      request: {} as unknown,
      response: {} as unknown,
      directionAfterSubmit: {} as unknown
    }
  },
  methods: {
    getDetail (id: string) {
      return this.service.getById(id)
        .then(value => {
          this.data = value
        }).catch(reason => {
          this.validateResponse(reason)
        })
    },
    doDelete (id:string) {
      return this.service.deleteById(id)
        .then(value => {
          this.data = value
          this.$router.push(this.directionAfterSubmit)
        }).catch(reason => {
          this.validateResponse(reason)
        })
    },
    doPost () {
      return this.service.doPost(this.request)
        .then(value => {
          this.data = value
          this.$router.push(this.directionAfterSubmit)
        }).catch(reason => {
          this.validateResponse(reason)
        })
    },
    doUpdate () {
      return this.service.doUpdate(this.request, this.$route.params.id)
        .then(value => {
          this.data = value
          this.$router.push(this.directionAfterSubmit)
        }).catch(reason => {
          this.validateResponse(reason)
        })
    }
  }
})
