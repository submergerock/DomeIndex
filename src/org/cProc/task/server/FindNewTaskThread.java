package org.cProc.task.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.UnhandledException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.cProc.tool.CommonUtils;


public class FindNewTaskThread implements Runnable {

	private static final Logger LOG = Logger.getLogger(FindNewTaskThread.class);

	private List<String> tasks = new CopyOnWriteArrayList<String>();

	private List<String> running = new CopyOnWriteArrayList<String>();

	public static final long startTime = System.currentTimeMillis();

	private NotifyPathFilter filter = new NotifyPathFilter(Const.SUFFIX);

	private FileSystem fs = null;

	private Object lock = new Object();

	// 全部重新做
	public FindNewTaskThread(Map<String, List<String>> maps) throws IOException {

		if (maps != null && !maps.isEmpty()) {

			for (Map.Entry<String, List<String>> entry : maps.entrySet()) {
				for (String path : entry.getValue())
					this.running.add(path);
				IRemoteImpl.FAILED.put(entry.getKey(), entry.getValue());
				IRemoteImpl.MAPS.put(entry.getKey(), entry.getValue());

			}

		}

		LOG.info("being start FindNewTashThread.........");
		Configuration conf = new Configuration();

		conf.set("fs.default.name", Const.HDFS_PATH);

		fs = FileSystem.get(conf);

	}

	public FileSystem getFS() {
		return fs;
	}

	public void run() {

		try {

			find();

		} catch (UnhandledException e) {
			LOG.fatal(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 获取 新的任务,每次只能一个访问获得锁定
	 */
	public synchronized List<String> getTasks() {
		List<String> list = new ArrayList<String>();

		synchronized (lock) {

			// 对tasks分表，
			// 获取task队列的第一个
			if (this.tasks.isEmpty())
				return list;
			String path = tasks.get(0);
			String tableName = CommonUtils.getNotifyPathTableName(path);

			list.add(path);
			int size = tasks.size();

			for (int i = 1; i < size; i++) {
				String temp = tasks.get(i);
				if (CommonUtils.getNotifyPathTableName(temp).equals(tableName)) {
					list.add(temp);

					if (list.size() >= Const.FILE_NUMBER)
						break;
				}
			}

			// 移除tasks,添加到running
			for (int i = 0; i < list.size(); i++) {
				this.tasks.remove(list.get(i));
				this.running.add(list.get(i));
			}
			//
		}

		return list;
	}

	/**
	 * 移除进行的任务
	 */

	public void removeRunning(List<String> list) {

		if (list != null && !list.isEmpty()) {

			LOG.info("begin remove running task size is:" + list.size());
			for (String str : list) {
				running.remove(str);
				LOG.info("task is:" + str);
			}
			LOG.info("end remove running task.........");
		}

	}

	public void find() {
		while (Const.SERVER_RUNNING.get()) {
			LOG.info("begin find task.......");
			FileStatus[] files = null;
			try {
				files = fs.listStatus(new Path(Const.NOTIFY_PATH), filter);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (files != null) {

				synchronized (lock) {

					List<String> tempList = new ArrayList<String>();

					for (int i = 0; i < files.length; i++) {

						FileStatus status = files[i];

						String path = status.getPath().getName();

						if (tasks.contains(path))
							continue;

						if (running.contains(path))
							continue;

						// 过滤不必要的

						tempList.add(path);
					}

					LOG.info("find new task size is:" + tempList.size());
					tasks.addAll(tempList);

				}

			}

			LOG.info("end find task.......");

			try {
				Thread.sleep(Const.WATCH_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
