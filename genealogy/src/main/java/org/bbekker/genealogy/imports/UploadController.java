package org.bbekker.genealogy.imports;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	MessageSource messageSource;

	@Value("${upload.folder}")
	String UPLOAD_FOLDER;

	@RequestMapping(path = "/upload", method = RequestMethod.GET)
	public String index() {
		return "upload";
	}

	@RequestMapping(path = "/upload", method = RequestMethod.POST)
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
			Locale locale) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		try {

			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);

			final String message = messageSource.getMessage("upload.finished",
					new Object[] { file.getOriginalFilename() }, locale);

			redirectAttributes.addFlashAttribute("message", message);

			logger.info(message);

		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";
	}

	@RequestMapping(path = "/uploadStatus", method = RequestMethod.GET)
	public String uploadStatus() {
		return "uploadStatus";
	}

}
