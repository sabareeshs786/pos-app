package com.increff.posapp.dto;

import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.BrandForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
import com.increff.posapp.util.ConverterDto;
import com.increff.posapp.util.FormNormalizer;
import com.increff.posapp.util.FormValidator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class BrandReportDto {
	@Autowired
	private BrandService brandService;
	
	public void add(BrandForm form) throws ApiException {
		FormValidator.brandFormValidator(form);
		FormNormalizer.brandFormNormalizer(form);
		BrandPojo brandPojo = ConverterDto.convertToBrandPojo(form);
		brandService.add(brandPojo);
	}

	public BrandData get(Integer id) throws ApiException {
		BrandPojo brandPojo = brandService.getById(id);
		return ConverterDto.convertToBrandData(brandPojo);
	}
	
	public ResponseEntity<byte[]> getAll() throws ApiException, IOException {
		List<BrandPojo> listBrandPojo = brandService.getAll();
		List<BrandData> listBrandData = new ArrayList<>();
		for(BrandPojo p: listBrandPojo) {
			listBrandData.add(ConverterDto.convertToBrandData(p));
		}

		// Create a file object representing the TSV file

		File file = new File("brandreport.tsv");
		if(!file.createNewFile()){
			PrintWriter writer = new PrintWriter(file);
			writer.print("");
			writer.close();
		}

		FileWriter myWriter = new FileWriter(file);
		myWriter.write("Files\tin\tJava\tmight\tbe\ttricky,\tbut\tit\tis\tfun\tenough!");
		myWriter.close();
		System.out.println("Successfully wrote to the file.");

		// Read the contents of the file into a byte array
		byte[] contents = FileUtils.readFileToByteArray(file);

		// Create a HttpHeaders object to set the response headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("text/tab-separated-values"));
		headers.setContentLength(contents.length);
		headers.setContentDispositionFormData("attachment", file.getName());

		// Return the file contents as a response entity
		return new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	}

	public List<BrandData> getByBrand(String brand) throws ApiException {
		List<BrandPojo> listBrandPojo = brandService.getByBrand(brand);
		List<BrandData> listBrandData = new ArrayList<>();
		for (BrandPojo p : listBrandPojo) {
			listBrandData.add(ConverterDto.convertToBrandData(p));
		}
		return listBrandData;
	}

	public List<BrandData> getByCategory(String category) throws ApiException {
		List<BrandPojo> listBrandPojo = brandService.getByCategory(category);
		List<BrandData> listBrandData = new ArrayList<>();
		for (BrandPojo p : listBrandPojo) {
			listBrandData.add(ConverterDto.convertToBrandData(p));
		}
		return listBrandData;
	}

	public BrandData getByBrandandCategory(String brand, String category) throws ApiException {
		BrandPojo brandPojo = brandService.getByBrandAndCategory(brand, category);
		return ConverterDto.convertToBrandData(brandPojo);
	}

	public void updateById(Integer id, BrandForm form) throws ApiException {
		FormValidator.brandFormValidator(form);
		FormNormalizer.brandFormNormalizer(form);
		BrandPojo p = ConverterDto.convertToBrandPojo(form);
		brandService.updateById(id, p);
	}
}
