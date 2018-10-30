package com.lkl.sample.channelpay.nativeapi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangyong on 2017/11/29.
 */

public class ResponseCode {

    public final static Map<Integer, String> POSP_ERR = new HashMap<Integer, String>() {
        {
            put(0xFFFFFFFF	, "失败");

            put(1		, "上送数据错误");
            put(2		, "插入数据失败");
            put(3		, "安全校验失败");
            put(4		, "交易被拒绝");
            put(5		, "创建POSP连接失败");
            put(6		, "接收POSP返回失败");
            put(7		, "数据库操作异常");
            put(8		, "网络异常");
            put(9		, "数值转换异常");
            put(10  	, "找不到服务器");
            put(11  	, "不支持的字符 ");
            put(12  	, "HTTP请求异常");
            put(13  	, "数据读取异常");
            put(14  	, "数据异常");
            put(15		, "未找到该证书信息");
            put(16		, "证书密码错误");
            put(17		, "证书文件不存在");
            put(18		, "下载证书出错");
            put(19		, "找不到应用信息");
            put(20		, "应用数据有误");
            put(21		, "应用文件不存在");
            put(22		, "未找到设备序列号对应的数据");
            put(23		, "未找到设备编号");
            put(24		, "应用文件不存在");
            put(25		, "未找到任务编号");
            put(26		, "未找到卡应用信息");
            put(27		, "未找到商户信息");
            put(28		, "未找到商户签购单名称");
            put(9999	, "系统繁忙其他未知异常");

            put(10001	, "交易超时");
            put(10002	, "网络通讯异常");
            put(10003	, "网络信号差");
            put(10004	, "连接服务器超时");
            put(10005	, "服务器无法访问");
            put(10006	, "服务器响应超时");
            put(10007	, "服务器请求超时");
            put(10008	, "连接地址未设置，请先设置");
            put(10009	, "主机地址无法解析");
            put(10010	, "创建请求对象异常");

            put(10101	, "组包异常");
            put(10102	, "数据加密异常");
            put(10103	, "未知错误");
            put(10104	, "文件不存在");
            put(10105	, "数据获取异常");
            put(10106	, "数据解析错误");
            put(10107	, "数据解析错误");
            put(10108	, "数据获取异常");

            put(10201	, "商户信息注销失败");
            put(10202	, "反激活失败");
            put(10203	, "终端验签不通过");
            put(10204	, "找不到证书文件  请联系客服");
            put(10205	, "本地处理码错误");
            put(10206	, "商户未开通");
            put(10207	, "微信商户未开通");
            put(10208	, "证书保存失败");
            put(10209	, "商户已激活，请勿重复操作");
            put(10211	, "绑定成功，数据保存失败");
            put(10214	, "终端尚未激活，请重新激活");

            put(10301	, "交易状态不明  请做扫码补单交易");
            put(10302	, "交易结果查询失败");
            put(10303	, "获取设备TID失败");
            put(10304	, "交易超时，请做被扫交易查询");
            put(10305	, "未知错误");
            put(10306	, "正在交易中");
            put(10307	, "交易超过最大笔数，请结算后再交易");

            put(10401	, "生成订单失败");
            put(10410	, "等待用户下单  此时用户没有通过扫码下单，POS+需支持查询");
            put(10411	, "交易进行中  此时用户已下单，正在支付中，POS+需支持查询");
            put(10412	, "商户订单不存在");
            put(10413	, "商户订单超时");
            put(10414	, "订单已关闭");
            put(10415	, "查询参数错误");
            put(10416	, "交易失败");
            put(10420	, "商户订单不存在  上送的交易单号错误");
            put(10421	, "商户订单已使用  订单已使用，商户应该重新生成订单");
            put(10422	, "刷新订单失败  通常由于系统错误导致");
            put(10430	, "交易进行中");
            put(10431	, "记录不存在  未找到交易记录");
            put(10432	, "关单失败  通常由于系统错误导致");
            put(10441	, "退货受理中");
            put(10442   , "记录不存在   退货上送的系统参数号不存在，未长到交易");
            put(10443   , "退货日期不符  退货上送的日期与交易日期不匹配");
            put(10444   , "该交易暂不支持退货");
            put(10445	, "退货受理失败");

            put(10446	, "读取证书密码失败");
            put(10447	, "数据签名失败");
            put(10448	, "结算对账不平");
            put(10449	, "参数格式有误");

            put(10450	, "订单已退款");

            put(20000 + 1    , "查询发卡方");
            put(20000 + 2    , "CALL BANK 查询");
            put(20000 + 3    , "无效商户");
            put(20000 + 5    , "不承兑");
            put(20000 + 10    , "承兑部分金额");
            put(20000 + 12    , "无效交易");
            put(20000 + 13    , "无效金额");
            put(20000 + 14    , "无效卡号");
            put(20000 + 19    , "稍候重做交易");
            put(20000 + 23    , "不能接受的交易费");
            put(20000 + 24    , "接收者不支持");
            put(20000 + 25    , "记录不存在");
            put(20000 + 26    , "重复的文件更新记录");
            put(20000 + 27    , "文件更新域错");
            put(20000 + 28    , "文件锁定");
            put(20000 + 29    , "文件更新不成功");
            put(20000 + 30    , "格式错误");
            put(20000 + 31    , "交换站不支持代理方");
            put(20000 + 33    , "到期卡, 请没收");
            put(20000 + 34    , "舞弊嫌疑, 请没收");
            put(20000 + 35    , "与受卡行联系");
            put(20000 + 36    , "黑名单卡, 没收");
            put(20000 + 40    , "请求的功能尚不支持");
            put(20000 + 41    , "遗失卡, 请没收");
            put(20000 + 43    , "被盗卡,请没收");
            put(20000 + 51    , "余额不足");
            put(20000 + 53    , "帐户不存在");
            put(20000 + 54    , "过期卡");
            put(20000 + 55    , "正常卡, 密码不符");
            put(20000 + 56    , "无卡记录");
            put(20000 + 57    , "持卡人无效交易");
            put(20000 + 58    , "终端无效交易");
            put(20000 + 59    , "舞蔽嫌疑");
            put(20000 + 61    , "超限额");
            put(20000 + 65    , "交易次数超限");
            put(20000 + 68    , "接收超时");
            put(20000 + 75    , "超过密码次数");
            put(20000 + 76    , "不允许手输卡号");
            put(20000 + 78    , "有效期错");
            put(20000 + 79    , "帐务处理超时");
            put(20000 + 80    , "MAC不正确");
            put(20000 + 81    , "网间MAC不正确");
            put(20000 + 82    , "返回码未定义");
            put(20000 + 83    , "无效终端");
            put(20000 + 84    , "限本地卡");
            put(20000 + 85    , "限异地信用卡");
            put(20000 + 86    , "单笔核对有误");
            put(20000 + 88    , "网络连接失败");
            put(20000 + 89    , "操作员密码错");
            put(20000 + 90    , "系统暂停");
            put(20000 + 91    , "交换站未操作");
            put(20000 + 92    , "找不到交易终点");
            put(20000 + 93    , "交易违法");
            put(20000 + 95    , "对帐不平");
            put(20000 + 96    , "系统故障");
            put(20000 + 256	, "微信支付授权码无效");
            put(20000 + 257	, "需要用户输入支付密码");
            put(20000 + 258	, "等待用户支付");
            put(20000 + 259	, "授权码无效");
            put(20000 + 0xC0   , "被扫交易处理中");
            put(20000 + 0xC1   , "识别付款码异常");

        }
    };

}
