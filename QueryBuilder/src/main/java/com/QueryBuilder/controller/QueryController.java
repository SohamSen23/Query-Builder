package com.QueryBuilder.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.QueryBuilder.communicator.FileReaderWriter;
import com.QueryBuilder.reader.QueryReader;

@RestController
public class QueryController {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private FileReaderWriter fileReaderWriter;
	
	@Autowired
	private QueryReader queryReader;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String handleFileUpload(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				String uploadsDir = "/uploads/";
				String realPathtoUploads = request.getServletContext().getRealPath(uploadsDir);
				if (!new File(realPathtoUploads).exists()) {
					new File(realPathtoUploads).mkdir();
				}
				String orgName = file.getOriginalFilename();
				String filePath = realPathtoUploads + orgName;
				File dest = new File(filePath);
				file.transferTo(dest);
				XSSFWorkbook xssfWorkbook = fileReaderWriter.readFile(filePath);
				queryReader.readQuery(xssfWorkbook);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/";
	}
	
	/*
	 * @PostMapping("/") public String handleFileUpload(@RequestParam("file")
	 * MultipartFile file, RedirectAttributes redirectAttributes) {
	 * 
	 * storageService.store(file); redirectAttributes.addFlashAttribute("message",
	 * "You successfully uploaded " + file.getOriginalFilename() + "!");
	 * 
	 * return "redirect:/"; }
	 */
}
