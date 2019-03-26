package com.share.found.activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.share.found.base.HomEvent;
import com.share.found.R;
import com.share.found.base.BaseActivity;
import com.share.found.base.RefreshEvent;
import com.share.found.bean.LostAndFound;
import com.share.found.bean.User;
import com.share.found.utils.ActivityManager;
import com.share.found.utils.PoiOverlay;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class HomeActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, View.OnClickListener {

    private MapView mMapView;

    private AMap aMap;
    MyLocationStyle myLocationStyle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActivityManager.addActivity(this);
        EventBus.getDefault().register(this);
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        myLocationStyle = new MyLocationStyle();
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
       // CameraUpdate cameraUpdate = CameraUpdate.

        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
        initView();
        initIm();
        initData();
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.hideInfoWindow();
                Intent intent = new Intent(HomeActivity.this,TrendDetailActivity.class);
                intent.putExtra("data",marker.getSnippet());
                startActivity(intent);
                return true;
            }
        });
    }
    ArrayList<LostAndFound> lostList  =  new ArrayList();
    private void initData() {
        BmobQuery<LostAndFound> query = new BmobQuery<LostAndFound>();
        query.order("-createdAt");
        query.include("user");
        query.findObjects(new FindListener<LostAndFound>() {

            @Override
            public void done(List<LostAndFound> diaries, BmobException e) {
                if (e == null) {
                    lostList.clear();
                    for (LostAndFound tr : diaries) {
                        lostList.add(tr);
                    }
                    addPoint();
                } else {
                }
            }

        });

    }

    private void initView() {
        View mFabEdit = findViewById(R.id.fab_edit);
        View mIvHome = findViewById(R.id.iv_home);
        View mIvSearch = findViewById(R.id.iv_search);
        mIvHome.setOnClickListener(this);
        mFabEdit.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_home:
                startActivity(new Intent(HomeActivity.this,PersonActivity.class));
                break;
            case R.id.fab_edit:
                startActivity(new Intent(HomeActivity.this,ReleaseActivity.class));
                break;
            case R.id.iv_search:
                startActivity(new Intent(HomeActivity.this,SearchActivity.class));
                break;
            default:

                break;
        }
    }



    private void addPoint() {
        List<PoiItem> poiItems = new ArrayList<>();
        for (LostAndFound lost : lostList) {
            List<String> list = lost.getTag();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
            }
            poiItems.add(new PoiItem(lost.getObjectId(),new LatLonPoint(lost.getGeoPoint().getLatitude(),lost.getGeoPoint().getLongitude()),sb.toString(),lost.getAddress()));
        }
        if (poiItems != null && poiItems.size() > 0) {
            aMap.clear();// 清理之前的图标
            ViewPoiOverlay poiOverlay = new ViewPoiOverlay(aMap, poiItems);
            poiOverlay.removeFromMap();
            poiOverlay.addToMap();
          //  poiOverlay.zoomToSpan();
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), 17, 0, 0)));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void init(HomEvent messageEvent) {
        Log.e("HomEvent","HomEvent123");
        initData();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
        ActivityManager.removeActivity(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
    private void initIm() {
        final User user = BmobUser.getCurrentUser(User.class);
        //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
        //判断用户是否登录，并且连接状态不是已连接，则进行连接操作
        if (!TextUtils.isEmpty(user.getObjectId()) &&
                BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
                        BmobIM.getInstance().
                                updateUserInfo(new BmobIMUserInfo(user.getObjectId(),
                                        user.getUsername(), user.getAvatar()));
                        EventBus.getDefault().post(new RefreshEvent());
                    } else {
                    }
                }
            });
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                  Log.e("imstatus",BmobIM.getInstance().getCurrentStatus().getMsg());
                }
            });
        }
    }
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        if (latLonPoint ==null){
            return null;
        }
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }



    public class ViewPoiOverlay extends PoiOverlay {

        public ViewPoiOverlay(AMap aMap, List<PoiItem> list) {
            super(aMap, list);
        }

        @Override
        protected BitmapDescriptor getBitmapDescriptor(int index) {
            View view = null;
            view = View.inflate(HomeActivity.this, R.layout.custom_view, null);
            TextView textView = ((TextView) view.findViewById(R.id.title));
            TextView tvDes = ((TextView) view.findViewById(R.id.tv_des));
            textView.setText(getTitle(index));
            tvDes.setText(getSnippet(index));

            return  BitmapDescriptorFactory.fromView(view);
        }
    }

}
