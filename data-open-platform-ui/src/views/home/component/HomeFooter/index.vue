<template>
  <div class="footer">
    <div class="footer-link">
      <div class="footer-wechat"><p>微信服务号</p></div>
      <div class="footer-wxapp"><p>微信小程序</p></div>
      <a href="https://www.tianapi.com/article/" title="天行数据平台公告" target="_blank">通知公告</a> |
      <a href="https://www.tianapi.com/help" title="天行数据常见问题" target="_blank">常见问题</a> |
      <a href="/support.html">联系方式</a> | <a href="https://www.tianapi.com/article/1">服务协议</a> |
      <a href="https://www.tianapi.com/article/75">隐私政策</a> |
      <a href="/about.html" title="关于天行数据" target="_blank">关于我们</a>

      <div class="footer-copy">
        <div class="footer-logo" />
        <div class="footer-info">互联网统一标准接口服务
          <a href="https://www.tianapi.com" title="天行数据API数据平台">超级码科技</a>
          移动开发API数据平台
          <div class="footer-beian"><a href="https://beian.miit.gov.cn" target="_blank">浙ICP备09106406号</a>
            <a target="_blank" href="http://www.beian.gov.cn">
              &emsp;&nbsp;&nbsp;浙公网安备 33010602001041 号</a>
          </div>
          Copyright © 2005-2025 超级码科技股份版权所有
        </div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: 'HomeFooter',
  data() {
    return {
    }
  },
  methods: {

  }
}
</script>

<style lang="scss">

.footer {
    height: 200px !important;
    width: 100%;
    padding: 50px 0;
    font-size: 12px;
    line-height: 24px;
    text-align: center;
    background: #373c3e;
    color: #fff;
    z-index: 99999;
}

.footer-link {
    clear: both;
    display: inline-block;
    width: 58%;
    height: 100px;
    padding: 6px 0;
    margin: 6px 20px;
}

.footer-wechat, .footer-wxapp {
    margin-left: 30px;
    float: right;
    display: inline-block;
    width: 106px;
    height: 106px;
    background: url(https://www.chaojima.com/images/home/code.png?v=8d786de41989f38fa41375a3130a4650) center center no-repeat;
    background-size: 98px auto;
}

.footer-wechat p, .footer-wxapp p {
    margin-top: 106px;
}

.footer-link a {
    margin: 0 12px;
    text-decoration: none;
}
.footer-copy {
    margin: 20px auto;
    width: 800px;
}

.footer-logo {
    float: left;
    display: inline-block;
    width: 76px;
    height: 70px;
    background: url(https://static.tianapi.com/static/img/touming-logo_2.png) center center no-repeat;
    background-size: 70px auto;
    margin-left: 22px;
}

.footer-info {
    width: 452px;
    margin-left: 98px;
}

.footer-copy a {
    margin: 0;
    padding: 0;
    :hover{
      text-decoration: underline;
    }
}
.footer a {
    color: #fff;
}
</style>
