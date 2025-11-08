<template>
  <div id="tooltip">
    <div class="subject">
      <div class="title">{{ title }}</div>
      <div class="button">
        <div class="no item">否</div>
        <div class="yes item">是</div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'toolTip',
  props: ['title']
}
</script>

<style lang="less" scoped>
#tooltip {
  position: absolute;
  top: 0;
  width: 390px;
  height: 852px;
  background: #00000080;
  .subject {
    margin: 336px auto;
    width: 342px;
    height: 133px;
    border-radius: 10px;
    background: #fffffff5;
    .title {
      margin-top: 18px;
      text-align: center;
      color: #000000cc;
      font-size: 18px;
    }
    .button {
      margin: 24px auto;
      display: flex;
      justify-content: space-between;
      width: 240px;
      .item {
        width: 100px;
        height: 40px;
        border-radius: 8px;
        border: 1px solid #0000003d;
        line-height: 38px;
        text-align: center;
        font-size: 18px;
      }
      .no {
        background: #f3cf80;
      }
    }
  }
}
</style>