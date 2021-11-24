<template>
  <v-card>
      <v-card-title>고객사 목록</v-card-title>
  <v-data-table
    :headers="headers"
    :items="companyList"
    :items-per-page="5"
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
                {text:'고객 수',value:'custNum'},
                {text:'사용 시스템 수',value:'sysNum'},
                {text:'사용 여부',value:'useYn'}
            ],
            companyList: [],
        }
    },
    created(){
      console.log("access"+this.$store.state.token.accessToken);
        API.getCompanyList(
          '',
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
      }
    }

}
</script>

<style>

</style>