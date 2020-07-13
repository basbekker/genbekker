package org.bbekker.genealogy.dto;

import java.util.LinkedList;
import java.util.List;

public class ExtendedOffspringListDTO {

	private List<OffSpringDTO> offspring;

	public ExtendedOffspringListDTO() {
		this.offspring = new LinkedList<OffSpringDTO>();
	}

	public ExtendedOffspringListDTO(List<OffSpringDTO> offspring) {
		this.offspring = offspring;
	}

	public List<OffSpringDTO> getOffspring() {
		return offspring;
	}

	public void setOffspring(List<OffSpringDTO> offspring) {
		this.offspring = offspring;
	}

	public void addOffspring(OffSpringDTO offspringOne) {
		offspring.add(offspringOne);
	}

	private List<OffSpringDTO> sortOffspring() {
		List<OffSpringDTO> sortedOffspringList = new LinkedList<OffSpringDTO>();

		// sort list based on level
		sortedOffspringList = offspring; // TODO do sort here, straight copy for now.

		return sortedOffspringList;
	}
}
