package com.example.http2.record;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PageTests {

	@Autowired
	Validator validator;
	final Random rand = new Random();

	public int getRand() {
		return rand.nextInt(0, 100);
	}

	@Test
	public void fromTest() {
		final Page page = Page.from(1, 5);

		assertThat(page).isNotNull();
	}

	@Test
	public void limitTest() {
		Page page = Page.from(1, 5);

		assertThat(page.limit()).isEqualTo(5);

		page = Page.from(getRand(), 5);

		assertThat(page.limit()).isEqualTo(5);
	}

	@Test
	public void skipTest() {
		int pageSize = 5;
		Page page = Page.from(1, pageSize);

		assertThat(page.skip()).isEqualTo(0);

		page = Page.from(2, pageSize);

		assertThat(page.skip()).isEqualTo(5);

		int rand = getRand();
		page = Page.from(rand, pageSize);

		assertThat(page.skip()).isEqualTo((rand - 1) * pageSize);
	}

	@Test
	public void validationTest() {
		Page page = Page.from(0, 0);

		Set<ConstraintViolation<Page>> violations = validator.validate(page);

		assertThat(violations).hasSize(2);

		page = Page.from(1, 1);

		violations = validator.validate(page);

		assertThat(violations).hasSize(0);
	}
}