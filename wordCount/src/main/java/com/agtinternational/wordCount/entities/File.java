package com.agtinternational.wordCount.entities;

public class File {

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getWordCount() {
		return wordCount;
	}
	public void setWordCount(Long wordCount) {
		this.wordCount = wordCount;
	}
	String name;
	Long wordCount;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((wordCount == null) ? 0 : wordCount.hashCode());
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
		File other = (File) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (wordCount == null) {
			if (other.wordCount != null)
				return false;
		} else if (!wordCount.equals(other.wordCount))
			return false;
		return true;
	}
	
}
