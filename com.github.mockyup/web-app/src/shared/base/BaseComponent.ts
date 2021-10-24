import { defineComponent } from 'vue'
import { AlertInterface, AlertType } from '@/shared/alert'
import { BaseResponse } from '@/service/webclient/base/BaseService'
import { StorageService } from '@/service/webclient/service/CommonService'
import { StorageKeyType } from '@/service/webclient/model/EnumModel'

export default defineComponent({
  name: 'BaseComponent',
  data () {
    return {
      placeHolderActive: true,
      alertAttributes: { closeable: true } as AlertInterface
    }
  },
  methods: {
    closeAlert (isShow: boolean) {
      this.alertAttributes.show = isShow
    },
    validateResponse (error: any) {
      let message = error.message
      let alertType = AlertType.ERROR
      if (error.response === undefined) {
        this.$store.dispatch('alertStore/setAlert', {
          show: true,
          alertType: alertType,
          content: message,
          closeable: true
        })
        return
      }

      const statusCode: number = error.response.status
      const responseData: BaseResponse = error.response.data
      const appsStatusCode: number = responseData.statusCode
      message = responseData.message
      if (statusCode >= 400 && statusCode < 500) {
        if (appsStatusCode === 4001) {
          alertType = AlertType.ERROR
          message = responseData.message
        } else if (appsStatusCode >= 4031 && appsStatusCode <= 4033) {
          StorageService.clearData(StorageKeyType.AUTH_PROFILE)
          this.$router.push('/login')
          this.$store.dispatch('alertStore/setAlert', {
            show: true,
            alertType: AlertType.WARNING,
            content: message,
            closeable: true
          })
          return
        } else if (appsStatusCode === 4011) {
          alertType = AlertType.WARNING
          message = responseData.message
        }
      } else if (statusCode >= 500) {
        alertType = AlertType.ERROR
        message = `${message} ${error.response.data.error.exception}`
      }
      this.$store.dispatch('alertStore/setAlert', {
        show: true,
        alertType: alertType,
        content: message,
        closeable: true
      })
    }
  }
})
