<template>
  <v-card>
      <v-card-title>최근 계약한 회사</v-card-title>
  <v-data-table
    :headers="headers"
    :items="newContCoList"
    :items-per-page="3"
     @click:row='goCoDetail'
    hide-default-footer
    class="elevation-1"
  ></v-data-table>
  </v-card>
</template>

<script>
import API from '@/api/API'

export default {
    data(){
        return{
            headers:[
                {text:'회사 코드',value:'coCd'},
                {text:'회사명',value:'coNm'},
                {text:'계약 시작 일자',value:'contBeginDt'},
            ],
            newContCoList:[],
        }
    },
    created(){
        API.getNewContCoList('',
            (res)=>{
                this.newContCoList=res,
                console.log(this.newContCoList)
            },
            (err)=>{
                console.log(err)
            }
        )
    },
    methods:{
       goCoDetail(target) {
        console.log(target.coCd+" "+target.coNm);
        this.$router.push('/manager/co/detail/'+target.coCd);
      }
    }
}
</script>

<style>

</style>