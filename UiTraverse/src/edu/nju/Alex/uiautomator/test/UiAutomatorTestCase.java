package edu.nju.Alex.uiautomator.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;  


public class UiAutomatorTestCase extends com.android.uiautomator.testrunner.UiAutomatorTestCase{
	
	public void test() throws UiObjectNotFoundException{
		
//		String pack = getUiDevice().getCurrentPackageName();
//		UiObject root = new UiObject(new UiSelector().packageName(pack));
//		UiObject object = root.getChild(new UiSelector().instance(8));
//		object.clickBottomRight();
		String serverAddress = "114.212.82.81";
		int port = 10000;
		Socket socket = null;
		boolean finished = false;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		while (!finished) {
			
			try {
				System.out.println(port);
				socket = new Socket(serverAddress, port);

				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String content;
				System.out.println("waiting for input");
				while (!finished) {
					while ((content = reader.readLine()) != null) {
						System.out.println(content);
						if (content.equals(Cmds.STOP)) {
							finished = true;
							break;
						}
						if (content.equals(Cmds.DUMP)) {
							final String dumpXml = "dump.xml";
							System.out.println("dump window hierarchy");
//							final File dump = new File(new File(Environment.getDataDirectory(),
//							"local/tmp"), dumpXml);
//							dump.mkdirs();
//							if (dump.exists()) {
//							    dump.delete();
//							}
							UiDevice.getInstance().dumpWindowHierarchy(dumpXml);
						} else {
							String[] params = content.split(" ");
							int instance = Integer.valueOf(params[0]);
							String order = params[1];
							String pack = getUiDevice().getCurrentPackageName();
							UiObject root = new UiObject(new UiSelector().packageName(pack));
							UiObject object = root.getChild(new UiSelector().instance(instance));
							if (order.equals(Cmds.CLICK)) {
								object.click();
							} else if (order.equals(Cmds.CLEARTEXTFIELD)) {
								object.clearTextField();
							} else if (order.equals(Cmds.CLICKBOTTOMRIGHT)) {
								object.clickBottomRight();
							} else if (order.equals(Cmds.CLICKTOPLEFT)) {
								object.clickTopLeft();
							} else if (order.equals(Cmds.LONGCLICK)) {
								object.longClick();
							} else if (order.equals(Cmds.LONGCLICKBOTTOMRIGHT)) {
								object.longClickBottomRight();
							} else if (order.equals(Cmds.LONGCLICKTOPLEFT)) {
								object.longClickTopLeft();
							} else if (order.equals(Cmds.SETTEXT)) {
								String text = params[2];
								object.setText(text);
							} else if (order.equals(Cmds.SWIPEDOWN)) {
								object.swipeDown(100);
							} else if (order.equals(Cmds.SWIPELEFT)) {
								object.swipeLeft(100);
							} else if (order.equals(Cmds.SWIPERIGHT)) {
								object.swipeRight(100);
							} else if (order.equals(Cmds.SWIPEUP)) {
								object.swipeUp(100);
							} else {
								System.out.println("Error: unknown order " + content);
								System.exit(-1);
							}
						}
						writer.write("okay");
						writer.newLine();
						writer.flush();
						System.out.println("okay");
					}
				}
				System.out.println("Stoped!");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
