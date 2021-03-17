package com.laola.apa.server.impl;

import com.laola.apa.utils.ByteUtils;
import com.laola.apa.utils.LaserHomogenizationUtils;

/**
 * API主类，涵盖了所有操作设备的接口
 * 
 * @author zf101
 *
 */
public class DeviceContral {

	/**
	 * 获取序列号
	 * 
	 * @return 序列号
	 */
	public static String getSN() {
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x05, 0x61 });
		if (!isSuccess) {
			System.out.println("getSN is failed");
			return null;
		}
		byte[] response = new byte[16];
		boolean flag = USBContral.receivedMessageByEP1(response);
		if (flag) {
			return ByteUtils.bytesToString(response);
		}
		return null;
	}

	/**
	 * 获取狭缝大小
	 * 
	 * @return 狭缝值
	 */
	public static String getSlit() {
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x05, 0x10 });
		if (!isSuccess) {
			System.out.println("getSlit is failed");
			return null;
		}
		byte[] response = new byte[16];
		boolean flag = USBContral.receivedMessageByEP1(response);
		if (flag) {
			return ByteUtils.bytesToString(response);
		}
		return null;
	}

	/**
	 * 获取坏点
	 * 
	 * @return 坏点
	 */
	public static int[] readBadPoints() {
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x05, 0x12 });
		if (!isSuccess) {
			System.out.println("getSlit is failed");
			return null;
		}
		int[] badPointCorrection = new int[] { 0, 0, 0, 0 };
		byte[] response = new byte[16];
		boolean flag = USBContral.receivedMessageByEP1(response);
		if (!flag) {
			return null;
		}
		String points = ByteUtils.bytesToString(response);
		try {
			for (int i = 0, j = 0; i < badPointCorrection.length && j < points.length(); i++, j = j + 4) {
				badPointCorrection[i] = Integer.parseInt(points.substring(j, j + 4));
			}
		} catch (NumberFormatException e) {
			System.out.println("readBadPoints: " + points);
		}
		return badPointCorrection;
	}

	/**
	 * 开启激光电源
	 * 
	 * @return 是否成功
	 */
	public static boolean setLaserPowerOn() {
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x21 });
		if (!isSuccess) {
			System.out.println("setLaserPowerOn is failed");
			return false;
		}
		return true;
	}

	/**
	 * 关闭激光电源
	 * 
	 * @return 是否成功
	 */
	public static boolean setLaserPowerOff() {
		// 关闭TEC
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x04, 0x00, 0x00 });
		if (!isSuccess) {
			System.out.println("getSlit-TEC is failed");
			return false;
		}
		// 关闭激光电源
		isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x00 });
		if (!isSuccess) {
			System.out.println("getSlit is failed");
			return false;
		}
		return true;
	}

	/**
	 * 开启激光
	 * 
	 * @return 是否成功
	 */
	public static boolean setLaserOn() {
		// TEC 激光器制冷
		Boolean isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x41 });
		if (!isSuccess) {
			System.out.println("setLaserPowerOn is failed");
			return false;
		}

		// LD 激光器
		isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x31 });
		if (!isSuccess) {
			System.out.println("setLaserPowerOn is failed");
			return false;
		}
		return true;
	}

	/**
	 * 关闭激光
	 * 
	 * @return 是否成功
	 */
	public static boolean setLaserOff() {
		// TEC 激光器制冷
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x04, 0x00, 0x00 });
		if (!isSuccess) {
			System.out.println("setLaserPowerOn is failed");
			return false;
		}

		// LD 激光器
		isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x30 });
		if (!isSuccess) {
			System.out.println("setLaserPowerOn is failed");
			return false;
		}

		// LD 激光器
		isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x40 });
		if (!isSuccess) {
			System.out.println("setLaserPowerOn is failed");
			return false;
		}
		return true;
	}

	/**
	 * 设置激光功率
	 * 
	 * @return 是否成功
	 */
	public static boolean setLaserPower(int powerValue) {
		// 激光功率
		int[] laserHomogenizationCoefficients = ReadLaserHomogenizationCoefficientsTask();
		assert laserHomogenizationCoefficients != null;
		int level = LaserHomogenizationUtils.calculateLevelByElectricity(laserHomogenizationCoefficients, powerValue);
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x04, (byte) (level >> 4), (byte) (level << 4) });
		if (!isSuccess) {
			System.out.println("setLaserPowerOn is failed");
			return false;
		}
		return true;
	}

	/**
	 * 获取激光功率校正系数
	 * 
	 * @return 是否成功
	 */
	public static int[] ReadLaserHomogenizationCoefficientsTask() {
		// 激光功率
		// TEC 激光器制冷
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x05, 0x0F });
		if (!isSuccess) {
			System.out.println("ReadLaserHomogenizationCoefficientsTask send failed");
			return null;
		}

		int[] badPointCorrection = new int[] { 0, 0, 0, 0 };
		byte[] response = new byte[16];
		boolean flag = USBContral.receivedMessageByEP1(response);
		if (!flag) {
			return null;
		}
		String points = ByteUtils.bytesToString(response);
		try {
			if (points.startsWith("A0")) {
				for (int i = 0, j = 2; i < badPointCorrection.length && j < points.length(); i++, j = j + 2) {
					if(i >= 10) {
						break;
					}
					badPointCorrection[i] = Integer.parseInt(points.substring(j, j + 2));
				}
			}
		} catch (NumberFormatException e) {
			System.out.println("readBadPoints: " + points);
		}
		return badPointCorrection;
	}

	/**
	 * 设置积分时间
	 * 
	 * @return 是否成功
	 */
	public static boolean setIntegrationTime(int integrationTime) {
		// 设置积分时间
		int time = integrationTime;
		if (integrationTime < 1) {
			time = 1;
		}
		time *= 2400;
		byte[] intTime = ByteUtils.intToBytes(time);
		byte[] request = new byte[5];
		request[0] = 0x02;
		for (int i = 0, j = 1; i < intTime.length; i++, j++) {
			request[j] = intTime[i];
		}
		boolean isSuccess = USBContral.sendCommand(request);
		if (!isSuccess) {
			System.out.println("getSlit is failed");
		}
		return isSuccess;
	}

	/**
	 * 采集背景光谱
	 * 
	 * @return 背景光谱
	 */
	public static double[] requestBackgroundSpectrum() {
		// 关闭激光
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x04, 0x00, 0x00 });
		if (!isSuccess) {
			System.out.println("requestBackgroundSpectrum 关闭激光失败");
			return null;
		}

		// 开启LD激光器
		isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x31 });
		if (!isSuccess) {
			System.out.println("requestBackgroundSpectrum 开启LD激光器失败");
			return null;
		}

//		// 设置积分时间
//		setIntegrationTime(integrationTime);
//
//		//
//		try {
//			Thread.sleep(integrationTime);
//		} catch (InterruptedException e) {
//			return null;
//		}

		// 请求采集背景
		isSuccess = USBContral.sendCommand(new byte[] { 0x09 });
		if (!isSuccess) {
			System.out.println("requestBackgroundSpectrum request is failed");
			return null;
		}

		// 获取背景光谱
		byte[] response = new byte[2048];
		double[] spectrumData = new double[2048];
		USBContral.receivedMessageByEP2(response);
		spectrumData[1] = 0;
		for (int i = 1, j = 0; i < 2048; i = i + 1, j = j + 2) {
			spectrumData[i] = (response[j] & 0xFF) + ((response[j + 1] & 0xFF) << 8);
		}

		//
		isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x30 });
		if (!isSuccess) {
			System.out.println("requestBackgroundSpectrum is failed");
			return null;
		}

		return spectrumData;
	}

	/**
	 * 采集光谱
	 * 
	 * @return 样本光谱
	 */
	public static double[] requestSampleSpectrum() {
		// 开启LD激光器
		boolean isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x31 });
		if (!isSuccess) {
			System.out.println("requestSampleSpectrum 开启LD激光器失败");
			return null;
		}

//				// 设置积分时间
//				setIntegrationTime(integrationTime);
		//
//				//
//				try {
//					Thread.sleep(integrationTime);
//				} catch (InterruptedException e) {
//					return null;
//				}

		// 请求采集背景
		isSuccess = USBContral.sendCommand(new byte[] { 0x09 });
		if (!isSuccess) {
			System.out.println("requestSampleSpectrum request is failed");
			return null;
		}

		// 获取背景光谱
		byte[] response = new byte[4096];
		double[] spectrumData = new double[2048];
		USBContral.receivedMessageByEP2(response);
		spectrumData[1] = 0;
		for (int i = 1, j = 0; i < 2048; i = i + 1, j = j + 2) {
			spectrumData[i] = (response[j] & 0xFF) + ((response[j + 1] & 0xFF) << 8);
		}

		//
		isSuccess = USBContral.sendCommand(new byte[] { 0x03, 0x00, 0x30 });
		if (!isSuccess) {
			System.out.println("requestSampleSpectrum is failed");
			return null;
		}

		return spectrumData;
	}

}
