<template>
    <div>
        <div v-if="gridMaxSize == 2">
            <main-page-grid
                key="xs"
                :type="type"
                :gridSize="gridMaxSize"
                :originData="originData"
                :columns="columns"
                :link="link"
            ></main-page-grid>
        </div>
        <div v-else-if="gridMaxSize == 3">
            <main-page-grid
                key="sm"
                :type="type"
                :gridSize="gridMaxSize"
                :originData="originData"
                :columns="columns"
                :link="link"
            ></main-page-grid>
        </div>
        <div v-else-if="gridMaxSize == 4">
            <main-page-grid
                key="md"
                :type="type"
                :gridSize="gridMaxSize"
                :originData="originData"
                :columns="columns"
                :link="link"
            ></main-page-grid>
        </div>
        <div v-else-if="gridMaxSize == 5">
            <main-page-grid
                key="lg"
                :type="type"
                :gridSize="gridMaxSize"
                :originData="originData"
                :columns="columns"
                :link="link"
            ></main-page-grid>
        </div>
        <div v-else-if="gridMaxSize == 7">
            <main-page-grid
                key="xl"
                :type="type"
                :gridSize="gridMaxSize"
                :originData="originData"
                :columns="columns"
                :link="link"
            ></main-page-grid>
        </div>
    </div>
</template>

<script>
import MainPageGrid from "./MainPageGrid.vue"

export default {
    components: {
        MainPageGrid        
    },
    props: {
        type: String,
        originData: Array,
        columns: Array,
        link: {
            type: String,
            default: ''
        }
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
    },
    created() {
        this.loop();
    },
    methods: {
        loop(){
            for(var idx=this.$props.originData.length; idx<this.gridMaxSize; idx++) 
                this.$props.originData.push(Object(null));
        }
    },
    watch: {
        gridMaxSize() {
            this.loop();
        }
    }

}
</script>

<style>

</style>