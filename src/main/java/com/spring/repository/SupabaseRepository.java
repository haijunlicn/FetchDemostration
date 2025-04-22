package com.spring.repository;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class SupabaseRepository {

	private final RestTemplate restTemplate;
	private final String supabaseUrl;
	private final String apiKey;
	private final String bucketName;
	private final ObjectMapper objectMapper;

	@Autowired
	public SupabaseRepository(RestTemplate restTemplate, @Value("${supabase.url}") String supabaseUrl,
			@Value("${supabase.apiKey}") String apiKey, @Value("${supabase.bucket}") String bucketName) {
		this.restTemplate = restTemplate;
		this.supabaseUrl = supabaseUrl;
		this.apiKey = apiKey;
		this.bucketName = bucketName;
		this.objectMapper = new ObjectMapper();
	}

	public String uploadFile(MultipartFile file, String folderName) throws IOException {
		String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
		String filePath = folderName + uniqueFileName;
		String uploadUrl = supabaseUrl + "storage/v1/object/" + bucketName + "/" + filePath;

		HttpHeaders headers = createHeaders();
		// Set the content type based on the file type
		if (file.getContentType() != null) {
			headers.setContentType(MediaType.parseMediaType(file.getContentType()));
		} else {
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		}
		headers.setContentDisposition(ContentDisposition.attachment().filename(uniqueFileName).build());

		HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
		ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity,
				String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return getSignedUrl(filePath, 31536000);
		} else {
			throw new IOException("Upload failed: " + response.getBody());
		}
	}

	public String getSignedUrl(String filePath, int expiresInSeconds) {
		String signedUrlEndpoint = supabaseUrl + "storage/v1/object/sign/" + bucketName + "/" + filePath;

		HttpHeaders headers = createHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>("{\"expiresIn\":" + expiresInSeconds + "}", headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(signedUrlEndpoint, HttpMethod.POST, requestEntity,
					String.class);

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				JsonNode jsonNode = objectMapper.readTree(response.getBody());
				return jsonNode.has("signedURL") ? supabaseUrl + "storage/v1/" + jsonNode.get("signedURL").asText()
						: null;
			}
		} catch (Exception e) {
			System.err.println("Error fetching signed URL: " + e.getMessage());
		}
		return null;
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("apikey", apiKey);
		headers.set("Authorization", "Bearer " + apiKey);
		return headers;
	}

	private String generateUniqueFileName(String originalFileName) {
		return UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
	}
}