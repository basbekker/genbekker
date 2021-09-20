package org.bbekker.genealogy.api;

import java.util.LinkedList;
import java.util.List;

public class OffspringListDTO {

	private List<OffspringDTO> offspring;

	public OffspringListDTO() {
		this.offspring = new LinkedList<OffspringDTO>();
	}

	public List<OffspringDTO> getOffspring() {
		return offspring;
	}

	public void setOffspring(List<OffspringDTO> offspring) {
		this.offspring = offspring;
	}

	public void addOffspring(OffspringDTO offspringOne) {
		offspring.add(offspringOne);
	}

	private List<OffspringDTO> sortOffspring() {
		List<OffspringDTO> sortedOffspringList = new LinkedList<OffspringDTO>();

		// sort list based on level
		sortedOffspringList = offspring; // TODO do sort here, straight copy for now.

		return sortedOffspringList;
	}
}
