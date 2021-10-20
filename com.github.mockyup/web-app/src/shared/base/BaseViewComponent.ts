import { defineComponent } from 'vue'
import { BaseCrudService } from '@/service/webclient/base/BaseService'
import BaseComponent from '@/shared/base/BaseComponent'

export default defineComponent({
  name: 'BaseComponent',
  mixins: [BaseComponent],
  data () {
    return {
      service: {} as BaseCrudService,
      data: {} as never,
      payloadRequest: {} as never,
      responsePost: {} as never,
      directionAfterSubmit: {} as never
    }
  },
  methods: {
    async getByDetail (id: string) {
      return this.service.getById(id)
        .then(value => {
          this.data = value
        }).catch(reason => {
          this.validateResponse(reason)
        })
    },
    createNewData () {
      return this.service.doPost(this.payloadRequest)
        .then(value => {
          this.responsePost = value
          this.$router.push(this.directionAfterSubmit)
        }).catch(reason => {
          this.validateResponse(reason)
        })
    },
    updateData () {
      return this.service.doUpdate(this.payloadRequest, this.$route.params.id)
        .then(value => {
          this.responsePost = value
          this.$router.push(this.directionAfterSubmit)
        }).catch(reason => {
          this.validateResponse(reason)
        })
    }
  }
})
