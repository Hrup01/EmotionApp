<template>
  <div class="scroll-wrapper" ref="scrollWrapper">
    <ul class="scroll-content">
        <slot></slot>
    </ul>
  </div>
</template>

<script>
import BScroll from 'better-scroll';
export default {
  name: 'ScrollContainer',
  mounted() {
    // 确保 DOM 渲染完成后初始化 better-scroll
    this.$nextTick(() => {
      this.initScroll();
    });
  },
  methods: {
    initScroll() {
      // 初始化 better-scroll，设置 scrollX 为 true 开启横向滚动
      this.scroll = new BScroll(this.$refs.scrollWrapper, {
        scrollX: true,
        click: true, // 允许点击事件
        preventDefault: false, // 防止默认事件（根据需求调整）
      });
    },
  },
  beforeDestroy() {
    // 组件销毁前销毁 better-scroll 实例，防止内存泄漏
    if (this.scroll) {
      this.scroll.destroy();
    }
  },
};
</script>

<style scoped>
.scroll-wrapper {
    width: 100%;
    overflow: hidden;
    /* 可根据需要设置高度 */
    /* height: 100px;  */
}

.scroll-content {
    margin-left: 12px;
    display: flex;
    /* 确保内容横向排列 */
    flex-direction: row; 
}
</style>