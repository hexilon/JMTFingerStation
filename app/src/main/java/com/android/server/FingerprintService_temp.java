//package com.android.server;
//
//import com.dkl.jmtfps.sensor.JmtSensor;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.os.Messenger;
//import android.os.SystemService;
//
//public class FingerprintService extends SystemService{
//	final int MSG_SET_ENROLL_COUNT =900;
//	final int MSG_ENROLL =901;
//	final int MSG_VERIFY =902;
//	final int MSG_CANCEL =903;
//	
//	Context context;
//	
//	public FingerprintService(Context context) {
//		super(context);
//		this.context=context;
//		JmtSensor.getInstance(context).setAppPathSingleton("data");
//		
//	}
//	
//	/**
//     * 从客户端接收消息的Handler
//     */
//    class FingerprintHandler extends Handler{
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//            	case MSG_SET_ENROLL_COUNT:
//            		JmtSensor.getInstance(context).SetEnrollCounts(msg.arg1);
//            		break;
//                case MSG_ENROLL:
//                	JmtSensor.getInstance(context).Enroll(new JmtSensor.OnEnrollProgressListener() {
//						@Override
//						public int value(int val) {
//							// TODO Auto-generated method stub
//							return 0;
//						}
//						
//						@Override
//						public int onEnrollProgress(int gid, int fid, int per) {
//							// TODO Auto-generated method stub
//							return 0;
//						}
//					});
//                    break;
//                case MSG_VERIFY:
//                	JmtSensor.getInstance(context).Verify(new JmtSensor.OnTouchListener() {
//						
//						@Override
//						public int value(int val) {
//							// TODO Auto-generated method stub
//							return 0;
//						}
//					});
//                    break;
//                case MSG_CANCEL:
//                	JmtSensor.getInstance(context).CancelAction();
//                    break;
//                default:
//                    super.handleMessage(msg);
//            }
//        }
//    }
//    /**
//     * 向客户端公布的用于向IncomingHandler发送信息的Messager
//     */
//    final Messenger mMessenger = new Messenger(new FingerprintHandler());
//   
//
//}
