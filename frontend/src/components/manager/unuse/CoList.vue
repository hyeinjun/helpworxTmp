<template>
  <v-card class="pa-6 my-5"><v-row class="justify-center">
    <v-col cols="3">
      
      <v-card-title>고객사 목록</v-card-title>
    </v-col>
    <v-spacer></v-spacer>
    <v-col cols="12" sm="1" xs="2">
        <v-btn color="rgb(42, 46, 72)" dark @click="insertCo">등록</v-btn>
    </v-col>

  </v-row>
  <v-data-table
    :headers="headers"
    :items="companyList"
    :items-per-page="5"
    @click:row='goCoDetail'
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
                {text:'고객 수',value:'custNum'},
                {text:'사용 시스템 수',value:'sysNum'},
                {text:'사용 여부',value:'useYn'}
            ],
            companyList: [],
        }
    },
    created(){
      console.log(this.$store.state.token.accessToken);
        API.getCompanyList('',
            (res) => {
          this.companyList=res;
          console.log(this.companyList);
        },
        (err) => {
          console.log(err);
        }
        )
    },
    methods:{
      goCoDetail(target) {
        console.log(target.coCd+" "+target.coNm);
        this.$router.push('/manager/co/detail/'+target.coCd);
      },
      insertCo(){
        this.$router.push('/manager/co/insert');
      }
    }

}
</script>

<style>

</style>