package org.bbekker.genealogy.service;

import org.springframework.stereotype.Service;

@Service
public class AppAbout {

	public static String getAbout() {
		return "Genealogy Application";
	}

	public static String getVersion() {
		return "v0.5 Alpha";
	}

	public static String getAuthors() {
		return "S.B.M. Bekker & C.B. Bekker";
	}

}
