<template>
  <div>
    <h2>分类博客数量统计</h2>
    <div ref="chart" style="width: 100%; height: 400px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import api from '../api'

const chart = ref(null)

onMounted(async () => {
  const res = await api.get('/categories/stats/blogCount')
  // 假设返回格式为 [["前端", 5], ["后端", 3], ...]
  const data = res.data
  const categories = data.map(item => item[0])
  const counts = data.map(item => item[1])

  const myChart = echarts.init(chart.value)
  myChart.setOption({
    title: { text: '分类博客数量' },
    tooltip: {},
    xAxis: { data: categories },
    yAxis: {},
    series: [{
      name: '博客数量',
      type: 'bar',
      data: counts,
      itemStyle: { color: '#007aff' }
    }]
  })
})
</script>

<style scoped>
h2 {
  text-align: center;
  margin-bottom: 24px;
  color: #1a2233;
}
</style>
