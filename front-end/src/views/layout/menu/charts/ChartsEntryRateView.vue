<script>

export default {
  name: "ChartsEntryRateView",
  data() {
    return {
      searchForm: {
        item: 'item1'
      },
      chartXData: ['2013', '2014', '2015', '2016', '2017', '2018', '2019'],
      chartYData: [7.3998, 7.3816, 7.3786, 7.4081, 9.3325, 7.3741, 7.4517],
    }
  },
  created() {
    this.onSearch();
  },
  methods: {

    onSearch() {
      // 查询图表数据
      this.$http.get('/charts/entry-rate').then(res => {
        this.chartXData = res.data.xdata;
        this.chartYData = res.data.ydata;
        this.drawChart();
      })
    },

    drawChart() {
      var chartDom = document.getElementById('chartsEntryRate');
      var myChart = this.$echarts.init(chartDom);
      var option = {
        xAxis: {
          type: 'category',
          data: this.chartXData
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: this.chartYData,
            itemStyle: {
              normal: {
                label : {show: true},
                color: '#7279ee'
              }
            },
            type: 'line'
          }
        ]
      };
      myChart.setOption(option);
    },

    handleItemChange() {
      console.log(this.searchForm.item)
      this.onSearch();
    },

    handleDateChange() {
      console.log(this.searchForm.date)
      this.onSearch();
    },

  },
}
</script>

<template>
  <div class="p-main-charts">
    <div class="p-main-charts-search" v-if="false">
    </div>
    <div class="p-main-charts-display" id="chartsEntryRate"></div>
  </div>
</template>

<style scoped>

</style>