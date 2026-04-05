<template>
  <div id="app">
    <!-- 滚动容器：固定高度500px，超出滚动 -->
    <div 
      class="virtual-list"
      ref="listContainer"
      @scroll="handleScroll"
    >
      <!-- 占位层：用来撑出滚动条的高度（模拟完整列表） -->
      <div class="list-placeholder" :style="{ height: totalHeight + 'px' }"></div>

      <!-- 可视区域的列表内容：通过top定位到正确位置 -->
      <div 
        class="list-content"
        ref="contentWrapper"
        :style="{ top: contentTop + 'px' }"
      >
        <!-- 只渲染可视区域内的列表项 -->
        <div 
          v-for="(item, index) in visibleList" 
          :key="item.id"
          class="list-item"
          ref="listItems"
          :data-index="startIndex + index" 
        >
          <!-- 自定义列表项内容（模拟你的日记内容，高度不固定） -->
          <h4>{{ item.title }}</h4>
          <p>{{ item.content }}</p>
          <span>{{ item.date }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      // 模拟1万条日记数据（高度不固定）
      diaryList: [],
      startIndex: 0,        // 可视区域起始索引
      endIndex: 10,         // 可视区域结束索引
      contentTop: 0,        // 列表内容的top偏移（核心：决定内容显示位置）
      totalHeight: 0,       // 列表总高度（预估+真实修正）
      estimateHeight: 100,  // 每项预估高度（仅初始化用）
      buffer: 5,           // 缓冲区：从5→10，多渲染前后10项，避免快速滚动空白
      heightMap: new Map(), // 存储每项的真实高度：key=索引，value=高度
    }
  },
  computed: {
    // 只取可视区域内的列表数据
    visibleList() {
      return this.diaryList.slice(this.startIndex, this.endIndex)
    }
  },
  mounted() {
    // 生成模拟数据（1万条，内容长度随机，高度不固定）
    this.generateMockData()
    // 初始化虚拟列表
    this.initVirtualList()
  },
  methods: {
    // 生成1万条模拟日记数据（模拟你项目中高度不固定的场景）
    generateMockData() {
      this.diaryList = Array.from({ length: 10000 }, (_, i) => ({
        id: i,
        title: `日记 ${i + 1}`,
        // 内容长度随机，导致高度不固定（这是不定高的核心场景）
        content: `这是我的第${i + 1}篇日记，记录今日心情：开心、平静、emo...`.repeat(Math.random() * 4),
        date: `2026-${Math.floor(Math.random()*12+1)}-${Math.floor(Math.random()*28+1)}`
      }))
    },

    // 初始化虚拟列表
    initVirtualList() {
      // 初始总高度 = 总条数 * 预估高度（仅初始化，后续会用真实高度修正）
      this.totalHeight = this.diaryList.length * this.estimateHeight
      // 计算初始可视区域
      this.calcVisibleRange()
      // DOM渲染完成后，立即获取真实高度（避免初始空白）
      this.$nextTick(() => {
        this.updateRealHeight()
      });
    },

    // 计算可视区域该显示哪些项（真实高度累加）
    calcVisibleRange() {
      const container = this.$refs.listContainer
      if (!container) return

      // 滚动条距离顶部的距离
      const scrollTop = container.scrollTop
      // 容器可视高度
      const viewHeight = container.clientHeight

      // 找起始索引 
      let currentHeight = 0 // 累加高度
      let startIndex = 0   // 初始起始索引
      // 遍历所有项，累加高度，找到滚动条所在位置对应的索引
      for (let i = 0; i < this.diaryList.length; i++) {
        // 优先用真实高度，没有则用预估高度
        const itemHeight = this.heightMap.get(i) || this.estimateHeight
        // 当累加高度超过滚动条位置时，说明滚动条停在第i项的位置
        if (currentHeight + itemHeight > scrollTop) {
          startIndex = i
          break
        }
        currentHeight += itemHeight
      }
      // 加上缓冲区，多渲染前面10项，避免滚动时空白
      this.startIndex = Math.max(0, startIndex - this.buffer)

      // 找结束索引 
      let endHeight = currentHeight // 从起始索引的高度开始累加
      let endIndex = startIndex    // 初始结束索引
      // 累加高度，直到超过「滚动条位置+容器高度」（可视区域底部）
      while (endIndex < this.diaryList.length && endHeight < scrollTop + viewHeight) {
        const itemHeight = this.heightMap.get(endIndex) || this.estimateHeight
        endHeight += itemHeight
        endIndex++
      }
      // 加上缓冲区，多渲染后面10项，避免滚动时空白
      this.endIndex = Math.min(this.diaryList.length, endIndex + this.buffer)

      // 重新计算contentTop（用真实高度累加） 
      // contentTop决定了列表内容显示的垂直位置，算错就会空白
      this.contentTop = this.calcContentTop(this.startIndex)
    },

    // 计算列表内容的top偏移（累加前面所有项的高度）
    calcContentTop(startIndex) {
      let top = 0
      // 累加起始索引之前所有项的高度（优先真实高度）
      for (let i = 0; i < startIndex; i++) {
        top += this.heightMap.get(i) || this.estimateHeight
      }
      return top
    },

    // 更新已渲染项的真实高度（每次渲染后都要更新，修正高度偏差）
    updateRealHeight() {
      const items = this.$refs.listItems
      if (!items) return

      // 遍历已渲染的项，记录真实高度
      items.forEach(item => {
        // 获取该项在原始列表中的索引
        const index = Number(item.dataset.index)
        // 获取DOM真实高度（包含padding/border，最准确）
        const realHeight = item.offsetHeight
        // 只在高度变化时更新（避免重复计算）
        if (this.heightMap.get(index) !== realHeight) {
          this.heightMap.set(index, realHeight)
        }
      })

      // 重新计算列表总高度（用真实高度修正，让滚动条长度准确）
      this.calcTotalHeight()
    },

    // 重新计算列表总高度（全部用真实高度，没有则用预估）
    calcTotalHeight() {
      let total = 0
      for (let i = 0; i < this.diaryList.length; i++) {
        total += this.heightMap.get(i) || this.estimateHeight
      }
      this.totalHeight = total
    },

    // 滚动事件处理
    handleScroll() {
      // 立即计算可视区域
      this.calcVisibleRange()
      // DOM更新后，立即更新真实高度
      this.$nextTick(() => {
        this.updateRealHeight()
      })
    }
  }
}
</script>

<style>
/* 全局样式 */
#app {
  width: 400px;
  margin: 20px auto;
}

/* 虚拟列表容器：固定高度，相对定位（必须） */
.virtual-list {
  position: relative;
  height: 500px;
  overflow: auto;
  border: 1px solid #eee;
}

/* 占位层：绝对定位，用来撑出滚动条（必须） */
/* pointer-events: none 避免拦截鼠标事件 */
.list-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  pointer-events: none;
}

/* 可视区域内容：绝对定位，根据top值移动（核心） */
/* 位置由contentTop决定，算错就会空白 */
.list-content {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
}

/* 列表项样式（模拟你的日记项，高度不固定） */
.list-item {
  padding: 15px;
  border-bottom: 1px solid #f5f5f5;
  background: #fff;
  box-sizing: border-box; /* 避免padding导致宽度溢出 */
}
.list-item h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
}
.list-item p {
  margin: 0 0 5px 0;
  color: #666;
  line-height: 1.6;
}
.list-item span {
  font-size: 12px;
  color: #999;
}
</style>