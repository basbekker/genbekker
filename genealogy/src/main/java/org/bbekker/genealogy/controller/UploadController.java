package org.bbekker.genealogy.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bbekker.genealogy.service.ImportService;
import org.bbekker.genealogy.service.IndividualService;
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
@RequestMapping(path = "/app/upload")
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	ImportService importService;

	@Autowired
	IndividualService individualService;

	@Value("${org.bbekker.genealogy.upload.folder}")
	String UPLOAD_FOLDER;

	@RequestMapping(path = "/index", method = RequestMethod.GET)
	public String index() {
		return "upload";
	}

	@RequestMapping(path = "/bekker", method = RequestMethod.POST)
	public String singleFileUpload(
			@RequestParam("file") MultipartFile file,
			@RequestParam("processUpload") boolean processUpload,
			RedirectAttributes redirectAttributes,
			Locale locale) {

		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		try {

			// Get the file and save it in a separate upload storage
			byte[] bytes = file.getBytes();
			String filename = file.getOriginalFilename();
			if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
				throw new IllegalArgumentException("Invalid filename");
			}
			Path uploadDir = Paths.get(UPLOAD_FOLDER).normalize().toAbsolutePath();
			Path path = uploadDir.resolve(filename).normalize().toAbsolutePath();
			if (!path.startsWith(uploadDir)) {
				throw new IllegalArgumentException("Invalid filename");
			}
			Files.write(path, bytes);

			Future<Boolean> importTask;
			if (processUpload) {
				importTask = importService.parseBekkerCsvFile(path.toString());

				while(!importTask.isDone()) {
					logger.info("Importing " + path.toString());
					Thread.sleep(1000); // 1 second

					String[] vars = new String[] { file.getOriginalFilename(),
							individualService.getNumberOfElements().toString() };
					String message = messageSource.getMessage("upload.ongoing", vars, locale);
					logger.info(message);
				}
				Boolean result = importTask.get();
			}

			String[] vars = new String[] { file.getOriginalFilename(),
					individualService.getNumberOfElements().toString() };
			final String message = messageSource.getMessage("upload.finished", vars, locale);

			redirectAttributes.addFlashAttribute("message", message);

			logger.info(message);

		} catch (IOException ioe) {
			logger.error(ioe.getLocalizedMessage());
			ioe.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (ExecutionException ee) {
			ee.printStackTrace();
		}

		return "redirect:/app/upload/uploadStatus";
	}

	@RequestMapping(path = "/uploadStatus", method = RequestMethod.GET)
	public String uploadStatus(Locale locale) {
		return "uploadStatus";
	}

}
