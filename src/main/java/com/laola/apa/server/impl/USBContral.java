package com.laola.apa.server.impl;

import javax.usb.*;
import java.util.List;


public class USBContral {

	final static short vendorId = (short) 0x4658;
	final static short productId = (short) 0x0010;

	private static UsbInterface iface;

	/**
	 * 初始化机器
	 */
	public static void init() {
		UsbServices services;
		UsbDevice device;
		try {
			services = UsbHostManager.getUsbServices();
			device = USBContral.findDevice((UsbHub) services.getRootUsbHub());
			if (device == null){
			    return;
			}
			iface = USBContral.linkUsbInterface(device);
		} catch (UsbException e) {
			System.out.println("error-init:" + e.getMessage());
		}
	}

	/**
	 * 找到该设备
	 * @param hub
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static UsbDevice findDevice(UsbHub hub) {
		for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
			UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
			if (desc.idVendor() == vendorId && desc.idProduct() == productId) {
				return device;
			}
			if (device.isUsbHub()) {
				device = findDevice((UsbHub) device);
				if (device != null) {
					return device;
				}
			}
		}
		return null;
	}

	/**
	 * 连接usb接口
	 * @param device
	 * @return
	 */
	private static UsbInterface linkUsbInterface(UsbDevice device) {
		UsbConfiguration configuration = device.getActiveUsbConfiguration();
		UsbInterface iface = configuration.getUsbInterface((byte) 0);
		try {
			iface.claim(new UsbInterfacePolicy() {
				@Override
				public boolean forceClaim(UsbInterface usbInterface) {
					return true;
				}
			});
		} catch (Exception e) {
			System.out.println("error-UsbInterface:" + e.getMessage());
		}
		return iface;
	}

	/**
	 * 关闭usb连接
	 */
	public static void clear() {
		if (iface.isActive()) {
			try {
				iface.release();
			} catch (UsbException e) {
				System.out.println("error-disLinkUsbInterface:" + e.getMessage());
			}
		}

	}

	/**
	 * 发送command
	 * @param data
	 * @return
	 */
	public static boolean sendCommand(byte[] data) {
		boolean isSuccess = false;
		if (null == iface ||!iface.isActive()) {
			return false;
		}
		UsbEndpoint sendPoint = iface.getUsbEndpoint((byte) 0x01);
		UsbPipe sendPipe = sendPoint.getUsbPipe();
		try {
			sendPipe.open();
			sendPipe.syncSubmit(data);
		} catch (UsbException e) {
			System.out.println("error-sendCommand:" + e.getMessage());
		} finally {
			try {
				sendPipe.close();
				isSuccess = true;
				sendPipe = null;
				sendPoint = null;
			} catch (UsbException e) {
				System.out.println("error-sendCommand-close:" + e.getMessage());
			}
		}
		return isSuccess;
	}

	/**
	 * 接受信息
	 * @param data
	 * @return
	 */
	public static boolean receivedMessageByEP1(byte[] data) {
		if (!iface.isActive()) {
			return false;
		}
		boolean isSuccess = false;
		UsbEndpoint receivePoint = iface.getUsbEndpoint((byte) 0x81);
		UsbPipe receivePipe = receivePoint.getUsbPipe();
		try {
			receivePipe.open();
			int rep = receivePipe.syncSubmit(data);
			if (rep >= 0) {
				isSuccess = true;
			}
		} catch (UsbException e) {
			System.out.println("error-receivedMessageByEP1:" + e.getMessage());
		} finally {
			try {
				receivePipe.close();
				receivePipe = null;
				receivePoint = null;
			} catch (UsbException e) {
				System.out.println("error-receivedMessageByEP1-close:" + e.getMessage());
			}
		}
		return isSuccess;
	}

	public static boolean receivedMessageByEP2(byte[] data) {
		if (!iface.isActive()) {
			return false;
		}

		boolean isSuccess = false;
		UsbEndpoint receivePoint = iface.getUsbEndpoint((byte) 0x82);
		UsbPipe receivePipe = receivePoint.getUsbPipe();
		try {
			receivePipe.open();
			int rep = receivePipe.syncSubmit(data);
			if (rep >= 0) {
				isSuccess = true;
			}
		} catch (UsbException e) {
			System.out.println("error-receivedMessageByEP1:" + e.getMessage());
		} finally {
			try {
				receivePipe.close();
			} catch (UsbException e) {
				System.out.println("error-receivedMessageByEP1-close:" + e.getMessage());
			}
		}
		return isSuccess;
	}
}
