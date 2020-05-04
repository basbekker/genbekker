package org.bbekker.genealogy.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.BaseName;
import org.bbekker.genealogy.repository.BaseNamePrefix;
import org.bbekker.genealogy.repository.BaseNamePrefixRepository;
import org.bbekker.genealogy.repository.BaseNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemInitController {

	@Autowired
	MessageSource messageSource;

	@Autowired
	private BaseNameRepository baseNameRepository;
	@Autowired
	private BaseNamePrefixRepository baseNamePrefixRepository;

	@RequestMapping(path = "/systeminit", method = RequestMethod.GET)
	public String systemInitializer(Locale locale) {

		Boolean result = Boolean.FALSE;

		if (loadBaseNames() & loadBaseNamePrefixes()) {
			result = Boolean.TRUE;
		}

		return messageSource.getMessage("init.finished", null, locale);
		//return result;
	}

	private Boolean loadBaseNames() {

		Boolean result = Boolean.FALSE;

		// load names from csv file, but only if the table is still emtpy
		long numOfBaseNameEntries = baseNameRepository.count();
		if (numOfBaseNameEntries == 0) {

			try {
				final Resource resource = new ClassPathResource(AppConstants.BASE_NAME_CSV_PATH);
				final InputStream inputStream = resource.getInputStream();

				final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;

				while ((line = bufferedReader.readLine()) != null) {

					// Skip empty entries
					final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
					if ( (lineList.get(1) != null && !lineList.get(1).isEmpty())
							&& (lineList.get(0) != null && !lineList.get(0).isEmpty())
							) {
						BaseName bn = new BaseName(lineList.get(1), lineList.get(0));
						bn = baseNameRepository.save(bn);
					}
				}

				bufferedReader.close();
				inputStream.close();

				result = Boolean.TRUE;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Table has rows, assume it's loaded already, do nothing.
			// TODO: do a table content refresh, maybe there is changed content.
			result = Boolean.TRUE;
		}

		return result;
	}

	private Boolean loadBaseNamePrefixes() {

		Boolean result = Boolean.FALSE;

		// load name prefixes from csv file, but only if the table is still emtpy
		long numOfBaseNamePrefixEntries = baseNamePrefixRepository.count();
		if (numOfBaseNamePrefixEntries == 0) {
			try {

				final Resource resource = new ClassPathResource(AppConstants.BASE_NAME_PREFIX_CSV_PATH);
				final InputStream inputStream = resource.getInputStream();

				final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;

				while ((line = bufferedReader.readLine()) != null) {

					// Skip empty entries
					final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
					if ( (lineList.get(1) != null && !lineList.get(1).isEmpty())
							&& (lineList.get(0) != null && !lineList.get(0).isEmpty())
							) {
						BaseNamePrefix bnp = new BaseNamePrefix(lineList.get(1), lineList.get(0));
						bnp = baseNamePrefixRepository.save(bnp);
					}
				}

				bufferedReader.close();
				inputStream.close();

				result = Boolean.TRUE;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Table has rows, assume it's loaded already, do nothing.
			// TODO: do a table content refresh, maybe there is changed content.
			result = Boolean.TRUE;
		}

		return result;
	}

}
