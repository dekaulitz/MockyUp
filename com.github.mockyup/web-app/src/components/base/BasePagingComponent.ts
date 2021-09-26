
import { defineComponent } from 'vue'
import { PageableParam } from '@/plugins/webclient/model/RequestModel'
import { PagingAttributes } from '@/shared/pagination'
import { BaseCrudService } from '@/plugins/webclient/base/BaseService'
import BaseComponent from '@/components/base/BaseComponent'

export default defineComponent({
  name: 'BasePagingComponent',
  mixins: [BaseComponent],
  data () {
    return {
      parameter: {} as PageableParam,
      values: [] as never,
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
          this.values = value
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
    getAllAndCount () {
      this.getAll()
      this.getCount()
    }
  }
})
