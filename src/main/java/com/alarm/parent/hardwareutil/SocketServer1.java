package com.alarm.parent.hardwareutil;

//	   public static void main(String args[]) throws IOException {
	
	import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
@Service
	public class SocketServer1 {
	//报警
//		@PostConstruct
		public void startAction(){
			System.out.println("开始socket...");
			ServerSocket serverSocket=null;
			try {
				serverSocket=new ServerSocket(9000);
				while(true){
					Socket socket=serverSocket.accept();
					System.out.println(socket.getPort());
					System.out.println(socket.getInetAddress());
					BufferedWriter writer0 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					writer0.write("AT\r\n");
					writer0.flush();
					
					
					
					new Thread(new MyRuns(socket)).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (serverSocket!=null) {
						serverSocket.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		class MyRuns implements Runnable{
			
			Socket socket;
			BufferedReader reader;
			BufferedWriter writer;
	 
			public MyRuns(Socket socket) {
				super();
				this.socket = socket;
			}
	 
			@Override
			public void run() {
				try {
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					String lineString="";
					while( (lineString=reader.readLine())!=null ){
						System.out.println("from  client ："+socket.hashCode()+"==="+lineString);
						System.out.println("return from server ："+socket.hashCode()+"==="+"success ok");
						writer.write("return from server:"+"success ok"+"\n");
						writer.flush();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (reader!=null) {
							reader.close();
						}
						if (writer!=null) {
							writer.close();
						}
						if (socket!=null) {
							socket.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
			
		}

	}