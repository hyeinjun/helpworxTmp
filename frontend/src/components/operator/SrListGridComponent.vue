<template>
    <div>
        <v-container fluid>
            <v-row
                no-gutters
                justify="space-between">
                <v-col cols="auto" class="ma-1">
                    {{totalCount}} 건
                </v-col>
                <v-col cols="auto">
                    <v-btn
                        class="ma-1"
                        color="rgb(42, 46, 72)" dark
                        @click="clickDeleteButton">
                        반려
                    </v-btn>
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

        <!-- 삭제 시 나오는 확인 메세지 -->
        <yes-no-dialog
            v-if="deleteDialog"
            dialogTitle="정말로 반려하시겠습니까?"
            dialogText="선택된 SR이 일괄 반려 처리됩니다."
            @click="clickDialog"
        ></yes-no-dialog>
    </div>
</template>

<script>
import 'tui-grid/dist/tui-grid.css'
import {Grid} from '@toast-ui/vue-grid'
import API from '../../api/API.js'
import YesNoDialog from '../common/YesNoDialog.vue'

export default {
    components: {
        Grid,
        YesNoDialog
    },
    props: {
        requestData: Object
    },
    data() {
        return {
            totalCount: 0,
            deleteDialog: false
        }
    },
    created() {
        const dataSource = {
            contentType: 'application/json',
            headers: { accessToken: "Bearer "+this.$store.state.token.accessToken },
            api : {
                readData: {url: `${process.env.VUE_APP_API_URL}/sr-operator`, method: 'GET', initParams: this.requestData }
            },
            initialRequest: false
        }
        this.gridProps = {
            data: dataSource,
            options: {
                rowHeaders: ['checkbox'],
                selectionUnit: 'row',
                pageOptions: {
                    perPage: 10
                }
            },
            columns: [
                {
                    header: 'No',
                    name: 'srId',
                    align: 'right',
                    width: 50
                },
                {
                    header: '고객사',
                    name: 'coCd',
                    align: 'center',
                    width: 100
                },
                {
                    header: '시스템',
                    name: 'sysCd',
                    align: 'center',
                    width: 100
                },
                {
                    header: '업무구분',
                    name: 'jobGbId',
                    align: 'center',
                    width: 100
                },
                {
                    header: '제목',
                    name: 'sbjct',
                    ellipsis: true,
                    minWidth: 150
                },
                {
                    header: '요청자',
                    name: 'usrId',
                    align: 'center',
                    width: 90
                },
                {
                    header: '요청일시',
                    name: 'regTim',
                    align: 'center',
                    width: 150
                },
                {
                    header: 'SR상태',
                    name: 'state',
                    align: 'center',
                    width: 70
                },
                {
                    header: '접수자',
                    name: 'recivrId',
                    align: 'center',
                    width: 90
                },
                {
                    header: '처리자',
                    name: 'opetrId',
                    align: 'center',
                    width: 90
                },
                {
                    header: '처리일시',
                    name: 'treatEndTim', // 처리일시: 처리 종료 일자
                    align: 'center',
                    width: 150
                },
            ]
        }
    },
    methods: {
        readGridData(params) {
            this.$refs.tuiGrid.invoke('readData', 1, params); 
        },
        onGridUpdated(ev) {
            // 총 조회 건수
            this.totalCount = this.$refs.tuiGrid.invoke('getPaginationTotalCount');

            // 고객사, 시스템과 업무구분이 코드로 들어오는데 vuex에 저장된 데이터로 코드를 이름으로 변경함

            // vuex에 저장된 고객사 리스트, 시스템 리스트, 업무구분 리스트
            let companyList = this.$store.state.CommonData.companyList;
            let systemList = this.$store.state.CommonData.systemName;
            let jobGbList = this.$store.state.CommonData.jobGbName;

            // updated 된 전체 gridData
            let rowData = ev.instance.store.data.rawData;
            let len = rowData.length;

            for(let index=0; index<len; index++) {
                // 고객사 코드 -> 고객사 이름
                let coCd = this.$refs.tuiGrid.invoke('getValue', index, 'coCd');
                let coName = companyList.find(element => element.value == coCd).text;
                this.$refs.tuiGrid.invoke('setValue', index, 'coCd', coName);

                // 시스템 코드 -> 시스템 이름으로 grid 내의 value 변경
                let sysCd = this.$refs.tuiGrid.invoke('getValue', index, 'sysCd');
                let sysName = systemList.find(element => element.value == sysCd).text;
                this.$refs.tuiGrid.invoke('setValue', index, 'sysCd', sysName);

                // 업무구분 코드 -> 업무구분 이름 으로 grid 내의 value 변경
                let jobGbId = this.$refs.tuiGrid.invoke('getValue', index, 'jobGbId');
                this.$refs.tuiGrid.invoke('setValue', index, 'jobGbId', jobGbList[jobGbId]);

                // 등록시간 2000-00-00T00:00:12.234+000 형식에서 2000-00-00 23:00 형식으로 변경
                let regTim = this.$refs.tuiGrid.invoke('getValue', index, 'regTim');
                this.$refs.tuiGrid.invoke('setValue', index, 'regTim', this.formattingDate(regTim));

                // 처리시간
                let treatTim = this.$refs.tuiGrid.invoke('getValue', index, 'treatEndTim');
                if(treatTim != null){
                    this.$refs.tuiGrid.invoke('setValue', index, 'treatEndTim', this.formattingDate(treatTim));
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
        clickDeleteButton() {
            if(this.$refs.tuiGrid.invoke('getCheckedRows').length == 0) {
                alert('선택된 행이 없습니다.');
            } else {
                this.deleteDialog = true;
            }
        },
        deleteCheckedRows() {
            let checkedRows = this.$refs.tuiGrid.invoke('getCheckedRows');
            let data = [];

            for(let row of checkedRows) {
                data.push(row.srId);
            }

            let params = 'srIds=' + data.toString();

            API.deleteSrListByOper(
                params,
                () => {
                    this.readGridData(this.requestData);
                },
                (err) => {
                    alert(JSON.parse(err.response.request.response).message);
                }
            )

        },
        doubleClick(evt) {
            let srId = this.$refs.tuiGrid.invoke('getValue',evt.rowKey,'srId');
            this.$router.push(`/operator/sr/rcpt-treat/${srId}`);
        },
        clickDialog(value) {
            if(value === 'Y') {
                this.deleteCheckedRows();
                this.deleteDialog = false;                
            }else if(value === 'N') {
                this.deleteDialog = false;
                let checkedRows = this.$refs.tuiGrid.invoke('getCheckedRows');
                for(let row of checkedRows) {
                    this.$refs.tuiGrid.invoke('uncheck', row.rowKey);
                }
            }
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