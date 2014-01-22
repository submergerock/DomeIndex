package org.cProc.task.server;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class NotifyPathFilter implements PathFilter {

	private String suffix;

	public NotifyPathFilter(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public boolean accept(Path path) {
		// TODO Auto-generated method stub

		if (path.getName().endsWith(this.suffix))
			return true;
		return false;
	}

}
