<template>
  <div id="shop">
    <NavBar :margin-left="marginLeft"></NavBar>
    <div class="navbarLeft">
        <div class="integral">{{ integral }}</div>
        <img src="@/assets/image/商店/星星.png" alt="">
    </div>
    <div class="box">
        <div class="subject">
            <div class="pet">
                <div class="title">宠物形象</div>
                <ul>
                    <li>
                        <img src="@/assets/image/商店/形象1.png" alt="">
                        <div class="price">200积分</div>
                    </li>
                    <li>
                        <img src="@/assets/image/商店/形象2.png" alt="">
                        <div class="price">200积分</div>
                    </li>
                </ul>
            </div>
            <div class="woodFish">
                <div class="title">木鱼形象</div>
                <ul>
                    <li>
                        <img src="@/assets/image/商店/木鱼1.png" alt="">
                        <div class="price">200积分</div>
                    </li>
                    <li>
                        <img src="@/assets/image/商店/木鱼2.png" alt="">
                        <div class="price">200积分</div>
                    </li>
                </ul>
            </div>
            <div class="whiteNoise">
                <div class="title">白噪音形象</div>
                <ul>
                    <li>
                        <img src="@/assets/image/商店/海豚.png" alt="" class="pic">
                        <div class="desc">
                            <p>海豚声</p>
                            <div class="play">
                                <img src="@/assets/image/商店/播放.png" alt="" v-if="isPlay">
                                <img src="@/assets/image/商店/播放键_暂停.png" alt="" v-else>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
    name: 'shopPage',
    components: {
        NavBar
    },
    data () {
        return {
            title: '商店',
            marginLeft: '129px',
            integral: 200,
            isPlay: false
        }
    }
}
</script>

<style lang="less" scoped>
#shop {
    width: 390px;
    height: 852px;
    background: #feefce;
}
.navbarLeft {
    position: absolute;
    top: 50px;
    right: 12px;
    height: 30px;
    width: 100px;
    // background-color: #fff;
    .integral {
        margin-top: 5px;
        width: 80px;
        height: 20px;
        border-radius: 20px;
        border: 1px solid #331a007d;
        background: #ffffff;
        line-height: 20px;
        text-align: center;
        color: #000000b3;
        font-size: 14px;
    }
    img {
        position: absolute;
        top: -4px;
        right: 8px;
        width: 34px;
    }
}
.box {
    margin: 7px auto 0 auto;
    height: 755px;
    width: 366px;
    overflow-y: auto;
    .subject {
        padding: 20px 22px;
        width: 366px;
        height: 1168px;
        border-radius: 20px;
        background: #fffbf4;
        .title {
            color: #472d13e6;
        }
        ul {
            margin: 12px 0 16px 0;
            display: flex;
            justify-content: space-between;
            li {
                display: flex;
                flex-direction: column;
                align-items: center;
                width: 156px;
                border-radius: 20px;
                border: 1px solid #9965317a;
                background: #ffffff;
                img {
                    margin: 14px 0 12px 0;
                    width: 100px;
                }
                .price {
                    width: 100px;
                    height: 30px;
                    border-radius: 30px;
                    background: #ffc48af5;
                    box-shadow: inset -2px -2px 4px 0 #ff633866;
                    color: #5c0000e8;
                    font-size: 14px;
                    line-height: 30px;
                    text-align: center;
                }
            }
        }
        .pet {
            li {
                height: 188px;
            }
            li:nth-child(2) {
                img {
                    width: 93px;
                }
            }
        }
        .woodFish {
            li {
                height: 161px;
            }
            li:nth-child(1) {
                img {
                    width: 96px;
                }
            }
            li:nth-child(2) {
                img {
                    margin-bottom: 15px;
                    width: 96px;
                }
            }
        }
        .whiteNoise {
            li {
                height: 150px;
            }
            li:nth-child(1) {
                .pic {
                    width: 54px;
                }
                .desc {
                    display: flex;
                    align-items: center;
                    .play {
                        img {
                            width: 17px;
                        }
                    }
                }
            }
        }
    }
}
</style>