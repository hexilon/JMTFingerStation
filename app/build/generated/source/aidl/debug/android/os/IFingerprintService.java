/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\hexh\\tools\\android\\JMTFingerStation\\app\\src\\main\\aidl\\android\\os\\IFingerprintService.aidl
 */
package android.os;
public interface IFingerprintService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements android.os.IFingerprintService
{
private static final java.lang.String DESCRIPTOR = "android.os.IFingerprintService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an android.os.IFingerprintService interface,
 * generating a proxy if needed.
 */
public static android.os.IFingerprintService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof android.os.IFingerprintService))) {
return ((android.os.IFingerprintService)iin);
}
return new android.os.IFingerprintService.Stub.Proxy(obj);
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
case TRANSACTION_initSensor:
{
data.enforceInterface(DESCRIPTOR);
android.os.IFingerprintCallBack _arg0;
_arg0 = android.os.IFingerprintCallBack.Stub.asInterface(data.readStrongBinder());
int _result = this.initSensor(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_GetSensorType:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.GetSensorType();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_CancelAction:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.CancelAction();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_Verify:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.Verify();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_SetEnrollCounts:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.SetEnrollCounts(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_Enrollment:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.Enrollment();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_Erase:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.Erase(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_EraseFull:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.EraseFull();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_SetWaitTouch:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.SetWaitTouch();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_CancelWaitTouch:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.CancelWaitTouch();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_WaitClick:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.WaitClick();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_StartNavigation:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.StartNavigation();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_GetSDKVersion:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.GetSDKVersion();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_GetOneFinger:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.GetOneFinger();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_GetFingerLoop:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.GetFingerLoop();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_SystemWakeup:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.SystemWakeup();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_SetInputKey:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.SetInputKey(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements android.os.IFingerprintService
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
@Override public int initSensor(android.os.IFingerprintCallBack CallBack) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((CallBack!=null))?(CallBack.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_initSensor, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int GetSensorType() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_GetSensorType, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int CancelAction() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_CancelAction, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int Verify() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_Verify, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int SetEnrollCounts(int cnt) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(cnt);
mRemote.transact(Stub.TRANSACTION_SetEnrollCounts, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int Enrollment() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_Enrollment, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int Erase(int index) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(index);
mRemote.transact(Stub.TRANSACTION_Erase, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int EraseFull() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_EraseFull, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int SetWaitTouch() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_SetWaitTouch, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int CancelWaitTouch() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_CancelWaitTouch, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int WaitClick() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_WaitClick, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int StartNavigation() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_StartNavigation, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String GetSDKVersion() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_GetSDKVersion, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int GetOneFinger() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_GetOneFinger, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int GetFingerLoop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_GetFingerLoop, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int SystemWakeup() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_SystemWakeup, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int SetInputKey(int key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(key);
mRemote.transact(Stub.TRANSACTION_SetInputKey, _data, _reply, 0);
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
static final int TRANSACTION_initSensor = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_GetSensorType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_CancelAction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_Verify = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_SetEnrollCounts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_Enrollment = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_Erase = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_EraseFull = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_SetWaitTouch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_CancelWaitTouch = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_WaitClick = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_StartNavigation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_GetSDKVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_GetOneFinger = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_GetFingerLoop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_SystemWakeup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_SetInputKey = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
}
public int initSensor(android.os.IFingerprintCallBack CallBack) throws android.os.RemoteException;
public int GetSensorType() throws android.os.RemoteException;
public int CancelAction() throws android.os.RemoteException;
public int Verify() throws android.os.RemoteException;
public int SetEnrollCounts(int cnt) throws android.os.RemoteException;
public int Enrollment() throws android.os.RemoteException;
public int Erase(int index) throws android.os.RemoteException;
public int EraseFull() throws android.os.RemoteException;
public int SetWaitTouch() throws android.os.RemoteException;
public int CancelWaitTouch() throws android.os.RemoteException;
public int WaitClick() throws android.os.RemoteException;
public int StartNavigation() throws android.os.RemoteException;
public java.lang.String GetSDKVersion() throws android.os.RemoteException;
public int GetOneFinger() throws android.os.RemoteException;
public int GetFingerLoop() throws android.os.RemoteException;
public int SystemWakeup() throws android.os.RemoteException;
public int SetInputKey(int key) throws android.os.RemoteException;
}
