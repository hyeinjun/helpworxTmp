import { Line } from 'vue-chartjs'

export default {
    extends: Line,
    props: {
        chartDataProps: {
            type: Object,
            default: null
        },
        chartOptionsProps: {
            type: Object,
            default: null
        }

    },
    mounted() {
        this.renderChart(this.chartDataProps, this.chartOptionsProps);
    },
    watch: {
        chartDataProps: {
            deep: true,
            handler() {
                this.renderChart(this.chartDataProps, this.chartOptionsProps);
            }
        }
    }
}