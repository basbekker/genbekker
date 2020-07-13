package org.bbekker.genealogy.service;

import java.util.List;
import java.util.Locale;

import org.bbekker.genealogy.dto.ExtendedOffspringListDTO;
import org.bbekker.genealogy.dto.OffSpringDTO;
import org.bbekker.genealogy.repository.Individual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	IndividualService individualService;

	@Override
	public ExtendedOffspringListDTO getExtendedReportOfOffspring(String individualId, Locale locale) {


		Individual rootIndividual = individualService.get(individualId);
		if (rootIndividual != null) {

			ExtendedOffspringListDTO offspringList = new ExtendedOffspringListDTO();
			addOffspringToList(offspringList, rootIndividual, "1");

			return offspringList;
		} else {
			return null;
		}

	}

	private void addOffspringToList(ExtendedOffspringListDTO offspringList, Individual individual, String level) {

		OffSpringDTO offspringOne = new OffSpringDTO();
		offspringOne.buildFromIndividual(individual, level);
		offspringOne.setPartnerName(individualService.getPartner(individual));
		offspringList.addOffspring(offspringOne);

		List<Individual> kids = individualService.getKids(individual);
		String nextSequence = getNextSequence(getNextLevel(level));

		for (Individual kid : kids) {
			addOffspringToList(offspringList, kid, nextSequence);
			nextSequence = getNextSequence(nextSequence);
		}
	}

	private String getNextLevel(String level) {
		return level + "." + "0";
	}

	private String getNextSequence(String level) {

		int dotPosition = level.lastIndexOf(".");
		int newSequence;
		if (dotPosition > -1) {
			String lastLevel = level.substring(dotPosition + 1);
			newSequence = Integer.parseInt(lastLevel);
			if (newSequence > -1) {
				newSequence = newSequence + 1;
			} else {
				newSequence = 0;
			}
		} else {
			// this is first root level
			newSequence = Integer.parseInt(level);
			if (newSequence > -1) {
				newSequence = newSequence + 1;
			} else {
				newSequence = 0;
			}
		}
		return level.substring(0, dotPosition + 1) + String.valueOf(newSequence);
	}

}
