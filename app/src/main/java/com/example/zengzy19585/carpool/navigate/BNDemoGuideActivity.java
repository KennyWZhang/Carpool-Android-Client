package com.example.zengzy19585.carpool.navigate;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRouteGuideManager.CustomizedLayerItem;
import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.NaviModuleFactory;
import com.baidu.navisdk.adapter.NaviModuleImpl;
import com.example.zengzy19585.carpool.R;
import com.example.zengzy19585.carpool.appoint.ReceivingOrdersActivity;

import java.util.ArrayList;
import java.util.List;

public class BNDemoGuideActivity extends Activity {

	private final String TAG = BNDemoGuideActivity.class.getName();
	private BNRoutePlanNode mBNRoutePlanNode = null;
	private BaiduNaviCommonModule mBaiduNaviCommonModule = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createHandler();
		View view = null;
			//使用通用接口
			mBaiduNaviCommonModule = NaviModuleFactory.getNaviModuleManager().getNaviCommonModule(
					NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE, this,
					BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE, mOnNavigationListener);
			if(mBaiduNaviCommonModule != null) {
				mBaiduNaviCommonModule.onCreate();
				view = mBaiduNaviCommonModule.getView();
			}

		if (view != null) {
			setContentView(view);
		}

		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable(ReceivingOrdersActivity.ROUTE_PLAN_NODE);
			}
		}
		//显示自定义图标
		if (hd != null) {
			hd.sendEmptyMessageAtTime(MSG_SHOW, 5000);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
			if(mBaiduNaviCommonModule != null) {
				mBaiduNaviCommonModule.onResume();
			}

	}

	protected void onPause() {
		super.onPause();

			if(mBaiduNaviCommonModule != null) {
				mBaiduNaviCommonModule.onPause();
			}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
			if(mBaiduNaviCommonModule != null) {
				mBaiduNaviCommonModule.onDestroy();
			}
	}

	@Override
	protected void onStop() {
		super.onStop();
			if(mBaiduNaviCommonModule != null) {
				mBaiduNaviCommonModule.onStop();
			}
	}

	@Override
	public void onBackPressed() {
			if(mBaiduNaviCommonModule != null) {
				mBaiduNaviCommonModule.onBackPressed(true);
			}
	}

	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
			if(mBaiduNaviCommonModule != null) {
				mBaiduNaviCommonModule.onConfigurationChanged(newConfig);
			}
	}


	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
			if(mBaiduNaviCommonModule != null) {
				Bundle mBundle = new Bundle();
				mBundle.putInt(RouteGuideModuleConstants.KEY_TYPE_KEYCODE, keyCode);
				mBundle.putParcelable(RouteGuideModuleConstants.KEY_TYPE_EVENT, event);
				mBaiduNaviCommonModule.setModuleParams(RouteGuideModuleConstants.METHOD_TYPE_ON_KEY_DOWN, mBundle);
				try {
					Boolean ret = (Boolean)mBundle.get(RET_COMMON_MODULE);
					if(ret) {
						return true;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onStart() {
		super.onStart();
		// TODO Auto-generated method stub
			if(mBaiduNaviCommonModule != null) {
				mBaiduNaviCommonModule.onStart();
			}
	}
	private void addCustomizedLayerItems() {
		List<CustomizedLayerItem> items = new ArrayList<CustomizedLayerItem>();
		CustomizedLayerItem item1;
		if (mBNRoutePlanNode != null) {
			item1 = new CustomizedLayerItem(mBNRoutePlanNode.getLongitude(), mBNRoutePlanNode.getLatitude(),
					mBNRoutePlanNode.getCoordinateType(), getResources().getDrawable(R.drawable.ic_menu_send),
					CustomizedLayerItem.ALIGN_CENTER);
			items.add(item1);

			BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
		}
		BNRouteGuideManager.getInstance().showCustomizedLayer(true);
	}

	private static final int MSG_SHOW = 1;
	private static final int MSG_HIDE = 2;
	private static final int MSG_RESET_NODE = 3;
	private Handler hd = null;

	private void createHandler() {
		if (hd == null) {
			hd = new Handler(getMainLooper()) {
				public void handleMessage(android.os.Message msg) {
					if (msg.what == MSG_SHOW) {
						addCustomizedLayerItems();
					} else if (msg.what == MSG_HIDE) {
						BNRouteGuideManager.getInstance().showCustomizedLayer(false);
					} else if (msg.what == MSG_RESET_NODE) {
						BNRouteGuideManager.getInstance().resetEndNodeInNavi(
								new BNRoutePlanNode(116.21142, 40.85087, "百度大厦11", null, CoordinateType.GCJ02));
					}
				};
			};
		}
	}

	private OnNavigationListener mOnNavigationListener = new OnNavigationListener() {

		@Override
		public void onNaviGuideEnd() {
			//退出导航
			finish();
		}

		@Override
		public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {

			if (actionType == 0) {
				//导航到达目的地 自动退出
				Log.i(TAG, "notifyOtherAction actionType = " + actionType + ",导航到达目的地！");
			}

			Log.i(TAG, "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:" + obj.toString());
		}

	};

	private final static String RET_COMMON_MODULE = "module.ret";

	private interface RouteGuideModuleConstants {
		final static int METHOD_TYPE_ON_KEY_DOWN = 0x01;
		final static String KEY_TYPE_KEYCODE = "keyCode";
		final static String KEY_TYPE_EVENT = "event";
	}
}
