package com.lkl.sample.channelpay.nativeapi;

import android.content.Context;

/**
 * Created by huangyong on 2017/11/29.
 */

public class CppSdkInterf {

    public CppSdkInterf(Context ctx) {
        ctx_ = ctx;
    }
    public String getDBPath() {
        String path = ctx_.getFilesDir().getAbsolutePath();
        //Log.d("debug-hy", "getDBPath: " + path);
        return path;
    }

    public int doActive(String activeCode) { return active(activeCode); }
    public int doDeactive() { return deActive(); }
    public int doLogin() { return login(); }

    //配置
    public String getVersion() { return getVersionNo(); }
    public String getMerchID() { return getMerchantId(); }
    public String getTermID() { return getTerminalId(); }
    public int getMaxTransNumber() { return getMaxTransNum(); }
    public void setMaxTransNumber(int maxNum) { setMaxTransNum(maxNum); }
    public int getTimeOut() { return getTransTimeOut(); }
    public void setTimeOut(int timeOut) { setTransTimeOut(timeOut); }
    public String getMerchName() { return getMerchantName(); }
    public String getTransNo() { return getCurrentTransNo(); }
    public int setTransNo(String transNo) { return setCurrentTransNo(transNo); }
    public String getBatchNo() { return getCurrentBatchNo(); }
    public int setBatchNo(String batchNo) { return setCurrentBatchNo(batchNo); }

    public String getLocation() { return getTermLocation(); }
    public int setLocation(String location) { return setTermLocation(location); }
    public String getModel() { return getTermModel(); }
    public int setModel(String model) { return setTermModel(model); }
    public String getMfrs() { return getTermMfrs(); }
    public int setMfrs(String mfrs) { return setTermMfrs(mfrs); }
    public String getACode() { return getAppCode(); }
    public int setACode(String appCode) { return setAppCode(appCode); }
    public String getAVer() { return getAppVer(); }
    public int setAVer(String appVer) { return setAppVer(appVer); }
    public String getSN() { return getTermSN(); }
    public int setSN(String sn) { return setTermSN(sn); }
    public String getFPrint() { return getFingerprint(); }
    public int setFPrint(String fingerprint) { return setFingerprint(fingerprint); }

    public int setTid(String tid) { return setDeviceId(tid); }
    public void setSqliteDBPath(String path) { setDBPath(path); }

    //被扫
    public int doConsume(String amount, String channel, String code, String thirdOrderNo, String thirdComment, LKLCodePayTransCodeResult rslt) {
        return consume(amount, channel, code, thirdOrderNo, thirdComment, rslt);
    }
    public int doRevoke(String oriTransNo, String oriBatchNo, LKLCodePayTransCodeResult rslt) {
        return revoke(oriTransNo, oriBatchNo, rslt);
    }
    public int doRefund(String oriRefNo, String oriDate, String amount, LKLCodePayTransCodeResult rslt) {
        return refund(oriRefNo, oriDate, amount, rslt);
    }
    public int doQuery(String oriTransNo, String batchNo, String oriDate, LKLCodePayTransCodeResult rslt) {
        return query(oriTransNo, batchNo, oriDate, rslt);
    }
    public int doSupplement(String oriOrderNo, LKLCodePayTransCodeResult rslt) {
        return supplement(oriOrderNo, rslt);
    }
    public int doSettle() {
        return settle();
    }

    //主扫
    public int doQRConsume(String amount, String orderId,String subject, String description, String location, LKLCodePayQRTransCodeResult rslt) {
        return qrConsume(amount, orderId, subject, description, location, rslt);
    }
    public int doQRQueryState(String tradeNo,String payNo, String lklOrderNo, LKLCodePayQRTransCodeResult rslt) {
        return qrQueryState(tradeNo, payNo, lklOrderNo, rslt);
    }
    public int doQRRefreshCode(String tradeNo, String orderId, LKLCodePayQRTransCodeResult rslt) {
        return qrRefreshCode(tradeNo, orderId, rslt);
    }
    public int doQRCloseOrder(String tradeNo, LKLCodePayQRTransCodeResult rslt) {
        return qrCloseOrder(tradeNo, rslt);
    }
    public int doQRRefund(String sRefNo, String oriDate,String amount, LKLCodePayQRTransCodeResult rslt) {
        return qrRefund(sRefNo, oriDate, amount, rslt);
    }

    private Context ctx_;

    ///////////////////////////////////////////////////
    //native functions

    //激活接口
    private native int active(String activeCode);
    //反激活接口
    private native int deActive();
    //签到
    private native int login();
    //配置接口
    private native String getVersionNo();
    private native String getMerchantId();
    private native String getTerminalId();

    private native int getMaxTransNum();
    private native void setMaxTransNum(int maxTransNum);

    private native int getTransTimeOut();
    private native void setTransTimeOut(int timeOut);

    private native String getMerchantName();

    private native String getCurrentTransNo();
    private native int setCurrentTransNo(String transNo);

    private native String getCurrentBatchNo();
    private native int setCurrentBatchNo(String batchNo);

    private native String getTermLocation();
    private native int setTermLocation(String location);

    private native String getTermModel();
    private native int setTermModel(String model);

    private native String getTermMfrs();
    private native int setTermMfrs(String mfrs);

    private native String getAppCode();
    private native int setAppCode(String appCode);

    private native String getAppVer();
    private native int setAppVer(String appVer);

    private native String getTermSN();
    private native int setTermSN(String sn);

    private native String getFingerprint();
    private native int setFingerprint(String fingerprint);

    private native int setDeviceId(String deviceId);
    private native void setDBPath(String dbPath);

    //被扫接口
    private native int consume(String amount, String channel, String code, String thirdOrderNo, String thirdDesc, Object rslt);
    private native int revoke(String oriTransNo, String oriBatchNo, Object rslt);
    private native int refund(String oriRefNo, String oriDate, String amount, Object rslt);
    private native int query(String oriTransNo, String batchNo, String oriDate, Object rslt);
    private native int supplement(String oriOrderNo, Object rslt);
    private native int settle();

    //主扫接口
    private native int qrConsume(String amount, String orderId,String subject, String description, String location, Object rslt);
    private native int qrQueryState(String tradeNo,String payNo, String lklOrderNo, Object rslt);
    private native int qrRefreshCode(String tradeNo, String orderId, Object rslt);
    private native int qrCloseOrder(String tradeNo, Object rslt);
    private native int qrRefund(String sRefNo, String oriDate,String amount, Object rslt);

    //Init
    static {
        System.loadLibrary("lklcodepaysdk");
    }
}
