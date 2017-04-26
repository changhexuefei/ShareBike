package com.dcch.sharebike.moudle.user.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;



public class InviteFriendsActivity extends BaseActivity {
//    @BindView(R.id.tv_tip_four)
//    TextView mTvTipFour;
//    @BindView(R.id.updateInvitationCode)
//    TextView mUpdateInvitationCode;
    @BindView(R.id.shareWeiChat)
    Button mShareWeiChat;
    @BindView(R.id.shareWeiChatCircle)
    Button mShareWeiChatCircle;
    @BindView(R.id.shareQQ)
    Button mShareQQ;
    @BindView(R.id.shareQQZONE)
    Button mShareQQZONE;
    @BindView(R.id.shareSina)
    Button mShareSina;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.inviteFriends));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void initListener() {
        super.initListener();
    }
    //R.id.updateInvitationCode,
    @OnClick( {R.id.shareWeiChat,R.id.shareWeiChatCircle,R.id.shareQQ,R.id.shareQQZONE,R.id.shareSina})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.updateInvitationCode:
//                String inviteCode = mUpdateInvitationCode.getText().toString().trim();
//                break;

            case R.id.shareWeiChat:
                if(ClickUtils.isFastClick()){
                    return;
                }
                LogUtils.d("点击","微信");
                //分享到微信
                Platform platWeiChat = ShareSDK.getPlatform(Wechat.NAME);
                showShare(platWeiChat.getName());
                break;
            case R.id.shareWeiChatCircle:
                if(ClickUtils.isFastClick()){
                    return;
                }
                //分享到微信朋友圈
                LogUtils.d("点击","微信朋友圈");
                Platform platWeiChatCircle = ShareSDK.getPlatform(WechatMoments.NAME);
                showShare(platWeiChatCircle.getName());
                break;
            case R.id.shareQQ:
                if(ClickUtils.isFastClick()){
                    return;
                }
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                LogUtils.d("点击","QQ");
                Platform plat = ShareSDK.getPlatform(QQ.NAME);
                showShare(plat.getName());
                break;

            case R.id.shareQQZONE:
                if(ClickUtils.isFastClick()){
                    return;
                }
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                LogUtils.d("点击","QQZone");
                Platform platQQZONE = ShareSDK.getPlatform(QZone.NAME);
                showShare(platQQZONE.getName());
                break;

            case R.id.shareSina:
                if(ClickUtils.isFastClick()){
                    return;
                }
                //比如分享到QQ，其他平台则只需要更换平台类名，例如Wechat.NAME则是微信
                Platform platSina = ShareSDK.getPlatform(SinaWeibo.NAME);
                showShare(platSina.getName());
                break;

        }
    }

    private void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.publicity));
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://www.70bikes.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("分享快乐，就骑麒麟单车");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://www.70bikes.com/MavenSSM/Images/qilin.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath();//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.70bikes.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("一起麒麟");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("70bikes");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.70bikes.cn");
        //启动分享
        oks.show(this);
    }

}
