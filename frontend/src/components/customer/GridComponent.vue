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
                        삭제
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
            @onGridUpdated="onGridUpdated"
        ></grid>

        <!-- 삭제 시 나오는 확인 메세지 -->
        <yes-no-dialog
            v-if="deleteDialog"
            dialogTitle="정말로 삭제하시겠습니까?"
            dialogText="삭제하면 되돌릴 수 없습니다."
            @click="clickDialog"
        ></yes-no-dialog>
    </div>
</template>

<script>
import 'tui-grid/dist/tui-grid.css'
import 'tui-pagination/dist/tui-pagination.css';
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
            headers: {'accessToken': "Bearer "+this.$store.state.token.accessToken},
            api : {
                readData: {url: `${process.env.VUE_APP_API_URL}/srs`, method: 'GET', initParams: this.requestData }
            },
            initialRequest: false
        };
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
                    header: '요청구분',
                    name: 'reqGb',
                    align: 'center',
                    width: 100
                },
                {
                    header: '제목',
                    name: 'sbjct',
                    ellipsis: true,
                    minWidth: 200
                },
                {
                    header: '요청자',
                    name: 'usrId',
                    align: 'center',
                    width: 100
                },
                {
                    header: 'SR상태',
                    name: 'state',
                    align: 'center',
                    width: 100
                },
                {
                    header: '등록시간',
                    name: 'regTim',
                    align: 'center',
                    width: 150
                }
            ],
        }
    },
    methods: {
        readGridData(params) {
            // grid rendering 후 readData 호출
            // ('readData', page', params)
            this.$refs.tuiGrid.invoke('readData', 1, params);
        },
        onGridUpdated(ev) {
            // 총 조회 건수
            this.totalCount = this.$refs.tuiGrid.invoke('getPaginationTotalCount');

            // 시스템과 업무구분이 코드로 들어오는데 vuex에 저장된 시스템과 업무구분 데이터로 코드를 이름으로 변경함
            // 등록시간 형식 변경

            // ev : onGridUpdated 이벤트에서 발생된 이벤트를 인자로 받음

            // vuex에 저장된 시스템 리스트, 업무구분 리스트
            let systemList = this.$store.state.CommonData.systemName;
            let jobGbList = this.$store.state.CommonData.jobGbName;

            // updated 된 전체 gridData
            let rowData = ev.instance.store.data.rawData;
            let len = rowData.length;

            for(let index=0; index<len; index++) {
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
            // grid 에서 check 된 rows 들을 삭제하기 위한 method
            let checkedRows = this.$refs.tuiGrid.invoke('getCheckedRows');
            let data = [];

            for(let row of checkedRows) {
                data.push(row.srId);
            }
            let params = 'srIds='+data.toString();
            
            // delete api call
            API.deleteSrList(
                params,
                () => {
                    //reload data
                    this.readGridData(this.requestData);
                },
                (err) => {
                    alert(JSON.parse(err.response.request.response).message);
                }
            )
        },
        doubleClick(evt) {
            let srId = this.$refs.tuiGrid.invoke('getValue',evt.rowKey,'srId');
            this.$router.push('/customer/sr/detail/'+srId);
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
            deep: true, // object 내의 변경 감지하기 위한 옵션
            handler(newData) {
                this.readGridData(newData);
            }
        }
    }
}
</script>

<style>
.tui-pagination .tui-is-selected, .tui-pagination strong {
    background: rgb(42, 46, 72);
    border-color: rgb(42, 46, 72);
}

.tui-pagination .tui-is-selected:hover {
    background-color: rgb(42, 46, 72);
}

.tui-pagination .tui-first-child.tui-is-selected {
    border-left: 1px solid rgb(42, 46, 72);
}

.tui-pagination .tui-last-child.tui-is-selected {
    border-right: 1px solid rgb(42, 46, 72);
}
</style>>