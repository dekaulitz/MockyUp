import { defineComponent } from 'vue'
import { accessType } from '@/service/helper/AccessHelper'
import { StorageService } from '@/service/webclient/service/CommonService'
import { StorageKeyType } from '@/service/webclient/model/EnumModel'
import { AuthResponse } from '@/service/webclient/model/ResponseModel'

export default defineComponent({
  name: 'BaseAccessMixins',
  methods: {
    hasAccessPermissions (...accessMenus: accessType[]): boolean {
      const userAccess = StorageService.getData<AuthResponse>(StorageKeyType.AUTH_PROFILE)
      let hasAccess = false
      // return userAccess.access.indexOf(accessMenu) > -1
      for (const accessMenu of accessMenus) {
        if ((userAccess.access.indexOf(accessMenu) > -1)) {
          hasAccess = true
        }
      }
      return hasAccess
    }
  }
})
