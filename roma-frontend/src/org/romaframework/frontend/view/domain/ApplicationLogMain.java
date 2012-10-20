package org.romaframework.frontend.view.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.command.impl.DownloadStreamViewCommand;
import org.romaframework.core.Roma;
import org.romaframework.core.exception.UserException;

@CoreClass(orderFields = "logFiles levels logText", orderActions = "prev next")
public class ApplicationLogMain {
	private static String					logDirectory	= null;
	private static String					logFile				= null;

	@ViewField(render = ViewConstants.RENDER_TABLE, selectionField = "selectedLogFile", enabled = AnnotationConstants.FALSE)
	private List<FileItem>				logFiles;
	@ViewField(visible = AnnotationConstants.FALSE)
	private FileItem							selectedLogFile;

	private static LogFileFilter	fileFilter		= new LogFileFilter();
	private static Log						log						= LogFactory.getLog(ApplicationLogMain.class);

	public static class LogFileFilter implements FilenameFilter {
		public boolean accept(File arg0, String arg1) {
			return arg1.startsWith(logFile);
		}
	}

	public ApplicationLogMain() {
		loadLogDirectory();
		reloadFileList();
	}

	public void reloadFileList() {
		if (logDirectory == null)
			return;

		File logDir = new File(logDirectory);

		logFiles = new ArrayList<FileItem>();
		for (File f : logDir.listFiles(fileFilter)) {
			logFiles.add(new FileItem(f));
		}

		Roma.fieldChanged(this, "logFiles");
	}

	public FileItem getSelectedLogFile() {
		return selectedLogFile;
	}

	public void setSelectedLogFile(FileItem selectedLogFile) throws IOException {
		this.selectedLogFile = selectedLogFile;
	}

	public void downloadFile() throws IOException {
		Roma.aspect(ViewAspect.class).pushCommand(
				new DownloadStreamViewCommand(new FileInputStream(selectedLogFile.getFile()), selectedLogFile.getFile().getName(),
						"text/plain"));
	}

	private void loadLogDirectory() {
		if (logDirectory == null) {
			synchronized (getClass()) {
				try {
					FileAppender app = (FileAppender) Logger.getRootLogger().getAppender("LOGFILE");
					if (app == null)
						return;

					String logFileName = app.getFile();

					int pos = logFileName.lastIndexOf("/");

					if (pos == -1) {
						pos = logFileName.lastIndexOf("\\");
						if (pos == -1)
							throw new UserException(this, "Bad log file name in log4j.xml file: " + logFileName + ". Check your configuration");
					}

					logDirectory = logFileName.substring(0, pos);
					logFile = logFileName.substring(pos + 1);
				} catch (Exception e) {
					log.error("[ApplicationLogMain] Cannot load log directory", e);
				}
			}
		}
	}

	public List<FileItem> getLogFiles() {
		return logFiles;
	}

}
