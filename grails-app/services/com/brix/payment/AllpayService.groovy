package com.brix.payment

import java.security.MessageDigest
import java.text.SimpleDateFormat

/**
 *
 */
class AllpayService {
    def grailsApplication

    /**
     *
     * @param choosePayment
     * @param tradeDesc
     * @param merchantTradeNo
     * @param totalAmount
     * @param itemName
     * @return
     */
    def getSendToAllpayMap(def choosePayment, def tradeDesc, def merchantTradeNo, def totalAmount, def itemName) {
        log.info("config : ${grailsApplication.config.allpay}")
        def configuration = grailsApplication.config.allpay
        def valueObj = [:]
        switch (choosePayment) {
            case "Credit":
                valueObj["ChoosePayment"] = "Credit"
                break
            case "UnionPay":
                valueObj['ChoosePayment'] = "Credit"
                valueObj['UnionPay'] = "1"
                break
        }

        def MerchantID = configuration.merchantID
        def MerchantTradeNo = merchantTradeNo
        def MerchantTradeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())
        def PaymentType = configuration.paymentType
        def TotalAmount = totalAmount
        def TradeDesc = tradeDesc
        def ReturnURL = configuration.returnURL
        def HashKey = configuration.hashKey
        def HashIV = configuration.hashIV
        def ClientBackURL = configuration.clientBackURL

        valueObj["MerchantID"] = MerchantID
        valueObj["MerchantTradeNo"] = MerchantTradeNo
        valueObj["MerchantTradeDate"] = MerchantTradeDate
        valueObj["PaymentType"] = PaymentType
        valueObj["TotalAmount"] = TotalAmount
        valueObj["TradeDesc"] = TradeDesc
        valueObj["ItemName"] = itemName
        valueObj["ReturnURL"] = ReturnURL
        valueObj["HashKey"] = HashKey
        valueObj["HashIV"] = HashIV
        valueObj['ClientBackURL'] = ClientBackURL


        def str = ""
        valueObj.sort { it.key }.each {
            str += "${it.key}=${it.value}&"
        }

        def CheckMacValueStr = "HashKey=${HashKey}&${str}HashIV=${HashIV}"
        def CheckMacValueCode = java.net.URLEncoder.encode(CheckMacValueStr).toString().toLowerCase()
        def CheckMacValue = md5(CheckMacValueCode)
        valueObj["CheckMacValue"] = CheckMacValue
        return valueObj
    }

    /**
     *
     * @param params
     * @return
     */
    def checkReceive(def params) {
        def configuration = grailsApplication.config.allpay
        def HashKey = configuration.hashKey
        def HashIV = configuration.hashIV

        params = [TradeDate:'2016/04/08 18:43:23', SimulatePaid:'0', CheckMacValue:'7A1CAD7BDE186144104C48A8B5CFA23C',
                    PaymentType:'Credit_CreditCard', MerchantID:'2000132', RtnCode:'1', MerchantTradeNo:'201604080015',
                    RtnMsg:'交易成功', TradeNo:'1604081843235074', PaymentDate:'2016/04/08 18:51:04',
                    PaymentTypeChargeFee:'40', TradeAmt:'1464', action:'receive', controller:'shopping']

        def str = ""
        params.sort { it.key }.each {
            if (it.key != 'CheckMacValue' && it.key != 'action' && it.key != 'controller' ) {
                str += "${it.key}=${it.value}&"
            }
        }

        def CheckMacValueStr = "HashKey=${HashKey}&${str}HashIV=${HashIV}"
        def CheckMacValueCode = java.net.URLEncoder.encode(CheckMacValueStr).toString().toLowerCase()
        def CheckMacValue = md5(CheckMacValueCode)

        return CheckMacValue.toLowerCase() == params?.CheckMacValue?.toString()?.toLowerCase()
    }




    private def md5(String value) {
        return MessageDigest.getInstance("MD5").digest(value.bytes).encodeHex().toString()
    }

}
