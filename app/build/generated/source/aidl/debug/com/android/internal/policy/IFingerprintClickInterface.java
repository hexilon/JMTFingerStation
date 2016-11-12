/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\hexh\\tools\\android\\JMTFingerStation\\app\\src\\main\\aidl\\com\\android\\internal\\policy\\IFingerprintClickInterface.aidl
 */
package com.android.internal.policy;
public interface IFingerprintClickInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.android.internal.policy.IFingerprintClickInterface
{
private static final java.lang.String DESCRIPTOR = "com.android.internal.policy.IFingerprintClickInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.android.internal.policy.IFingerprintClickInterface interface,
 * generating a proxy if needed.
 */
public static com.android.internal.policy.IFingerprintClickInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.android.internal.policy.IFingerprintClickInterface))) {
return ((com.android.internal.policy.IFingerprintClickInterface)iin);
}
return new com.android.internal.policy.IFingerprintClickInterface.Stub.Proxy(obj);
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
case TRANSACTION_registerClick:
{
data.enforceInterface(DESCRIPTOR);
com.android.internal.policy.IFingerprintClickCallback _arg0;
_arg0 = com.android.internal.policy.IFingerprintClickCallback.Stub.asInterface(data.readStrongBinder());
this.registerClick(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterClick:
{
data.enforceInterface(DESCRIPTOR);
this.unregisterClick();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.android.internal.policy.IFingerprintClickInterface
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
@Override public void registerClick(com.android.internal.policy.IFingerprintClickCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerClick, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterClick() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_unregisterClick, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerClick = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterClick = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void registerClick(com.android.internal.policy.IFingerprintClickCallback callback) throws android.os.RemoteException;
public void unregisterClick() throws android.os.RemoteException;
}
