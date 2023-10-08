<template>
  <div>
    <el-container>
      <el-header class="header" style="padding: 0;">
        <home-index />
      </el-header>
      <el-main style="margin:40px">
        <div>
          <el-row :gutter="40" style="margin-left:0">
            <el-col :span="4" style="background-image: linear-gradient(180deg, #17a8ed, #84ccec);border-radius:10px">
              <div class="index-menu-item1">接口分类</div>
              <div v-for="classify in classifyList" :key="classify.id" class="index-menu-item">
                <router-link :to="{ path:'/apiList', query: { classityId: classify.id} }">
                  {{ classify.name }}
                  <i class="el-icon-arrow-right" />
                </router-link>
              </div>
            </el-col>
            <el-col :span="20"><div class="index-menu-right">
              <el-carousel height="280px">
                <el-carousel-item>
                  <img style="width:100%" src="@/assets/index_images/1.jpeg">
                </el-carousel-item>
                <el-carousel-item>
                  <img style="width:100%" src="@/assets/index_images/2.jpeg">
                </el-carousel-item>
                <el-carousel-item>
                  <img style="width:100%" src="@/assets/index_images/3.jpeg">
                </el-carousel-item>
                <el-carousel-item>
                  <img style="width:100%" src="@/assets/index_images/4.jpg">
                </el-carousel-item>
              </el-carousel>
            </div></el-col>
          </el-row>
        </div>

        <div v-for="classifyApi in classifyApiList" :key="classifyApi.id" style="margin-top: 30px;">
          <div>
            <span class="apilist_square3" />
            {{ classifyApi.name }}
            <!-- <span class="apilist_sub">Lift API</span> -->
            <span class="apilist_more">
              <router-link :to="{ path:'/apiList', query: { classityId: classifyApi.id} }">
                更多>>
              </router-link>
            </span>
          </div>
          <el-row :gutter="40">
            <el-col :span="4" style="padding:0">
              <div class="apitype" :style="{'background-color':randomColor()}">
                <img class="apitypeimg" src="@/assets/life.png">
                <p class="listsub">{{ classifyApi.description }}</p>
                <router-link :to="{ path:'/apiList', query: { classityId: classifyApi.id} }" style="color:#fff">
                  全部接口>>
                </router-link>
              </div>
            </el-col>
            <el-col :span="20">
              <div class="index-menu-right">
                <template v-for="(item, index) in classifyApi.apiList">
                  <el-row v-if="index%3===0" :key="index" style="margin-left:20px">
                    <el-col :span="8" style="display: flex;justify-content: left;">
                      <router-link :to="{ path:'/apiview', query: { id: item.id} }" style="color:#fff">
                        <div class="api-item">
                          <img :src="item.imageUrl">
                          <strong>{{ item.name }}</strong>
                          <span>{{ item.description }}</span>
                          <p>
                            <small>
                              <i class="el-icon-user-solid" />
                              <span class="o">244</span>
                            </small>
                            <small>
                              <i class="el-icon-star-on" />
                              <span class="o">281</span>
                            </small>
                          </p>
                        </div>
                      </router-link>
                    </el-col>
                    <el-col v-if="classifyApi.apiList.length > index+1" :span="8" style="display: flex;justify-content: center;">
                      <router-link :to="{ path:'/apiview', query: { id: classifyApi.apiList[index+1].id} }" style="color:#fff">
                        <div class="api-item">
                          <img :src="classifyApi.apiList[index+1].imageUrl">
                          <strong>{{ classifyApi.apiList[index+1].name }}</strong>
                          <span>{{ classifyApi.apiList[index+1].description }}</span>
                          <p>
                            <small>
                              <i class="el-icon-user-solid" />
                              <span class="o">244</span>
                            </small>
                            <small>
                              <i class="el-icon-star-on" />
                              <span class="o">281</span>
                            </small>
                          </p>
                        </div>
                      </router-link>
                    </el-col>
                    <el-col v-if="classifyApi.apiList.length > index+2" :span="8" style="display: flex;justify-content: end;">
                      <router-link :to="{ path:'/apiview', query: { id: classifyApi.apiList[index+2].id} }" style="color:#fff">
                        <div class="api-item">
                          <img :src="classifyApi.apiList[index+2].imageUrl">
                          <strong>{{ classifyApi.apiList[index+2].name }}</strong>
                          <span>{{ classifyApi.apiList[index+2].description }}</span>
                          <p>
                            <small>
                              <i class="el-icon-user-solid" />
                              <span class="o">244</span>
                            </small>
                            <small>
                              <i class="el-icon-star-on" />
                              <span class="o">281</span>
                            </small>
                          </p>
                        </div>
                      </router-link>
                    </el-col>
                  </el-row>
                </template>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import HomeIndex from '@/views/home/component/HomeHeader'
import HomeFooter from '@/views/home/component/HomeFooter'
import { classifyList, apiList } from '@/api/index-api'

export default {
  name: 'Home',
  components: { HomeIndex, HomeFooter },
  data() {
    return {
      classifyList: [],
      classifyApiList: [],
      carouselList: [
        '@/assets/index_images/1.jpeg',
        '@/assets/index_images/2.jpeg',
        '@/assets/index_images/3.jpeg',
        '@/assets/index_images/4.jpg'
      ],
      apitypeColor:[
        "#fcac38", "#fca3df","#5de0dd", "#00343f", "#10b8ff", "#39d17b", "#1da3e2"
      ]
    }
  },
  computed: {
    ListArr() {
      return this.list.filter((_, index) => index === 1)
    }
  },
  mounted() {
    this.getClassifyList()
    this.getApiList()
  },
  methods: {
    randomColor(){
      const randomIndex = Math.floor(Math.random()*7)
      return this.apitypeColor[randomIndex]
    },
    toApiDetail() {
      this.$router.push({
        name: 'ApiView',
        params: {
          apiId: 123
        }
      })
    },
    getClassifyList() {
      classifyList().then(response => {
        this.classifyList = response.data
      })
    },
    getApiList() {
      apiList().then(response => {
        this.classifyApiList = response.data
      })
    }
  }
}
</script>

<style lang="scss">

.index-menu-item1{
  height: 40px;
  color: #fff;
  margin-left: -20px;
  margin-right: -20px;
  text-align: center;
  line-height: 40px;
  background: #1da3e2;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}

.apilist_square3 {
    width: 6px;
    height: 20px;
    background-color: #39d17b;
    display: inline-block;
    vertical-align: middle;
    margin-top: -2px;
    margin-right: 8px;
}
 .apilist_more a {
    font-size: 14px;
    color: #777;
    text-align: right;
    float: right;
    margin-right: 20px;
    font-weight: normal;
}
.apitype{
    border: none;
    float: left;
    width: 100%;
    height: 448px;
    margin-top: 10px;
    margin-left: 18px;
    margin-right: 22px;
    background-color: #39d17b;
    text-align: center;
    vertical-align: middle;
    position: relative;

}
.apitypeimg {
    width: 200px;
    position: absolute;
    top: 38%;
    left: 50%;
    margin-top: -96px;
    margin-left: -100px;
}

 .listsub {
    color: #fff;
    margin-top: 250px;
    padding: 0px 38px;
}

.listlink {
    color: #fff;
}
.index-menu-item{
  height: 40px;
  color: #fff;
  text-align: center;
  line-height: 40px;
  i{
    margin-left:40%
  }
}

.apilist_li_vip {
    position: absolute;
    width: 80px;
    height: 20px;
    background-color: #fda20b;
    color: white;
    text-align: center;
    transform: rotate(-40deg);
    margin-top: -6px;
    left: -26px;
    font-size: 10px;
    line-height: 20px;
}

.api-item{
    background-color: #fff;
    overflow: hidden;
    display: inline-block;
    margin: 12px 0;
    padding: 10px 0px;
    border: 1px solid #ececec;
    width: 252px;
    height: 210px;
    text-align: center;
  strong{
    font-weight: normal;
    font-size: 16px;
    padding-bottom: 6px;
    display: block;
    color: #1da3e2;
  }

  span{
    font-size: 14px;
    color: #999999;
  }

  img{
    width: 60px;
    height: 60px;
    margin: 14px 0;
  }

  p{
    margin-top: 32px;
  }
  small{
        margin: 0px 16px;
    font-size: 14px;
    color: #ccc;
  }

i{
  vertical-align: middle;
    margin-right: 0px;
    display: inline-block;
}
.o{
      margin-left: 6px;
    vertical-align: middle;
}
}

.index-menu-right{
   height: 280px;
}

 .el-carousel__item h3 {
    background: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 280px;
    margin: 0;
  }
  </style>
