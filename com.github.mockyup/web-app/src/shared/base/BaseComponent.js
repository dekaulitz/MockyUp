import { defineComponent } from 'vue';
import { AlertType } from '@/shared/alert';
import { StorageService } from '@/service/webclient/service/CommonService';
import { StorageKeyType } from '@/service/webclient/model/EnumModel';
export default defineComponent({
    name: 'BaseComponent',
    data() {
        return {
            placeHolderActive: true,
            alertAttributes: { closeable: true }
        };
    },
    methods: {
        closeAlert(isShow) {
            this.alertAttributes.show = isShow;
        },
        validateResponse(error) {
            let message = error.message;
            let alertType = AlertType.ERROR;
            if (error.response === undefined) {
                this.alertAttributes = {
                    show: true,
                    alertType: alertType,
                    content: message,
                    closeable: true
                };
                return;
            }
            const statusCode = error.response.status;
            const responseData = error.response.data;
            const appsStatusCode = responseData.statusCode;
            message = responseData.message;
            if (statusCode >= 400) {
                if (appsStatusCode === 4001) {
                    alertType = AlertType.ERROR;
                    message = responseData.message;
                }
                else if (appsStatusCode === 4032) {
                    StorageService.clearData(StorageKeyType.AUTH_PROFILE);
                    this.$router.push('/login');
                    return;
                }
                else if (appsStatusCode === 4011) {
                    alertType = AlertType.WARNING;
                    message = responseData.message;
                }
                else if (appsStatusCode === 4033) {
                    alertType = AlertType.WARNING;
                    message = responseData.error.message.message;
                }
            }
            else if (statusCode >= 500) {
                alertType = AlertType.ERROR;
            }
            this.alertAttributes = {
                show: true,
                alertType: alertType,
                content: message,
                closeable: true
            };
        }
    }
});
//# sourceMappingURL=BaseComponent.js.map