package ru.erked.spaceflight.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ru.erked.spaceflight.random.INF;
import ru.erked.spaceflight.tech.SFTextListener;

/**
 * ������������ ������ ��������� � ������ �������
 */
public class Client {
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private static final int port = 8283;

	/**
	 * �������������� ����� ����������� � ��������
	 */
	public Client() {
		try {
			
			socket = new Socket("127.0.0.1", port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(INF.login);

			Resender resend = new Resender();
			resend.start();

			String str = "";
			while (!str.equals("exit")) {
				str = SFTextListener.inputLine;
				out.println(str);
			}
			resend.setStop();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	/**
	 * ��������� ������� � �������� ������ � �����
	 */
	private void close() {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			System.err.println("������ �� ���� �������!");
		}
	}

	/**
	 * ����� � ��������� ���� ���������� ��� ��������� �� ������� � �������.
	 * �������� ���� �� ����� ������ ����� setStop().
	 * 
	 * @author ����
	 */
	private class Resender extends Thread {

		private boolean stoped;
		
		/**
		 * ���������� ��������� ���������
		 */
		public void setStop() {
			stoped = true;
		}

		/**
		 * ��������� ��� ��������� �� ������� � �������� �� � �������.
		 * ��������������� ������� ������ setStop()
		 * @see Thread#run()
		 */
		@Override
		public void run() {
			try {
				while (!stoped) {
					String str = in.readLine();
				}
			} catch (IOException e) {
				System.err.println("������ ��� ��������� ���������.");
				e.printStackTrace();
			}
		}
	}

}
