/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\hexh\\tools\\android\\JMTFingerStation\\app\\src\\main\\aidl\\com\\android\\internal\\policy\\IFingerprintLockInterface.aidl
 */
package com.android.internal.policy;
/** {@hide} */
public interface IFingerprintLockInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.android.internal.policy.IFingerprintLockInterface
{
private static final java.lang.String DESCRIPTOR = "com.android.internal.policy.IFingerprintLockInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.android.internal.policy.IFingerprintLockInterface interface,
 * generating a proxy if needed.
 */
public static com.android.internal.policy.IFingerprintLockInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.android.internal.policy.IFingerprintLockInterface))) {
return ((com.android.internal.policy.IFingerprintLockInterface)iin);
}
return new com.android.internal.policy.IFingerprintLockInterface.Stub.Proxy(obj);
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
case TRANSACTION_startUi:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _arg4;
_arg4 = data.readInt();
boolean _arg5;
_arg5 = (0!=data.readInt());
this.startUi(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
return true;
}
case TRANSACTION_stopUi:
{
data.enforceInterface(DESCRIPTOR);
this.stopUi();
reply.writeNoException();
return true;
}
case TRANSACTION_onScreenTurnedOff:
{
data.enforceInterface(DESCRIPTOR);
com.android.internal.policy.IFingerprintLockCallback _arg0;
_arg0 = com.android.internal.policy.IFingerprintLockCallback.Stub.asInterface(data.readStrongBinder());
this.onScreenTurnedOff(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onScreenTurnedOn:
{
data.enforceInterface(DESCRIPTOR);
com.android.internal.policy.IFingerprintLockCallback _arg0;
_arg0 = com.android.internal.policy.IFingerprintLockCallback.Stub.asInterface(data.readStrongBinder());
this.onScreenTurnedOn(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_registerCallback:
{
data.enforceInterface(DESCRIPTOR);
com.android.internal.policy.IFingerprintLockCallback _arg0;
_arg0 = com.android.internal.policy.IFingerprintLockCallback.Stub.asInterface(data.readStrongBinder());
this.registerCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
com.android.internal.policy.IFingerprintLockCallback _arg0;
_arg0 = com.android.internal.policy.IFingerprintLockCallback.Stub.asInterface(data.readStrongBinder());
this.unregisterCallback(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.android.internal.policy.IFingerprintLockInterface
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
@Override public void startUi(android.os.IBinder containingWindowToken, int x, int y, int width, int height, boolean useLiveliness) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(containingWindowToken);
_data.writeInt(x);
_data.writeInt(y);
_data.writeInt(width);
_data.writeInt(height);
_data.writeInt(((useLiveliness)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_startUi, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopUi() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopUi, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onScreenTurnedOff(com.android.internal.policy.IFingerprintLockCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_onScreenTurnedOff, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onScreenTurnedOn(com.android.internal.policy.IFingerprintLockCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_onScreenTurnedOn, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void registerCallback(com.android.internal.policy.IFingerprintLockCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterCallback(com.android.internal.policy.IFingerprintLockCallback cb) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((cb!=null))?(cb.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startUi = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stopUi = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onScreenTurnedOff = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onScreenTurnedOn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_unregisterCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void startUi(android.os.IBinder containingWindowToken, int x, int y, int width, int height, boolean useLiveliness) throws android.os.RemoteException;
public void stopUi() throws android.os.RemoteException;
public void onScreenTurnedOff(com.android.internal.policy.IFingerprintLockCallback cb) throws android.os.RemoteException;
public void onScreenTurnedOn(com.android.internal.policy.IFingerprintLockCallback cb) throws android.os.RemoteException;
public void registerCallback(com.android.internal.policy.IFingerprintLockCallback cb) throws android.os.RemoteException;
public void unregisterCallback(com.android.internal.policy.IFingerprintLockCallback cb) throws android.os.RemoteException;
}
