<template>
    <div>
        <grid
            ref="tuiGrid"
            :data="gridProps.data"
            :columns="gridProps.columns"
            :options="gridProps.options"
            @dblclick="doubleClick"
        ></grid>

        <!-- 실시간 문의 모달 -->
        <chat-modal-dialog
            :showDialog="showDialog"
            :targetInquId="targetInquId"
            @close="closeDialog"
        ></chat-modal-dialog>
    </div>
</template>

<script>
import 'tui-grid/dist/tui-grid.css'
import {Grid} from '@toast-ui/vue-grid'
import ChatModalDialog from '../rltmInqu/ChatModalDialog.vue'

export default {
    components: {
        Grid,
        ChatModalDialog
    },
    data() {
        return {
            showDialog: false,
            dialog_chat: false,
            targetInquId: 0
        }
    },
    props: {
        type: String, // router or modal
        originData: Array,
        columns: Array,
        gridSize: Number,
        link: String
    },
    created() {
        this.gridProps = {
            data: this.originData.slice(0,this.gridSize),
            columns: this.columns,
            options: {
                selectionUnit: 'row',
            }
        }
    },
    methods: {
        doubleClick(evt) {
            let id = this.$refs.tuiGrid.invoke('getValue', evt.rowKey, 'id');
            if(this.type == 'router') {
                this.$router.push(this.link+id);
            }else if(this.type == 'modal') {
                this.targetInquId = id;
                this.showDialog = true;
            }
        },
        closeDialog() {
            this.showDialog = false;
        }
    }
}
</script>

<style>

</style>