<template>
    <div>
        <v-main class="ma-3">
            <v-container fluid>
                <div class="pageTitle">HOME</div>
                <sr-main-table-card></sr-main-table-card>

                <v-row class="mb-6 d-flex" v-if="loading1 && loading2">
                    <!-- 미완료 SR -->
                    <v-col cols="6">
                        <v-card class="pa-3">
                            <div class="grid-title">미완료 SR</div>
                            <responsive-main-grid
                                type="router"
                                :gridSize="gridMaxSize"
                                :originData="originIncmpSrData"
                                :columns="gridIncmpSrColumns"
                                link="/operator/sr/rcpt-treat/"
                            ></responsive-main-grid>
                        </v-card>
                    </v-col>

                    <!-- 진행중 문의 -->
                    <v-col cols="6">
                        <v-card class="pa-3">
                            <div class="grid-title">진행중 문의</div>
                            <responsive-main-grid
                                type="modal"
                                :gridSize="gridMaxSize"
                                :originData="originProceedInquData"
                                :columns="gridProceedInquColumns"
                            ></responsive-main-grid>
                        </v-card>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>
    </div>
</template>

<script>
import API from "@/api/API";
import SrMainTableCard from '../../components/customer/MainSystem.vue'
import ResponsiveMainGrid from '../../components/mainPage/ResponsiveMainGrid.vue'

export default {
    components: {
        SrMainTableCard,
        ResponsiveMainGrid
    },
    data() {
        return {
            loading1: false,
            loading2: false,
            originIncmpSrData: [],
            gridIncmpSrColumns: [
                {
                    header: 'No',
                    name: 'id',
                    align: 'right',
                    width: 50
                },
                {
                    header: '시스템',
                    name: 'sysNm',
                    align: 'center'
                },
                {
                    header: '제목',
                    name: 'sbjct',
                    ellipsis: true
                },
                {
                    header: '요청일',
                    name: 'regTim',
                    align: 'center'
                },
                {
                    header: '요청자',
                    name: 'usrId',
                    align: 'center'
                }
            ],
            originProceedInquData: [],
            gridProceedInquColumns: [
                {
                    header: 'No',
                    name: 'id',
                    align: 'right',
                    width: 50
                },
                {
                    header: '시스템',
                    name: 'sysNm',
                    align: 'center'
                },
                {
                    header: '제목',
                    name: 'sbjct',
                    ellipsis: true
                },
                {
                    header: '문의 시작 시간',
                    name: 'beginTim',
                    align: 'center'
                },
                {
                    header: '요청자',
                    name: 'usrId',
                    align: 'center'
                }
            ]
        }
    },
    created() {
        this.getIncmpSrList();
        this.getProceedInqu();
    },
    methods: {
        getIncmpSrList() {
            // 미완료 sr grid
            API.getIncmpListWithCoCd(`coCd=${this.$store.state.usrInfo.coCd}`,
            (res) => {
                let resData = res;
                // data formatting srId->id
                resData.forEach((element, index) => {
                    resData[index].id = element.srId;
                })
                this.originIncmpSrData = resData;
                this.loading1 = true;
            },
            (err) => {
                console.log(err);
            })
        },
        getProceedInqu() {
            // 진행중 문의 grid
            let systemList = this.$store.state.CommonData.systemName;
            API.getProceedInqu(`coCd=${this.$store.state.usrInfo.coCd}`,
            (res) => {
                let resData = res;
                // data formatting
                resData.forEach((element, index) => {
                    resData[index].id = element.inquId;
                    resData[index].sysNm = systemList.find(e => e.value == element.sysCd).text;
                    resData[index].beginTim = this.formattingDate(element.beginTim);
                });

                this.originProceedInquData = resData;
                this.loading2 = true;
            },
            (err) => {
                console.log(err);
            })
        },
        formattingDate(time) {
            let formattedDate = new Date(time);
            let month = formattedDate.getMonth()+1;
            if(month<10) {
                month = '0'+month;
            }
            let date = formattedDate.getDate();
            if(date<10) {
                date = '0' + date;
            }
            return formattedDate.getFullYear()+'-'+month+'-'+date;
        },
    },
    computed: {
        gridMaxSize() {
            switch(this.$vuetify.breakpoint.name) {
                case 'xs': return 2;
                case 'sm': return 3;
                case 'md': return 4;
                case 'lg': return 5;
                case 'xl': return 7;
            }
            return 0;
        },
    }
}
</script>

<style scoped>
.grid-title {
    font-size: 1.1em;
    text-align: left;
    padding: 5px;
}
</style>