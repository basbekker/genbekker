package org.bbekker.genealogy;

import org.bbekker.genealogy.configuration.LuceneConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EntityScan({"org.bbekker.genealogy.repository"})
@Import(LuceneConfiguration.class)
public class GenealogyApplication {

	//private int maxUploadSizeInMb = 10 * 1024 * 1024; // 10 MB

	public static void main(String[] args) {
		SpringApplication.run(GenealogyApplication.class, args);
	}

}
