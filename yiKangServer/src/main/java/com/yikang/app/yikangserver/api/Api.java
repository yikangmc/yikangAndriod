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
import com.yikang.app.yikangserver.bean.Department;
import com.yikang.app.yikangserver.bean.Expert;
import com.yikang.app.yikangserver.bean.FileResponse;
import com.yikang.app.yikangserver.bean.HpWonderfulContent;
import com.yikang.app.yikangserver.bean.LablesBean;
import com.yikang.app.yikangserver.bean.Order;
import com.yikang.app.yikangserver.bean.PaintsData;
import com.yikang.app.yikangserver.bean.TagHot;
import com.yikang.app.yikangserver.bean.User;
import com.yikang.app.yikangserver.data.MyData;
import com.yikang.app.yikangserver.utils.LOG;
import com.yikang.app.yikangserver.utils.UpdateManger.AndroidUpdate;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 本应用的所有api
 * @author 郝晓东 2016-05-27
 */
public class Api {
    /** 中国主机*/
    //private static final String SERVER_HOST = "http://54.223.35.197:8088";

    /** 测试主机 */
    //private static final String SERVER_TEST = "http://54.223.53.20:8080";
    private static final String SERVER_TEST = "http://192.168.1.18:8088";

    private static final String SERVER_LOCAL_TEST2 = "http://54.223.53.20:8080";



    /************* 下面才是基地址 ***********/
    /**接口开发的地址*/
    public static final String BASE_URL = SERVER_TEST +"/yikangservice/service";
    /*** 文件上传服务器的地址*/
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
        Type type = new TypeToken<ResponseContent<List<Order>>>(){}.getType();
        ApiClient.execute(url, param, callback,type);
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
        Type type = new TypeToken<ResponseContent<Void>>(){}.getType();
        ApiClient.execute(url, param, callback,type);
    }



    /**
     * 登录
     * @param userName
     * @param password
     * @param callback
     */
    public static void login(String userName,String password,ResponseCallback<String> callback){
        String url = BASE_URL + "/login";
        RequestParam param = new RequestParam("", "");
        param.add("loginName", userName);
        param.add("passWord", password);
        param.add("machineCode", AppContext.getAppContext().getDeviceID());
        Type type = new TypeToken<ResponseContent<String>>(){}.getType();
        LOG.i("debug","type222------>"+type);
        ApiClient.execute(url, param, callback,type);
    }

    /**
     * 注册设备
     * @param codeType 设备编码类型
     * @param deviceCode 设备码
     * @param callback
     */
    public static void registerDevice(int codeType,String deviceCode,ResponseCallback<Void> callback){
        String url = BASE_URL + "/00-18-01";
        RequestParam param = new RequestParam();
        param.add("deviceType", 0);
        param.add("codeType", codeType);
        param.add("deviceCode", deviceCode);
        Type type = new TypeToken<ResponseContent<Void>>(){}.getType();
        ApiClient.execute(url, param, callback,type);
    }


    /**
     * 获取极光推送的别名
     * @param callback
     */
    public static void getPushAlias(ResponseCallback<Map<String,String>> callback){
        String url = BASE_URL + "/00-18-02";
        Type type = new TypeToken<ResponseContent<Map<String,String>>>(){}.getType();
        ApiClient.execute(url, new RequestParam(), callback,type);

    }

    /**
     * 注册
     * @param paramMap
     * @param callback
     */
    public static void register(Map<String, Object> paramMap, ResponseCallback<Void> callback){
        String url = BASE_URL + "/registerUserAndSaveServiceInfo";
        RequestParam param = new RequestParam("", "");
        param.addAll(paramMap);
        Type type = new TypeToken<Void>(){}.getType();
        ApiClient.execute(url, param, callback,type);
    }



    /**
     * 下载新版apk
     * @param callback
     */
    public static void downloadNewApk(String downloadUrl,String savePath, DownloadCallback callback){
        ApiClient.downloadFile(downloadUrl, savePath, callback);
    }

    /**
     * 检查更新
     * @param callback
     */
    public static void checkUpdate(ResponseCallback<AndroidUpdate> callback){
        String url ="";
        RequestParam param = new RequestParam();
        Type type = new TypeToken<ResponseContent<AndroidUpdate>>(){}.getType();
        ApiClient.execute(url, param, callback, type);

    }


    /**
     * 上传文件
     * @param file
     * @param callback
     */
    public static void uploadFile(File file,ResponseCallback<FileResponse> callback){
        String url = BASE_FILE_URL + "/fileUpload/imageFileUpload";
        FileRequestParam param = new FileRequestParam("headImage");
        param.addFile(file);
        LOG.i("debug","file-->"+file);
        Type type = new TypeToken<ResponseContent<FileResponse>>(){}.getType();
        ApiClient.postFilesAsyn(url, param, callback, type);

    }
    /**
     * 上传文件集合
     * @param files  上传图片集合
     * @param callback
     */
    public static void uploadListFiles(List<File> files,ResponseCallback<FileResponse> callback){
        String url = BASE_FILE_URL + "/fileUpload/imageFileUpload";
        FileRequestParam param = new FileRequestParam("headImage");
        for(File file : files){
            param.addFile(file);
            LOG.i("debug","files111-->"+file);
        }
        LOG.i("debug","files-->"+files);
        Type type = new TypeToken<ResponseContent<FileResponse>>(){}.getType();
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
    public static void registerNew(String phoneNumber, String password, ResponseCallback<Void> callback) {
        String url = BASE_URL + "/registerUser";
        RequestParam param = new RequestParam("", "");
        param.add("loginName", phoneNumber);
        param.add("password", password);
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
        Type type = new TypeToken<ResponseContent<User>>() {}.getType();
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
        Type type = new TypeToken<ResponseContent<PaintsData>>() {}.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取擅长列表
     */
    public static void getExperts(int profession,ResponseCallback<List<Expert>> callback) {
        String url = BASE_URL + "/00-23-01";
        RequestParam param = new RequestParam();
        param.add("userPosition",profession);
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
    public static void alterName(String name,ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("userName",name);
        Type type = new TypeToken<ResponseContent<Void>>() {}.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改医院
     */
    public static void alterHospital(String hospital,ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("hospital",hospital);
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
        param.add("userPosition",profession);
        Type type = new TypeToken<ResponseContent<Void>>() {}.getType();
        ApiClient.execute(url, param, callback, type);
    }



    /**
     * 修改地址
     */
    public static void alterAddr(String addressTitle,String cdCode,String detailAddress,ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("mapPositionAddress",addressTitle);
        param.add("districtCode",cdCode);
        param.add("addressDetail",detailAddress);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }



    /**
     * 修改科室
     * TODO
     */
    public static void alterOffices(String offices,ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("offices",offices);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改擅长
     */
    public static void alterExpert(List<Expert> experts,ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        ArrayList<String> special = new ArrayList<>();
        for(Expert e : experts){
            special.add(e.id);
        }
        param.add("adepts",special);
        Type type = new TypeToken<ResponseContent<Void>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }



    /**
     * 修改用户头像
     */
    public static void alterAvatar(String avatarUrl,ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("photoUrl",avatarUrl);
        Type type = new TypeToken<ResponseContent<Void>>() {}.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 修改用户头像
     */
    public static void alterWorkType(int workType,ResponseCallback<Void> callback) {
        String url = BASE_URL + "/00-17-12";
        RequestParam param = new RequestParam();
        param.add("jobCategory",workType);
        Type type = new TypeToken<ResponseContent<Void>>() {}.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 初始化用户信息
     * @param user
     * @param callback
     */
    public static void initUserInfo(User user,ResponseCallback<Void> callback){
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

        param.add("userName",user.name);
        param.add("userPosition",user.profession);
        param.add("infoWrite",user.infoStatus);

        if(!TextUtils.isEmpty(user.avatarImg))
            param.add("photoUrl",user.avatarImg);


        if(user.profession == MyData.DOCTOR){
            param.add("hospital",user.hospital);
            param.add("offices",user.department.departmentId);
        }else if(user.profession == MyData.NURSING){
            param.add("addressDetail",user.addressDetail);
            param.add("mapPositionAddress",user.mapPositionAddress);
            param.add("districtCode",user.districtCode);

            ArrayList<String> special = new ArrayList<>();
            for(Expert e : user.special){
                special.add(e.id);
            }
            param.add("adepts",special);
            param.add("jobCategory",user.jobType);
            param.add("hospital", user.hospital);

        }else if(user.profession == MyData.THERAPIST){

            param.add("addressDetail",user.addressDetail);
            param.add("mapPositionAddress",user.mapPositionAddress);
            param.add("districtCode",user.districtCode);

            ArrayList<String> special = new ArrayList<>();
            for(Expert e : user.special){
                special.add(e.id);
            }
            param.add("adepts",special);
            param.add("jobCategory",user.jobType);
        }

        Type type = new TypeToken<ResponseContent<Void>>() {}.getType();
        LOG.i("debug","type333------>"+type);
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
        Type type = new TypeToken<ResponseContent<List<BannerBean>>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 模糊搜索标签列表
     *
     * @param callback
     */
    public static void getSearchContent(String tagName,ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-27-01";
        RequestParam param = new RequestParam();
        param.add("tagName",tagName);
        Type type = new TypeToken<ResponseContent<String>>() {}.getType();
        LOG.i("debug","type------>"+type);
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

        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }
   /* public static void getWonderfulContent(ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-28-01";
        RequestParam param = new RequestParam();

        Type type = new TypeToken<ResponseContent<String>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }
*/
    /**
     * 首页精彩解答
     */
    public static void getWonderfulAnswer(ResponseCallback<String > callback) {
        String url = BASE_URL + "/00-28-01";
        RequestParam param = new RequestParam();

        Type type = new TypeToken<ResponseContent<String>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取某一个文章详情
     *
     * @param callback
     */
    public static void getDetailContent(int id,ResponseCallback<HpWonderfulContent> callback) {
        String url = BASE_URL + "/00-28-02";
        RequestParam param = new RequestParam();
        param.add("forumPostId",id);
        Type type = new TypeToken<ResponseContent<HpWonderfulContent>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }



    /**
     * 获取首页热门标签
     */
    public static void getHotLableContent(ResponseCallback<List<TagHot>> callback) {
        String url = BASE_URL + "/00-30-01";
        RequestParam param = new RequestParam();
        //param.add("forumPostId",id);
        Type type = new TypeToken<ResponseContent<List<TagHot>>>() {}.getType();
        LOG.i("debug","type------>"+type);
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
        //param.add("forumPostId",id);
        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 获取二级标签列表
     *
     * @param callback
     */
    public static void getSecondLableContent(int id,ResponseCallback<List<LablesBean>> callback) {
        String url = BASE_URL + "/00-30-03";
        RequestParam param = new RequestParam();
        param.add("parentId",id);
        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {}.getType();
        LOG.i("debug","type------>"+type);
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
        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取某一个标签下的所有问题
     */
    public static void getAllQuestionContent(int taglibId,ResponseCallback<List<LablesBean>> callback) {
        String url = BASE_URL + "/00-29-05";
        RequestParam param = new RequestParam();
        param.add("taglibId",taglibId);
        Type type = new TypeToken<ResponseContent<List<LablesBean>>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 获取某一个标签下的所有文章
     */
    public static void getAllLableContent(int taglibId,ResponseCallback<List<HpWonderfulContent>> callback) {
        String url = BASE_URL + "/00-28-06";
        RequestParam param = new RequestParam();
        param.add("taglibId",taglibId);
        Type type = new TypeToken<ResponseContent<List<HpWonderfulContent>>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 添加问题接口
     *
     * @param callback
     */
    public static void addAnswer(String title,String content,int[] taglibIds,String [] images,ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-29-01";
        RequestParam param = new RequestParam();
       // param.add("formPostId",formPostId);
        param.add("title",title);
        param.add("content",content);
        param.add("taglibIds",taglibIds);
        param.add("images",images);
        Type type = new TypeToken<ResponseContent<String>>() {}.getType();
        LOG.i("debug","type------>"+type);
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
    public static void publishContent(String title,String content,int [] taglibIds,String[] images,ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-28-04";
        RequestParam param = new RequestParam();

        param.add("title",title);
        param.add("content",content);
        param.add("taglibIds",taglibIds);//标签组
        param.add("images",images);//图片组
        Type type = new TypeToken<ResponseContent<String>>() {}.getType();
        LOG.i("debug","type------>"+type);
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 支持文章或取消支持
     * @param id  文章id
     *
     * @param callback
     */
    public static void support(int id,ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-28-03";
        RequestParam param = new RequestParam("", "");
        param.add("forumPostId", id);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 问题回答的支持或取消支持
     * @param id  文章id
     *
     * @param callback
     */
    public static void supportAnswer(int id,ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-29-02";
        RequestParam param = new RequestParam("", "");
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
    public static void addAnswerContentDetail(int formPostId,String content,ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-28-05";
        RequestParam param = new RequestParam("", "");

        param.add("formPostId", formPostId);//文章id
        param.add("content", content);
       // param.add("toUserId", toUserId);//回复给谁的

        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }


    /**
     * 查询问题详情
     * @param questionId  文章id
     *
     * @param callback
     */
    public static void searchQuestionDetail(int questionId,ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-29-04";
        RequestParam param = new RequestParam("", "");
        param.add("questionId", questionId);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }

    /**
     * 添加问题回复
     * @param questionId
     * @param callback
     */
    public static void addAnswerQuestionDetail(int questionId,String content,ResponseCallback<String> callback) {
        String url = BASE_URL + "/00-29-03";
        RequestParam param = new RequestParam("", "");
        param.add("questionId", questionId);
        param.add("content", content);
        Type type = new TypeToken<ResponseContent<String>>() {
        }.getType();
        ApiClient.execute(url, param, callback, type);
    }






}
