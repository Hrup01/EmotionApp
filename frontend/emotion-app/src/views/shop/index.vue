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
                        <div class="name">小团</div>
                        <div class="price"  @click="buyItme(testObj)">2000积分</div>
                    </li>
                    <li>
                        <img src="@/assets/image/商店/形象2.png" alt="">
                        <div class="name">柴柴</div>
                        <div class="price">200积分</div>
                    </li>
                </ul>
            </div>
            <div class="woodFish">
                <div class="title">木鱼形象</div>
                <ul>
                    <li>
                        <img src="@/assets/image/商店/木鱼1.png" alt="">
                        <div class="name">水豚木鱼</div>
                        <div class="price">200积分</div>
                    </li>
                    <li>
                        <img src="@/assets/image/商店/木鱼2.png" alt="">
                        <div class="name">熊猫木鱼</div>
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
                        <div class="price">200积分</div>
                    </li>
                    <li>
                        <img src="@/assets/image/商店/麦浪.png" alt="" class="pic">
                        <div class="desc">
                            <p>麦浪声</p>
                            <div class="play">
                                <img src="@/assets/image/商店/播放.png" alt="" v-if="isPlay">
                                <img src="@/assets/image/商店/播放键_暂停.png" alt="" v-else>
                            </div>
                        </div>
                        <div class="price">200积分</div>
                    </li>
                    <li>
                        <img src="@/assets/image/商店/风铃.png" alt="" class="pic">
                        <div class="desc">
                            <p>风铃声</p>
                            <div class="play">
                                <img src="@/assets/image/商店/播放.png" alt="" v-if="isPlay">
                                <img src="@/assets/image/商店/播放键_暂停.png" alt="" v-else>
                            </div>
                        </div>
                        <div class="price">200积分</div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- 确认兑换页面 -->
    <div class="confirm-exchange toast" v-show="isExchangePage">
        <div class="subject">
            <div class="title">确认兑换</div>
            <div class="tip">本次兑换<span>{{ testObj.shopName }}</span>需要消耗<span>{{ testObj.price }}积分</span></div>
            <div class="button">
                <div class="cancel item" @click="cancelExchange">取消</div>
                <div class="confirm item" @click="confirmExchange">确定</div>
            </div>
        </div>
    </div>
    <!-- 兑换成功页面 -->
    <div class="exchange-success toast" v-show="exchangeResult">
        <div class="subject">
            <div class="title" v-if="successExchange">兑换成功</div>
            <div class="title" v-else>兑换失败</div>
            <div class="tip" v-if="successExchange">已兑换<span>{{ testObj.shopName }}</span></div>
            <div class="tip" v-else>积分不足，购买失败</div>
            <div class="button" @click="exchangeResult = false">确定</div>
        </div>
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import axios from 'axios'
export default {
    name: 'shopPage',
    components: {
        NavBar
    },
    data () {
        return {
            token: JSON.parse(localStorage.getItem('emotion_app_info')).token,
            title: '商店',
            marginLeft: '129px',
            integral: 200,
            isPlay: false,
            shopList: [],
            petList: [],
            woodFishList: [],
            whiteNoiseList: [],
            testObj: {
                id: 1,
                price: 2000,
                shopName: '小团',
                status: 1
            },
            // 确认兑换页面显示与否
            isExchangePage: false,
            // 是否为兑换商品
            isExchange: false,
            exchangeResult: false,
            successExchange: true,
            // failerExchange: ''
            // 购买所需信息
            userId: null,
            shopId: null,
            shopName: null,
            buyStatus: null,
            payPrice: null
        }
    },
    methods: {
        // 购买商品
        async buyItme (itemObj) {
            const userId = JSON.parse(localStorage.getItem('emotion_app_info')).id
            const { id:shopId, price:payPrice, shopName, status:buyStatus } = itemObj
            // console.log(userId, shopId, payPrice, shopName, buyStatus)
            this.isExchangePage = true
            this.userId = userId
            this.shopId = shopId    
            this.shopName = shopName
            this.buyStatus = buyStatus
            this.payPrice = payPrice
        },
        // 取消兑换
        cancelExchange () {
            this.isExchangePage = false
            this.isExchange = false
        },
        // 确认兑换
        async confirmExchange () {
            this.isExchangePage = false
            this.isExchange = true
            // this.successExchange = true
            const res = await axios.post('http://localhost:8080/shopOrder', {
                userId: this.userId,
                shopId: this.shopId,
                shopName: this.shopName,
                buyStatus: this.buyStatus,
                payPrice: this.payPrice
            }, {
                headers: {
                    Authorization: 'Bearer ' + this.token
                }
            })
            // console.log(res)
            if (res.data.msg === '购买失败') {
                this.successExchange = false
                
            }else {
                this.successExchange = true
            }
            this.exchangeResult = true
        }
    },
    // async mounted () {
    //     // 获取全部商品列表
    //     const res = await axios.get('http://localhost:8080/itemShop', {
    //         headers: {
    //             Authorization: 'Bearer ' + this.token
    //         }
    //     })
    //     console.log(res)
    //     this.shopList = res.data.data
    //     // console.log(this.shopList)
    //     this.petList = this.shopList.filter(item => item.type === '宠物形象')
    //     // console.log(this.petList)
    //     this.woodFishList = this.shopList.filter(item => item.type === '木鱼')
    //     this.whiteNoiseList = this.shopList.filter(item => item.type === '白噪音')
    // }
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
        // height: 1168px;
        border-radius: 20px;
        background: #fffbf4;
        .title {
            color: #472d13e6;
        }
        ul {
            margin: 12px 0 4px 0;
            display: flex;
            justify-content: space-between;
            flex-wrap: wrap;
            li {
                position: relative;
                margin-bottom: 12px;
                display: flex;
                flex-direction: column;
                align-items: center;
                // justify-content: space-between;
                width: 156px;
                border-radius: 20px;
                border: 1px solid #9965317a;
                background: #ffffff;
                img {
                    width: 100px;
                }
                .price {
                    position: absolute;
                    bottom: 13px;
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
                .name {
                    position: absolute;
                    bottom: 44px;
                    // margin-bottom: 2px;
                    color: #4f0000e8;
                }
            }
        }
        .pet {
            li {
                height: 188px;
            }
            li:nth-child(2) {
                img {
                    margin-top: 6px;
                    width: 88px;
                }
            }
        }
        .woodFish {
            li {
                height: 161px;
                img {
                    margin-top: 10px;
                }
            }
            li:nth-child(1) {
                img {
                    margin-bottom: 3px;
                    width: 81px;
                }
            }
            li:nth-child(2) {
                img {
                    margin-bottom: 5px;
                    width: 80px;
                }
            }
        }
        .whiteNoise {
            li {
                height: 150px;
                .pic {
                    margin-top: 16px;
                }
                .desc {
                    margin-top: 14px;
                    display: flex;
                    align-items: center;
                    .play {
                        img {
                            margin-left: 4px;
                            display: flex;
                            align-items: center;
                            width: 17px;
                        }
                    }
                }
                .price {
                    margin-top: 9px;
                }
            }
            li:nth-child(1) {
                .pic {
                    width: 54px;
                }
            }
            li:nth-child(2) {
                .pic {
                    width: 45px;
                }
                .desc {
                    margin-top: 8px;
                }
            }
            li:nth-child(3) {
                .pic {
                    width: 40px;
                }
                .desc {
                    margin-top: 8px;
                }
            }
        }
    }
}
.toast {
    position: fixed;
    top: 0;
    width: 390px;
    height: 844px;
    background: #00000080;
    .subject {
        position: relative;
        margin: 307px auto;
        width: 300px;
        height: 157px;
        border-radius: 10px;
        background: #ffffff;
        text-align: center;
        .title {
            margin: 29px 0 16px 0;
        }
        .tip {
            font-size: 12px;
            span {
                margin: 0 4px;
                color: #964b00;
                font-size: 14px;
            }
        }
    }
}
.confirm-exchange {
    .subject {
        .button {
            position: absolute;
            bottom: 0;
            display: flex;
            .item {
                width: 390px;
                width: 150px;
                height: 40px;
                line-height: 40px;
            }
            .cancel {
                border-top: 1px solid #ff810391;
            }
            .confirm {
                border-radius: 0 0 10px 0;
                background: #ff810391;
            }
        }
    }
}
.exchange-success {
    .button {
        margin: 18px auto;
        width: 138px;
        height: 38px;
        border-radius: 10px;
        background: #ff810391;
        line-height: 38px;
    }
}
</style>