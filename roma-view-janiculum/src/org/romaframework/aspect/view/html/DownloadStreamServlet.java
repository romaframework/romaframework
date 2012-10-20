package org.romaframework.aspect.view.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.romaframework.aspect.session.SessionAspect;
import org.romaframework.aspect.view.command.impl.DownloadReaderViewCommand;
import org.romaframework.aspect.view.command.impl.DownloadStreamViewCommand;
import org.romaframework.aspect.view.command.impl.ReportingDownloadViewCommand;
import org.romaframework.core.Roma;

public class DownloadStreamServlet extends RomaServlet {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		DownloadStreamViewCommand streamCommand = (DownloadStreamViewCommand) Roma.component(SessionAspect.class).getProperty(
				DownloadStreamViewCommand.class.getSimpleName());
		DownloadReaderViewCommand readerCommand = (DownloadReaderViewCommand) Roma.component(SessionAspect.class).getProperty(
				DownloadReaderViewCommand.class.getSimpleName());
		ReportingDownloadViewCommand reportCommand = (ReportingDownloadViewCommand) Roma.component(SessionAspect.class).getProperty(
				ReportingDownloadViewCommand.class.getSimpleName());

		if (streamCommand != null) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + streamCommand.getFileName() + "\"");
			InputStream input = streamCommand.getIn();

			OutputStream output = response.getOutputStream();

			if (streamCommand.getContentType() != null) {
				response.setContentType(streamCommand.getContentType());
			}

			byte[] buffer = new byte[1024];
			int read = input.read(buffer);
			while (read > 0) {
				output.write(buffer, 0, read);
				read = input.read(buffer);
			}
			output.flush();
		} else if (readerCommand != null) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + readerCommand.getFileName() + "\"");
			Reader reader = readerCommand.getReader();
			PrintWriter output = response.getWriter();

			if (readerCommand.getContentType() != null) {
				response.setContentType(readerCommand.getContentType());

			}
			if (readerCommand.getEncodingType() != null) {
				response.setCharacterEncoding(readerCommand.getEncodingType());
			}

			char[] buffer = new char[1024];
			int read = reader.read(buffer);
			while (read > 0) {
				output.write(buffer, 0, read);
				read = reader.read(buffer);
			}
			output.flush();
		} else if (reportCommand != null) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + reportCommand.getFileName() + "\"");
			OutputStream output = response.getOutputStream();

			if (reportCommand.getContentType() != null) {
				response.setContentType(reportCommand.getContentType());
			}
			reportCommand.write(output);
			output.flush();
		}
	}

}
