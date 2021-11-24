<template>
    <div>
        <v-row fluid no-gutters>
            <div class="d-flex">
                <sr-data-table-card
                    cardTitle="전체 건 수"
                    :itemLabels="totalItemsLabel"
                    :itemData="totalItems"
                ></sr-data-table-card>
            </div>
            <div class="d-flex">
                <sr-data-table-card
                    cardTitle="월별 건 수"
                    :itemLabels="mmItemsLabel"
                    :itemData="mmItems"
                ></sr-data-table-card>
            </div>
            <div class="d-flex" v-if="isStatus">
                <sr-data-table-card
                    cardTitle="처리시간(hr)"
                    :itemLabels="avgTimesLabel"
                    :itemData="avgTimes"
                ></sr-data-table-card>
            </div>
        </v-row>
    </div>
</template>

<script>
import API from "@/api/API";
import SrDataTableCard from "./SrDataTableCard.vue"

export default {
    components: {
        SrDataTableCard
    },
    props: {
        coCd: String,
        sysCd: Number,
        treatAvgTime: Number,
        rcptAvgTime: Number,
        isStatus: Boolean
    },
    data() {
        return {
            totalItemsLabel: ["총 (건)", "수정", "개선", "오류", "문의"],
            totalItems: {
                totalCnt: 0,
                dataModCnt: 0,
                imprvmCnt: 0,
                errorCnt: 0,
                inquCnt: 0,
            },
            mmItemsLabel: ["이번달", "지난달"],
            mmItems: {
                thisMmCnt: 0,
                prevMmCnt: 0
            },
            avgTimesLabel: ["평균 접수시간", "평균 처리시간"],
            avgTimes: {
                rcptTime: this.rcptAvgTime,
                treatTime: this.treatAvgTime,
            }
        }
    },
    methods: {
        getSrData() {
            API.getCntWithSysCd(
                `coCd=${this.coCd}&sysCd=${this.sysCd}`
                ,(res) => {
                    this.totalItems.totalCnt= res.totalCnt;
                    this.totalItems.dataModCnt= res.dataModCnt;
                    this.totalItems.imprvmCnt= res.imprvmCnt;
                    this.totalItems.errorCnt= res.errorCnt;
                    this.totalItems.inquCnt= res.inquCnt;
                    this.mmItems.thisMmCnt= res.thisMmCnt;
                    this.mmItems.prevMmCnt= res.prevMmCnt;
                }
                ,(err) => {console.log(err);}
            )
        }
    },
    mounted() {
        this.getSrData();
    },
    watch: {
        sysCd: function() {
            this.getSrData();
        },
        rcptAvgTime: function() {
            console.log(this.rcptAvgTime);
            this.avgTimes.rcptTime = this.rcptAvgTime;
        },
        treatAvgTime: function() {
            this.avgTimes.treatTime = this.treatAvgTime;
        }
    }
}
</script>

<style scoped>

</style>