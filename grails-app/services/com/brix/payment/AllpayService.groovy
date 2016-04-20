package com.brix.payment

import java.security.MessageDigest
import java.text.SimpleDateFormat

class AllpayService {

    static transactional = false

    def grailsApplication

    Map getSendToAllpayMap(choosePayment, tradeDesc, merchantTradeNo, totalAmount, itemName) {
        def configuration = grailsApplication.config.allpay
        log.info("config : $configuration")

        def HashKey = configuration.hashKey
        def HashIV = configuration.hashIV

        def valueObj = [
            MerchantID: configuration.merchantID,
            MerchantTradeNo: merchantTradeNo,
            MerchantTradeDate: new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()),
            PaymentType: configuration.paymentType,
            TotalAmount: totalAmount,
            TradeDesc: tradeDesc,
            ItemName: itemName,
            ReturnURL: configuration.returnURL,
            HashKey: HashKey,
            HashIV: HashIV,
            ClientBackURL: configuration.clientBackURL]

        switch (choosePayment) {
            case "Credit":
                valueObj.ChoosePayment = "Credit"
                break
            case "UnionPay":
                valueObj.ChoosePayment = "Credit"
                valueObj.UnionPay = "1"
                break
        }

        def str = new StringBuilder()
        valueObj.sort { it.key }.each {
            str << it.key << '=' << it.value << '&'
        }

        String CheckMacValueStr = "HashKey=${HashKey}&${str}HashIV=${HashIV}"
        String CheckMacValueCode = URLEncoder.encode(CheckMacValueStr).toString().toLowerCase()
        valueObj.CheckMacValue = md5(CheckMacValueCode)
        return valueObj
    }

    boolean checkReceive(params) {
        def configuration = grailsApplication.config.allpay
        def HashKey = configuration.hashKey
        def HashIV = configuration.hashIV

        params = [TradeDate:'2016/04/08 18:43:23', SimulatePaid:'0', CheckMacValue:'7A1CAD7BDE186144104C48A8B5CFA23C',
                    PaymentType:'Credit_CreditCard', MerchantID:'2000132', RtnCode:'1', MerchantTradeNo:'201604080015',
                    RtnMsg:'交易成功', TradeNo:'1604081843235074', PaymentDate:'2016/04/08 18:51:04',
                    PaymentTypeChargeFee:'40', TradeAmt:'1464', action:'receive', controller:'shopping']

        def str = new StringBuilder()
        params.sort { it.key }.each {
            if (it.key != 'CheckMacValue' && it.key != 'action' && it.key != 'controller' ) {
                str << it.key << '=' << it.value << '&'
            }
        }

        String CheckMacValueStr = "HashKey=${HashKey}&${str}HashIV=${HashIV}"
        String CheckMacValueCode = URLEncoder.encode(CheckMacValueStr).toString().toLowerCase()
        String CheckMacValue = md5(CheckMacValueCode)

        return CheckMacValue.toLowerCase() == params?.CheckMacValue?.toString()?.toLowerCase()
    }

    private String md5(String value) {
        return MessageDigest.getInstance("MD5").digest(value.bytes).encodeHex().toString()
    }
}
