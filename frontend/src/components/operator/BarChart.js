import { Bar } from 'vue-chartjs'
import API from "@/api/API";

export default {
  extends: Bar,
  props: {
    chartDataProps: {
      type: Array,
      default: null
    },
    chartLabelProps: Array
  },
  data: () => ({
    tempDatasets: [],
    chartdata: {
      labels: [],
      datasets: []
    },
    options: {
      scales: {
        xAxes: [{
          stacked: true
        }],
        yAxes: [{
          ticks: {
            beginAtZero: true,
            suggestedMax: 20,
            maxTicksLimit: 10
          },
          stacked: true,
          
        }]
      },
      maintainAspectRatio: false

    }
  }),
  created() {

  },
  mounted () {
    this.chartdata.labels = this.chartLabelProps;
    this.chartdata.datasets = this.chartDataProps;
    this.renderChart(this.chartdata, this.options)
  }

}