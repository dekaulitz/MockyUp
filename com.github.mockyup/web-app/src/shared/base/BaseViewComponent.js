import { defineComponent } from 'vue';
import BaseComponent from '@/shared/base/BaseComponent';
export default defineComponent({
    name: 'BaseComponent',
    mixins: [BaseComponent],
    data() {
        return {
            service: {},
            data: {},
            request: {},
            response: {},
            directionAfterSubmit: {}
        };
    },
    methods: {
        getDetail(id) {
            return this.service.getById(id)
                .then(value => {
                this.data = value;
            }).catch(reason => {
                this.validateResponse(reason);
            });
        },
        doDelete(id) {
            return this.service.deleteById(id)
                .then(value => {
                this.data = value;
                this.$router.push(this.directionAfterSubmit);
            }).catch(reason => {
                this.validateResponse(reason);
            });
        },
        doPost() {
            return this.service.doPost(this.request)
                .then(value => {
                this.data = value;
                this.$router.push(this.directionAfterSubmit);
            }).catch(reason => {
                this.validateResponse(reason);
            });
        },
        doUpdate() {
            return this.service.doUpdate(this.request, this.$route.params.id)
                .then(value => {
                this.data = value;
                this.$router.push(this.directionAfterSubmit);
            }).catch(reason => {
                this.validateResponse(reason);
            });
        }
    }
});
//# sourceMappingURL=BaseViewComponent.js.map