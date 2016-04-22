# grails-allpay-plugin
grails 2 allpay(歐付寶) plugin

# Install

    compile ':allpay:0.1'

# Config
設定 grails-app/conf/Config.groovy

    environments {
        development {
          allpay {
            merchantID = "2000132"
            paymentType = "aio"
            returnURL = "write your returnURL"
            hashKey = "5294y06JbISpM5x9"
            hashIV = "v77hoKGq4kWxNNIS"
            url = "http://payment-stage.allpay.com.tw/Cashier/AioCheckOut"
            clientBackURL = "write your clientBackURL"
          }
        }
    }

# Usage
plugin 裡有一個 AllpayService 會負責 Allpay 付款跟檢查 Allpay 回傳資料驗證

controller 最前面加這個 Dependency Injection

    com.brix.payment.AllpayService allpayService

要付款時需要傳入 付款方式,tradeDesc,訂單說明,訂單編號,訂單金額,購買項目名稱

在用 plugin 裡的 view allpay.gsp 送給 Allpay

    def val = [:]
    def url = grailsApplication.config.allpay.url
    val = allpayService.getSendToAllpayMap("Credit", "<訂單說明>", "<訂單編號>", "<訂單金額>", "<購買項目名稱>")
    render(plugin:'allpay', view: '/sendToAllpay.gsp', model: [url:url, valueObj: val])

接收付款結果的頁面設定在 returnURL

    if (allpayService.checkReceive(params)) {
      // 收到的資料正確
      if (arams.getInt('RtnCode') == 1) {
          // 付款成功
      } else {
          // 付款失敗
      }
    }
