import { defineComponent } from 'vue'
import { AlertInterface, AlertType } from '@/shared/alert'
import { BaseResponse } from '@/plugins/webclient/base/BaseService'
import { StorageService } from '@/plugins/webclient/tmp/serice/CommonService'
import { StorageKeyType } from '@/plugins/webclient/model/EnumModel'

export default defineComponent({
  name: 'BaseComponent',
  data () {
    return {
      alertAttributes: {} as AlertInterface
    }
  },
  methods: {
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
