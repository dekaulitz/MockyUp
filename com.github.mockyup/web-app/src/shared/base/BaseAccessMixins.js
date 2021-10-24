import { defineComponent } from 'vue';
import { StorageService } from '@/service/webclient/service/CommonService';
import { StorageKeyType } from '@/service/webclient/model/EnumModel';
export default defineComponent({
    name: 'BaseAccessMixins',
    methods: {
        hasAccessPermissions(...accessMenus) {
            const userAccess = StorageService.getData(StorageKeyType.AUTH_PROFILE);
            let hasAccess = false;
            // return userAccess.access.indexOf(accessMenu) > -1
            for (const accessMenu of accessMenus) {
                if ((userAccess.access.indexOf(accessMenu) > -1)) {
                    hasAccess = true;
                }
            }
            return hasAccess;
        }
    }
});
//# sourceMappingURL=BaseAccessMixins.js.map