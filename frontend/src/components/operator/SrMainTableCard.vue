<template>
        <v-card class="pa-2 my-5">
                <v-row class="mb-n8 pt-1" style="align-items:baseline;">
                    <v-col cols="1">
                        * 시스템
                    </v-col>
                    <v-col cols="2">
                        <combo-box-component
                            :items="systemList"
                            @selected="selectedSystemComboBox"
                        ></combo-box-component>
                    </v-col>
                </v-row>
                <sr-data-table
                    :coCd="coCd"
                    :sysCd="sysCd"
                    :isStatus="false"
                ></sr-data-table>
        </v-card>
</template>

<script>
import { mapState } from 'vuex'
import ComboBoxComponent from "../customer/ComboBoxComponent"
import SrDataTable from "./SrDataTable.vue"

export default {
    components: {
        ComboBoxComponent,
        SrDataTable
    },
    data() {
        return {
            systemList: [],
            coCd: this.$store.state.usrInfo.coCd,
            sysCd: 0
        }
    },
    created() {
        this.setSystemList();
        this.selectedSystemComboBox(this.systemList[0]);
    },
    methods: {
        setSystemList() {
            this.systemList = this.systemByCompany[this.coCd];
        },
        selectedSystemComboBox(value) {
            this.sysCd = value;
        }
    },
    computed: {
        ...mapState({
            companyList: state => state.CommonData.companyList,
            systemByCompany: state => state.CommonData.systemByCompany
        })
    }
}
</script>

<style>

</style>