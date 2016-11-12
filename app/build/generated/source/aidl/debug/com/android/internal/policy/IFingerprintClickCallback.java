/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\hexh\\tools\\android\\JMTFingerStation\\app\\src\\main\\aidl\\com\\android\\internal\\policy\\IFingerprintClickCallback.aidl
 */
package com.android.internal.policy;
public interface IFingerprintClickCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.android.internal.policy.IFingerprintClickCallback
{
private static final java.lang.String DESCRIPTOR = "com.android.internal.policy.IFingerprintClickCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.android.internal.policy.IFingerprintClickCallback interface,
 * generating a proxy if needed.
 */
public static com.android.internal.policy.IFingerprintClickCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.android.internal.policy.IFingerprintClickCallback))) {
return ((com.android.internal.policy.IFingerprintClickCallback)iin);
}
return new com.android.internal.policy.IFingerprintClickCallback.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_singleClick:
{
data.enforceInterface(DESCRIPTOR);
this.singleClick();
reply.writeNoException();
return true;
}
case TRANSACTION_doubleClick:
{
data.enforceInterface(DESCRIPTOR);
this.doubleClick();
reply.writeNoException();
return true;
}
case TRANSACTION_longPress:
{
data.enforceInterface(DESCRIPTOR);
this.longPress();
reply.writeNoException();
return true;
}
case TRANSACTION_navigation:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.navigation(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.android.internal.policy.IFingerprintClickCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
	 * 单击
	 */
@Override public void singleClick() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_singleClick, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 双击
	 */
@Override public void doubleClick() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_doubleClick, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 长按
	 */
@Override public void longPress() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_longPress, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * 导航
	 * the x,y is form -7 to +7
	 */
@Override public void navigation(int x, int y) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(x);
_data.writeInt(y);
mRemote.transact(Stub.TRANSACTION_navigation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_singleClick = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_doubleClick = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_longPress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_navigation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
/**
	 * 单击
	 */
public void singleClick() throws android.os.RemoteException;
/**
	 * 双击
	 */
public void doubleClick() throws android.os.RemoteException;
/**
	 * 长按
	 */
public void longPress() throws android.os.RemoteException;
/**
	 * 导航
	 * the x,y is form -7 to +7
	 */
public void navigation(int x, int y) throws android.os.RemoteException;
}
