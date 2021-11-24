<template>
    <v-dialog
        v-model="dialog_chat"
        persistent
        width="400px">
        <rltm-inqu-chat-modal
            :inquId="inquId"
            :sysCd="targetSysCd"
            @changeDialogChat="changeDialogChat"
        ></rltm-inqu-chat-modal>
    </v-dialog>
</template>

<script>
import RltmInquChatModal from './RltmInquChatModal.vue'

export default {
    components: {
        RltmInquChatModal
    },
    data() {
        return {
            dialog_chat: false,
            inquId: 0
        }
    },
    props: {
        targetInquId: Number,
        targetSysCd: Number,
        showDialog: Boolean
    },
    methods: {
        changeDialogChat(dialog_chat) {
            this.$emit('close');
        }
    },
    watch: {
        showDialog() {
            this.dialog_chat = this.showDialog;
        },
        targetInquId() {
            this.inquId = this.targetInquId;
        },
    }
}
</script>

<style>

</style>