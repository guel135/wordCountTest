package com.agtinternational.wordCount.entities;

import java.util.List;

public class Directory {

	String name;
	List<GeneralFile> files;
	List<Directory> subdirectories;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<GeneralFile> getFiles() {
		return files;
	}
	public void setFiles(List<GeneralFile> files) {
		this.files = files;
	}
	public List<Directory> getSubdirectories() {
		return subdirectories;
	}
	public void setSubdirectories(List<Directory> subdirectories) {
		this.subdirectories = subdirectories;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((files == null) ? 0 : files.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((subdirectories == null) ? 0 : subdirectories.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Directory other = (Directory) obj;
		if (files == null) {
			if (other.files != null)
				return false;
		} else if (!files.equals(other.files))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (subdirectories == null) {
			if (other.subdirectories != null)
				return false;
		} else if (!subdirectories.equals(other.subdirectories))
			return false;
		return true;
	}
	
}
