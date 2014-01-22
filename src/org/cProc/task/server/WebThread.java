package org.cProc.task.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WebThread implements Runnable {

	private FindNewTaskThread thread = null;

	public WebThread(FindNewTaskThread thread) {
		this.thread = thread;
	}

	public void run() {
		Server server = new Server(Const.JETTY_PORT);

		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(new ServletHolder(new IRemoteImpl(thread)), "/index");

		try {
			server.start();
			// server.join();

			while (Const.SERVER_RUNNING.get()) {
				Thread.sleep(1000 * 30);
			}
			server.stop();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//new Thread(new WebThread()).start();

	}

}
