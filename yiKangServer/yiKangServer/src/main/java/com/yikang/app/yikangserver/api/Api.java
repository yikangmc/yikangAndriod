package com.yikang.app.yikangserver.api;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yikang.app.yikangserver.api.callback.DownloadCallback;
import com.yikang.app.yikangserver.api.callback.ResponseCallback;
import com.yikang.app.yikangserver.api.client.ApiClient;
import com.yikang.app.yikangserver.api.client.FileRequestParam;
import com.yikang.app.yikangserver.api.client.RequestParam;
import com.yikang.app.yikangserver.api.client.ResponseContent;
import com.yikang.app.yikangserver.application.AppContext;
import com.yikang.app.yikangserver.bean.BannerBean;
import com.yikang.app.yikangserver.bean.ChattingAboutActivity;
import com.yikang.app.yikangserver.bean.Childs;
import com.yikang.app.yikangserver.bean.Department;
import com.yikang.app.yikangserver.bean.Expert;
import com.yikang.app.yikangserver.bean.Fanhao;
import com.yikang.app.yikangserver.bean.FileResponse;
import com.yikang.app.yikangserver.bean.FundBean;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.bean.Message;
import com.yikang.app.yikangserver.bean.MessageState;
import com.yikang.app.yikangserver.bean.MyFocusPerson;
import com.yikang.app.yikangserver.bean.Order;
import com.yikang.app.yikangserver.bean.PaintsData;
import com.yikang.app.yikangserver.bean.QuestionAnswers;
import com.yikang.app.yikangserver.bean.QuestionAnswersComment;
import com.yikang.app.yikangserver.bean.SignScore;
import com.yikang.app.yikangserver.bean.TagHot;
import com.yikang.app.yikangserver.bean.Taglibs;
import com.yikang.app.yikangserver.bean.UpdateInfoRes;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.bean.UserInfo;
import com.yikang.app.yikangserver.bean.WonderfulActivity;
import com.yikang.app.yikangserver.bean.WonderfulAnswers;
import com.yikang.app.yikangserver.data.MyData;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本应用的所有api
 *
 * @author 郝晓东 2016-05-27
 */
public class Api {
    /**
     * 中国主机
     */
   // private static final String SERVER_TEST = "http://54.223.35.197:8088";

    /** 测试主机 */
    //private static final String SERVER_TEST = "http://54.222.247.20:8080";
    /**
     * 刘帅主机
     */
    private static final String SERVER_TEST = "http://192.168.1.63:8088";
    /**
     * 积分服务器
     */
    private static final String SERVER_LOCAL_JF = "http://54.223.35.197:8088/yikangjifenservice/service";
   // private static final String SERVER_LOCAL_JF = "http://192.168.3.98:8088/yikangjifenservice/service";

    /************* 下面才是基地址 ***********/
    /**
     * 接口开发的地址
     */
    public static final String BASE_URL = SERVER_TEST + "/yikangservice/service";
    /***
     * 文件上传服务器的地址
     */
    private static final String BASE_FILE_URL = "http://54.223.35.197:8088/yikangFileManage";
    //private static final String BASE_FILE_URL ="http://192.168.1.18:8081";

    /**
     * 获取订单列表
     *
     * @param userId
     * @param callback
     */
    public static void getOrderList(String userId, ResponseCallback<List<Order>> callback) {
        String url = BASE_URL + "/00-21-07";
        RequestParam param = new RequestParam();
        if (!TextUtils.isEmpty(userId)) {
            param.add("paramUserId", userId);
        }
        Type type = new TypeToken<ResponseContent<List<Order>>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 重置密码
     *
     * @param userName
     * @param password
     * @param callback
     */
    public static void resetPassword(String userName, String password, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-08";
        RequestParam param = new RequestParam();
        param.add("loginName", userName);
        param.add("password", password);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param callback
     */
    public static void login(String userName, String password, ResponseCallback<String> callback) {
        String url = BASE_URL + "/login";
        RequestParam param = new RequestParam("", "");
        param.add("loginName", userName);
        param.add("passWord", password);
        param.add("machineCode", AppContext.getAppContext().getDeviceID());
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        // LOG.i("debug","type222------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 第三方登录接口
     */
    public static void loginThreePart(String accountId, ResponseCallback<String> callback) {
        String url = BASE_URL + "/loginForThreepartLogin";
        RequestParam param = new RequestParam("", "");
        param.add("accountId", accountId);
        param.add("machineCode", AppContext.getAppContext().getDeviceID());
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        // LOG.i("debug","type222------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 注册设备
     *
     * @param codeType   设备编码类型
     * @param deviceCode 设备码
     * @param callback
     */
    public static void registerDevice(int codeType, String deviceCode, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-18-01";
        RequestParam param = new RequestParam();
        param.add("deviceType", 0);
        param.add("codeType", codeType);
        param.add("deviceCode", deviceCode);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取极光推送的别名
     *
     * @param callback
     */
    public static void getPushAlias(ResponseCallback<Map<String, String>> callback) {
        String url = BASE_URL + "/00-18-02";
        Type type = new TypeToken<ResponseContent<Map<String, String>>>() {
        }.getType();
        ApiClient.execute(url, new RequestParam(), callback, type);

    }

    /**
     * 注册
     *
     * @param paramMap
     * @param callback
     */
    public static void register(Map<String, Object> paramMap, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/registerUserAndSaveServiceInfo";
        RequestParam param = new RequestParam("", "");
        param.addAll(paramMap);
        Type type = new TypeToken<Void>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 下载新版apk
     *
     * @param callback
     */
    public static void downloadNewApk(String downloadUrl, String savePath, DownloadCallback callback) {
        ApiClient.downloadFile(downloadUrl, savePath, callback);
    }

  /*  *//**
     * 检查更新
     * @param callback
     *//*
    public static void checkUpdate(ResponseCallback<AndroidUpdate> callback){
        String url ="";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<AndroidUpdate>>(){}.getType();
        ApiClient.execute(url, param, callback, type);

    }*/


    /**
     * 上传文件
     *
     * @param file
     * @param callback
     */
    public static void uploadFile(File file, ResponseCallback<FileResponse> callback) {
        String url = BASE_FILE_URL + "/fileUpload/imageFileUpload";
        FileRequestParam param = new FileRequestParam("headImage");
        param.addFile(file);
        // LOG.i("debug","file-->"+file);
        Type type = new TypeToken<ResponseContent<FileResponse>>() {
        }.getType();
        ApiClient.postFilesAsyn(url, param, callback, type);

    }

    /**
     * 上传文件集合
     *
     * @param files    上传图片集合
     * @param callback
     */
    public static void uploadListFiles(List<File> files, ResponseCallback<FileResponse> callback) {
        String url = BASE_FILE_URL + "/fileUpload/imageFileUpload";
        FileRequestParam param = new FileRequestParam("headImage");
        for (File file : files) {
            param.addFile(file);
            // LOG.i("debug","files111-->"+file);
        }
        // LOG.i("debug","files-->"+files);
        Type type = new TypeToken<ResponseContent<FileResponse>>() {
        }.getType();
        ApiClient.postFilesAsyn(url, param, callback, type);

    }


    /***********************V1.1************************/

    /**
     * 获取短信验证码
     *
     * @param phone
     */
    public static void getVerifyCode(String phone, final ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-25-01";
        RequestParam param = new RequestParam("", "");
        param.add("mobileNumber", phone);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type, false);
    }


    /**
     * 获取短信验证码
     *
     * @param phone
     */
    public static void verifyPhone(String phone, String verlifyCode, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-25-02";
        RequestParam param = new RequestParam("", "");
        param.add("mobileNumber", phone);
        param.add("captchar", verlifyCode);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 新版注册接口
     *
     * @param phoneNumber 手机号
     * @param password    密码
     * @param callback    回调接口
     */
    public static void registerNew(String phoneNumber, String password,String userFrom,ResponseCallback<Void> callback) {
        String url = BASE_URL + "/registerUser";
        RequestParam param = new RequestParam("", "");
        param.add("loginName", phoneNumber);
        param.add("password", password);
        param.add("userFrom", userFrom);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 新版第三方登录用户注册
     */

    public static void registerNewFromThreePart(String userName, String gender, String accountId, HashMap<String, Object> threePartAccountInfo, int userSource, String userFrom,ResponseCallback<Void> callback) {
        String url = BASE_URL + "/insertRegisterUserForThreepart";
        RequestParam param = new RequestParam("", "");
        param.add("userName", userName);
        param.add("gender", gender);
        param.add("accountId", accountId);
        param.add("threePartAccountInfo", threePartAccountInfo);
        param.add("userSource", userSource);
        param.add("userFrom", userFrom);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取用户信息
     *
     * @param callback
     */
    public static void getUserInfo(ResponseCallback<User> callback) {
        String url = BASE_URL + "/00-17-10";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<User>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取我的患者
     *
     * @param userStatus
     * @param callback
     */
    public static void getMyPaintList(int userStatus, ResponseCallback<PaintsData> callback) {
        String url = BASE_URL + "/00-17-11";
        RequestParam param = new RequestParam();
        param.add("userStatus", userStatus);
        Type type = new TypeToken<ResponseContent<PaintsData>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取某一个服务人员的信息
     */
    public static void getServerInfo(int serverUserId, ResponseCallback<UserInfo> callback) {
        String url = BASE_URL + "/00-17-15";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        Type type = new TypeToken<ResponseContent<UserInfo>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取某一个服务人员的关注的用户   38-04   serverUserId
     */
    public static void getServerFocus(int serverUserId, ResponseCallback<User> callback) {
        String url = BASE_URL + "/00-38-04";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        Type type = new TypeToken<ResponseContent<User>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }



    /**
     * 获取擅长列表
     */
    public static void getExperts(int profession, ResponseCallback<List<Expert>> callback) {
        String url = BASE_URL + "/00-23-01";
        RequestParam param = new RequestParam();
        param.add("userPosition", profession);
        Type type = new TypeToken<ResponseContent<List<Expert>>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取科室列表
     */
    public static void getDepartment(ResponseCallback<List<Department>> callback) {
        String url = BASE_URL + "/00-24-01";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<Department>>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 修改名字
     */
    public static void alterName(String name, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("userName", name);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 修改用户资料
     */
    public static void alterInformation(int jobCategory, long districtCode, String addressDetail, String photoUrl, String userPosition, String userName, String offces, int[] adepts, String userIntroduce, String oraganizationName, String workRealm, String userCertificate, String birthday, int userSex, String hospital, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("jobCategory", jobCategory);
        param.add("districtCode", districtCode);
        param.add("addressDetail", addressDetail);
        param.add("photoUrl", photoUrl);
        param.add("userPosition", userPosition);
        param.add("userName", userName);
        param.add("offces", offces);
        param.add("adepts", adepts);
        param.add("userIntroduce", userIntroduce);
        param.add("oraganizationName", oraganizationName);
        param.add("workRealm", workRealm);
        param.add("userCertificate", userCertificate);
        param.add("birthday", birthday);
        param.add("userSex", userSex);
        param.add("hospital", hospital);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改番号
     */
    public static void alterFanhao(int avatarUrl, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("designationId", avatarUrl);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改机构科室
     */
    public static void alterOffces(String avatarUrl, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("offces", avatarUrl);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 修改机构名称
     */
    public static void alterOraganizationName(String avatarUrl, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("oraganizationName", avatarUrl);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改执业证书
     */
    public static void alterUserCertificate(String avatarUrl, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("userCertificate", avatarUrl);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 修改个人简介
     */
    public static void alterUserIntroduce(String avatarUrl, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("userIntroduce", avatarUrl);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 修改用户生日
     */
    public static void alterUserBirthday(String avatarUrl, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("birthday", avatarUrl);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 修改用户性别
     */
    public static void alterUserSex(int avatarUrl, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("userSex", avatarUrl);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 修改医院
     */
    public static void alterHospital(String hospital, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("hospital", hospital);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 申请修改职位
     */
    public static void applyChangeProfession(int profession, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-13";
        RequestParam param = new RequestParam();
        param.add("userPosition", profession);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改地址
     */
    public static void alterAddr(String addressTitle, String cdCode, String detailAddress, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("mapPositionAddress", addressTitle);
        param.add("districtCode", cdCode);
        param.add("addressDetail", detailAddress);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改科室
     * TODO
     */
    public static void alterOffices(String offices, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("offices", offices);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改擅长
     */
    public static void alterExpert(List<Expert> experts, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        ArrayList<String> special = new ArrayList<>();
        for (Expert e : experts) {
            special.add(e.id);
        }
        param.add("adepts", special);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改用户头像
     */
    public static void alterAvatar(String avatarUrl, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("photoUrl", avatarUrl);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改
     */
    public static void alterWorkType(int workType, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("jobCategory", workType);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 初始化用户信息
     *
     * @param user
     * @param callback
     */
    public static void initUserInfo(User user, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";

        /**
         *  private static final int CODE_NAME = 1<<0;
         private static final int CODE_PROFESSION = 1<<1;
         private static final int CODE_ADDRESS= 1<<2;
         private static final int CODE_HOSPITAL = 1<<3;
         private static final int CODE_AVATAR = 1<<4;
         private static final int CODE_SPECIAL = 1<<5;
         private static final int CODE_OFFICE = 1<<6;
         private static final int CODE_WORK_TYPE = 1<<7;
         */


        RequestParam param = new RequestParam();

        param.add("userName", user.name);
        param.add("userPosition", user.profession);
        param.add("infoWrite", user.infoStatus);

        if (!TextUtils.isEmpty(user.avatarImg))
            param.add("photoUrl", user.avatarImg);


        if (user.profession == MyData.DOCTOR) {
            param.add("hospital", user.hospital);
            param.add("offices", user.department.departmentId);
        } else if (user.profession == MyData.NURSING) {
            param.add("addressDetail", user.addressDetail);
            param.add("mapPositionAddress", user.mapPositionAddress);
            param.add("districtCode", user.districtCode);

            ArrayList<String> special = new ArrayList<>();
            for (Expert e : user.special) {
                special.add(e.id);
            }
            param.add("adepts", special);
            param.add("jobCategory", user.jobType);
            param.add("hospital", user.hospital);

        } else if (user.profession == MyData.THERAPIST) {

            param.add("addressDetail", user.addressDetail);
            param.add("mapPositionAddress", user.mapPositionAddress);
            param.add("districtCode", user.districtCode);

            ArrayList<String> special = new ArrayList<>();
            for (Expert e : user.special) {
                special.add(e.id);
            }
            param.add("adepts", special);
            param.add("jobCategory", user.jobType);
        }

        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        // LOG.i("debug","type333------>"+type);
        ApiClient.execute(url, param, callback, type);

    }


    /**
     * 获取首页banner列表
     *
     * @param callback
     */
    public static void getBannerContent(ResponseCallback<List<BannerBean>> callback) {
        String url = BASE_URL + "/00-26-01";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<BannerBean>>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 模糊搜索标签列表
     *
     * @param callback
     */
    public static void getSearchContent(String tagName, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-27-01";
        RequestParam param = new RequestParam();
        param.add("tagName", tagName);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取首页精彩内容
     *
     * @param callback
     */
    public static void getWonderfulContent(ResponseCallback<List<HpWonderfulContent>> callback) {
        String url = BASE_URL + "/00-28-01";
        RequestParam param = new RequestParam();

        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 首页精彩解答
     */
    public static void getWonderfulAnswer(ResponseCallback<List<WonderfulAnswers>> callback) {
        String url = BASE_URL + "/00-29-06";
        RequestParam param = new RequestParam();

        Type type = new TypeToken<ResponseContent<List<WonderfulAnswers>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 首页精彩活动
     */
    public static void getWonderfulActivity(ResponseCallback<List<WonderfulActivity>> callback) {
        String url = BASE_URL + "/00-31-01";
        RequestParam param = new RequestParam();

        Type type = new TypeToken<ResponseContent<List<WonderfulActivity>>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取某一个活动
     */

    public static void getActivityDetailContent(int id, ResponseCallback<WonderfulActivity> callback) {
        String url = BASE_URL + "/00-31-02";
        RequestParam param = new RequestParam();
        param.add("activetyId", id);
        Type type = new TypeToken<ResponseContent<WonderfulActivity>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 发布活动
     */
    public static void publishActivity(String recommendPicUrl, String title, String content, String startTime, String endTime, String entryStartTime, String entryEndTime, String mapPositionAddress, String detailAddress, Long districtCode, Double lng, Double lat, int personNumber, String cost, int[] taglibs, int activetyMode, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-31-03";
        RequestParam param = new RequestParam();
        param.add("recommendPicUrl", recommendPicUrl);
        param.add("title", title);
        param.add("content", content);
        param.add("startTime", startTime);
        param.add("endTime", endTime);
        param.add("entryStartTime", entryStartTime);
        param.add("entryEndTime", entryEndTime);
        param.add("mapPositionAddress", mapPositionAddress);
        param.add("detailAddress", detailAddress);
        param.add("districtCode", districtCode);
        param.add("lng", lng);
        param.add("lat", lat);
        param.add("personNumber", personNumber);
        param.add("cost", cost);
        param.add("taglibs", taglibs);
        param.add("activetyMode", activetyMode);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 报名参加某一个活动
     */
    public static void joinActivity(int id, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-31-04";
        RequestParam param = new RequestParam();
        param.add("activetyId", id);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 评论某一个活动
     */
    public static void commentActivity(int id, String content, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-31-05";
        RequestParam param = new RequestParam();
        param.add("activetyId", id);
        param.add("content", content);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取某一个活动所有评论
     */
    public static void getAllCommentsAboutActivity(int id, ResponseCallback<List<ChattingAboutActivity>> callback) {
        String url = BASE_URL + "/00-31-06";
        RequestParam param = new RequestParam();
        param.add("activetyId", id);
        Type type = new TypeToken<ResponseContent<List<ChattingAboutActivity>>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取某一个标签的详情
     */
    public static void getTaglibId(int id, ResponseCallback<Taglibs> callback) {
        String url = BASE_URL + "/00-30-07";
        RequestParam param = new RequestParam();
        param.add("taglibId", id);
        Type type = new TypeToken<ResponseContent<Taglibs>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取某一个人参加的所有活动
     */

    public static void getAllActiviysWhoTakePartIn(Long serverUserId,int page,ResponseCallback<List<WonderfulActivity>> callback) {
        String url = BASE_URL + "/00-31-08";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<WonderfulActivity>>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 发布的活动
     * @param callback
     * 31-09查询自己的
     */
    public static void getAllActiviysWhoPublish(Long serverUserId,int page,ResponseCallback<List<WonderfulActivity>> callback) {
        String url = BASE_URL + "/00-31-09";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<WonderfulActivity>>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 发布的活动
     * @param callback
     * 31-09查询自己的
     */
    public static void getAllActiviysPersonPublish(Long serverUserId,int page,ResponseCallback<List<WonderfulActivity>> callback) {
        String url = BASE_URL + "/00-31-10";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<WonderfulActivity>>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }



    /**
     * 获取某一个文章详情
     *
     * @param callback
     */
    public static void getDetailContent(int id, ResponseCallback<HpWonderfulContent> callback) {
        String url = BASE_URL + "/00-28-02";
        RequestParam param = new RequestParam();
        param.add("forumPostId", id);
        Type type = new TypeToken<ResponseContent<HpWonderfulContent>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取首页热门标签
     */
    public static void getHotLableContent(ResponseCallback<List<TagHot>> callback) {
        String url = BASE_URL + "/00-30-01";
        RequestParam param = new RequestParam();
        //param.add("forumPostId",id);
        Type type = new TypeToken<ResponseContent<List<TagHot>>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取一级标签列表
     *
     * @param callback
     */
    public static void getFristLableContent(ResponseCallback<List<LablesBean>> callback) {
        String url = BASE_URL + "/00-30-02";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取二级标签列表
     *
     * @param callback
     */
    public static void getSecondLableContent(int id, ResponseCallback<List<Childs>> callback) {
        String url = BASE_URL + "/00-30-03";
        RequestParam param = new RequestParam();
        param.add("parentId", id);
        Type type = new TypeToken<ResponseContent<List<Childs>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取一级二级标签列表
     *
     * @param callback
     */
    public static void getFirstSecondLableContent(ResponseCallback<List<LablesBean>> callback) {
        String url = BASE_URL + "/00-30-04";
        RequestParam param = new RequestParam();

        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 二级所有标签列表
     *
     * @param callback
     */
    public static void getAllLable(ResponseCallback<List<LablesBean>> callback) {
        String url = BASE_URL + "/00-30-06";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 推荐标签
     *
     * @param callback
     */

    public static void getTuijianAllLable(ResponseCallback<List<LablesBean>> callback) {
        String url = BASE_URL + "/00-41-02";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 最热帖子
     *
     * @param callback
     */
    public static void getHotTiezi(int page,ResponseCallback<List<HpWonderfulContent>> callback) {
        String url = BASE_URL + "/00-28-17";
        RequestParam param = new RequestParam();
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 系统消息
     *
     * @param callback
     */
    public static void getSystemMessage(int page,ResponseCallback<List<Message>> callback) {
        String url = BASE_URL + "/00-35-07";
        RequestParam param = new RequestParam();
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<Message>>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 动态消息
     *
     * @param callback
     */
    public static void getDynamicMessage(ResponseCallback<List<Message>> callback) {
        String url = BASE_URL + "/00-35-02";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<Message>>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 动态消息
     *
     * @param callback
     */
    public static void getDynamicMessageNew(int page,ResponseCallback<List<Message>> callback) {
        String url = BASE_URL + "/00-35-06";
        RequestParam param = new RequestParam();
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<Message>>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }



    /**
     * 我的问题
     *
     * @param callback
     *
     * 29-07
     */
    public static void getMyQuestion(Long serverUserId,int page,ResponseCallback<List<QuestionAnswers>> callback) {
        String url = BASE_URL + "/00-29-08";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<QuestionAnswers>>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 我的帖子
     *
     * @param callback
     *
     * 28-12
     */
    public static void getMyTiezi(Long serverUserId,int page,ResponseCallback<List<HpWonderfulContent>> callback) {
        String url = BASE_URL + "/00-28-15";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 我的解答
     *39-03
     * @param callback
     */
    public static void getMyAnswer(Long serverUserId,int page,ResponseCallback<List<QuestionAnswers>> callback) {
        String url = BASE_URL + "/00-39-03";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<QuestionAnswers>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取某一个标签下的所有问题
     */
    public static void getAllQuestionContent(int taglibId, int page, ResponseCallback<List<LablesBean>> callback) {
        String url = BASE_URL + "/00-29-05";
        RequestParam param = new RequestParam();
        param.add("taglibId", taglibId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取某一个标签下的所有文章
     */
    public static void getAllLableContent(int taglibId, int page, ResponseCallback<List<HpWonderfulContent>> callback) {
        String url = BASE_URL + "/00-28-06";
        RequestParam param = new RequestParam();
        param.add("taglibId", taglibId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取某一个标签下的所有文章
     */
    public static void getAllLableContents(int taglibId, int page, ResponseCallback<List<HpWonderfulContent>> callback) {
        String url = BASE_URL + "/00-28-09";
        RequestParam param = new RequestParam();
        param.add("taglibId", taglibId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 发布专业内容
     */

    public static void publishProfessionalContent(String title, String forumPostDetailContent, String forumPostHtmlDetailContent, String recommendPicUrl, int[] taglibIds, String[] images, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-28-10";
        RequestParam param = new RequestParam();
        param.add("title", title);
        param.add("forumPostDetailContent", forumPostDetailContent);
        param.add("forumPostHtmlDetailContent", forumPostHtmlDetailContent);
        param.add("recommendPicUrl", recommendPicUrl);
        param.add("taglibIds", taglibIds);//标签组
        param.add("images", images);//图片组
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    public static void publishProfessionalContentNoPic(String title, String forumPostDetailContent, String forumPostHtmlDetailContent, String recommendPicUrl, int[] taglibIds, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-28-10";
        RequestParam param = new RequestParam();
        param.add("title", title);
        param.add("forumPostDetailContent", forumPostDetailContent);
        param.add("forumPostHtmlDetailContent", forumPostHtmlDetailContent);
        param.add("recommendPicUrl", recommendPicUrl);
        param.add("taglibIds", taglibIds);//标签组
        //param.add("images", images);//图片组
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 我的专业内容
     * 28-11
     */
    public static void getMyContents(Long serverUserId,int page,ResponseCallback<List<HpWonderfulContent>> callback) {
        String url = BASE_URL + "/00-28-16";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    public static void getMyContent(Long serverUserId,int page,ResponseCallback<List<LablesBean>> callback) {
        String url = BASE_URL + "/00-28-16";
        RequestParam param = new RequestParam();
        param.add("serverUserId", serverUserId);
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 添加问题接口
     *
     * @param callback
     */
    public static void addAnswer(String title, String content, int[] taglibIds, String[] images, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-29-01";
        RequestParam param = new RequestParam();
        param.add("title", title);
        param.add("content", content);
        param.add("taglibIds", taglibIds);
        param.add("images", images);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 文章发布,帖子发布
     *
     * @param title
     * @param content
     * @param taglibIds
     * @param callback
     */
    public static void publishContent(String title, String content, int[] taglibIds, String[] images, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-28-04";
        RequestParam param = new RequestParam();

        param.add("title", title);
        param.add("content", content);
        param.add("taglibIds", taglibIds);//标签组
        param.add("images", images);//图片组
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /*public static void publishContent(String title,String content,int [] taglibIds,String[] images,ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-28-04";
        RequestParam param = new RequestParam();

        param.add("title",title);
        param.add("content",content);
        param.add("taglibIds",taglibIds);//标签组
        param.add("images",images);//图片组
        Type type = new TypeToken<ResponseContent<String>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }*/


    /**
     * 支持文章或取消支持
     *
     * @param id       文章id
     * @param callback
     */
    public static void support(int id, ResponseCallback<HpWonderfulContent> callback) {
        String url = BASE_URL + "/00-28-03";
        RequestParam param = new RequestParam();
        param.add("forumPostId", id);
        Type type = new TypeToken<ResponseContent<HpWonderfulContent>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 问题回答的支持或取消支持
     *
     * @param id       文章id
     * @param callback
     */
    public static void supportAnswer(int id, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-29-02";
        RequestParam param = new RequestParam();
        param.add("questionAnswerId", id);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 添加文章回复
     *
     * @param callback
     */
    public static void addAnswerContentDetail(int formPostId, String content, int toUserId, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-28-05";
        RequestParam param = new RequestParam();

        param.add("formPostId", formPostId);//文章id
        param.add("content", content);
        param.add("toUserId", toUserId);//回复给谁的

        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 查询问题详情
     *
     * @param questionId 文章id
     * @param callback
     */
    public static void searchQuestionDetail(int questionId, ResponseCallback<HpWonderfulContent> callback) {
        String url = BASE_URL + "/00-29-04";
        RequestParam param = new RequestParam();
        param.add("questionId", questionId);
        Type type = new TypeToken<ResponseContent<HpWonderfulContent>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 添加问题回复
     *
     * @param questionId
     * @param callback
     */
    public static void addAnswerQuestionDetail(int questionId, String detailContent, String htmlDetailContent, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-29-03";
        RequestParam param = new RequestParam();
        param.add("questionId", questionId);
        param.add("detailContent", detailContent);
        param.add("htmlDetailContent", htmlDetailContent);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 添加关注人员
     */
    public static void addMyFocusPerson(int followUserId, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-38-01";
        RequestParam param = new RequestParam();
        param.add("followUserId", followUserId);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 删除关注人员
     *
     * @param followUserId
     * @param callback
     */
    public static void deleteMyFocusPerson(int followUserId, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-38-03";
        RequestParam param = new RequestParam();
        param.add("followUserId", followUserId);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        // LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 添加关注标签
     */
    public static void addMyFocusLables(int taglibId, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-36-01";
        RequestParam param = new RequestParam();
        param.add("taglibId", taglibId);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 删除关注标签
     */
    public static void deleteMyFocusLables(int taglibId, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-36-02";
        RequestParam param = new RequestParam();
        param.add("taglibId", taglibId);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 关注下的专业人士
     */
    public static void getAllMyFocusPerson(ResponseCallback<List<MyFocusPerson>> callback) {
        String url = BASE_URL + "/00-38-02";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<MyFocusPerson>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 关注下的收藏
     */
    public static void getAllMyFocusCollection(ResponseCallback<List<HpWonderfulContent>> callback) {
        String url = BASE_URL + "/00-37-01";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 添加收藏
     *
     * @param callback
     */
    public static void addMyFocusCollections(int forumPostId, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-37-03";
        RequestParam param = new RequestParam();
        param.add("forumPostId", forumPostId);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 删除收藏
     *
     * @param forumPostId
     * @param callback
     */
    public static void adeleteMyFocusCollections(int forumPostId, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-37-02";
        RequestParam param = new RequestParam();
        param.add("forumPostId", forumPostId);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 关注下的标签
     */
    public static void getAllMyFocusLables(ResponseCallback<List<Taglibs>> callback) {
        String url = BASE_URL + "/00-36-03";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<Taglibs>>>() {
        }.getType();
        //    LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 发现模块轮播图
     */
    public static void getFindBannerContent(ResponseCallback<List<BannerBean>> callback) {
        String url = BASE_URL + "/00-26-02";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<BannerBean>>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取我的番号列表
     */
   /* public static void getMineFanhao(ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-40-01";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<String>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);


        39-02回答的详情
    }*/
    public static void getMineFanhao(ResponseCallback<List<Fanhao>> callback) {
        String url = BASE_URL + "/00-40-01";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<Fanhao>>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 问题的回答的详情
     *
     * @param forumPostId
     * @param callback
     */
    public static void answerDetail(int forumPostId, ResponseCallback<QuestionAnswers> callback) {
        String url = BASE_URL + "/00-39-02";
        RequestParam param = new RequestParam();
        param.add("questionAnswerId", forumPostId);
        Type type = new TypeToken<ResponseContent<QuestionAnswers>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 社区发现推荐康复师
     */
    public static void getKangfushi(ResponseCallback<List<User>> callback) {
        String url = BASE_URL + "/00-41-01";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<List<User>>>() {
        }.getType();
        //   LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 意见反馈   申请认证:17 -12
     */

    public static void submitFankui(String content, String connectMethod, ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-42-01";
        RequestParam param = new RequestParam();
        param.add("content", content);
        param.add("connectMethod", connectMethod);
        Type type = new TypeToken<String>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 二维码上传00-43-01
     */

    public static void submitErweima(String uniqueCode, int editorType, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-43-01";
        RequestParam param = new RequestParam();
        param.add("uniqueCode", uniqueCode);
        param.add("editorType", editorType);
        Type type = new TypeToken<Void>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 版本更新00-44-01
     */
    public static void updateVersion(int mobilePhoneType, ResponseCallback<UpdateInfoRes.UpdateData> callback) {
        String url = BASE_URL + "/00-44-01";
        RequestParam param = new RequestParam();
        param.add("mobilePhoneType", mobilePhoneType);//0:ios  1:安卓
        Type type = new TypeToken<ResponseContent<UpdateInfoRes.UpdateData>>() {
        }.getType();
        //  LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 积分之获取任务记录
     */

    public static void getJFDetail(ResponseCallback<FundBean> callback) {
        String url = SERVER_LOCAL_JF + "/00-01-01";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<FundBean>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 领取积分
     */
    public static void receiveJF(long jobId , ResponseCallback<FundBean> callback) {
        String url =SERVER_LOCAL_JF + "/00-01-02";
        RequestParam param = new RequestParam();
        param.add("jobId", jobId );
        Type type = new TypeToken<ResponseContent<FundBean>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 签到接口
     */
    public static void signJF(ResponseCallback<SignScore> callback) {
        String url =SERVER_LOCAL_JF + "/00-02-01";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<SignScore>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 相关推荐数据
     */
    public static void getXiangguan(long forumPostId ,ResponseCallback<List<HpWonderfulContent>> callback) {
        String url =BASE_URL + "/00-28-13";
        RequestParam param = new RequestParam();
        param.add("forumPostId", forumPostId );
        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 增加解答回复评论接口
     * @param callback
     */
    public static void addHuifuForQuestion(long question_answer_id,long to_userId ,String content,ResponseCallback<Void> callback) {
        String url =BASE_URL + "/00-45-01";
        RequestParam param = new RequestParam();
        param.add("question_answer_id", question_answer_id );
        param.add("to_userId", to_userId );
        param.add("content", content );
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 增加解答
     * @param question_answer_id
     * @param content
     * @param callback
     */
    public static void addAnswerForQuestion(long question_answer_id ,String content,ResponseCallback<String> callback) {
        String url =BASE_URL + "/00-45-01";
        RequestParam param = new RequestParam();
        param.add("question_answer_id", question_answer_id );
        param.add("content", content );
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 评论列表的查询
     */
    public static void answersForQuestion(long question_answer_id ,int page,ResponseCallback<List<QuestionAnswersComment>> callback) {
        String url =BASE_URL + "/00-45-02";
        RequestParam param = new RequestParam();
        param.add("question_answer_id", question_answer_id );
        param.add("page", page);
        Type type = new TypeToken<ResponseContent<List<QuestionAnswersComment>>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 根据登录用户ID查询他的动态/系统 消息的未读数量
     */

    public static void messageState(ResponseCallback<MessageState> callback) {
        String url =BASE_URL + "/00-35-04";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<MessageState>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 通过消息标识标记此条状态为已读
     */


    public static void changeMessageState(long messagesId ,ResponseCallback<String> callback) {
        String url =BASE_URL + "/00-35-03";
        RequestParam param = new RequestParam();
        param.add("messagesId", messagesId );
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取某一个用户的消息配置
     */
    public static void messageSettingState(ResponseCallback<MessageState> callback) {
        String url =BASE_URL + "/00-32-01";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<MessageState>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 修改用户消息开头配置
     * @param callback
     */
    public static void changeMessageSettingSysState(int systemAlert ,ResponseCallback<Void> callback) {
        String url =BASE_URL + "/00-32-02";
        RequestParam param = new RequestParam();
        param.add("systemAlert", systemAlert );

        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    public static void changeMessageSettingDynState(int dynamicAlert,ResponseCallback<Void> callback) {
        String url =BASE_URL + "/00-32-02";
        RequestParam param = new RequestParam();
        param.add("dynamicAlert", dynamicAlert );
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

}
