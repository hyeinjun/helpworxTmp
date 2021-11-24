<template>
  <v-card>
      <v-card-title>계약 종료 예정 회사</v-card-title>
  <v-data-table
    :headers="headers"
    :items="endSchdCoList"
    :items-per-page="3"
     @click:row='goCoDetail'
    hide-default-footer
    class="elevation-1"
  ></v-data-table>
  </v-card>
</template>

<script>
import API from "@/api/API";

export default {
    data(){
        return{
            headers:[
                {text:'회사 코드',value:'coCd'},
                {text:'회사명',value:'coNm'},
                {text:'종료 일자',value:'contEndDt'},
            ],
            endSchdCoList:[],
        }
    },
    created(){
        API.getEndSchdCoList('',
        (res)=>{
            this.endSchdCoList=res,
            console.log(this.endSchdCoList)
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