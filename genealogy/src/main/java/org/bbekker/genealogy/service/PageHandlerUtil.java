package org.bbekker.genealogy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageHandlerUtil<T> {

	private List<T> list;
	private int numberOfElements;
	private int totalPages;

	public PageHandlerUtil(Page<T> page) {
		list = new ArrayList<T>();
		numberOfElements = 0;
		totalPages = 0;
		if (page != null) {
			for (T t : page) {
				list.add(t);
			}
			numberOfElements = page.getNumberOfElements();
			totalPages = page.getTotalPages();
		}

	}

	public PageHandlerUtil(Iterable<T> iterable) {
		list = new ArrayList<T>();
		numberOfElements = 0;
		totalPages = 0;
		if (iterable != null) {
			for (T t : iterable) {
				list.add(t);
			}
			numberOfElements = list.size();
			totalPages = 1;
		}
	}

	public void set(Page<T> page) {
		list = new ArrayList<T>();
		numberOfElements = 0;
		totalPages = 0;
		if (page != null) {
			for (T t : page) {
				list.add(t);
			}
			numberOfElements = page.getNumberOfElements();
			totalPages = page.getTotalPages();
		}

	}

	public void set(Iterable<T> iterable) {
		list = new ArrayList<T>();
		numberOfElements = 0;
		totalPages = 0;
		if (iterable != null) {
			for (T t : iterable) {
				list.add(t);
			}
			numberOfElements = list.size();
			totalPages = 1;
		}
	}

	public List<T> get() {
		return list;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

}
