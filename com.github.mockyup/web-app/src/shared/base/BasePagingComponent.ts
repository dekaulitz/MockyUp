import { defineComponent } from 'vue'
import { PageableParam } from '@/service/webclient/model/RequestModel'
import { PagingAttributes } from '@/shared/pagination'
import { BaseCrudService } from '@/service/webclient/base/BaseService'
import BaseComponent from '@/shared/base/BaseComponent'

export default defineComponent({
  name: 'BasePagingComponent',
  mixins: [BaseComponent],
  data () {
    return {
      parameter: {} as PageableParam,
      data: [] as never,
      pagingAttributes: {
        pageSize: 10,
        totalData: 0
      } as PagingAttributes,
      service: {} as BaseCrudService
    }
  },
  methods: {
    getAll () {
      this.service.getAll(this.parameter)
        .then(value => {
          this.data = value
        }).catch(reason => {
          this.validateResponse(reason)
        })
    },
    getCount () {
      this.service.getCount(this.parameter)
        .then(value => {
          this.pagingAttributes.totalData = value
        }).catch(reason => {
          this.validateResponse(reason)
        })
    },
    deleteById (id: string) {
      this.service.deleteById(id)
        .then(value => {
          this.getAllAndCount()
        }).catch(reason => {
          this.validateResponse(reason)
        })
    },
    getAllAndCount () {
      this.service.getAll(this.parameter)
        .then(value => {
          this.data = value
          this.service.getCount(this.parameter)
            .then(value => {
              this.pagingAttributes.totalData = value
              this.placeHolderActive = false
            })
        }).catch(reason => {
          this.validateResponse(reason)
        })
    }
  }
})
