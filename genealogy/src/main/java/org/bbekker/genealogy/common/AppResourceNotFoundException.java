package org.bbekker.genealogy.common;

public class AppResourceNotFoundException extends AppException {

	private static final long serialVersionUID = 1L;

	private String resourceReference;

	public static AppResourceNotFoundException createWith(String resourceReference) {
		return new AppResourceNotFoundException(resourceReference);
	}

	private AppResourceNotFoundException(String resourceReference) {
		this.resourceReference = resourceReference;
	}

	@Override
	public String getMessage() {
		return "Resource '" + resourceReference + "' not found";
	}

}
