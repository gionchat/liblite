/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, Jeek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.suntront.liblite.bluetooth.connect;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.suntront.liblite.bluetooth.LiteBluetooth;
import com.suntront.liblite.bluetooth.exception.BleException;
import com.suntront.liblite.bluetooth.exception.GattException;
import com.suntront.liblite.bluetooth.exception.InitiatedException;
import com.suntront.liblite.bluetooth.exception.OtherException;
import com.suntront.liblite.bluetooth.log.BleLog;
import com.suntront.liblite.bluetooth.utils.HexUtil;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ble Device Connector.
 * <p>
 * Note: Be Sure Call Methods of Connector On Main(UI) Thread!
 * Note: Be Sure Call Methods of Connector On Main(UI) Thread!
 * Note: Be Sure Call Methods of Connector On Main(UI) Thread!
 * <p>
 *
 * @author lijun
 * @date 2015-10-31
 */
public class LiteBleConnector {
    private static final String TAG = LiteBleConnector.class.getSimpleName();
    private static final int MSG_WRIATE_CHA = 1;
    private static final int MSG_WRIATE_DES = 2;
    private static final int MSG_READ_CHA = 3;
    private static final int MSG_READ_DES = 4;
    private static final int MSG_READ_RSSI = 5;
    private static final int MSG_NOTIY_CHA = 6;
    private static final int MSG_NOTIY_DES = 7;
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(HEART_RATE_MEASUREMENT);
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattService service;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGattDescriptor descriptor;
    private LiteBluetooth liteBluetooth;
    private int timeOutMillis = 30000;
    private Handler handler = new MyHanlder();

    public LiteBleConnector(LiteBluetooth liteBluetooth) {
        this.liteBluetooth = liteBluetooth;
        this.bluetoothGatt = liteBluetooth.getBluetoothGatt();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public LiteBleConnector(LiteBluetooth liteBluetooth, BluetoothGattService service,
                            BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor) {
        this(liteBluetooth);
        this.service = service;
        this.characteristic = characteristic;
        this.descriptor = descriptor;
    }

    public LiteBleConnector(LiteBluetooth liteBluetooth,
                            UUID serviceUUID, UUID charactUUID, UUID descriptorUUID) {
        this(liteBluetooth);
        withUUID(serviceUUID, charactUUID, descriptorUUID);
    }

    public LiteBleConnector(LiteBluetooth liteBluetooth,
                            String serviceUUID, String charactUUID, String descriptorUUID) {
        this(liteBluetooth);
        withUUIDString(serviceUUID, charactUUID, descriptorUUID);
    }

    public LiteBleConnector withUUID(UUID serviceUUID, UUID charactUUID, UUID descriptorUUID) {
        if (serviceUUID != null && bluetoothGatt != null) {
            service = bluetoothGatt.getService(serviceUUID);
        }
        if (service != null && charactUUID != null) {
            characteristic = service.getCharacteristic(charactUUID);
        }
        if (characteristic != null && descriptorUUID != null) {
            descriptor = characteristic.getDescriptor(descriptorUUID);
        }
        return this;
    }

    public LiteBleConnector withUUIDString(String serviceUUID, String charactUUID, String descriptorUUID) {
        return withUUID(formUUID(serviceUUID), formUUID(charactUUID), formUUID(descriptorUUID));
    }

    private UUID formUUID(String uuid) {
        return uuid == null ? null : UUID.fromString(uuid);
    }

    /**
     * write data to characteristic
     */
    public boolean writeCharacteristic(byte[] data, BleCharactCallback bleCallback) {
        return writeCharacteristic(getCharacteristic(), data, bleCallback);
    }

    // _____________________ main operation _____________________

    /**
     * write data to specified characteristic
     */
    public boolean writeCharacteristic(BluetoothGattCharacteristic charact, byte[] data,
                                       final BleCharactCallback bleCallback) {
        if (BleLog.isPrint) {
            BleLog.i(TAG, charact.getUuid() + " characteristic write bytes: "
                    + Arrays.toString(data) + " ,hex: " + HexUtil.encodeHexStr(data));
        }
        handleCharacteristicWriteCallback(bleCallback);
        charact.setValue(data);
        return handleAfterInitialed(getBluetoothGatt().writeCharacteristic(charact), bleCallback);
    }

    /**
     * write data to descriptor
     */
    public boolean writeDescriptor(byte[] data, BleDescriptorCallback bleCallback) {
        return writeDescriptor(getDescriptor(), data, bleCallback);
    }

    /**
     * write data to specified descriptor
     */
    public boolean writeDescriptor(BluetoothGattDescriptor descriptor, byte[] data, BleDescriptorCallback bleCallback) {
        if (BleLog.isPrint) {
            BleLog.i(TAG, descriptor.getUuid() + " descriptor write bytes: "
                    + Arrays.toString(data) + " ,hex: " + HexUtil.encodeHexStr(data));
        }
        handleDescriptorWriteCallback(bleCallback);
        descriptor.setValue(data);
        return handleAfterInitialed(getBluetoothGatt().writeDescriptor(descriptor), bleCallback);
    }

    /**
     * read data from characteristic
     */
    public boolean readCharacteristic(BleCharactCallback bleCallback) {
        return readCharacteristic(getCharacteristic(), bleCallback);
    }

    /**
     * read data from specified characteristic
     */
    public boolean readCharacteristic(BluetoothGattCharacteristic charact, BleCharactCallback bleCallback) {
        if ((characteristic.getProperties() | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            // If there is an active notification on a characteristic, clear
            // it first so it doesn't update the data field on the user interface.
            setCharacteristicNotification(getBluetoothGatt(), charact, false);
            handleCharacteristicReadCallback(bleCallback);
            return handleAfterInitialed(getBluetoothGatt().readCharacteristic(charact), bleCallback);
        } else {
            if (bleCallback != null) {
                bleCallback.onFailure(new OtherException("Characteristic [is not] readable!"));
            }
            return false;
        }
    }

    /**
     * read data from descriptor
     */
    public boolean readDescriptor(BleDescriptorCallback bleCallback) {
        return readDescriptor(getDescriptor(), bleCallback);
    }

    /**
     * read data from specify descriptor
     */
    public boolean readDescriptor(BluetoothGattDescriptor descriptor, BleDescriptorCallback bleCallback) {
        handleDescriptorReadCallback(bleCallback);
        return handleAfterInitialed(getBluetoothGatt().readDescriptor(descriptor), bleCallback);
    }

    /**
     * read rssi of device
     */
    public boolean readRemoteRssi(BleRssiCallback bleCallback) {
        handleRSSIReadCallback(bleCallback);
        return handleAfterInitialed(getBluetoothGatt().readRemoteRssi(), bleCallback);
    }

    /**
     * enable characteristic notification
     */
    public boolean enableCharacteristicNotification(BleCharactCallback bleCallback) {
        return enableCharacteristicNotification(getCharacteristic(), bleCallback);
    }

    /**
     * enable characteristic notification
     */
    public boolean enableCharacteristicNotification(BluetoothGattCharacteristic charact,
                                                    BleCharactCallback bleCallback) {
        if ((charact.getProperties() | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            handleCharacteristicNotificationCallback(bleCallback);
            return setCharacteristicNotification(getBluetoothGatt(), charact, true);
        } else {
            if (bleCallback != null) {
                bleCallback.onFailure(new OtherException("Characteristic [not supports] readable!"));
            }
            return false;
        }
    }

    /**
     * enable characteristic notification
     */
    public boolean enableDescriptorNotification(BleDescriptorCallback bleCallback) {
        return enableDescriptorNotification(getDescriptor(), bleCallback);
    }

    /**
     * enable characteristic notification
     */
    public boolean enableDescriptorNotification(BluetoothGattDescriptor descriptor, BleDescriptorCallback bleCallback) {
        handleDescriptorNotificationCallback(bleCallback);
        return setDescriptorNotification(getBluetoothGatt(), descriptor, true);
    }

    /**
     * {@link BleCallback#onInitiatedSuccess} will be called,
     * if the read operation was initiated successfully.
     * Otherwize {@link BleCallback#onFailure} will be called.
     *
     * @return true, if the read operation was initiated successfully
     */
    private boolean handleAfterInitialed(boolean initiated, BleCallback bleCallback) {
        if (bleCallback != null) {
            if (initiated) {
                bleCallback.onInitiatedSuccess();
            } else {
                bleCallback.onFailure(new InitiatedException());
            }
        }
        return initiated;
    }

    /**
     * Enable or disable notifications/indications for the characteristic and descriptor.
     */
    public boolean setNotification(boolean enable) {
        return setNotification(getBluetoothGatt(), getCharacteristic(), getDescriptor(), enable);
    }

    /**
     * Enable or disable notifications/indications for a given characteristic and descriptor.
     */
    public boolean setNotification(BluetoothGatt gatt,
                                   BluetoothGattCharacteristic characteristic,
                                   BluetoothGattDescriptor descriptor, boolean enable) {
        return setCharacteristicNotification(gatt, characteristic, enable)
                && setDescriptorNotification(gatt, descriptor, enable);
    }

    /**
     * Enable or disable notifications/indications for a given characteristic.
     * <p>
     * <p>Once notifications are enabled for a characteristic, a
     * {@link BluetoothGattCallback#onCharacteristicChanged} callback will be
     * triggered if the remote device indicates that the given characteristic
     * has changed.
     * <p>
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @param characteristic The characteristic for which to enable notifications
     * @param enable         Set to true to enable notifications/indications
     * @return true, if the requested notification status was set successfully
     */
    public boolean setCharacteristicNotification(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 boolean enable) {
        if (gatt != null && characteristic != null) {
            BleLog.i(TAG, "Characteristic set notification value: " + enable);
            boolean success = gatt.setCharacteristicNotification(characteristic, enable);
            // This is specific to Heart Rate Measurement.
            if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
                BleLog.i(TAG, "Heart Rate Measurement set [descriptor] notification value: " + enable);
                BluetoothGattDescriptor descriptor = characteristic
                        .getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
            }
            return success;
        }
        return false;
    }

    /**
     * Write the value of a given descriptor to the associated remote device.
     * <p>
     * <p>A {@link BluetoothGattCallback#onDescriptorWrite} callback is
     * triggered to report the result of the write operation.
     * <p>
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} permission.
     *
     * @return true, if the write operation was initiated successfully
     */
    public boolean setDescriptorNotification(BluetoothGatt gatt,
                                             BluetoothGattDescriptor descriptor,
                                             boolean enable) {
        if (gatt != null && descriptor != null) {
            BleLog.i(TAG, "Descriptor set notification value: " + enable);
            if (enable) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            } else {
                descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            }
            return gatt.writeDescriptor(descriptor);
        }
        return false;
    }

    // _____________________ handle call back _____________________
    private void handleCharacteristicWriteCallback(final BleCharactCallback bleCallback) {
        if (bleCallback != null) {
            listenAndTimer(bleCallback, MSG_WRIATE_CHA, new BluetoothGattCallback() {
                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt,
                                                  BluetoothGattCharacteristic characteristic, int status) {
                    handler.removeMessages(MSG_WRIATE_CHA, this);
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        bleCallback.onSuccess(characteristic);
                    } else {
                        bleCallback.onFailure(new GattException(status));
                    }
                }
            });
        }
    }

    private void handleDescriptorWriteCallback(final BleDescriptorCallback bleCallback) {
        if (bleCallback != null) {
            listenAndTimer(bleCallback, MSG_WRIATE_DES, new BluetoothGattCallback() {
                @Override
                public void onDescriptorWrite(BluetoothGatt gatt,
                                              BluetoothGattDescriptor descriptor, int status) {
                    handler.removeMessages(MSG_WRIATE_DES, this);
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        bleCallback.onSuccess(descriptor);
                    } else {
                        bleCallback.onFailure(new GattException(status));
                    }
                }
            });
        }
    }

    private void handleCharacteristicReadCallback(final BleCharactCallback bleCallback) {
        if (bleCallback != null) {
            listenAndTimer(bleCallback, MSG_READ_CHA, new BluetoothGattCallback() {
                AtomicBoolean msgRemoved = new AtomicBoolean(false);

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic, int status) {
                    if (!msgRemoved.getAndSet(true)) {
                        handler.removeMessages(MSG_READ_CHA, this);
                    }
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        bleCallback.onSuccess(characteristic);
                    } else {
                        bleCallback.onFailure(new GattException(status));
                    }
                }
            });
        }
    }

    private void handleDescriptorReadCallback(final BleDescriptorCallback bleCallback) {
        if (bleCallback != null) {
            listenAndTimer(bleCallback, MSG_READ_DES, new BluetoothGattCallback() {
                AtomicBoolean msgRemoved = new AtomicBoolean(false);

                @Override
                public void onDescriptorRead(BluetoothGatt gatt,
                                             BluetoothGattDescriptor descriptor, int status) {
                    if (!msgRemoved.getAndSet(true)) {
                        handler.removeMessages(MSG_READ_DES, this);
                    }
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        bleCallback.onSuccess(descriptor);
                    } else {
                        bleCallback.onFailure(new GattException(status));
                    }
                }
            });
        }
    }

    private void handleRSSIReadCallback(final BleRssiCallback bleCallback) {
        if (bleCallback != null) {
            listenAndTimer(bleCallback, MSG_READ_RSSI, new BluetoothGattCallback() {
                @Override
                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                    handler.removeMessages(MSG_READ_RSSI, this);
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        bleCallback.onSuccess(rssi);
                    } else {
                        bleCallback.onFailure(new GattException(status));
                    }
                }
            });
        }
    }

    private void handleCharacteristicNotificationCallback(final BleCharactCallback bleCallback) {
        if (bleCallback != null) {
            listenAndTimer(bleCallback, MSG_NOTIY_CHA, new BluetoothGattCallback() {
                AtomicBoolean msgRemoved = new AtomicBoolean(false);

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    if (!msgRemoved.getAndSet(true)) {
                        handler.removeMessages(MSG_NOTIY_CHA, this);
                    }
                    bleCallback.onSuccess(characteristic);
                }
            });
        }
    }

    private void handleDescriptorNotificationCallback(final BleDescriptorCallback bleCallback) {
        if (bleCallback != null) {
            listenAndTimer(bleCallback, MSG_NOTIY_DES, new BluetoothGattCallback() {
                @Override
                public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                    handler.removeMessages(MSG_NOTIY_DES, this);
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        bleCallback.onSuccess(descriptor);
                    } else {
                        bleCallback.onFailure(new GattException(status));
                    }
                }
            });
        }
    }

    /**
     * listen bluetooth gatt callback, and send a delayed message.
     */
    private void listenAndTimer(final BleCallback bleCallback, int what, BluetoothGattCallback callback) {
        bleCallback.setBluetoothGattCallback(callback);
        liteBluetooth.addGattCallback(callback);
        Message msg = handler.obtainMessage(what, bleCallback);
        handler.sendMessageDelayed(msg, timeOutMillis);
    }

    // _____________________ getter and setter _____________________
    public BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public LiteBleConnector setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
        return this;
    }

    public BluetoothGattService getService() {
        return service;
    }

    public LiteBleConnector setService(BluetoothGattService service) {
        this.service = service;
        return this;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

    public LiteBleConnector setCharacteristic(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
        return this;
    }

    public BluetoothGattDescriptor getDescriptor() {
        return descriptor;
    }

    public LiteBleConnector setDescriptor(BluetoothGattDescriptor descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public int getTimeOutMillis() {
        return timeOutMillis;
    }

    public LiteBleConnector setTimeOutMillis(int timeOutMillis) {
        this.timeOutMillis = timeOutMillis;
        return this;
    }

    private class MyHanlder extends Handler {
        @Override
        public void handleMessage(Message msg) {
            BleCallback call = (BleCallback) msg.obj;
            if (call != null) {
                liteBluetooth.removeGattCallback(call.getBluetoothGattCallback());
                call.onFailure(BleException.TIMEOUT_EXCEPTION);
            }
            msg.obj = null;
        }
    }
}
