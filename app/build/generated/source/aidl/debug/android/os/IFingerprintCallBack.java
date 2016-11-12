/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\hexh\\tools\\android\\JMTFingerStation\\app\\src\\main\\aidl\\android\\os\\IFingerprintCallBack.aidl
 */
package android.os;
public interface IFingerprintCallBack extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements android.os.IFingerprintCallBack
{
private static final java.lang.String DESCRIPTOR = "android.os.IFingerprintCallBack";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an android.os.IFingerprintCallBack interface,
 * generating a proxy if needed.
 */
public static android.os.IFingerprintCallBack asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof android.os.IFingerprintCallBack))) {
return ((android.os.IFingerprintCallBack)iin);
}
return new android.os.IFingerprintCallBack.Stub.Proxy(obj);
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
case TRANSACTION_onEnrollProgress:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
int _result = this.onEnrollProgress(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_onEnrollProgress2:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _result = this.onEnrollProgress2(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_onAdvice:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.onAdvice(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_onShowImage:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _result = this.onShowImage(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
reply.writeByteArray(_arg0);
return true;
}
case TRANSACTION_onShowData:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _result = this.onShowData(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
reply.writeByteArray(_arg0);
return true;
}
case TRANSACTION_onNavigation:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _result = this.onNavigation(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_onClick:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.onClick(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements android.os.IFingerprintCallBack
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
@Override public int onEnrollProgress(java.lang.String finger_id, int percentage) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(finger_id);
_data.writeInt(percentage);
mRemote.transact(Stub.TRANSACTION_onEnrollProgress, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int onEnrollProgress2(int gid, int fid, int percentage) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(gid);
_data.writeInt(fid);
_data.writeInt(percentage);
mRemote.transact(Stub.TRANSACTION_onEnrollProgress2, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int onAdvice(int advice) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(advice);
mRemote.transact(Stub.TRANSACTION_onAdvice, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int onShowImage(byte[] pfpimage, int ifpwidth, int ifpheight) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(pfpimage);
_data.writeInt(ifpwidth);
_data.writeInt(ifpheight);
mRemote.transact(Stub.TRANSACTION_onShowImage, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readByteArray(pfpimage);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int onShowData(byte[] pfpimage, int ifpwidth, int ifpheight) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(pfpimage);
_data.writeInt(ifpwidth);
_data.writeInt(ifpheight);
mRemote.transact(Stub.TRANSACTION_onShowData, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
_reply.readByteArray(pfpimage);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int onNavigation(int result, int ver, int hor) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(result);
_data.writeInt(ver);
_data.writeInt(hor);
mRemote.transact(Stub.TRANSACTION_onNavigation, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int onClick(int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_onClick, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_onEnrollProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onEnrollProgress2 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onAdvice = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onShowImage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onShowData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onNavigation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_onClick = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
public int onEnrollProgress(java.lang.String finger_id, int percentage) throws android.os.RemoteException;
public int onEnrollProgress2(int gid, int fid, int percentage) throws android.os.RemoteException;
public int onAdvice(int advice) throws android.os.RemoteException;
public int onShowImage(byte[] pfpimage, int ifpwidth, int ifpheight) throws android.os.RemoteException;
public int onShowData(byte[] pfpimage, int ifpwidth, int ifpheight) throws android.os.RemoteException;
public int onNavigation(int result, int ver, int hor) throws android.os.RemoteException;
public int onClick(int type) throws android.os.RemoteException;
}
