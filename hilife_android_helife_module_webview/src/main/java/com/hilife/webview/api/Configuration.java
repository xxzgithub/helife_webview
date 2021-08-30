
package com.hilife.webview.api;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.dajia.android.base.exception.AppException;
import com.dajia.android.base.util.StringUtil;
import com.hilife.mobile.android.base.BaseConfiguration;
import com.hilife.mobile.android.base.cache.CacheAppData;
import com.hilife.mobile.android.base.constant.BaseConstant;
import com.hilife.mobile.android.framework.DJUtil;
import com.hilife.view.BuildConfig;
import com.hilife.view.R;
import com.hilife.view.mcompnents.LogComponent;
import com.hilife.view.mcompnents.PrivacyComponent;
import com.hilife.view.other.cache.DJCacheUtil;
import com.hilife.view.other.util.Constants;
import com.hilife.view.other.util.DJFileUtil;

import java.util.Iterator;
import java.util.Map;

public class Configuration extends BaseConfiguration {

    /**
     * 隐私协议api
     *
     * @param mContext
     * @return
     */
    public static String getPortalmanageUrl(Context mContext) {
        return getPortalmanageUrl(mContext, R.string.getPrivacy);
    }

    public static String getUnregistApi(Context mContext) {
        return getPortalmanageUrl(mContext, R.string.doUnregist);
    }

    public static String getUnregistNoticeUrl(Context mContext) {
        return getPortalmanageUrl(mContext, R.string.unregistNotice);
    }

    public static String getUnregistStatusApi(Context mContext) {
        return getPortalmanageUrl(mContext, R.string.getUnregistStatus);
    }

    public enum ExpState {
        ExpStateFormal(0),//体验已登录
        ExpStateNoLogin(1),//体验未登录
        ExpStateNo(2);//非体验
        public int code;

        ExpState(int code) {
            this.code = code;
        }

        public static ExpState expStateWithCode(int code) {
            ExpState[] values = ExpState.values();
            for (ExpState expState : values) {
                if (expState.code == code) {
                    return expState;
                }
            }
            return null;
        }

    }

    private static ExpState MISEXP;
    // 认证服务器地址
    public static String OAUTH_HOST;
    // 移动服务器地址
    public static String MOBILE_HOST;
    // 管家服务器地址
    public static String HOUSEKEEPER_HOST;
    // 门禁服务器地址
    public static String ERP_HOST;
    // 楼户服务器地址
    public static String HOUSE_HOST;
    //缴费服务器地址
    public static String ESTATEPAYMENT_HOST;
    //支付服务器地址
    public static String PAYMENTINTEGRATION_HOST;
    //报修服务器地址
    public static String TICKETS_HOST;
    //广告管理服务器地址
    public static String ADVERT_HOST;
    //shop管理服务器地址
    public static String VShOP_HOST;
    //积分服务器地址
    public static String SCORE_HOST;
    // web服务器地址
    public static String WEB_HOST;
    // web服务器地址
    public static String MD_WEB_HOST;
    // web即时通讯服务器地址
    public static String MOBILE_IM_HOST;
    // web支付服务器地址
    public static String MOBILE_PAY_HOST;
    // 移动端计步服务器地址
    public static String MOBILE_STEP_HOST;
    // 移动端性能分析提交服务器地址
    public static String MOBILE_PERFORMANCE_HOST;

    public static String PORTALMANAGE_HOST;

    public static ExpState getMISEXP(Context context) {
        if (MISEXP == null) {
            MISEXP = ExpState.expStateWithCode(CacheAppData.readInt(context, Constants.KEY_EXP, ExpState.ExpStateNo.code));
        }
        return MISEXP;
    }

    public static void setMISEXP(Context context, ExpState expState) {
        Log.v("-----misexp", "--" + expState.code);
        MISEXP = expState;
        CacheAppData.keepInt(context, Constants.KEY_EXP, expState.code);
        init(context);
        DJCacheUtil.resetCache();
    }

    public static void init(Context context) {
        CacheAppData cache = CacheAppData.getInstance(context);
        MOBILE_HOST = cache.read(BaseConstant.MOBILE_HOST);
        HOUSEKEEPER_HOST = cache.read(BaseConstant.HOUSEKEEPER_HOST);
        ERP_HOST = cache.read(BaseConstant.ERP_HOST);
        ESTATEPAYMENT_HOST = cache.read(BaseConstant.ESTATEPAYMENT_HOST);
        TICKETS_HOST = cache.read(BaseConstant.TICKETS_HOST);
        SCORE_HOST = cache.read(BaseConstant.SCORE_HOST);
        PAYMENTINTEGRATION_HOST = cache.read(BaseConstant.PAYMENTINTEGRATION_HOST);
        OAUTH_HOST = cache.read(BaseConstant.OAUTH_HOST);
        WEB_HOST = cache.read(BaseConstant.WEB_HOST);
        MOBILE_STEP_HOST = cache.read(BaseConstant.MOBILE_STEP_HOST);
        MOBILE_PERFORMANCE_HOST = cache.read(BaseConstant.MOBILE_PERFORMANCE_HOST);

    }

    /**
     * 获得认证服务器
     */
    public static String getAuthHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.OAUTH_HOST);
        }
        return getProperty(context, BaseConstant.OAUTH_HOST);
    }

    /**
     * 获得mobile服务地址
     */
    public static String getMobileHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.MOBILE_HOST);
        }
        return getProperty(context, BaseConstant.MOBILE_HOST);
    }

    /**
     * 获得隐私协议服务地址
     */
    public static String getPortalmanageHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.PORTALMANAGE_HOST);
        }
        return getProperty(context, BaseConstant.PORTALMANAGE_HOST);
    }

    /**
     * 获得keeper服务地址
     */
    public static String getKeeperHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.HOUSEKEEPER_HOST);
        }
        return getProperty(context, BaseConstant.HOUSEKEEPER_HOST);
    }

    /**
     * 获得erp服务地址
     */
    public static String getErpHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.ERP_HOST);
        }
        return getProperty(context, BaseConstant.ERP_HOST);
    }

    /**
     * 获得paymentintegration.host服务地址
     *
     * @param context
     * @return
     */
    private static String getPaymentHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.ESTATEPAYMENT_HOST);
        }
        return getProperty(context, BaseConstant.ESTATEPAYMENT_HOST);
    }

    /**
     * 获得house.host服务地址
     */
    public static String getHouseHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.HOUSE_HOST);
        }
        return getProperty(context, BaseConstant.HOUSE_HOST);
    }

    /**
     * 获得tickets.host服务地址
     */
    public static String getTicketsHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.TICKETS_HOST);
        }
        return getProperty(context, BaseConstant.TICKETS_HOST);
    }

    /**
     * 获得advert.host服务地址
     */
    public static String getAdvertHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.ADVERT_HOST);
        }
        return getProperty(context, BaseConstant.ADVERT_HOST);
    }

    /**
     * 获得vshop.host服务地址
     */
    public static String getVshopHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.VSHOP_HOST);
        }
        return getProperty(context, BaseConstant.VSHOP_HOST);
    }

    /**
     * 获得Score服务地址
     */
    public static String getScoreHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.SCORE_HOST);
        }
        return getProperty(context, BaseConstant.SCORE_HOST);
    }

    /**
     * 获得mobile服务地址
     */
    public static String getWebHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.WEB_HOST);
        }
        return getProperty(context, BaseConstant.WEB_HOST);
    }

    /**
     * 获得建行web服务地址
     */
    public static String getMdWebHost(Context context) {
        return getProperty(context, BaseConstant.MD_WEB_HOST);
    }

    private static final String TAG = "Configuration";

    public static String getPortalmanageUrl(Context context, int urlId) {
        if (StringUtil.isBlank(PORTALMANAGE_HOST)) {
            PORTALMANAGE_HOST = com.hilife.view.weight.Configuration.getPortalmanageHost(DJUtil.application());
        }
        String privacyUrl = PORTALMANAGE_HOST + DJUtil.application().getString(urlId);
        LogComponent.i(TAG, "隐私协议host " + privacyUrl);
        return privacyUrl;
    }

    public static String getMobileUrl(Context context, int urlId) {
        if (StringUtil.isBlank(MOBILE_HOST)) {
            MOBILE_HOST = com.hilife.view.weight.Configuration.getMobileHost(DJUtil.application());
        }
        return MOBILE_HOST + DJUtil.application().getString(urlId);
    }

    /**
     * 报修服务地址
     *
     * @param context
     * @param urlId
     * @return
     */
    public static String getTicketsUrl(Context context, int urlId) {
        if (StringUtil.isBlank(TICKETS_HOST)) {
            TICKETS_HOST = com.hilife.view.weight.Configuration.getTicketsHost(DJUtil.application());
        }
        return TICKETS_HOST + DJUtil.application().getString(urlId);
    }

    /**
     * 广告管理服务地址
     *
     * @param context
     * @param urlId
     * @return
     */
    public static String getAdvertUrl(Context context, int urlId) {
        if (StringUtil.isBlank(ADVERT_HOST)) {
            ADVERT_HOST = com.hilife.view.weight.Configuration.getAdvertHost(DJUtil.application());
        }
        return ADVERT_HOST + DJUtil.application().getString(urlId);
    }

    /**
     * vshop服务地址
     *
     * @param context
     * @param urlId
     * @return
     */
    public static String getVshopUrl(Context context, int urlId) {
        if (StringUtil.isBlank(VShOP_HOST)) {
            VShOP_HOST = com.hilife.view.weight.Configuration.getVshopHost(DJUtil.application());
        }
        return VShOP_HOST + DJUtil.application().getString(urlId);
    }

    /**
     * 积分服务地址
     *
     * @param context
     * @param urlId
     * @return
     */
    public static String getScoreUrl(Context context, int urlId) {
        if (StringUtil.isBlank(TICKETS_HOST)) {
            SCORE_HOST = com.hilife.view.weight.Configuration.getScoreHost(DJUtil.application());
        }
        return SCORE_HOST + DJUtil.application().getString(urlId);
    }

    /**
     * 管家服务地址
     *
     * @param context
     * @param urlId
     * @return
     */
    public static String getHousekeeperUrl(Context context, int urlId) {
        if (StringUtil.isBlank(HOUSEKEEPER_HOST)) {
            HOUSEKEEPER_HOST = com.hilife.view.weight.Configuration.getKeeperHost(DJUtil.application());
        }
        return HOUSEKEEPER_HOST + DJUtil.application().getString(urlId);
    }

    /**
     * 门禁服务地址
     *
     * @param context
     * @param urlId
     * @return
     */
    public static String getErpHostUrl(Context context, int urlId) {
        if (StringUtil.isBlank(ERP_HOST)) {
            ERP_HOST = com.hilife.view.weight.Configuration.getErpHost(DJUtil.application());
        }
        return ERP_HOST + DJUtil.application().getString(urlId);
//        return "http://192.168.1.185:8053" + DJUtil.application().getString(urlId);
    }

    /**
     * 缴费服务地址
     *
     * @param context
     * @param urlId
     * @return
     */
    public static String getPaymentHostUrl(Context context, int urlId) {
        if (StringUtil.isBlank(ESTATEPAYMENT_HOST)) {
            ESTATEPAYMENT_HOST = com.hilife.view.weight.Configuration.getPaymentHost(DJUtil.application());
        }
        return ESTATEPAYMENT_HOST + DJUtil.application().getString(urlId);
    }

    /**
     * 楼栋服务地址
     *
     * @param context
     * @param urlId
     * @return
     */
    public static String getHouseHostUrl(Context context, int urlId) {
        if (StringUtil.isBlank(HOUSE_HOST)) {
            HOUSE_HOST = com.hilife.view.weight.Configuration.getHouseHost(DJUtil.application());
        }
        return HOUSE_HOST + DJUtil.application().getString(urlId);
    }

    public static String getWebUrl(Context context, int urlId) {
        if (StringUtil.isBlank(WEB_HOST)) {
            WEB_HOST = com.hilife.view.weight.Configuration.getWebHost(context);
        }
        return WEB_HOST + context.getString(urlId);
    }

    public static String getMdWebUrl(Context context, int urlId) {
        if (StringUtil.isBlank(MD_WEB_HOST)) {
            MD_WEB_HOST = com.hilife.view.weight.Configuration.getMdWebHost(context);
        }
        if (StringUtil.isBlank(MD_WEB_HOST)) {
            MD_WEB_HOST = com.hilife.view.weight.Configuration.getWebHost(context); //如何还是空的就传webhost
        }
        return MD_WEB_HOST + context.getString(urlId);
    }

    public static String getMobileStepHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.MOBILE_STEP_HOST);
        }
        return getProperty(context, BaseConstant.MOBILE_STEP_HOST);
    }

    /**
     * 获取移动端性能分析提交地址
     *
     * @param context
     * @return
     */
    public static String getMobilePerformanceHost(Context context) {
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            return getProperty(context, BaseConstant.MOBILE_PERFORMANCE_HOST);
        }
        return getProperty(context, BaseConstant.MOBILE_PERFORMANCE_HOST);
    }

    /**
     * 获取客户端提交计步数地址
     *
     * @param context
     * @param urlId
     * @return
     */
    public static String getMobileStepUrl(Context context, int urlId) {
        if (StringUtil.isBlank(MOBILE_STEP_HOST)) {
            MOBILE_STEP_HOST = com.hilife.view.weight.Configuration.getMobileStepHost(context);
        }
        if (StringUtil.isBlank(MOBILE_STEP_HOST)) {
            MOBILE_STEP_HOST = "https://step.dajiashequ.com";
        }
        return MOBILE_STEP_HOST + context.getString(urlId);
    }

    public static String getMobileImUrl(Context context, int urlId) {
        if (StringUtil.isBlank(MOBILE_IM_HOST)) {
            MOBILE_IM_HOST = com.hilife.view.weight.Configuration.getMobileImHost(context);
            if (StringUtil.isBlank(MOBILE_IM_HOST)) {
                MOBILE_IM_HOST = com.hilife.view.weight.Configuration.getMobileHost(context);
            }
        }
        return MOBILE_IM_HOST + context.getString(urlId);
    }

    public static String getMobilePayUrl(Context context, int urlId) {
        if (StringUtil.isBlank(MOBILE_PAY_HOST)) {
            MOBILE_PAY_HOST = com.hilife.view.weight.Configuration.getMobileImHost(context);
            if (StringUtil.isBlank(MOBILE_PAY_HOST)) {
                MOBILE_PAY_HOST = com.hilife.view.weight.Configuration.getMobileHost(context);
            }
        }
        return MOBILE_PAY_HOST + context.getString(urlId);
    }

    public static String getPersonShow(Context context) {
        return getMobileUrl(context, R.string.person_all_show);
    }

    public static String getSelectCommunity(Context context) {
        return com.hilife.view.weight.Configuration.getHouseHostUrl(context, R.string.select_community);
    }

    public static String getPersonCurrent(Context context) {
        return getMobileUrl(context, R.string.person_current);
    }

    public static String getPersonList(Context context) {
        return getMobileUrl(context, R.string.person_list);
    }

    public static String listAllContacts(Context context) {
        return getMobileUrl(context, R.string.person_real_community_all);
    }

    public static String isCompanyOpenRecharge(Context context) {
        return getMobileUrl(context, R.string.isCompanyOpenRecharge);
    }

    /**
     * 获取 IM 服务入口
     */
    public static String getIMService(Context context) {
        return getMobileUrl(context, R.string.rongim_service);
    }

    /**
     * 获取 IM Token
     */
    public static String getIMToken(Context context) {
        return getMobileUrl(context, R.string.rongim_imtoken);
    }

    /**
     * 刷新 IM Token
     */
    public static String getRefreshIMToken(Context context) {
        return getMobileUrl(context, R.string.rongim_refreshimtoken);
    }

    /**
     * 获取社区的基本信息
     */
    public static String getCommunityShow(Context context) {
        return getMobileUrl(context, R.string.community_get);
    }

    /**
     * 获取用户群组
     */
    public static String getUserGroups(Context context) {
        return getMobileUrl(context, R.string.group_show);
    }

    /**
     * 获取当前人员的社区列表(不含营销社区)
     */
    public static String getCommunityList(Context context) {
        return getMobileUrl(context, R.string.community_list);
    }

    /**
     * 获取当前人员的社区列表(不含营销社区)
     */
    public static String getMyCommunity(Context context) {
        return getMobileUrl(context, R.string.community_mine);
    }

    /**
     * 是否允许加入社区
     */
    public static String getCanAddCommunity(Context context) {
        return getMobileUrl(context, R.string.community_canAdd);
    }

    /**
     * 加入社区
     */
    public static String getJoinCommunity(Context context) {
        return getMobileUrl(context, R.string.community_join);
    }

    /**
     * 根据SubID加入社区
     */
    public static String getJoinCommunityBySubID(Context context) {
        return getMobileUrl(context, R.string.community_join_sub);
    }

    /**
     * 加入社区并返回当前人员的社区列表
     */
    public static String getJoinCommunity2(Context context) {
        return getMobileUrl(context, R.string.community_all_join);
    }

    /**
     * 退出社区
     */
    public static String getQuitCommunity(Context context) {
        return getMobileUrl(context, R.string.community_quit);
    }

    /**
     * 获取社区分类下的社区列表URl
     *
     * @param context
     * @return
     */
    public static String getOuterCommunityList(Context context) {
        return getMobileUrl(context, R.string.community_outer_list);
    }

    /**
     * 获取社区分类url
     *
     * @param context
     * @return
     */
    public static String getCommunityCategory(Context context) {
        return getMobileUrl(context, R.string.community_category_tag);
    }

    /**
     * 获取社区分类下的社区列表URl
     *
     * @param context
     * @return
     */
    public static String getCategoryCommunityList(Context context) {
        return getMobileUrl(context, R.string.community_category_tag_list);
    }

    /**
     * 获取搜索社区url
     *
     * @param context
     * @return
     */
    public static String getSearchCommunityList(Context context) {
        return getMobileUrl(context, R.string.community_search_list);
    }

    /**
     * 获取社区全部消息
     *
     * @param context
     * @return
     */
    public static String getCommunityComplate(Context context) {
        return getMobileUrl(context, R.string.community_complate);
    }

    /**
     * 某区域下的社区
     *
     * @param context
     * @return
     */
    public static String listCommunityInLocation(Context context) {
        return getMobileUrl(context, R.string.community_location);
    }

    /**
     * 企业分布的位置
     *
     * @param context
     * @return
     */
    public static String listLocationInCompany(Context context) {
        return getMobileUrl(context, R.string.community_location_list);
    }

    /**
     * 获取热门话题的地址
     */
    public static String getTopicHot(Context context) {
        return getMobileUrl(context, R.string.topic_hotline);
    }

    /**
     * 获取话题搜索的地址
     */
    public static String getTopicSearch(Context context) {
        return getMobileUrl(context, R.string.topic_search);
    }

    /**
     * 获取发布中心主题的地址
     */
    public static String getTopicPublish(Context context) {
        return getMobileUrl(context, R.string.topic_preset_all);
    }

    /**
     * 获取单条发布中心主题的地址
     */
    public static String getTopicPublishShow(Context context) {
        return getMobileUrl(context, R.string.topic_preset_get);
    }

    /**
     * 获取接收中心主题的地址
     */
    public static String getTopicReceive(Context context) {
        return getMobileUrl(context, R.string.topic_preset_receive_list);
    }

    /**
     * 获取单条接收中心主题的地址
     */
    public static String getTopicReceiveShow(Context context) {
        return getMobileUrl(context, R.string.topic_preset_receive_show);
    }

    public static String getTopicPresetConfig(Context context) {
        return getMobileUrl(context, R.string.topicPreset_config);
    }

    /**
     * 获取信息流主题下未读数据地址
     */
    public static String getPortalHomeUnread(Context context) {
        return getMobileUrl(context, R.string.portal_home_unread);
    }

    /**
     * 获取/mobile/topicPreset/list
     */
    public static String getTopicPresetList(Context context) {
        return getMobileUrl(context, R.string.topic_preset_list);
    }

    /**
     * 获取积分短链
     */
    public static String getMemberIntegral(Context context) {
        return getMobileUrl(context, R.string.member_integral);
    }

    /**
     * 获取信息流主题下未读数据地址
     */
    public static String getHomeTimeline(Context context) {
        return getMobileUrl(context, R.string.home_timeline);
    }

    /**
     * 获取群成员
     */
    public static String getGroupMembers(Context context) {
        return getMobileUrl(context, R.string.group_members_show);
    }

    /**
     * 获取社区内所有群
     */
    public static String getAllUserGroups(Context context) {
        return getMobileUrl(context, R.string.group_all_timeline);
    }

    /**
     * 获取搜索到的群
     */
    public static String searchGroup(Context context) {
        return getMobileUrl(context, R.string.group_search);
    }

    /**
     * 获取群信息
     */
    public static String getGroupMessage(Context context) {
        return getMobileUrl(context, R.string.group_announcement_timeline);
    }

    /**
     * 申请加群通知点击
     */
    public static String joinApproveGroup(Context context) {
        return getMobileUrl(context, R.string.group_join_approve);
    }

    /**
     * 申请加群通知点击
     */
    public static String quitApproveGroup(Context context) {
        return getMobileUrl(context, R.string.group_quit_approve);
    }

    /**
     * 获取推荐群
     */
    public static String getRecommendGroups(Context context) {
        return getMobileUrl(context, R.string.group_recommend_timeline);
    }

    /**
     * 获取指定用户加入的群组
     */
    public static String getAssignUserGroups(Context context) {
        return getMobileUrl(context, R.string.group_user_timeline);
    }

    /**
     * 搜索群内成员
     */
    public static String searchMembersFormGroup(Context context) {
        return getMobileUrl(context, R.string.search_members_show);
    }

    /**
     * 获取社区未读通知
     */
    public static String getUnreadNotification(Context context) {
        return getMobileUrl(context, R.string.notification_all_show);
    }

    /**
     * 获取精选首页地址
     */
    public static String getPlazaConfig(Context context) {
        return getMobileUrl(context, R.string.plaza_config_show);
    }

    /**
     * 获取人气人物地址
     */
    public static String getPlazaRankPerson(Context context) {
        return getMobileUrl(context, R.string.plaza_rankperson_show);
    }

    /**
     * 获取热门群组地址
     */
    public static String getPlazaGroup(Context context) {
        return getMobileUrl(context, R.string.plaza_group_show);
    }

    /**
     * 获取精选信息流地址
     */
    public static String getPlazaFeed(Context context) {
        return getMobileUrl(context, R.string.plaza_feed_show);
    }

    /**
     * 获取搜索信息流地址
     */
    public static String getSearchFeed(Context context) {
        return getMobileUrl(context, R.string.search_feed_show);
    }

    /**
     * 获取主题地址
     */
    public static String getTopicFeed(Context context) {
        return getMobileUrl(context, R.string.feed_topic_timeline);
    }

    /**
     * 获取联系人的信息流
     */
    public static String getUserFeed(Context context) {
        return getMobileUrl(context, R.string.feed_user_timeline);
    }

    /**
     * 获取发送上传信息流
     */
    public static String insertFeed(Context context) {
        return getMobileUrl(context, R.string.feed_create);
    }

    /**
     * 获取点赞人列表
     */
    public static String getPraisePerson(Context context) {
        return getMobileUrl(context, R.string.feed_praise_person);
    }

    /**
     * 获取分享界面feed
     */
    public static String getShareFeed(Context context) {
        return getMobileUrl(context, R.string.feedShare);
    }

    /**
     * 删除信息流
     */
    public static String deleteFeed(Context context) {
        return getMobileUrl(context, R.string.feed_destroy);
    }

    /**
     * 点赞操作
     */
    public static String insertPraise(Context context) {
        return getMobileUrl(context, R.string.feed_praise_add);
    }

    /**
     * 取消赞操作
     */
    public static String deletePraise(Context context) {
        return getMobileUrl(context, R.string.feed_praise_cancel);
    }

    /**
     * 获取当前博客
     */
    public static String getBlog(Context context) {
        return getMobileUrl(context, R.string.blog_show);
    }

    /**
     * 获取评论
     */
    public static String getComments(Context context) {
        return getMobileUrl(context, R.string.get_comment);
    }

    /**
     * 获取@我的评论
     */
    public static String getCommentsToMe(Context context) {
        return getMobileUrl(context, R.string.comment_to_me);
    }

    /**
     * 获取@我的评论
     */
    public static String getComment(Context context) {
        return getMobileUrl(context, R.string.comment_get);
    }

    /**
     * 获取发布评论
     */
    public static String insertComment(Context context) {
        return getMobileUrl(context, R.string.comment_create);
    }

    /**
     * 获取删除评论
     */
    public static String deleteComment(Context context) {
        return getMobileUrl(context, R.string.destroy_comment);
    }

    /**
     * 获取发布批示
     */
    public static String insertInstruction(Context context) {
        return getMobileUrl(context, R.string.instruction_create);
    }

    /**
     * 获取要批示的人员
     */
    public static String getCompanyContactPerson(Context context) {
        return getMobileUrl(context, R.string.getCompanyContactPerson);
    }

    /**
     * 获取要批示的群组
     */
    public static String getInstructionGroups(Context context) {
        return getMobileUrl(context, R.string.getInstructionGroups);
    }

    /**
     * 获取批示和推荐权限
     */
    public static String getRight(Context context) {
        return getMobileUrl(context, R.string.instruction_get_right);
    }

    /**
     * 转发到IM
     */
    public static String uploadToIm(Context context) {
        return getMobileUrl(context, R.string.upload_to_im);
    }

    /**
     * 获取推荐接口
     */
    public static String insertRecommend(Context context) {
        return getMobileUrl(context, R.string.recommend_create);
    }

    /**
     * 获取分享界面blog
     */
    public static String getShareBlog(Context context) {
        return getMobileUrl(context, R.string.blogShare);
    }

    /**
     * 获取群组信息流地址
     */
    public static String getGroupFeed(Context context) {
        return getMobileUrl(context, R.string.feed_group_timeline);
    }

    /**
     * 获取文件短链
     */
    public static String getFileShortChain(Context context) {
        return getMobileUrl(context, R.string.file_short_chain);
    }

    /**
     * 获取全部通知
     */
    public static String MNotificationAll(Context context) {
        return getMobileUrl(context, R.string.notification_all_show);
    }

    /**
     * 获取@我通知
     */
    public static String getAtMeNotifications(Context context) {
        return getMobileUrl(context, R.string.notification_mention_show);
    }

    /**
     * 获取请求通知
     */
    public static String getOperationNotifications(Context context) {
        return getMobileUrl(context, R.string.notification_operation_show);
    }

    /**
     * 获取系统通知
     */
    public static String getSystemNotifications(Context context) {
        return getMobileUrl(context, R.string.notification_system_show);
    }

    /**
     * 获取邀请加社区的通知
     */
    public static String handleInviteNotification(Context context) {
        return getMobileUrl(context, R.string.notification_operation_approve);
    }

    /**
     * 注册绑定离线推送
     */
    public static String addPushDevice(Context context) {
        return getMobileUrl(context, R.string.push_device_create);
    }

    /**
     * 注册解绑离线推送
     */
    public static String removePushDevice(Context context) {
        return getMobileUrl(context, R.string.push_device_destroy);
    }

    /**
     * 获取信息流表单查看地址
     */
    public static String getFormShowUrl(Context context) {
        return getWebUrl(context, R.string.form_show);
    }

    /**
     * 获取信息流表单查看地址
     */
    public static String getFormShowUrl(Context context, String formID) {
        return getFormShowUrl(context) + formID + "?access_token=" + DJCacheUtil.readToken() + "&companyID=" + DJCacheUtil.readCommunityID();
    }

    public static String getFormRecordEditUrl(Context context) {
        return getWebUrl(context, R.string.form_record_edit);
    }

    /**
     * 获取信息流表单填写内容地址
     */
    public static String getFormRecordEditUrl(Context context, String formID) {
        return getFormRecordEditUrl(context) + formID + "?access_token=" + DJCacheUtil.readToken() + "&companyID=" + DJCacheUtil.readCommunityID();
    }

    /**
     * 获取信息流表单填写内容查看地址
     */
    public static String getFormRecordShowUrl(Context context, String formID, String recordID) {
        return getWebUrl(context, R.string.form_record_show) + "?formID=" + formID + "&recordID=" + recordID;
    }

    /**
     * 获取getOrderFormShowUrl
     */
    public static String getMasterOrderFormShowUrl(Context context, String formID, String version) {
        String webUrl = getWebUrl(context, R.string.form_orderform_show) + "?orderFormID=" + formID + "&access_token=" + DJCacheUtil.readToken();
        if (!StringUtil.isEmpty(version)) {
            webUrl += "&version=" + version;
        }
        return webUrl;
    }

    /**
     * 获取getMasterOrderFormShowUrl
     */
    public static String getOrderFormShowUrl(Context context, String formID, String version) {
        String webUrl = getWebUrl(context, R.string.form_orderform_show_me) + "?orderFormID=" + formID + "&access_token=" + DJCacheUtil.readToken();
        if (!StringUtil.isEmpty(version)) {
            webUrl += "&version=" + version;
        }
        return webUrl;
    }

    /**
     * 获取申请退款通知跳转URL
     *
     * @param context
     * @param orderID
     * @return
     */
    public static String getOrderRefundUrl(Context context, String orderID) {
        return getWebUrl(context, R.string.form_orderform_refund) + DJCacheUtil.readCommunityID() + "/" + orderID;
    }

    /**
     * 获取退款完成通知跳转URL
     *
     * @param context
     * @param orderID
     * @return
     */
    public static String getOrderRefundFinishUrl(Context context, String orderID) {
        return getWebUrl(context, R.string.form_orderform_refund_finish) + DJCacheUtil.readCommunityID() + "/" + orderID;
    }

    /**
     * 获取服务单查看详情地址
     */
    public static String getServiceReceiptsShowUrl(Context context, String recordID) {
        return getWebUrl(context, R.string.service_receipts_show) + "/" + recordID + "?access_token=" + DJCacheUtil.readToken() + "&companyID=" + DJCacheUtil.readCommunityID();
    }

    /**
     * 待缴费通知到订单详情
     */
    public static String getOrderdetailUrl(Context context, String recordID) {
        return getWebUrl(context, R.string.service_order_detail) + recordID + "?access_token=" + DJCacheUtil.readToken() + "&companyID=" + DJCacheUtil.readCommunityID();
    }

    /**
     * 订单超期通知的跳转
     */
    public static String getOrderTimeoutUrl(Context context, String recordID) {
        return getWebUrl(context, R.string.service_order_timeout) + "?access_token=" + DJCacheUtil.readToken() + "&companyID=" + DJCacheUtil.readCommunityID() + "&orderID=" + recordID;
    }

    /**
     * 个人资料查看更多
     */
    public static String getQueryInformationUrl(Context context, String recordID) {
        return getWebUrl(context, R.string.service_query_information) + "?personID=" + DJCacheUtil.readPersonID() + "&formID=" + recordID;
    }

    /**
     * 个人资料查看更多
     */
    public static String getClectFormUrl(Context context, String recordID) {
        return getWebUrl(context, R.string.form_collect_show) + "?access_token=" + DJCacheUtil.readToken() + "&companyID=" + DJCacheUtil.readCommunityID() + "&formID=" + recordID;
    }

    /**
     * 获取服务单抢单
     */
    public static String getServiceFlowDistribution(Context context) {
        return getMobileUrl(context, R.string.serviceflow_distribution);
    }

    /**
     * 获取服务单查看详情地址
     */
    public static String getProductTemplateList(Context context) {
        StringBuilder sb = new StringBuilder(getProperty(context, BaseConstant.WEB_HOST) + context.getString(R.string.producttemplate_list) + "?");
        sb.append("companyID=" + CacheAppData.read(context, "Exp_CommunityID"));
        if (StringUtil.isNotEmpty(CacheAppData.read(context, "Exp_RoleName"))) {
            sb.append("&roleName=" + CacheAppData.read(context, "Exp_RoleName"));
        }
        if (getMISEXP(context) == ExpState.ExpStateNo) {
            if (StringUtil.isNotEmpty(DJCacheUtil.readToken())) {
                sb.append("&access_token=" + DJCacheUtil.readToken());
            }
        }
        String customID = BuildConfig.COMPANY_KEY;
        if (StringUtil.isNotBlank(customID)) {
            sb.append("&customID=" + customID);
        }
        CacheAppData.remove(context, "Exp_RoleName");
        CacheAppData.remove(context, "Exp_CommunityID");
        sb.append("&t=" + System.currentTimeMillis());
        return sb.toString();
    }

    /**
     * 收藏列表显示地址
     */
    public static String getFavoriteListShowUrl(Context context) {
        return getMobileUrl(context, R.string.favorite_feed_show);
    }

    /**
     * 新增收藏地址
     */
    public static String getFavoriteCreateUrl(Context context) {
        return getMobileUrl(context, R.string.favorite_create);
    }

    /**
     * 取消收藏地址
     */
    public static String getFavoriteCancelUrl(Context context) {
        return getMobileUrl(context, R.string.favorite_destroy);
    }

    /**
     * 取消收藏信息流
     */
    public static String getFavoriteFeeds(Context context) {
        return getMobileUrl(context, R.string.favorite_feed_show);
    }

    /**
     * 获取信息流
     */
    public static String getFeedShow(Context context) {
        return getMobileUrl(context, R.string.feed_show);
    }

    /**
     * 标记某条信息流已读，未读
     */
    public static String getFeedActionRead(Context context) {
        return getMobileUrl(context, R.string.feed_read_add);
    }

    /**
     * 修改信息流发送范围
     */
    public static String getFeedUpdateRange(Context context) {
        return getMobileUrl(context, R.string.feed_update_range);
    }

    /**
     * 获取信息流发送范围
     */
    public static String getFeedRange(Context context) {
        return getMobileUrl(context, R.string.feed_range);
    }

    /**
     * 获取信息流发送范围
     */
    public static String getFeedSearch() {
        return getMobileUrl(null, R.string.feed_search);
    }

    /**
     * 获取短信模板
     */
    public static String getTheSMS() {
        return getMobileUrl(null, R.string.feed_sms_template);
    }

    /**
     * 判断微信公众号
     */
    public static String checkTheWechat() {
        return getMobileUrl(null, R.string.feed_weixin_check);
    }

    /**
     * 判断微信公众号
     */
    public static String getSmsCount() {
        return getMobileUrl(null, R.string.feed_sms_check);
    }

    /**
     * 标记某条信息流已读，未读
     */
    public static String listReadAction(Context context) {
        return getMobileUrl(context, R.string.feed_read_person);
    }

    /**
     * 获取签到历史记录
     */
    public static String getCheckSignOn(Context context) {
        return getMobileUrl(context, R.string.signon_check);
    }

    /**
     * 获取签到历史记录
     */
    public static String getSignInHistoryUrl(Context context) {
        return getMobileUrl(context, R.string.signin_history);
    }

    /**
     * 获取群组签到列表
     */
    public static String getGroupSignInHistoryUrl(Context context) {
        return getMobileUrl(context, R.string.group_signin_list);
    }

    /**
     * 获取群组中人员签到列表
     */
    public static String getPersonSignInHistoryUrl(Context context) {
        return getMobileUrl(context, R.string.person_signin_list);
    }

    /**
     * 获取签到URl
     */
    public static String getSignInUrl(Context context) {
        return getMobileUrl(context, R.string.signin_add);
    }

    /**
     * 获取群组信息
     */
    public static String getGroupShow(Context context) {
        return getMobileUrl(context, R.string.group_show);
    }

    /**
     * 获取微信登陆信息
     */
    public static String getRegWechat(Context context) {
        return getMobileUrl(context, R.string.reg_wechat);
    }

    /**
     * 获取发送验证码的服务路劲
     */
    public static String getRegVcodeSend(Context context) {
        return getMobileUrl(context, R.string.reg_vcode_send);
    }

    /**
     * 获取当前广告
     */
    public static String getCurrentAd(Context context) {
        return getMobileUrl(context, R.string.ad_getCurrentAd);
    }

    /**
     * 获取广告
     */
    public static String recordVisitAd(Context context) {
        return getMobileUrl(context, R.string.ad_recordVisitAd);
    }

    /**
     * 获取验证码登陆信息
     */
    public static String getRegVcode(Context context) {
        return getMobileUrl(context, R.string.reg_vcode);
    }

    /**
     * 绑定微信
     */
    public static String getWechatBind(Context context) {
        return getMobileUrl(context, R.string.wechat_bind);
    }

    /**
     * 解绑微信
     */
    public static String getWechatUnBind(Context context) {
        return getMobileUrl(context, R.string.wechat_unbind);
    }

    /**
     * 获取个人信息接口
     */
    public static String getPersonCommon(Context context) {
        return getMobileUrl(context, R.string.person_common);
    }

    /**
     * 获取个人基本信息（不含社区信息）
     */
    public static String getPersonBasic(Context context) {
        return getMobileUrl(context, R.string.person_basic);
    }

    /**
     * 获取用户账号信息
     */
    public static String getPersonAccount(Context context) {
        return getMobileUrl(context, R.string.person_account);
    }

    /**
     * 搜索用户
     */
    public static String getSearchPerson(Context context) {
        return getMobileUrl(context, R.string.person_search);
    }

    /**
     * 添加黑名单
     */
    public static String addToBlackList(Context context) {
        return getMobileUrl(context, R.string.person_black_add);
    }

    /**
     * 判断是否在黑名单内
     */
    public static String checkBlackList(Context context) {
        return getMobileUrl(context, R.string.person_black_check);
    }

    /**
     * 从黑名单删除
     */
    public static String deleteInBlackList(Context context) {
        return getMobileUrl(context, R.string.person_black_delete);
    }

    /**
     * 获取黑名单列表
     */
    public static String listBlackList(Context context) {
        return getMobileUrl(context, R.string.person_black_list);
    }

    /**
     * 添加联系人
     */
    public static String addFriend(Context context) {
        return getMobileUrl(context, R.string.person_friend_add);
    }

    /**
     * 添加联系人通知
     */
    public static String dealApplyFriend(Context context) {
        return getMobileUrl(context, R.string.person_friend_deal);
    }

    /**
     * 所有联系人获取
     */
    public static String getAllPersonInCommunity(Context context) {
        return getMobileUrl(context, R.string.person_community_all);
    }

    /**
     * 聊天人员检查
     */
    public static String getIMChatCheck(Context context) {
        return getMobileUrl(context, R.string.person_chat_check);
    }

    /**
     * "我的"界面的配置
     */
    public static String getPersonalSource(Context context) {
        return getMobileUrl(context, R.string.personal_source);
    }

    /**
     * 删除好友关系
     */
    public static String deleteFriend(Context context) {
        return getMobileUrl(context, R.string.person_friend_delete);
    }

    /**
     * 上传门户转发信息
     */
    public static String getPortalForward(Context context) {
        return getMobileUrl(context, R.string.portal_forward);
    }
//	/**
//	 * 上传门户转发信息  废弃
//	 */
//	public static String getPortalHomeResource(Context context){
//		return getMobileUrl(context, R.string.portal_home_resource);
//	}

    /**
     * 上传门户转发信息
     */
    public static String getPortalHomeTemplate(Context context) {
        return getMobileUrl(context, R.string.portal_home_template);
    }

    /**
     * 上传门户转发信息
     */
    public static String getPortalHomeGroup(Context context) {
        return getMobileUrl(context, R.string.portal_home_group);
    }

    public static String getPortalAllGroup(Context context) {
        return getMobileUrl(context, R.string.portal_all_group);
    }

    /**
     * portal 商品列表
     */
    public static String getPortalHomeProduct(Context context) {
        return getMobileUrl(context, R.string.portal_home_product);
    }

    /**
     * portal 列表
     */
    public static String getPortalList(Context context) {
        return getMobileUrl(context, R.string.portal_home_list);
    }

    /**
     * 通过mID获取具体PresetMenu信息
     */
    public static String getTopicPresetReceive(Context context) {
        return getMobileUrl(context, R.string.topicPreset_receive_get);
    }

    /**
     * 处理二维码内容
     *
     * @param context
     * @return
     */
    public static String getProcessQRCode(Context context) {
        return getMobileUrl(context, R.string.qrcode_processqrcode);
    }

    /**
     * 获取全部人员荣誉信息
     *
     * @param context
     * @return
     */
    public static String getAllBadgePerson(Context context) {
        return getMobileUrl(context, R.string.get_all_badgeperson);
    }

    /**
     * 获取指定人员荣誉信息
     *
     * @param context
     * @return
     */
    public static String getOneBadgePerson(Context context) {
        return getMobileUrl(context, R.string.get_one_badgeperson);
    }

    /**
     * 获得微信转发apiKey
     */
    public static String getWeChat(Context context) {
        return BuildConfig.WX_ID;
    }

    /**
     * 新浪微博开放平台 key
     */
    public static String getSinaWeiboKey(Context context) {
        return BuildConfig.SINA_WEIBO_KEY;
    }

    /**
     * QQ、QQZone
     */
    public static String getTencentQQKey(Context context) {
        return BuildConfig.QQ_ID;
    }

    public static boolean isCustomizationSupport(Context context, int resId) {
        if (isCustomization(context)) {
            String isSuport = context.getString(resId);
            return Constants.GLOBAL_SWITCH_Y.equals(isSuport);
        }
        return true;
    }

    public static String getAppCode(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public static boolean isSupport(Context context, int resId) {
        String isSuport = context.getString(resId);
        if (!Constants.GLOBAL_SWITCH_Y.equals(isSuport)) {
            return false;
        }
        return true;
    }

    /**
     * 微信登录开关
     */
    public static boolean isWXLoginSupport(Context context, int resId) {
        String isSuport = context.getString(resId);
        if (!Constants.GLOBAL_SWITCH_Y.equals(isSuport)) {
            return false;
        }
        return true;
    }

    /**
     * 二维码登录开关
     */
    public static boolean isQRLoginSupport(Context context, int resId) {
        String isSuport = context.getString(resId);
        if (!Constants.GLOBAL_SWITCH_Y.equals(isSuport)) {
            return false;
        }
        return true;
    }

    public static boolean isCustomization(Context context) {
        String customization_switch = context.getString(R.string.customization_switch);
        if (Constants.GLOBAL_SWITCH_Y.equals(customization_switch)) {
            return true;
        }
        return false;
    }

    public static boolean isCanShowSearch(Context context) {
        String customization_switch = context.getString(R.string.canshowsearch_switch);
        if (Constants.GLOBAL_SWITCH_Y.equals(customization_switch)) {
            return true;
        }
        return false;
    }

    public static boolean isCanShowSwitchCommunity(Context context) {
        String customization_switch = context.getString(R.string.canshowswitchcommunity_switch);
        if (Constants.GLOBAL_SWITCH_Y.equals(customization_switch)) {
            return true;
        }
        return false;
    }

    public static boolean isSelectLoginSupport(Context context, int resId) {
        String isSuport = context.getString(resId);
        if (!Constants.GLOBAL_SWITCH_Y.equals(isSuport)) {
            return false;
        }
        return true;
    }

    private static String customID = null;

    public static String getCustomID(Context context) {
        if (customID == null) {
            if (isCustomization(context)) {
                customID = BuildConfig.COMPANY_KEY;
            }
        }
        return customID;
    }

    private static String customCompanyID = null;

    public static String getCustomCompanyID(Context context) {
        if (customCompanyID == null) {
            if (isCustomization(context)) {
                String isolationID = CacheAppData.read(context, BaseConstant.MOBILE_ACCOUNT_ISOLATION, "10000");
                if ("10000".equals(isolationID)) {
                    if (Constants.GLOBAL_SWITCH_N.equals(context.getString(R.string.company_switch))) {
                        customCompanyID = BuildConfig.COMPANY_KEY;
                    }
                } else {
                    customCompanyID = BuildConfig.COMPANY_KEY;
                    if (StringUtil.isBlank(customCompanyID))
                        throw new AppException("account isolation company need company_key！");
                }
            }
        }
        return customCompanyID;
    }

    private static String buildVersion = null;

    public static String getBuildVersion(Context context) {
        if (buildVersion == null) {
            buildVersion = context.getString(R.string.buildVersion);
        }
        return buildVersion;
    }

    public static String getRichFeedDetailUrl(Context context,
                                              String feedID, String communityID) {
        return getWebUrl(context, R.string.feed_detail_path) + "/" + communityID + "/" + feedID + "?showOperate=0";
    }

    public static String getShowUrl(Context context, String code, Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        String content = getShowWorksUrl(context, code);
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (content.lastIndexOf("?") == content.length() - 1) {
                content = content + entry.getKey() + "=" + entry.getValue();
            } else {
                content = content + "&" + entry.getKey() + "=" + entry.getValue();
            }
        }
        return content;
    }

    public static String getShowWorksUrl(Context context, String code) {
        switch (code) {
            case Constants.TOPIC_CODE_WORKSSHOW:
                return getWebUrl(context, R.string.works_show);
            default:
                return null;
        }
    }

    public static String getPaymentCheckUrl(Context context) {
        return getMobilePayUrl(context, R.string.payment_check);
    }

    public static String getPaymentConfirmUrl(Context context) {
        return getMobilePayUrl(context, R.string.payment_confirm);
    }

    public static String getProduct4QyUrl(Context context) {
        return getMobilePayUrl(context, R.string.product_detail4qy);
    }

    public static String getPersonalSourceFilePath(Context context) {
        return getPersonalSourceDirPath(context) + "/personalSource.json";
    }

    public static String getPersonalSourceDirPath(Context context) {
        return getPersonalSourcePreDirPath(context) + "/" + DJCacheUtil.readCommunityID();
    }

    public static String getPersonalSourcePreDirPath(Context context) {
        return getPersonalSourceCusDirPath(context) + "/" + DJCacheUtil.readPersonID();
    }

    public static String getPersonalSourceCusDirPath(Context context) {
        String cusPath = context.getPackageName();
        if (StringUtil.isNotEmpty(cusPath)) {
            cusPath = cusPath.replaceAll("\\.", "_");
        }
        return DJFileUtil.getSourceFolder() + "/" + cusPath;
    }

    public static String getIntroSort(Context mContext) {
        return mContext.getResources().getString(R.string.intro_guide_sort);
    }

    /*
     * 获取手机屏幕尺寸，屏幕宽*屏幕高 如：480x800（单位px）
     *
     */
    public static String getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "x" + dm.heightPixels;
    }

    /**
     * 获取分项列表url
     *
     * @param context
     * @return
     */
    public static String getCategoryListUrl(Context context) {
        return getWebUrl(context, R.string.fenxiangliebiao);
    }

    /**
     * 计步功能，客户端同步数据url
     *
     * @param context
     * @return
     */
    public static String getStepCommitUrl(Context context) {
        return getMobileStepUrl(context, R.string.step_url_commit);
    }

    /**
     * 模板信息请求url
     *
     * @param context
     * @return
     */
    public static String getH5TemplateUrl(Context context) {
        return getMobileUrl(context, R.string.h5_template_url);
    }

    /**
     * 安徽邮政个人资料查看更多
     */
    public static String getQueryInformationUrl093(Context context, String companyID) {
        return getWebUrl(context, R.string.service_query_information_093) + "?companyID=" + companyID + "&access_token=" + DJCacheUtil.readToken();
    }

    /**
     * 设置手势密码
     */
    public static String getSetGestureUrl(Context context) {
        return getMobileUrl(context, R.string.gesture_set);
    }

    /**
     * 校验手势密码
     */
    public static String getGestureValidateUrl(Context context) {
        return getMobileUrl(context, R.string.gesture_validate);
    }

    /**
     * 获取绑定的手机号码
     */
    public static String getCurrentPersonPhoneNumber(Context context) {
        return getMobileUrl(context, R.string.person_phone_number);
    }

    /**
     * 检查手势是否存在
     */
    public static String getGestureStatus(Context context) {
        return getMobileUrl(context, R.string.gesture_status);
    }

    /**
     * 清除手势密码
     */
    public static String gestureClearUrl(Context context) {
        return getMobileUrl(context, R.string.gesture_clear);
    }

    /**
     * 打开手势开关
     */
    public static String gestureOpenUrl(Context context) {
        return getMobileUrl(context, R.string.gesture_open);
    }

    /**
     * 获取获取图片验证码
     */
    public static String getImage_VCodeSend(Context context) {
        return getMobileUrl(context, R.string.reg_vcode_send_image);
    }

    /**
     * 个人资料url
     */
    public static String getPersonDetailUrl(Context context, String personID) {
        return getMdWebUrl(context, R.string.person_detail) + "&personID=" + personID + "&access_token=" + DJCacheUtil.readToken() + "&communityID=" + DJCacheUtil.readCommunityID();
    }

    /**
     * 小灵通信息收集单接口  (已经废弃)
     */
    public static String getInformationFormUrl(Context context) {
        return getMobileUrl(context, R.string.informationForm);
    }

    public static String judgePersonStatusInCommunityUrl(Context context) {
        return getMobileUrl(context, R.string.checkPersonStatusInCommunityUrl);
    }

    /**
     * 获取我的订单url
     */
    public static String getMyOrdersUrl(Context context) {
        return getWebUrl(context, R.string.myOrders) + "?companyID=" + DJCacheUtil.readCommunityID() + "&access_token=" + DJCacheUtil.readToken();
    }

    /**
     * 传给服务端用户使用app次数url
     */
    public static String getLoginRecordUrl(Context context) {
        return getMobileUrl(context, R.string.login_record);
    }

    /**
     * 登陆用户协议
     */
    public static String getAgreement(Context context) {
        return getWebUrl(context, R.string.go_service_agreement);
    }

    /**
     * 登陆隐私协议
     */
    public static String getPolicies(Context context) {
        String linkUrl = PrivacyComponent.getLocalPrivacyLink();
        if (StringUtil.isEmpty(linkUrl)) { //如果本地没有，就用固定的
            linkUrl = getWebUrl(context, R.string.go_primary_policies);
        }
        return linkUrl;

    }

    /**
     * TODO App开门
     * 下拉小区列表
     */
    public static String getCardNos(Context context) {
        return getErpHostUrl(context, R.string.cardnos_three);
    }

    /**
     * app开门首次 搜索公用
     */
    public static String getDoorsUrl(Context context) {
        return getErpHostUrl(context, R.string.getdoors);
    }

    /**
     * 点击收藏
     */
    public static String changeCollectionDoorUrl(Context context) {
        return getErpHostUrl(context, R.string.change_collection);
    }

    /**
     * 点击开门
     */
    public static String getOpendoorThreeUrl(Context context) {
        return getErpHostUrl(context, R.string.opendoor_three);
    }

    /**
     * 门列表下拉刷新
     */
    public static String getOpenListUrl(Context context) {
        return getErpHostUrl(context, R.string.openlist);
    }

    /**
     * 绑定门禁卡获取验证码
     */
    public static String getSnamenkaCodeUrl(Context context) {
        return getErpHostUrl(context, R.string.get_snamenka_code);
    }

    /**
     * 绑定门禁卡小区列表
     */
    public static String getBindCommunityListUrl(Context context) {
        return getErpHostUrl(context, R.string.get_communitylist);
    }

    /**
     * 绑定门禁卡
     */
    public static String getBindingSnamenkaUrl(Context context) {
        return getErpHostUrl(context, R.string.bind_card);
    }

    /**
     * 推荐小区列表
     */
    public static String getRecommendCommunityUrl(Context context) {
        return getErpHostUrl(context, R.string.recommend_community);
    }

    /**
     * 授权列表
     */
    public static String getAuthorizeListUrl(Context context) {
        return getErpHostUrl(context, R.string.cancel_authorized);
    }

    /**
     * 取消授权
     */
    public static String getCancelAuthorizationUrl(Context context) {
        return getErpHostUrl(context, R.string.cancel_authorized);
    }

    /**
     * 拒绝授权
     */
    public static String getRejectAuthorizationUrl(Context context) {
        return getErpHostUrl(context, R.string.cancel_authorized);
    }

    /**
     * 同意授权
     */
    public static String getConsentAuthorizationUrl(Context context) {
        return getErpHostUrl(context, R.string.agree_authorized);
    }

    /**
     * 用户等级
     */
    public static String getUserLevelUrl(Context context) {
        return getErpHostUrl(context, R.string.user_level);
    }

    /**
     * 解绑门禁卡
     */
    public static String getUnbindCardUrl(Context context) {
        return getErpHostUrl(context, R.string.unbind_card);
    }

    /**
     * 门卡列表
     */
    public static String getCardNosUrl(Context context) {
        return getErpHostUrl(context, R.string.cardnos);
    }

    /**
     * 我的管家
     */
    public static String getStewardListUrl(Context context) {
        return getHousekeeperUrl(context, R.string.housekeeper_steward);
    }

    /**
     * 获得认证服务器
     */
    public static String getSelectCommunityUrl(Context context) {
        return getHouseHostUrl(context, R.string.select_community);
    }

    /**
     * 判断是否认证
     */
    public static String getJudgeAuthenticationUrl(Context context) {
        return getErpHostUrl(context, R.string.judge_authentication);
    }

    /**
     * 认证
     */
    public static String getGoAuthenticationUrl(Context context) {
        return getHouseHostUrl(context, R.string.go_authentication);
    }

    /**
     * 业主去评价
     */
    public static String getGoEvaluateUrl(Context context) {
        return getHousekeeperUrl(context, R.string.go_evaluate);
    }

    /**
     * 业主查看管家详情
     */
    public static String getStewardMsgUrl(Context context) {
        return getHousekeeperUrl(context, R.string.steward_detail);
    }

    /**
     * 获取我的房间
     */
    public static String getMineRoomUrl(Context context) {
        return getHouseHostUrl(context, R.string.mine_room_url);
    }

    /**
     * 获取所有状态我的房间列表
     */
    public static String getOwnerHouseList(Context context) {
        return getHouseHostUrl(context, R.string.get_owner_house_list);
    }

    /**
     * 取消房间审核
     */
    public static String cancelHouseAudit(Context context) {
        return getHouseHostUrl(context, R.string.cancel_house_audit);
    }
    /****************************缴费Start*************************************/
    /**
     * 获取是否为管家
     *
     * @param context
     */
    public static String judgeIsSteward(Context context) {
        return getHousekeeperUrl(context, R.string.judge_is_steward);
    }

    /**
     * 判断登录者是否有认证房屋  1 有认证房间  2待认证  3管家
     *
     * @param context
     */
    public static String judgeIsHouseOwner(Context context) {
        return getHouseHostUrl(context, R.string.judge_is_house_owner);
    }

    /**
     * 业主去获取缴费房间列表
     *
     * @param context
     */
    public static String getPayroomList(Context context) {
        return getHouseHostUrl(context, R.string.get_payroom_list);
    }

    /**
     * 代缴时 租户去 获取业主房间列表信息 添加房间
     *
     * @param context
     */
    public static String getHouseOwnerRoomList(Context context) {
        return getHouseHostUrl(context, R.string.get_house_owner_room_list);
    }

    /**
     * 获取极致customerId
     *
     * @param context
     * @return front/interface/propertyFees/getJeezCustomerId
     */
    public static String getJeezCustomerId(Context context) {
        return getPaymentHostUrl(context, R.string.get_jeez_customer_id);
    }

    /**
     * 获取通通蓝牙mac地址信息
     *
     * @param context
     * @return front/interface/propertyFees/getJeezCustomerId
     */
    public static String getBluetoothDoor(Context context) {
        return getWebUrl(context, R.string.queryPersonalOpenBluetoothCode);
    }

    /**
     * 获取西墨蓝牙mac地址信息
     *
     * @param context
     * @return front/interface/propertyFees/getJeezCustomerId
     */
    public static String getXMBleInfo(Context context) {
        return getWebUrl(context, R.string.getPersonAuthXiMoDevices);
    }

    /**
     * 预缴账单时校验当期账单是否已缴费  如未缴费弹窗提示
     *
     * @param context
     */
    public static String checkTollArrears(Context context) {
        return getPaymentHostUrl(context, R.string.check_toll_arrears);
    }

    /**
     * 获取发票实付金额
     *
     * @param context
     */
    public static String getRealPayMoney(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 提交发票申请
     *
     * @param context
     */
    public static String sumitInvoicelApply(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 获取FAQ数据
     *
     * @param context
     */
    public static String getFaqData(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 获取发票详情信息
     *
     * @param context
     */
    public static String getInvoiceDetailMsg(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 获取发票图片
     *
     * @param context
     */
    public static String getInvoiceImage(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 保存发票信息
     *
     * @param context
     */
    public static String saveInvoiceInfo(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 获取发票列表
     *
     * @param context
     */
    public static String saveInvocieList(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 选择默认地址
     *
     * @param context
     */
    public static String selectDefAddress(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 删除发票
     *
     * @param context
     */
    public static String deleteInvoice(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 获取我的发票数据
     *
     * @param context
     */
    public static String getMyInvoiceData(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 获取缴费项目列表
     *
     * @param context
     */
    public static String getPaymentProjectData(Context context) {
        return getPaymentHostUrl(context, R.string.get_payment_project);
    }

    /**
     * 取消绑定科目
     *
     * @param context
     */
    public static String bindingSubject(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 取消绑定科目
     *
     * @param context
     */
    public static String cancelBindingSuject(Context context) {
        return getPaymentHostUrl(context, R.string.go_evaluate);
    }

    /**
     * 获取下拉列表数据
     *
     * @param context
     */
    public static String getDownSelectData(Context context) {
        return getPaymentHostUrl(context, R.string.get_history_iteams_and_years);
    }

    /**
     * 请求物业缴费记录列表
     *
     * @param context
     */
    public static String getPaymentRecordList(Context context) {
        return getPaymentHostUrl(context, R.string.get_pay_list);
    }

    /**
     * 物业待缴账单
     *
     * @param context
     */
    public static String getWaitPayBills(Context context) {
        return getPaymentHostUrl(context, R.string.get_wait_pay_bills);
    }

    /**
     * 提交缴费账单
     *
     * @param context
     */
    public static String submitPaymentBill(Context context) {
        return getPaymentHostUrl(context, R.string.submit_payment_bill);
    }
    /****************************缴费End*************************************/
    /***************************积分Start*************************************/
    /**
     * 获取积分类型
     *
     * @param context
     */
    public static String getIntegralTypeCenter(Context context) {
        return getMobileUrl(context, R.string.integral_centertype_url);
    }

    /**
     * 获取积分
     *
     * @param context
     */
    public static String getIntegralCenter(Context context) {
        return getMobileUrl(context, R.string.integral_center_url);
    }

    /**
     * 签到
     *
     * @param context
     */
    public static String getSignin(Context context) {
        return getMobileUrl(context, R.string.sign_in_url);
    }

    /**
     * 积分规则
     *
     * @param context
     */
    public static String getIntegralRuleUrl(Context context) {
        return getScoreUrl(context, R.string.integral_rule_url);
    }
    /****************************积分End*************************************/
    /****************************报修Start*************************************/
    /**
     * 报修提交
     *
     * @param context
     * @return
     */
    public static String toRepair(Context context) {
        return getTicketsUrl(context, R.string.repair_toRepair);
    }

    /**
     * 报修订单列表
     *
     * @param context
     * @return
     */
    public static String repairList(Context context) {
        return getTicketsUrl(context, R.string.repair_repairList);
    }

    /**
     * 报修订单详情
     *
     * @param context
     * @return
     */
    public static String repaidDetail(Context context) {
        return getTicketsUrl(context, R.string.repair_repaidDetail);
    }

    /**
     * 取消报修
     *
     * @param context
     * @return
     */
    public static String cancelRepair(Context context) {
        return getTicketsUrl(context, R.string.repair_cancelRepair);
    }

    /**
     * 报修订单信息
     *
     * @param context
     * @return
     */
    public static String commentRepair(Context context) {
        return getTicketsUrl(context, R.string.repair_commentRepair);
    }

    /**
     * 订单支付平台信息
     *
     * @param context
     * @return
     */
    public static String feesDetail(Context context) {
        return getTicketsUrl(context, R.string.repair_feesDetail);
    }

    /**
     * 报修订单锁单
     *
     * @param context
     * @return
     */
    public static String lockOrder(Context context) {
        return getTicketsUrl(context, R.string.repair_lockOrder);
    }

    /**
     * 支付平台数据
     *
     * @param context
     * @return
     */
    public static String payBill(Context context) {
        return getTicketsUrl(context, R.string.repair_payBill);
    }

    /**
     * 上传图片
     *
     * @param context
     * @return
     */
    public static String toRepairImgs(Context context) {
        return getTicketsUrl(context, R.string.repair_uploadImgs);
    }

    /**
     * 管家物业代报修，订单列表
     *
     * @param context
     * @return
     */
    public static String getManagerRepairList(Context context) {
        return getTicketsUrl(context, R.string.repair_managerRepairList);
    }

    /**
     * 管家代报修列表
     *
     * @param context
     * @return
     */
    public static String getRoomList(Context context) {
        return getTicketsUrl(context, R.string.repair_getRoomList);
    }

    /**
     * 管家代报修房间用户信息
     *
     * @param context
     * @return
     */
    public static String selectHouseOwner(Context context) {
        return getTicketsUrl(context, R.string.repair_selectHouseOwner);
    }

    /**
     * 管家物业代报修，默认信息
     *
     * @param context
     * @return
     */
    public static String getManagerOwnerInfo(Context context) {
        return getTicketsUrl(context, R.string.repair_getManagerOwnerInfo);
    }
    /****************************报修End*************************************/
    /***************************广告管理Start*******************************/
    /**
     * 获取首页广告
     *
     * @param context
     * @return
     */
    public static String getHomeAdver(Context context) {
        return getAdvertUrl(context, R.string.get_advertisement_to_app);
    }

    /**
     * 获取广告
     *
     * @param context
     * @return
     */
    public static String getAdvertisement(Context context) {
        return getAdvertUrl(context, R.string.get_ad);
    }

    /**
     * 下架广告
     *
     * @param context
     * @return
     */
    public static String offlineAdvertisement(Context context) {
        return getAdvertUrl(context, R.string.offline_ad);
    }

    /**
     * 广告次数修改
     *
     * @param context
     * @return
     */
    public static String getSubAdSum(Context context) {
        return getAdvertUrl(context, R.string.get_sub_ad_sum);
    }

    /**
     * 获取首页弹窗广告
     *
     * @param context
     * @return
     */
    public static String getHomewindowAdvertisement(Context context) {
        return getAdvertUrl(context, R.string.find_advertisement_homewindow);
    }

    /**
     * 内链电商类型广告设置广告进入标识
     *
     * @param context
     * @return
     */
    public static String advertisementSetSession(Context context) {
        return getVshopUrl(context, R.string.advertisement_setSession);
    }

    /**
     * 统计曝光量
     *
     * @param context
     * @return
     */
    public static String advertisementCountTotalExposure(Context context) {
        return getAdvertUrl(context, R.string.advertisement_countTotal_exposure);
    }

    /**
     * 统计点击量
     *
     * @param context
     * @return
     */
    public static String advertisementCountTotalClickCount(Context context) {
        return getAdvertUrl(context, R.string.advertisement_countTotal_clickCount);
    }

    /**
     * 统计跳过量
     *
     * @param context
     * @return
     */
    public static String advertisementCountActiveJumpCount(Context context) {
        return getAdvertUrl(context, R.string.advertisement_countActive_jumpCount);
    }
    /***************************广告管理End*******************************/

    /**
     * 获取个人开门二维码图片地址
     *
     * @param context GET /AccessControlMobile/getPersonalOpeningQRCode
     * @return
     */
    public static String getPersonalOpeningQRCode(Context context) {
        return getWebUrl(context, R.string.getPersonalOpeningQRCode);
    }

    /**
     * 查询小区开门方式ÅÅ
     *
     * @param context GET  /AccessControlMobile/queryOpenTypes
     * @return
     */
    public static String getOpenTypes(Context context) {
        return getWebUrl(context, R.string.getOpenTypes);
    }

    /**
     * 查询云对讲信息
     *
     * @param context GET /AccessControlMobile/getSDKLoginParam
     * @return
     */
    public static String getCloudTalkInfo(Context context) {
        return getWebUrl(context, R.string.getCloudTalkInfo);
    }


    /**
     * 个人资料页面
     *
     * @return
     */
    public static String getPersonInfoById(Context context) {
        return getWebUrl(context, R.string.getPersonInfoById);
    }

    public static String savePersonInfo(Context context) {
        return getWebUrl(context, R.string.personSave);
    }
}
