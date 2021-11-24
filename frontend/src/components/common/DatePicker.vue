<template>
    <v-dialog
        ref="dialog"
        v-model="modal"
        :return-value.sync="dateInfo"
        persistent
        width="290px"
    >
        <template v-slot:activator="{ on, attrs }">
            <v-text-field
                v-model="dateInfo"
                label="Picker in dialog"
                prepend-icon="mdi-calendar"
                v-bind="attrs"
                v-on="on"
                background-color="#EEEEEE" solo flat dense filled
            ></v-text-field>
        </template>

        <v-date-picker v-model="dateInfo" :min="minTime" :max="maxTime" scrollable :readonly="readOnly">
            <v-spacer></v-spacer>
            <v-btn
                text
                color="primary"
                @click="modal = false"
            >
                Cancel
            </v-btn>
            <v-btn
                text
                color="primary"
                @click="$refs.dialog.save(dateInfo)"
            >
                OK
            </v-btn>
        </v-date-picker>
    </v-dialog>
</template>

<script>
export default {
    props: {
        propsDateInfo: String,
        readOnly: Boolean,
        minTime: {
            Type: String,
            Default: ""
        },
        maxTime:{
            Type: String,
            Default: ""
        },
    },
    data() {
        return {
            modal: false,
            dateInfo: this.propsDateInfo
        }
    },
    watch: {
        propsDateInfo() {
            this.dateInfo = this.propsDateInfo;
        },
        dateInfo() {
            this.$emit("change", this.dateInfo);
        }
    }
}
</script>

<style>

</style>