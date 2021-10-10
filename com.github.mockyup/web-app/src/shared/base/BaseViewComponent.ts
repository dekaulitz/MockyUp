import { defineComponent } from 'vue'
import { BaseCrudService, BaseResponse } from '@/plugins/webclient/base/BaseService'
import { AlertInterface, AlertType } from '@/shared/alert'
import { StorageService } from '@/plugins/webclient/tmp/serice/CommonService'
import { StorageKeyType } from '@/plugins/webclient/model/EnumModel'
import BaseComponent from '@/shared/base/BaseComponent'

export default defineComponent({
  name: 'BaseComponent',
  mixins: [BaseComponent],
  data () {
    return {
      service: {} as BaseCrudService,
      value: {} as never,
      payloadRequest: {} as never,
      responsePost: {} as never,
      directionAfterSubmit: {} as never
    }
  },
  methods: {
    getByDetail (id: string) {
      return this.service.getById(id)
        .then(value => {
          this.value = value
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
    closeAlert (isShow: boolean) {
      this.alertAttributes.show = isShow
    },
    validateResponse (error: any) {
      if (!error.status) {
        console.log(error)
        this.alertAttributes = {
          show: true,
          alertType: AlertType.ERROR,
          content: error.message
        }
      }
      const statusCode: number = error.response.status
      const responseData: BaseResponse = error.response.data
      const appsStatusCode: number = responseData.statusCode
      if (statusCode >= 401) {
        if (appsStatusCode === 4032) {
          StorageService.clearData(StorageKeyType.AUTH_PROFILE)
          this.$router.push('/login')
        } else if (appsStatusCode === 4011) {
          this.alertAttributes = {
            show: true,
            alertType: AlertType.WARNING,
            content: responseData.message,
            closeable: true
          }
        }
      } else if (statusCode >= 500) {

      }
    }
  }
})
