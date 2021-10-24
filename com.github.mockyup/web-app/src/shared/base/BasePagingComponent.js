import { defineComponent } from 'vue';
import BaseComponent from '@/shared/base/BaseComponent';
export default defineComponent({
    name: 'BasePagingComponent',
    mixins: [BaseComponent],
    data() {
        return {
            parameter: {},
            data: [],
            pagingAttributes: {
                pageSize: 10,
                totalData: 0
            },
            service: {}
        };
    },
    methods: {
        getAll() {
            this.service.getAll(this.parameter)
                .then(value => {
                this.data = value;
            }).catch(reason => {
                this.validateResponse(reason);
            });
        },
        getCount() {
            this.service.getCount(this.parameter)
                .then(value => {
                this.pagingAttributes.totalData = value;
            }).catch(reason => {
                this.validateResponse(reason);
            });
        },
        deleteById(id) {
            this.service.deleteById(id)
                .then(value => {
                this.getAllAndCount();
            }).catch(reason => {
                this.validateResponse(reason);
            });
        },
        getAllAndCount() {
            this.getAll();
            this.getCount();
        }
    }
});
//# sourceMappingURL=BasePagingComponent.js.map