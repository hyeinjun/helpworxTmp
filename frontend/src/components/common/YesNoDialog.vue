<template>
    <div>
    <v-dialog :max-width="maxWidth" v-model="dialog">
        <v-card>
            <v-card-title>{{dialogTitle}}</v-card-title>
            <v-card-text>{{dialogText}}</v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>

                <v-btn
                    color="green darken-1"
                    text
                    @click="clickNo"
                >No</v-btn>

                <v-btn
                    color="red darken-1"
                    text
                    @click="clickYes"
                >Yes</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
    </div>
</template>

<script>
export default {
    props: {
        maxWidth: {
            type: Number,
            default: 500
        },
        dialogTitle: String,
        dialogText: String
    },
    data () {
        return {
            dialog: true
        }
    },
    methods: {
        clickYes() {
            this.$emit('click', 'Y');
        },
        clickNo() {
            this.$emit('click', 'N');
        }
    }
}
</script>

<style>

</style>