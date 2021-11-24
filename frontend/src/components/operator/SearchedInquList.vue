<template>


<!-- grid -->
        <div>
        <v-container fluid>
            <v-row
                no-gutters
                justify="space-between">
                <v-col cols="auto" class="ma-1">
                    {{totalCount}} 건
                </v-col>

            </v-row>
        </v-container>
        <grid
            ref="tuiGrid"
            :data="gridProps.data"
            :columns="gridProps.columns"
            :options="gridProps.options"
            @dblclick="doubleClick"
            @onGridUpdated="onGridUpdated">
        </grid>

        <v-dialog
            v-model="dialog_chat"
            persistent
            width="400px"
        >
        <rltm-inqu-chat-modal :inquId="targetInquId" :sysCd="targetSysCd" @changeDialogChat="changeDialogChat"></rltm-inqu-chat-modal>

    </v-dialog>

    </div>

  
</template>



<script>
import RltmInquChatModal from '../rltmInqu/RltmInquChatModal.vue'
import 'tui-grid/dist/tui-grid.css'
import {Grid} from '@toast-ui/vue-grid'

export default {
    components: { 
        RltmInquChatModal,
        Grid
    },
    data(){
        return{
            dialog_chat: false,
            targetInquId: 0,
            targetSysCd: 0,
            totalCount: 0,
        }
    },
    props: ["requestData", "router"],
    created(){
        const dataSource = {
            contentType: 'application/json',
            headers: { accessToken: "Bearer "+this.$store.state.token.accessToken },
            api : {
                readData: {url: `${process.env.VUE_APP_API_URL}`+this.$props.router, method: 'GET', initParams: this.requestData }
            },
            initialRequest: false
        }
        this.gridProps = {
            data: dataSource,
            options: {
                selectionUnit: 'row',
                pageOptions: {
                    perPage: 10
                }
            },
            columns: [
                {
                    header: 'No',
                    name: 'inquId',
                    align: 'right',
                    width: 50
                },
                
                {
                    header: '문의제목',
                    name: 'sbjct',
                    minWidth: 150
                },
                {
                    header: '문의자',
                    name: 'usrId',
                    align: 'center',
                    width: 90
                },
                {
                    header: '상태',
                    name: 'state',
                    align: 'center',
                    width: 70
                },
                {
                    header: '시작시간',
                    name: 'beginTim', 
                    align: 'center',
                    width: 150
                },

                {
                    header: '종료시간',
                    name: 'endTim', 
                    align: 'center',
                    width: 150
                },
            ]
        }
    },
    methods:{
        readGridData(params) {
            this.$refs.tuiGrid.invoke('readData', 1, params); 
        },
        onGridUpdated(ev) {
            // 총 조회 건수
            console.log(this.$refs.tuiGrid)
            this.totalCount = this.$refs.tuiGrid.invoke('getPaginationTotalCount');

            // updated 된 전체 gridData
            let rowData = ev.instance.store.data.rawData;
            let len = rowData.length;

            for(let index=0; index<len; index++) {
                // 시작 2000-00-00T00:00:12.234+000 형식에서 2000-00-00 23:00 형식으로 변경
                let beginTim = this.$refs.tuiGrid.invoke('getValue', index, 'beginTim');
                this.$refs.tuiGrid.invoke('setValue', index, 'beginTim', this.formattingDate(beginTim));

                // 종료시간
                let endTim = this.$refs.tuiGrid.invoke('getValue', index, 'endTim');
                if(endTim != null){
                    this.$refs.tuiGrid.invoke('setValue', index, 'endTim', this.formattingDate(endTim));
                }
            }
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
            let hour = formattedDate.getHours();
            if(hour<10) {
                hour = '0' + hour;
            }       
            let minute = formattedDate.getMinutes();
            if(minute<10) {
                minute = '0' + minute;
            }
            return formattedDate.getFullYear()+'-'+month+'-'+date+' '+hour+':'+minute;
        },
        doubleClick(evt) {
            let inquId = this.$refs.tuiGrid.invoke('getValue',evt.rowKey,'inquId');
            let sysCd = this.$refs.tuiGrid.invoke('getValue',evt.rowKey,'sysCd');
            this.targetInquId  = inquId;
            this.targetSysCd = sysCd;
            this.dialog_chat = true;
        },
        changeDialogChat(dialog_chat) {
            this.dialog_chat = dialog_chat;
        }
    },
    watch: {
        requestData: {
            deep: true,
            handler(newData) {
                this.readGridData(newData);
            }
        }
    }

}
</script>

<style>

</style>