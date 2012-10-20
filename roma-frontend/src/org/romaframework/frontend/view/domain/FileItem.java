package org.romaframework.frontend.view.domain;

import java.io.File;

public class FileItem {
  private File file;

  public File getFile() {
    return file;
  }

  @Override
  public String toString() {
    return file != null ? file.getName() : "<empty>";
  }

  public void setFile(File file) {
    this.file = file;
  }

  public FileItem(File file) {
    super();
    this.file = file;
  }
}
